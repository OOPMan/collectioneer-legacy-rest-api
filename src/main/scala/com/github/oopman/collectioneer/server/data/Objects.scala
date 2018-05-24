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
    def >(right: LocalDateTime) = quote(infix"$left > $right".as[Boolean])
    def >=(right: LocalDateTime) = quote(infix"$left >= $right".as[Boolean])
    def <(right: LocalDateTime) = quote(infix"$left < $right".as[Boolean])
    def <=(right: LocalDateTime) = quote(infix"$left <= $right".as[Boolean])
  }

  type NullOption[T] = Option[Option[T]]
  type QuotedQuery[T] = Quoted[Query[T]]
  type QuotedQueryTransform[T] = Quoted[Query[T] => Query[T]]
  type QuotedQueryTransformWithValue[T, U] = Quoted[(Query[T], U) => Query[T]]

  case class QueryBuilder[T](quillQuery: QuotedQuery[T]) {

    def ifDefined[U: Encoder](value: Option[U])
                             (f: QuotedQueryTransformWithValue[T, U]): QueryBuilder[T] = {
      value match {
        case None => this
        case Some(v) => QueryBuilder {
          quote {
            f(quillQuery, lift(v))
          }
        }
      }
    }

    def ifEmpty[U: Encoder](value: Option[U])
                           (f: QuotedQueryTransform[T]): QueryBuilder[T] = {
      value match {
        case Some(_) => this
        case None => QueryBuilder(quote(f(quillQuery)))
      }
    }

    def ifInnerDefined[U: Encoder](value: NullOption[U])
                                  (f: QuotedQueryTransformWithValue[T, U]): QueryBuilder[T] = {
      value match {
        case Some(Some(v)) => QueryBuilder {
          quote {
            f(quillQuery, lift(v))
          }
        }
        case _ => this
      }
    }

    def ifInnerEmpty[U: Encoder](value: NullOption[U])
                                (f: QuotedQueryTransform[T]): QueryBuilder[T] = {
      value match {
        case Some(None) => QueryBuilder(quote(f(quillQuery)))
        case _ => this
      }
    }

    def build: QuotedQuery[T] = quillQuery
  }
}
