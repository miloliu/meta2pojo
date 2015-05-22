/**
 *
 */
package schema2pojo.filter

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
trait Filter {
  def filter(data : AnyRef) : AnyRef
}