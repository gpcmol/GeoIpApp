package nl.molnet.app

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

object WebApp : AbstractVerticle() {

    override fun start() {
        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        router.get("/:ip").handler {

            val ip = it.request().getParam("ip")
            var future = ArangoDbAccess.getDocumentByKey(ip)

            future
                    .thenAcceptAsync({ r ->
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

}
