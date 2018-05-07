package com.github.oopman.collectioneer.server.data

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

class Objects[Dialect <: SqlIdiom, Naming <: NamingStrategy](val context: JdbcContext[Dialect, Naming]) {
}
