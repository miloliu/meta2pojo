/**
 *
 */
package schema2pojo.bind

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.POJONode
import com.fasterxml.jackson.databind.node.ArrayNode
import schema2pojo.bind.mysql.TypeMapper

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
class Meta2Binding(name : String) {

  def apply() : Meta2Binding = {
    treeNode.put("id", name)
    treeNode.put("type", "object")
    treeNode.put("properties", new ObjectNode(JsonNodeFactory.instance))
    this
  }

  def getName : String = {
    name
  }

  private[this] val treeNode : ObjectNode = new ObjectNode(JsonNodeFactory.instance)
  def +(node : ObjectNode) : Meta2Binding = {
    addProperty(node.get("name").textValue(), node)
    this
  }

  private[this] def addProperty(name : String, node : ObjectNode) = {
    val prop : ObjectNode = treeNode.get("properties").asInstanceOf[ObjectNode]
    prop.put(name, node)
  }
  
  private[this] def addPropertyAnntation(name : String, alist : ArrayNode) = {
    val prop : ObjectNode = treeNode.get("properties").asInstanceOf[ObjectNode]
    Option(prop.get(name)) match {
      case Some(node) => {
        node.asInstanceOf[ObjectNode].put("annotation", alist)
      }
      case None =>
    }
  }

  def +(name : String, nodeType : String, annotation : String*) : Meta2Binding = {
    import TypeMapper._
    val typeMapper : TypeMapper = nodeType
    val node = new ObjectNode(JsonNodeFactory.instance)
    val subnode = new ObjectNode(JsonNodeFactory.instance)
    addProperty(name, subnode)
    val additionalNode = new ObjectNode(JsonNodeFactory.instance)
    val alist : ArrayNode = new ArrayNode(JsonNodeFactory.instance)
    for (a <- annotation) yield (alist.add(a))
    additionalNode.putPOJO("annotation", alist)
    subnode.put("javaType", typeMapper ->)
    //addAdditionalProperties(name, additionalNode)
    
    this
  }

  private[this] def addAdditionalProperties(field : String, alist : ObjectNode) = {
    var node : ObjectNode = this.treeNode.get("additionalProperties").asInstanceOf[ObjectNode]
    Option(node) match {
      case None => {
        node = new ObjectNode(JsonNodeFactory.instance)
        this.treeNode.put("additionalProperties", node)
      }
      case Some(_) =>
    }
    Option(node.get(field)) match {
      case Some(f) => {
        node.remove(field)
        node.put(field, alist)
      }
      case None => {
        node.put(field, alist)
      }
    }

  }

  def +@(name : String, annotation : String*) : Meta2Binding = {
    val alist : ArrayNode = new ArrayNode(JsonNodeFactory.instance)
    for (a <- annotation) yield (alist.add(a))
    addPropertyAnntation(name, alist)
    /*val additionalNode = new ObjectNode(JsonNodeFactory.instance)
    additionalNode.putPOJO("annotation", alist)
    addAdditionalProperties(name, additionalNode)*/
    this
  }

  def =~() : ObjectNode = {
    treeNode
  }
}

object Meta2Binding {

  implicit def convert2(name : String) : Meta2Binding = {
    val binding = new Meta2Binding(name)
    binding()
  }
}