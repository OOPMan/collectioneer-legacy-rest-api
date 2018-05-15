package com.github.oopman.collectioneer.server.data

import com.github.oopman.collectioneer.server.Config
import com.github.oopman.collectioneer.server.data.Models.Category
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

class CategoryObjects[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val context: JdbcContext[Dialect, Naming]) extends Objects(context) {
  import context._

  /**
    * Retrieve a Category object by id
    *
    * @param id ID of Category to retrieve
    * @return
    */
  def getCategory(id: Int): Option[Category] = context.run(query[Category].filter(_.id == lift(id))) match {
    case List(category) => Some(category)
    case Nil => None
  }

  /**
    * Retrieve a Sequence of Categories constrained by various input parameters
    *
    * @param parentId Optional parentId to constrain Categories retrieved by
    * @param offset Offset within Categories dataset to retrieve
    * @param limit Number of Categories to retrieve
    * @return
    */
  def getCategories(parentId: Option[Option[Int]]=None,
                    offset: Int=Config.defaultOffset,
                    limit: Int=Config.defaultLimit): Seq[Category] = {
    val categoriesQuery = quote {
      query[Category].drop(lift(offset)).take(lift(limit))
    }
    val finalCategoriesQuery = parentId match {
      case Some(optionalParentId) => optionalParentId match {
        case Some(actualParentId) => quote {
          categoriesQuery.filter(_.parentId.contains(lift(actualParentId)))
        }
        case None => quote {
          categoriesQuery.filter(_.parentId.isEmpty)
        }
      }
      case None => categoriesQuery
    }
    context.run(finalCategoriesQuery)
  }

  /**
    * Insert a Category object and return the ID of the newly created Category
    *
    * @param name Name of Category
    * @param parentId Optional. ID of Category to associate this Category with
    * @return
    */
  def insertCategory(name: String,
                     parentId: Option[Int]): Long = {
    context.run(query[Category].insert(
      _.name -> lift(name),
      _.parentId -> lift(parentId)
    ))
  }

  /**
    * Update a Category object, returning its ID in the process
    * @param category A Category instance
    * @return
    */
  def updateCategory(category: Category): Long = {
    context.run(query[Category].filter(_.id == lift(category.id)).update(lift(category)))
  }

  /**
    * Delete a Category object. Will fail if any Tag, Item or Collection objects
    * associated with the Category exist
    *
    * @param id ID of Category to delete
    * @return
    */
  def deleteCategory(id: Int): Long = {
    context.run(query[Category].filter(_.id == lift(id)).delete)
  }
}
