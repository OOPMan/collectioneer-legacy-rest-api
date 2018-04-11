package com.github.oopman.collectioneer.server.data

import java.time.Instant

/**
  * This object defines the model classes used with Quill
  */
object Models {
  case class Category(id: Option[Int],
                      parentId: Option[Int],
                      name: String)

  case class Collection(id: Option[Int],
                        name: String,
                        categoryId: Option[Int],
                        description: Option[String],
                        datetimeCreated: Option[Instant]=None,
                        datetimeModified: Option[Instant]=None,
                        deleted: Option[Boolean]=Some(false),
                        active: Option[Boolean]=Some(true))

  case class Item(id: Option[Int],
                  name: String,
                  categoryId: Option[Int],
                  version: Option[String],
                  data: Option[String],
                  datetimeCreated: Option[Instant]=None,
                  datetimeModified: Option[Instant]=None,
                  deleted: Option[Boolean]=Some(false),
                  active: Option[Boolean]=Some(true))

  case class CollectionItemAssn(collectionId: Int,
                                itemId: Int,
                                quantity: Option[Int]=Some(1))

  case class CollectionParentCollectionAssn(collectionId: Int,
                                            parentCollectionId: Int)

  case class Tag(name: String,
                 categoryId: Option[Int]=None,
                 data: Option[String]=None,
                 id: Option[Int]=None)

  case class TagCollectionAssn(tagId: Int,
                               collectionId: Int)

  case class TagItemAssn(tagId: Int,
                         itemId: Int)
}
