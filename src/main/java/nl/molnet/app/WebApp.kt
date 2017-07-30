package nl.molnet.app

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import java.time.Duration
import java.util.concurrent.*
import java.util.concurrent.Executors.newScheduledThreadPool
import java.util.concurrent.CompletableFuture

object WebApp : AbstractVerticle() {

    override fun start() {
        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        router.get("/:ip").handler {

            val ip = it.request().getParam("ip")
            var future = ArangoDbAccess.getDocumentByKey(ip)

            val responseFuture = within(future, Duration.ofSeconds(1))

            responseFuture
                    .thenAccept({ r ->
                        if (r == null) {
                            it.response().end("{}")
                        } else {
                            it.response().end(r)
                        }

                    })
                    .exceptionally({ throwable ->
                        print("Unrecoverable error" + throwable)
                        it.response().setStatusCode(500).end(throwable.message)
                        //it.fail(throwable)
                        null
                    })

        }

        vertx.createHttpServer().requestHandler {
            router.accept(it)
        }.listen(AppConfig.APP_PORT.toInt())
    }

    fun <A> identity(): (A) -> A = {it}

    fun <T> within(future: CompletableFuture<T>, duration: Duration): CompletableFuture<T> {
        val timeout = failAfter<T>(duration)
        return future.applyToEither(timeout, identity())
    }

    fun <T> failAfter(duration: Duration): CompletableFuture<T> {
        val promise = CompletableFuture<T>()
        scheduler.schedule<Boolean>({
            val ex = TimeoutException("Timeout after " + duration)
            promise.completeExceptionally(ex)
        }, duration.toMillis(), TimeUnit.MILLISECONDS)
        return promise
    }

    private val scheduler = newScheduledThreadPool(
            1,
            ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat("failAfter-%d")
                    .build())

}
