
import com.github.oopman.collectioneer.server._
import com.github.oopman.collectioneer.server.servlets.{CollectioneerServlet, TagsServlet}
import io.getquill._
import org.scalatra._
import javax.servlet.ServletContext
import org.slf4j.{Logger, LoggerFactory}
import data.QuillContext

class ScalatraBootstrap extends LifeCycle {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  val quillContext = QuillContext("collectioneer", SnakeCase)

  override def init(context: ServletContext) {
    context.mount(new TagsServlet(quillContext), "/tags")
  }
}
