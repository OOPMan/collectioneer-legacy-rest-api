package com.github.oopman.collectioneer.server.servlets

import com.github.oopman.collectioneer.server.data.Models.Tag
import com.github.oopman.collectioneer.server.data.TagObjects
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import org.json4s._


case class InsertTag(name: String,
                     categoryId: Option[Int]=None,
                     data: Option[String]=None)

case class UpdateTag(name: Option[String],
                     categoryId: Option[String],
                     data: Option[String])

/**
  * This servlet manages interactions with the Tag objects within the system
  *
  * @param ctx
  * @tparam Dialect
  * @tparam Naming
  */
class TagsServlet[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val ctx: JdbcContext[Dialect, Naming]) extends CollectioneerServlet(ctx) {
  val tagObjects = new TagObjects(ctx)

  /**
    * GET Tags
    *
    */
  get("/?") {
    contentType = formats("json")
    val categoryId = extractOptionalNullableParameter("categoryId", _.toInt)
    val (limit, offset) = extractLimitAndOffset
    tagObjects.getTags(categoryId, offset, limit)
  }

  /**
    * Get a specific Tag
    */
  get("/:id") {
    contentType = formats("json")
    tagObjects.getTag(params("id").toInt) match {
      case Some(tag) => tag
      case None => halt(status=404, body="")
    }
  }

  /**
    * POST a new Tag
    */
  post("/?") {
    contentType = formats("json")
    val tag = parse(request.body).extract[InsertTag]
    val tagId = tagObjects.insertTag(tag.name, tag.categoryId, tag.data)
    response.setStatus(201)
    tagId
  }

  /**
    * PATCH an existing Tag
    */
  patch("/:id") {
    contentType = formats("json")
    val id = params("id").toInt
    val currentTag = tagObjects.getTag(id) match {
      case Some(tag) => tag
      case _ => halt(status=404, body="")
    }
    val json = parse(request.body)
    val updateTag = json.extract[UpdateTag]
    val name = updateTag.name.getOrElse(currentTag.name)
    val categoryId = json \ "categoryId" match {
      case JNothing => currentTag.categoryId
      case JNull => None
      case JLong(n) => Some(n.toInt)
      case _ => halt(status=400, body="categoryId must be long or null")
    }
    val data = json \ "data" match {
      case JNothing => currentTag.data
      case JNull => None
      case JString(s) => Some(s)
      case _ => halt(status=400, body="data must be a string or null")
    }
    tagObjects.updateTag(Tag(id, name, categoryId, data))
    response.setStatus(202)
  }

  /**
    * DELETE an existing Tag
    */
  delete("/:id") {
    contentType = formats("json")
    tagObjects.deleteTag(params("id").toInt)
  }
}
