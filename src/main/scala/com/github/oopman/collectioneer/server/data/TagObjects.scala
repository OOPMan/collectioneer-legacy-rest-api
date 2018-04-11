package com.github.oopman.collectioneer.server.data

import com.github.oopman.collectioneer.server.data.Models.Tag
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

class TagObjects[Dialect <: SqlIdiom, Naming <: NamingStrategy](override val context: JdbcContext[Dialect, Naming]) extends Objects(context) {
  import context._

  /**
    * Retrieve a single Tag by name
    *
    * @param name
    * @return
    */
  def getTag(name: String): Option[Tag] = context.run(query[Tag].filter(_.name == lift(name))) match {
    case List(tag) => Some(tag)
    case Nil => None
  }


  /**
    * Retrieve a Sequence of Tags constrained by various input parameters
    *
    * @param offset Offset within Tags dataset to retrieve
    * @param limit Number of Tags to retrieve
    * @return A Sequence of Tags
    */
  def getTags(offset: Int=defaultOffset, limit: Int=defaultLimit): Seq[Tag] = {
    context.run(query[Tag].drop(lift(offset)).take(lift(limit)))
  }

  /**
    * Insert a Tag object and return it
    *
    * @param name Name of the Tag. This is the primary key and thus must be unique within the dataset
    * @param categoryId Optional. ID of the Category to associate the Tag with
    * @param data Optional. Additional data associated with the Tag
    * @return
    */
  def insertTag(name: String,
                categoryId: Option[Int]=None,
                data: Option[String]=None): Tag = {
    val tag = Tag(name=name, categoryId=categoryId, data=data)
    context.run(query[Tag].insert(lift(tag)))
    tag
  }

}
