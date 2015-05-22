/**
 *
 */
package schema2pojo.bind.mysql

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
class TypeMapper(mysqlType : String) {
  def -> () = {
    mysqlType match {
      case "BLOB" => "java.lang.Byte[]"
      case "TINYINT" => "byte"
      case "VARCHAR" => "String"
      case "INT" => "Integer"
      case "BIGINT" => "Integer"
      case "TEXT" => "String"
      case _ => "String"
    }
  }
}

object TypeMapper {

  implicit def convert2(mysqlType : String) : TypeMapper = {
    new TypeMapper(mysqlType)
  }
}