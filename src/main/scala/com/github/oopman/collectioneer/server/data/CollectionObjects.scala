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
    * @param dateTimeModifiedAfter
    * @param dateTimeModifiedBefore
    * @param offset
    * @param limit
    * @return
    */
  def getCollections(categoryId: Option[Option[Int]]=None,
                     active: Option[Boolean]=None,
                     deleted: Option[Boolean]=None,
                     datetimeCreatedAfter: Option[LocalDateTime]=None,
                     datetimeCreatedBefore: Option[LocalDateTime]=None,
                     dateTimeModifiedAfter: Option[LocalDateTime]=None,
                     dateTimeModifiedBefore: Option[LocalDateTime]=None,
                     offset: Int=Config.defaultOffset,
                     limit: Int=Config.defaultLimit): Seq[Collection] = {
    val baseQuery = quote {
      query[Collection].drop(lift(offset)).take(lift(limit))
    }
    val filterQueryA = categoryId match {
      case Some(Some(categoryIdValue)) => quote { baseQuery.filter(_.categoryId.contains(lift(categoryIdValue))) }
      case Some(None) => quote { baseQuery.filter(_.categoryId.isEmpty) }
      case None => baseQuery
    }
    val filteredQueryB = active match {
      case Some(activeValue) => quote { filterQueryA.filter(_.active == lift(activeValue)) }
      case None => filterQueryA
    }
    val filteredQueryC = deleted match {
      case Some(deletedValue) => quote { filteredQueryB.filter(_.deleted == lift(deletedValue)) }
      case None => filteredQueryB
    }
    val filteredQueryD = datetimeCreatedAfter match {
      case Some(datetimeCreatedValue) => quote { filteredQueryC.filter(_.datetimeCreated >= lift(datetimeCreatedValue)) }
      case None => filteredQueryC
    }
    val filteredQueryE = datetimeCreatedBefore match {
      case Some(datetimeCreatedValue) => quote { filteredQueryD.filter(_.datetimeCreated <= lift(datetimeCreatedValue)) }
      case None => filteredQueryD
    }
    val filteredQueryF = dateTimeModifiedAfter match {
      case Some(datetimeModifiedValue) => quote { filteredQueryE.filter(_.datetimeModified >= lift(datetimeModifiedValue)) }
      case None => filteredQueryE
    }
    val filteredQueryG = dateTimeModifiedAfter match {
      case Some(datetimeModifiedValue) => quote { filteredQueryF.filter(_.datetimeModified <= lift(datetimeModifiedValue)) }
      case None => filteredQueryF
    }
    context.run(filteredQueryG)
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
