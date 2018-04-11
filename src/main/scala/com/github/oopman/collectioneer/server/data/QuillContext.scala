package com.github.oopman.collectioneer.server.data

import com.typesafe.config.{Config, ConfigFactory}
import io.getquill.{H2JdbcContext, NamingStrategy, PostgresJdbcContext}
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

object QuillContext {
  val config: Config = ConfigFactory.load()

  def apply[Naming <: NamingStrategy](configPrefix: String, naming: Naming): JdbcContext[_ <: SqlIdiom, Naming] = {
    val dataSourceClassName: String = config.getString(s"$configPrefix.dataSourceClassName")
    val context: JdbcContext[_ <: SqlIdiom, Naming] = dataSourceClassName match {
      case "org.h2.jdbcx.JdbcDataSource" => new H2JdbcContext(naming, configPrefix)
      case "org.postgresql.ds.PGSimpleDataSource" => new PostgresJdbcContext(naming, configPrefix)
      case _ => throw new RuntimeException(s"$dataSourceClassName is not supported")
    }
    context
  }
}
