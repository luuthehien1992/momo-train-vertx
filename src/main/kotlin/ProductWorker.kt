import codecs.ProductCodec
import com.google.common.util.concurrent.Futures
import io.vertx.core.AbstractVerticle
import pojos.Product
import java.lang.Thread.sleep
import java.util.concurrent.Executors


class ProductWorker : AbstractVerticle() {

  companion object {
    const val GET_PRODUCT = "GET_PRODUCT"

    @JvmStatic
    fun buildAddressName(method: String): String {
      return ProductWorker::class.simpleName + "_" + method
    }
  }

  private val executor = Executors.newWorkStealingPool(10)

  override fun start() {
    val eb = vertx.eventBus()

    eb.registerDefaultCodec(Product::class.java, ProductCodec())

    eb.consumer<Product>(buildAddressName(GET_PRODUCT)) { message ->
      val future = Futures.submit({
        println("Doing in " + Thread.currentThread().name)
        sleep(2000) // Simulate heavy task
        println("Done in " + Thread.currentThread().name)
      }, executor)

      Futures.transform(future, {
        message.reply(getProduct())
      }, executor)

      Futures.catching(future, Exception::class.java, {
        message.reply(getProduct())
      }, executor)
    }
  }

  override fun stop() {

  }

  private fun getProduct(): Product {
    return Product("id", "name")
  }

}