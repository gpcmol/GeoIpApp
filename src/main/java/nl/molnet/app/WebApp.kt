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
            var result = "{}"
            try {
                result = ArangoDbAccess.getDocumentByKey(ip).get()
            } catch (ex: Exception) {
                // no results
            }

            it.response().end(result)
        }

        vertx.createHttpServer().requestHandler { router.accept(it) }.listen(9090)
    }


}