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
    * Extract an optional nullable String parameter
    *
    * @param param Parameter key
    * @return A value that indicates whether the parameter was omitted, "null" or another String
    */
  def extractOptionalNullableParameter(param: String): Option[Option[String]] = {
    params.get(param) match {
      case Some(potentialStringOrNull) =>
        Some(potentialStringOrNull.trim.toLowerCase match {
          case "null" => None
          case potentialString => Some(potentialString)
        })
      case None => None
    }
  }

  /**
    * Extract an optional nullable Int parameter
    *
    * @param param Parameter key
    * @return A value that indicates whether the parameter was omitted, "null" or an Int
    */
  def extractOptionalNullableIntParameter(param: String): Option[Option[Int]] = {
    extractOptionalNullableParameter(param) match {
      case Some(Some(value)) => Some(Some(value.toInt))
      case Some(None) => Some(None)
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
