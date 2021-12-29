package codecs

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.JsonObject
import pojos.Product

class ProductCodec : MessageCodec<Product, Product> {

  override fun transform(product: Product): Product {
    // If a message is sent *locally* across the event bus.
    // This example sends message just as is
    return product
  }

  override fun name(): String {
    return this::javaClass.name
  }

  override fun systemCodecID(): Byte {
    // Always -1
    return -1
  }

  override fun encodeToWire(buffer: Buffer, product: Product) {
    val jsonObject = JsonObject()
      .put("id", product.id)
      .put("name", product.name
      )
    val jsonString = jsonObject.encode()

    buffer.appendInt(jsonString.length)
    buffer.appendString(jsonString)
  }

  override fun decodeFromWire(position: Int, buffer: Buffer): Product {
    // Length of JSON
    val length = buffer.getInt(position)

    // Get JSON string by it`s length
    // Jump 4 because getInt() == 4 bytes
    val jsonStr = buffer.getString(position + 4, position + length)
    val contentJson = JsonObject(jsonStr)

    val id = contentJson.getString("id")
    val name = contentJson.getString("name")
    return Product(id, name)
  }

}