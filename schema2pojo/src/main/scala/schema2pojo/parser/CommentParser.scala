/**
 *
 */
package schema2pojo.parser

import scala.util.matching.Regex
import scala.util.parsing.combinator.JavaTokenParsers

import net.liftweb.common.Loggable

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
trait CommentParser extends JavaTokenParsers with Loggable {

  override implicit def regex(r: Regex): Parser[String] = new Parser[String] {
    def apply(in: Input) = {
      val source = in.source
      val offset = in.offset
      val start = handleWhiteSpace(source, offset)
      (r findPrefixMatchOf (source.subSequence(start, source.length))) match {
        case Some(matched) => {
          val names = matched groupNames;
          if (names.contains("$1")) {
            logger.debug(matched group "$1")
            val res = matched group "$1"
            Success(res.toString,
              in.drop(start + res.length - offset))
          } else
            Success(source.subSequence(start, start + matched.end).toString,
              in.drop(start + matched.end - offset))
        }
        case None =>
          val found = if (start == source.length()) "end of source" else "`" + source.charAt(start) + "'"
          Failure("string matching regex `" + r + "' expected but " + found + " found", in.drop(start - offset))
      }
    }
  }

  def annotationStart: Parser[Any] = "[["

  def annotationEnd: Parser[Any] = "]]"

  def annotationToken: Parser[Any] = "@" ~> """(\S*?)(?=[ @\]])""".r("$1")
  //"""[@]\S*[(].*[)]""".r 

  def annotationTokens: Parser[Any] = ident ~> annotationStart ~> (annotationToken *) <~ annotationEnd <~ ident

  def eveluate(in: CharSequence): Any = {
    val res: ParseResult[Any] = this.parseAll(annotationTokens, in)
    res match {
      case Success(rt,_) => {
        logger.debug(rt.toString())
        rt
      }
      case NoSuccess(_,_) => {}
    }
  }
}

object CommentParser extends CommentParser