import codecs.ProductCodec
import io.vertx.core.AbstractVerticle
import pojos.Product
import java.lang.Thread.sleep


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
      println("Doing in " + Thread.currentThread().name)
      sleep(2000) // Simulate heavy task
      println("Done in " + Thread.currentThread().name)

      message.reply(getProduct())
    }
  }

  override fun stop() {

  }

  private fun getProduct(): Product {
    return Product("id", "name")
  }

}