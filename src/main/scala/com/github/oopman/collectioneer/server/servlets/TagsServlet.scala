package com.github.oopman.collectioneer.server.servlets

import com.github.oopman.collectioneer.server.data.TagObjects
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom


case class Tag(name: String,
               categoryId: Option[Int]=None,
               data: Option[String]=None)

/**
  * This servlet manages interactions with the Tag objects within the system
  *
  * @param ctx
  * @tparam Dialect
  * @tparam Naming
  */
class TagsServlet[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val ctx: JdbcContext[Dialect, Naming]) extends CollectioneerServlet(ctx) {
  val tagObjects = new TagObjects(ctx)

  get("/?") {
    contentType = formats("json")
    tagObjects.getTags()
  }

  get("/:id") {
    contentType = formats("json")
    tagObjects.getTag(params("id").toInt) match {
      case Some(tag) => tag
      case None => halt(status=404, body="")
    }
  }

  post("/?") {
    contentType = formats("json")
    val tag = parse(request.body).extract[Tag]
    val tagId = tagObjects.insertTag(tag.name, tag.categoryId, tag.data)
    response.setStatus(201)
    tagId
  }
}
