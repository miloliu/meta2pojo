package schema2pojo

import org.jsonschema2pojo.cli.Jsonschema2PojoCLI
import schema2pojo.filter.TableMetaFilter
import schema2pojo.parser.CommentParser
import java.io.File

object Test {
  def main(args : Array[String]) {
    val dir : File = 
      new File("/Users/c5219821/Documents/workspace/dbpojo/")
    if (dir.exists() && dir.isDirectory()) {
      dir.delete()
    }
    
    Context.put("config-anntation-package", "schema2pojo.annotation.")
    
    MySqlMetaReader.generateMeta(new TableMetaFilter("/Users/c5219821/Documents/workspace/dbpojo/"))

    CommentParser.eveluate("""gg [[@ddd(d="a") @aaa(a="g",c="a")]] aa""")
    val arr : Array[String] = Array("-p", "com.txx.payment.model", "-t", "/Users/c5219821/Documents/workspace/dbpojo/", 
        "-s", "/Users/c5219821/Documents/workspace/dbpojo/txx_security_user.json", "-b", "-r", "-P", "-l", "-a", "JACKSON2", "-A", "schema2pojo.DataSchemaAnnotator",
        "-T", "JSONSCHEMA", "-j", "-303")
    Jsonschema2PojoCLI.main(arr)
  }
}