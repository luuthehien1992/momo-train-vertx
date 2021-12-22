import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

fun main(args: Array<String>) {
  val vertx = Vertx.vertx()

  val server: HttpServer = vertx.createHttpServer()
  val router = Router.router(vertx)
  val productVerticle = ProductVerticle(router)
  val productWorker = ProductWorker()

  vertx.deployVerticle(productVerticle)
  vertx.deployVerticle(productWorker, DeploymentOptions().setWorker(true))

  server.requestHandler(router).listen(8080)
}
