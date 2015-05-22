/**
 *
 */
package schema2pojo.filter

import net.liftweb.common.Loggable
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import schema2pojo.bind.Meta2Binding
import java.io.File

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
class TableMetaFilter(path : String) extends Filter with Loggable{
  override def filter(data : AnyRef) : AnyRef = {
    data match {
      case m : scala.collection.mutable.Map[String, AnyRef] => {
        logger.info(m.get("name"))
        val mapper : ObjectMapper = new ObjectMapper()
        for (key <- m.keys) {
          logger.info(m.get(key).toString())
        }
        val writer : ObjectWriter = mapper.writerWithType(m.getClass)
        logger.info(writer.writeValueAsString(m))
        data
      }
      case m : Meta2Binding => {
        val mapper : ObjectMapper = new ObjectMapper()
        logger.info(m =~)
        logger.info(mapper.writeValueAsString(m =~))
        var file = new File(path + m.getName + ".json")
        mapper.writeValue(file, m =~)
        data
      }
    }
  }
}