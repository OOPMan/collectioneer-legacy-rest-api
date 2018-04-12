create table CATEGORY
(
  ID        INTEGER AUTO_INCREMENT primary key,
  PARENT_ID INTEGER
    constraint CATEGORY_CATEGORY_ID_FK
    references CATEGORY,
  NAME      CLOB
);

create table COLLECTION
(
  ID                INTEGER AUTO_INCREMENT primary key,
  NAME              VARCHAR(255),
  CATEGORY_ID       INTEGER default NULL
    constraint COLLECTION_CATEGORY_ID_FK
    references CATEGORY,
  DESCRIPTION       CLOB,
  DATETIME_CREATED  TIMESTAMP WITH TIME ZONE default NOW() not null,
  DATETIME_MODIFIED TIMESTAMP WITH TIME ZONE default NOW() not null,
  DELETED           BOOLEAN default FALSE                          not null,
  ACTIVE            BOOLEAN default TRUE                           not null
);

create index COLLECTION_ACTIVE_INDEX
  on COLLECTION (ACTIVE);

create index COLLECTION_DELETED_INDEX
  on COLLECTION (DELETED);

create index COLLECTION_NAME_INDEX
  on COLLECTION (NAME);

create table COLLECTION_PARENT_COLLECTION_ASSN
(
  COLLECTION_ID        INTEGER              not null
    constraint COLLECTION__PARENT_COLLECTION_COLLECTION_ID_FK
    references COLLECTION,
  PARENT_COLLECTION_ID INTEGER              not null
    constraint COLLECTION__PARENT_COLLECTION_COLLECTION_ID_FK_2
    references COLLECTION,
  EXCLUSIVE            BOOLEAN default TRUE not null,
  constraint COLLECTION__PARENT_COLLECTION_COLLECTION_ID_PARENT_COLLECTION_ID_PK
  primary key (COLLECTION_ID, PARENT_COLLECTION_ID)
);

create table ITEM
(
  ID                INTEGER AUTO_INCREMENT primary key,
  NAME              VARCHAR(255)                                   not null,
  CATEGORY_ID       INTEGER      default NULL
    constraint ITEM_CATEGORY_ID_FK
    references CATEGORY,
  VERSION           VARCHAR(255),
  DATA              CLOB,
  DATETIME_CREATED  TIMESTAMP WITH TIME ZONE default NOW() not null,
  DATETIME_MODIFIED TIMESTAMP WITH TIME ZONE default NOW() not null,
  DELETED           BOOLEAN default FALSE                          not null,
  ACTIVE            BOOLEAN default TRUE                           not null
);

create table COLLECTION_ITEM_ASSN
(
  COLLECTION_ID INTEGER           not null
    constraint COLLECTION__ITEM_COLLECTION_ID_FK
    references COLLECTION,
  ITEM_ID       INTEGER           not null
    constraint COLLECTION__ITEM_ITEM_ID_FK
    references ITEM,
  QUANTITY      INTEGER default 1 not null,
  constraint COLLECTION__ITEM_COLLECTION_ID_ITEM_ID_PK
  primary key (COLLECTION_ID, ITEM_ID)
);

create index ITEM_ACTIVE_INDEX
  on ITEM (ACTIVE);

create index ITEM_DELETED_INDEX
  on ITEM (DELETED);

create index ITEM_NAME_INDEX
  on ITEM (NAME);

create index ITEM_NAME_VERSION_INDEX
  on ITEM (NAME, VERSION);

create index ITEM_VERSION_INDEX
  on ITEM (VERSION);

create table TAG
(
  ID          INTEGER AUTO_INCREMENT PRIMARY KEY,
  NAME        CLOB not null,
  CATEGORY_ID INTEGER default NULL
    constraint TAG_CATEGORY_ID_FK
    references CATEGORY,
  DATA        CLOB
);

create table TAG_COLLECTION_ASSN
(
  TAG_ID      INTEGER not null
    constraint TAG_COLLECTION_ASSN_TAG_ID_FK
    references TAG,
  COLLECTION_ID INTEGER      not null
    constraint TAG_COLLECTION_ASSN_COLLECTION_ID_FK
    references COLLECTION,
  constraint TAG_COLLECTION_ASSN_TAG_ID_COLLECTION_ID_PK
  primary key (TAG_ID, COLLECTION_ID)
);

create table TAG_ITEM_ASSN
(
  TAG_ID INTEGER not null
    constraint TAG_ITEM_ASSN_TAG_ID_FK
    references TAG,
  ITEM_ID  INTEGER      not null
    constraint TAG_ITEM_ASSN_ITEM_ID_FK
    references ITEM,
  constraint TAG_ITEM_ASSN_TAG_ID_ITEM_ID_PK
  primary key (TAG_ID, ITEM_ID)
);


