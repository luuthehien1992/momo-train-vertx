import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import pojos.Product


class ProductVerticle(private val router: Router) : AbstractVerticle() {

  override fun start() {
    router.get("/product").handler {
      getProduct(it)
    }
  }

  override fun stop() {
    this::class.java.simpleName
  }

  private fun getProduct(ctx: RoutingContext) {
    delegateToWorker(ctx, JsonObject(), ProductWorker.GET_PRODUCT)
  }

  private fun sendResponse(ctx: RoutingContext, data: Any) {
    val jsonString = JsonObject.mapFrom(data).encode()

    ctx.response()
      .putHeader("content-type", "application/json")
      .end(jsonString)
  }

  private fun delegateToWorker(ctx: RoutingContext, data: JsonObject, address: String) {
    val fullAddress = ProductWorker.buildAddressName(address)
    vertx.eventBus().request<Product>(fullAddress, data.encode()) {
      if (it.succeeded()) {
        sendResponse(ctx, it.result().body())
      } else {
        sendResponse(ctx, Product("", ""))
      }
    }
  }
}