import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

// Please use postman for request and don't use chrome. Since chrome use 1 thread for request 1 per same path.
// Path: http://127.0.0.1:8080/product

fun main(args: Array<String>) {
  val vertx = Vertx.vertx()

  val server: HttpServer = vertx.createHttpServer()
  val router = Router.router(vertx)
  val productVerticle = ProductVerticle(router)
  val productWorker = ProductWorker()

  vertx.deployVerticle(productVerticle)
  vertx.deployVerticle(productWorker, DeploymentOptions().setWorkerPoolSize(10).setWorker(true))

  server.requestHandler(router).listen(8080)
}
