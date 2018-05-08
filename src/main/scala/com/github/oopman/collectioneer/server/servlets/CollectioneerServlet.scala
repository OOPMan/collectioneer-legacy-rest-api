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

  def extractLimitAndOffset(params: Params): (Int, Int) = {
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
