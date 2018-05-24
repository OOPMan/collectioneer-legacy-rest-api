package com.github.oopman.collectioneer.server.data

import java.time.{Instant, LocalDateTime}

import com.github.oopman.collectioneer.server.Config
import com.github.oopman.collectioneer.server.data.Models.Collection
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

class CollectionObjects[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val context: JdbcContext[Dialect, Naming]) extends Objects(context) {
  import context._

  /**
    *
    * @param id
    * @return
    */
  def getCollection(id: Int): Option[Collection] = context.run(query[Collection].filter(_.id == lift(id))) match {
    case List(collection) => Some(collection)
    case Nil => None
  }

  /**
    *
    * @param categoryId
    * @param active
    * @param deleted
    * @param datetimeCreatedAfter
    * @param datetimeCreatedBefore
    * @param datetimeModifiedAfter
    * @param datetimeModifiedBefore
    * @param offset
    * @param limit
    * @return
    */
  def getCollections(categoryId: Option[Option[Int]]=None,
                     active: Option[Boolean]=None,
                     deleted: Option[Boolean]=None,
                     datetimeCreatedAfter: Option[LocalDateTime]=None,
                     datetimeCreatedBefore: Option[LocalDateTime]=None,
                     datetimeModifiedAfter: Option[LocalDateTime]=None,
                     datetimeModifiedBefore: Option[LocalDateTime]=None,
                     offset: Int=Config.defaultOffset,
                     limit: Int=Config.defaultLimit): Seq[Collection] = {
    val collectionsQuery: Quoted[Query[Collection]] = QueryBuilder {
        quote(query[Collection]).drop(lift(offset)).take(lift(limit))
      }
      .ifInnerDefined(categoryId) {
        (query: Query[Collection], categoryId: Int) => query.filter(_.categoryId.contains(categoryId))
      }
      .ifInnerEmpty(categoryId) {
        query: Query[Collection] => query.filter(_.categoryId.isEmpty)
      }
      .ifDefined(active) {
        (query: Query[Collection], active: Boolean) => query.filter(_.active == active)
      }
      .ifDefined(deleted) {
        (query: Query[Collection], deleted: Boolean) => query.filter(_.deleted == deleted)
      }
      .ifDefined(datetimeCreatedAfter) {
        (query: Query[Collection], dateTimeCreatedBefore: LocalDateTime) => query.filter(_.datetimeCreated >= dateTimeCreatedBefore)
      }
      .ifDefined(datetimeCreatedBefore) {
        (query: Query[Collection], datetimeCreatedBefore: LocalDateTime) => query.filter(_.datetimeCreated <= datetimeCreatedBefore)
      }
      .ifDefined(datetimeModifiedAfter) {
        (query: Query[Collection], datetimeModifiedAfter: LocalDateTime) => query.filter(_.datetimeModified >= datetimeModifiedAfter)
      }
      .ifDefined(datetimeModifiedBefore) {
        (query: Query[Collection], datetimeModifiedBefore: LocalDateTime) => query.filter(_.datetimeModified <= datetimeModifiedBefore)
      }
      .build
    context.run(collectionsQuery)
  }

  /**
    *
    * @param name
    * @param categoryId
    * @param description
    * @return
    */
  def insertCollection(name: String,
                       categoryId: Option[Int],
                       description: Option[String]): Long = ???

  /**
    *
    * @param collection
    * @return
    */
  def updateCollection(collection: Collection): Long = ???

  /**
    *
    * @param id
    * @return
    */
  def deleteCollection(id: Int): Long = ???
}
