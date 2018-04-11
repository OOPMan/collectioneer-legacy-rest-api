package com.github.oopman.collectioneer.server.data

import java.time.Instant

object Models {
  case class Category(id: Option[Int],
                      parentId: Option[Int],
                      name: String)

  case class Collection(id: Option[Int],
                        name: String,
                        categoryId: Option[Int],
                        description: Option[String],
                        datetimeCreated: Option[Instant],
                        datetimeModified: Option[Instant],
                        deleted: Option[Boolean],
                        active: Option[Boolean])

  case class Item(id: Option[Int],
                  name: String,
                  categoryId: Option[Int],
                  version: Option[String],
                  data: Option[String],
                  datetimeCreated: Option[Instant],
                  datetimeModified: Option[Instant],
                  deleted: Option[Boolean],
                  active: Option[Boolean])

  case class CollectionItemAssn(collectionId: Int,
                                itemId: Int,
                                quantity: Option[Int])

  case class CollectionParentCollectionAssn(collectionId: Int,
                                            parentCollectionId: Int)

  case class Tag(name: String,
                 categoryId: Option[Int],
                 data: Option[String])

  case class TagCollectionAssn(tagName: String,
                               collectionId: Int)

  case class TagItemAssn(tagName: String,
                         itemId: Int)
}
