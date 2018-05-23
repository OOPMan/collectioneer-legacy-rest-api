package com.github.oopman.collectioneer.server.data

import java.time.LocalDateTime

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

class Objects[Dialect <: SqlIdiom, Naming <: NamingStrategy](val context: JdbcContext[Dialect, Naming]) {
  import context._

  /**
    * Implicit class to perform comparisons between LocalDateTime instances
    * with Quill. See http://getquill.io/#extending-quill-infix-comparison-operators
    *
    * @param left LocalDateTime to compare with
    */
  implicit class LocalDateTimeQuotes(left: LocalDateTime) {
    def >(right: LocalDateTime): Quoted[Boolean] = quote(infix"$left > $right".as[Boolean])
    def >=(right: LocalDateTime): Quoted[Boolean] = quote(infix"$left >= $right".as[Boolean])
    def <(right: LocalDateTime): Quoted[Boolean] = quote(infix"$left < $right".as[Boolean])
    def <=(right: LocalDateTime): Quoted[Boolean] = quote(infix"$left <= $right".as[Boolean])
  }
}
