/**
 *
 */
package schema2pojo.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.DatabaseMetaData
import java.sql.ResultSet

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
class DBConnection(conn : Connection) {
  def | : DatabaseMetaData = {
    this.conn.getMetaData;
  }
  
  def ## (sql : String) : ResultSet = {
    this.conn.createStatement().executeQuery(sql)
  }
  
  def ~ {
    this.conn.close()
  }
}

object DBConnection {
  def getConnection(url : String) : Connection = {
    val conn = DriverManager.getConnection(url);
    conn;
  }
  
  implicit def connection(url : String) : DBConnection = {
    new DBConnection(getConnection(url));
  }
}