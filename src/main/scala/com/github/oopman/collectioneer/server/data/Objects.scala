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

  type QuotedQuery[T] = Quoted[Query[T]]
  type QuotedQueryTransform[T] = Quoted[Query[T] => Query[T]]
  type QuotedQueryTransformWithValue[T, U] = Quoted[(Query[T], U) => Query[T]]

  /**
    * The QueryBuilder allows for the transformation of Queries in a conditional,
    * step-wise fashion.
    *
    * Query transformation is handled by defining code to be executed in the
    * event of input Option instances to be executed in the event that the
    * Option instance is either defined or empty.
    *
    * @param query A base Query to build from
    * @tparam T Model type to generate queries with
    */
  case class QueryBuilder[T](query: QuotedQuery[T]) {

    /**
      * If the input value is defined, execute a transformation function against
      * the Query wrapped by this QueryBuilder in conjunction with the defined
      * value and return a new QueryBuilder instance
      *
      * @param value
      * @param f
      * @tparam U
      * @return
      */
    def transformIfDefined[U: Encoder](value: Option[U])
                                      (f: QuotedQueryTransformWithValue[T, U]): QueryBuilder[T] = {
      value match {
        case None => this
        case Some(v) => QueryBuilder {
          quote {
            f(query, lift(v))
          }
        }
      }
    }

    /**
      * If the input is empty, execute a transformation function against the
      * Query wrapped by this QueryBuilder and return a new QueryBuilder instance
      *
      * @param value
      * @param f
      * @tparam U
      * @return
      */
    def transformIfEmpty[U: Encoder](value: Option[U])
                                    (f: QuotedQueryTransform[T]): QueryBuilder[T] = {
      value match {
        case Some(_) => this
        case None => QueryBuilder(quote(f(query)))
      }
    }

    /**
      * If the input value is defined, execute a transformation function against
      * this QueryBuilder in conjunction with the defined value and return a new
      * QueryBuilder instance
      *
      * @param value
      * @param f
      * @tparam U
      * @return
      */
    def descendIfDefined[U: Encoder](value: Option[U])
                                    (f: (QueryBuilder[T], U) => QueryBuilder[T]): QueryBuilder[T] = {
      value match {
        case None => this
        case Some(v) => f(this, v)
      }
    }

    /**
      * If the input value is empty, execute a transformation function against
      * this QueryBuilder and return a new QueryBuilder instance
      *
      * @param value
      * @param f
      * @tparam U
      * @return
      */
    def descendIfEmpty[U: Encoder](value: Option[U])
                                  (f: QueryBuilder[T] => QueryBuilder[T]): QueryBuilder[T] = {
      value match {
        case None => f(this)
        case _ => this
      }
    }

    /**
      * Return the Query wrapped within this QueryBuilder instance
      *
      * @return
      */
    def build: QuotedQuery[T] = query
  }
}
