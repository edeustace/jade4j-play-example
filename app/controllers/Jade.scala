package controllers

import java.io.{InputStreamReader, BufferedReader, Reader}

import de.neuland.jade4j.{Jade4J, JadeConfiguration}
import de.neuland.jade4j.template.{JadeTemplate, TemplateLoader}
import play.api.{Mode, Play}
import play.twirl.api.Html

trait Jade {

  import play.api.Play.current

  private class InternalTemplateLoader(val root: String) extends TemplateLoader {

    private def toPath(name: String) = {
      s"$root/$name${if (name.endsWith(".jade")) "" else ".jade"}"
    }

    override def getLastModified(name: String): Long = Play.resource(toPath(name)).map { url =>
      url.openConnection().getLastModified
    }.getOrElse { throw new RuntimeException(s"getLastModified - Unable to load jade file as a resource from: ${toPath(name)}") }

    override def getReader(name: String): Reader = {
      val expandedPath = toPath(name)
      Play.resource(expandedPath).map { url =>
        val returnValue = new BufferedReader(new InputStreamReader(url.openStream()))
        returnValue
      }.getOrElse { throw new RuntimeException(s"getReader - Unable to load jade file as a resource from: ${expandedPath}") }
    }
  }

  val jadeConfig = {
    val c = new JadeConfiguration
    /** Note: I'm not sure if this is the best place to put the jade, perhaps app/assets/jade is more appropriate */
    c.setTemplateLoader(new InternalTemplateLoader("public/jade"))
    c.setMode(Jade4J.Mode.HTML)
    c.setPrettyPrint(play.api.Play.current.mode == Mode.Dev)
    c
  }

  private def loadTemplate(name: String): JadeTemplate = {

    def readIn = {
      val out = jadeConfig.getTemplate(name)
      out
    }

    /** Note: I'm recompiling the jade each time here - in prod mode you don't want to do this. */
    jadeConfig.clearCache()
    readIn
  }

  def renderJade(name: String, params: Map[String,Object]): Html = {
    require(params != null, "params is null")
    import scala.collection.JavaConversions._
    val template = loadTemplate(name)
    val rendered = jadeConfig.renderTemplate(template, params)
    Html(new StringBuilder(rendered).toString)
  }
}
