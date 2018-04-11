package com.github.oopman.collectioneer.server.servlets

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class CollectioneerServlet[Dialect <: SqlIdiom, Naming <: NamingStrategy](val ctx: JdbcContext[Dialect, Naming]) extends ScalatraServlet with NativeJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  get("/") {
    views.html.hello()
  }

  get("/tags") {
    contentType = formats("json")
  }

//  post("/tags") {
//    val body = request.body
//    val tagJSON = parse(body)
//    val tag = tagJSON.extract[Tag]
//    dao.run(query[Tag].insert(lift(tag)))
//    response.setStatus(201)
//  }
}
