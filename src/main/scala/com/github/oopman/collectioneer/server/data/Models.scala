package com.github.oopman.collectioneer.server.data

import java.time.LocalDateTime

/**
  * This object defines the model classes used with Quill
  */
object Models {
  case class Category(id: Int,
                      parentId: Option[Int],
                      name: String)

  case class Collection(id: Int,
                        name: String,
                        categoryId: Option[Int],
                        description: Option[String],
                        datetimeCreated: LocalDateTime,
                        datetimeModified: LocalDateTime,
                        deleted: Boolean=false,
                        active: Boolean=true)

  case class Item(id: Int,
                  name: String,
                  categoryId: Option[Int],
                  version: Option[String],
                  data: Option[String],
                  datetimeCreated: LocalDateTime,
                  datetimeModified: LocalDateTime,
                  deleted: Boolean=false,
                  active: Boolean=true)

  case class CollectionItemAssn(collectionId: Int,
                                itemId: Int,
                                quantity: Option[Int]=Some(1))

  case class CollectionParentCollectionAssn(collectionId: Int,
                                            parentCollectionId: Int)

  case class Tag(id: Int,
                 name: String,
                 categoryId: Option[Int]=None,
                 data: Option[String]=None)

  case class TagCollectionAssn(tagId: Int,
                               collectionId: Int)

  case class TagItemAssn(tagId: Int,
                         itemId: Int)
}
