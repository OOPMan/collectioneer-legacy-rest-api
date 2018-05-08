package com.github.oopman.collectioneer.server.servlets

import com.github.oopman.collectioneer.server.Config
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class CollectioneerServlet[Dialect <: SqlIdiom, Naming <: NamingStrategy](val ctx: JdbcContext[Dialect, Naming]) extends ScalatraServlet with NativeJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  /**
    * Extract an optional nullable typed parameter from parameters
    *
    * @param param Parameter key
    * @param transformer A callable that can transform a string to the expected type T
    * @return A value that indicates whether the parameter was omitted, "null" or another String
    */
  def extractOptionalNullableParameter[T](param: String, transformer: String => T): Option[Option[T]] = {
    params.get(param) match {
      case Some(potentialStringOrNull) =>
        Some(potentialStringOrNull.trim.toLowerCase match {
          case "null" => None
          case string => Some(transformer(string))
        })
      case None => None
    }
  }

  /**
    *
    * @return A 2-tuple of Ints
    */
  def extractLimitAndOffset: (Int, Int) = {
    val limit = params.get("limit") match {
      case None => Config.defaultLimit
      case Some(potentialLimit) => potentialLimit.toInt
    }
    val offset = params.get("offset") match {
      case None => Config.defaultOffset
      case Some(potentialOffset) => potentialOffset.toInt
    }
    (limit, offset)
  }
}
