/**
 *
 */
package schema2pojo.annotation

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
@scala.annotation.meta.field
class Secret extends scala.annotation.StaticAnnotation{
  var value : String = ""
}