package com.github.oopman.collectioneer.server.servlets

import com.github.oopman.collectioneer.server.data.CategoryObjects
import com.github.oopman.collectioneer.server.data.Models.Category
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import org.json4s.JsonAST.{JLong, JNothing, JNull}


case class InsertCategory(parentId: Option[Int], name: String)

case class UpdateCategory(parentId: Option[Int], name: Option[String])

/**
  * This servlet manages interactions with Category objects within the system
  * @param ctx
  * @tparam Dialect
  * @tparam Naming
  */
class CategoriesServlet[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val ctx: JdbcContext[Dialect, Naming]) extends CollectioneerServlet(ctx) {
  val categoryObjects = new CategoryObjects(ctx)

  /**
    * GET Categories
    */
  get("/?") {
    contentType = formats("json")
    val parentId = transformParams("categoryId", _.toInt)
    val (limit, offset) = extractLimitAndOffset
    categoryObjects.getCategories(parentId, offset, limit)
  }

  /**
    * GET an existing Category
    */
  get("/:id") {
    contentType = formats("json")
    categoryObjects.getCategory(params("id").toInt) match {
      case Some(category) => category
      case None => halt(status=404, body="")
    }
  }

  /**
    * POST a new Category
    */
  post("/?") {
    contentType = formats("json")
    val category = parse(request.body).extract[InsertCategory]
    val tagId = categoryObjects.insertCategory(category.name, category.parentId)
    response.setStatus(201)
    tagId
  }

  /**
    * PATCH an existing Category
    */
  patch("/:id") {
    contentType = formats("json")
    val id = params("id").toInt
    val currentCategory = categoryObjects.getCategory(id) match {
      case Some(category) => category
      case _ => halt(status=404, body="")
    }
    val json = parse(request.body)
    val updateCategory = json.extract[UpdateCategory]
    val name = updateCategory.name.getOrElse(currentCategory.name)
    val parentId = json \ "parentId" match {
      case JNothing => currentCategory.parentId
      case JNull => None
      case JLong(n) => Some(n.toInt)
    }
    categoryObjects.updateCategory(Category(id, parentId, name))
    response.setStatus(202)
  }

  /**
    * DELETE an existing Tag
    */
  delete("/:id") {
    contentType = formats("json")
    categoryObjects.deleteCategory(params("id").toInt)
  }
}
