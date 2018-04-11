package com.github.oopman.collectioneer.server.servlets

import com.github.oopman.collectioneer.server.data.TagObjects
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

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

  get("/:name") {
    contentType = formats("json")
    tagObjects.getTag(params("name")) match {
      case Some(tag) => tag
      case None => halt(status=404, body="")
    }
  }
}
