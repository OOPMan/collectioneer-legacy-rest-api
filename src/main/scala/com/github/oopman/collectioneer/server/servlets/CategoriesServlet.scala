package com.github.oopman.collectioneer.server.servlets

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

/**
  * This servlet manages interactions with Category objects within the system
  * @param ctx
  * @tparam Dialect
  * @tparam Naming
  */
class CategoriesServlet[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val ctx: JdbcContext[Dialect, Naming]) extends CollectioneerServlet(ctx) {

}
