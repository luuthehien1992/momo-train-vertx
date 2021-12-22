import codecs.ProductCodec
import io.vertx.core.AbstractVerticle
import pojos.Product


class ProductWorker : AbstractVerticle() {

  companion object {
    const val GET_PRODUCT = "GET_PRODUCT"

    @JvmStatic
    fun buildAddressName(method: String): String {
      return ProductWorker::class.simpleName + "_" + method
    }
  }

  override fun start() {
    val eb = vertx.eventBus()

    eb.registerDefaultCodec(Product::class.java, ProductCodec())

    eb.consumer<Product>(buildAddressName(GET_PRODUCT)) { message ->
      message.reply(getProduct())
    }
  }

  override fun stop() {

  }

  private fun getProduct(): Product {
    return Product("id", "name")
  }

}