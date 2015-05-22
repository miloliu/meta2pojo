/**
 *
 */
package schema2pojo

import scala.collection.mutable.Map
/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
object Context {
  private val ctx : ThreadLocal[Map[String, String]] = new ThreadLocal[Map[String, String]]()
  
  def put (key : String, value : String) : Unit = {
    Option(ctx.get) match {
      case Some(map) => {
        map.put(key, value)
      }
      case None => {
        val map = Map[String, String]()
        ctx.set(map)
        map.put(key, value)
      }
    }
  }
  
  def get (key : String) : String = {
    Option(ctx.get) match {
      case Some(map) => {
        map.get(key).get
      }
      case None => ""
    }
  }
}