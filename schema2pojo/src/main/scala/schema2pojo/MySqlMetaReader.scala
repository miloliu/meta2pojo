/**
 *
 */
package schema2pojo

import java.sql.DatabaseMetaData
import java.sql.ResultSet
import net.liftweb.common.Loggable
import schema2pojo.db.DBConnection._
import schema2pojo.db.DBConnection
import schema2pojo.filter.Filter
import schema2pojo.parser.CommentParser
import schema2pojo.bind.Meta2Binding

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
object MySqlMetaReader extends Loggable {
  def generateMeta(filter : Filter) : Unit = {
    val conn = "jdbc:mysql://localhost:3306/txx_payment_security?useUnicode=true&characterEncoding=utf8&characterset=utf8&user=root&password="
    try {
      val meta : DatabaseMetaData = conn |
      val rs = meta.getTables(null, "txx_payment_security", "%", null)
      import Meta2Binding._
      while (rs.next) {
        val tn = rs.getString("TABLE_NAME")
        logger.info(tn)
        val binding : Meta2Binding = tn
        val crs = meta.getColumns(null, "txx_payment_security", tn, "%")
        //val tmap : scala.collection.mutable.Map[String, scala.collection.mutable.Map[String, Any]] = collection.mutable.Map[String, scala.collection.mutable.Map[String, Any]]()
        //val tname = collection.mutable.Map[String, Any]()
        //tname put ("name", tn)

        //tmap put ("name", tname)
        while (crs.next) {
          /*logger.info("begin")
          logger.info(crs.getString("COLUMN_NAME"))
          logger.info(crs.getString("TYPE_NAME"))
          logger.info(crs.getString("COLUMN_SIZE"))
          logger.info("end")*/
          /*val cmap = collection.mutable.Map[String, Any]()
          cmap put ("name", crs.getString("COLUMN_NAME"))
          cmap put ("type", crs.getString("TYPE_NAME"))
          cmap put ("size", crs.getString("COLUMN_SIZE"))
          tmap put (crs.getString("COLUMN_NAME"), cmap)*/
          binding + (crs.getString("COLUMN_NAME"), crs.getString("TYPE_NAME"))
        }
        val comments = readComment("txx_payment_security", tn, conn)
        logger.info(comments)
        for (key <- comments.keys) {
          val cmap = (binding =~) path (key) //tmap.get(key)
          Option(cmap) match {
            case Some(m) => {
              //m put ("comment", comments.get(key).get)
              binding +@ (key, comments.get(key).get.toString())
            }
            case None =>
          }
        }
        filter.filter(binding)
      }
    } finally {
      conn ~
    }
  }

  def readComment(schema : String, table : String, conn : DBConnection) : collection.mutable.Map[String, Any] = {
    val comments = collection.mutable.Map[String, Any]()
    val sql = """SELECT COLUMN_NAME, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE table_schema='%s' 
        AND table_name='%s'""".format(schema, table)
    //logger.debug(sql)
    val rs : ResultSet = conn ## sql
    while (rs.next) {
      comments put (rs.getString("COLUMN_NAME"), CommentParser.eveluate(rs.getString("COLUMN_COMMENT")))
    }
    comments
  }
}