package com.github.oopman.collectioneer.server

import com.typesafe.config.{Config, ConfigFactory}

object Config {
  private val config: Config = ConfigFactory.load()
  val defaultOffset: Int = config.getInt("defaultOffset")
  val defaultLimit: Int = config.getInt("defaultLimit")
}
