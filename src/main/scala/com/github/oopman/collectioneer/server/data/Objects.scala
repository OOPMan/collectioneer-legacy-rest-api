package com.github.oopman.collectioneer.server.data

import com.typesafe.config.{Config, ConfigFactory}
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

class Objects[Dialect <: SqlIdiom, Naming <: NamingStrategy](val context: JdbcContext[Dialect, Naming]) {
  val config: Config = ConfigFactory.load()
  val defaultOffset: Int = config.getInt("defaultOffset")
  val defaultLimit: Int = config.getInt("defaultLimit")
}
