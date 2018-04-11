create table CATEGORY
(
  ID        INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_CFF10AEB_6B7F_4006_AF34_D69CFF4C4C6A)
    primary key,
  PARENT_ID INTEGER
    constraint CATEGORY_CATEGORY_ID_FK
    references CATEGORY,
  NAME      CLOB
);

create table COLLECTION
(
  ID                INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_70839B98_1CB0_48AD_BA42_F902010D431B)
    primary key,
  NAME              VARCHAR(255),
  CATEGORY_ID       INTEGER default NULL
    constraint COLLECTION_CATEGORY_ID_FK
    references CATEGORY,
  DESCRIPTION       CLOB    default 'NULL',
  DATETIME_CREATED  TIMESTAMP WITH TIME ZONE(30, 10) default NOW() not null,
  DATETIME_MODIFIED TIMESTAMP WITH TIME ZONE(30, 10) default NOW() not null,
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
  ID                INTEGER      default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_BBD3D322_26C2_404E_AF73_85E74248ECF0)
    primary key,
  NAME              VARCHAR(255)                                   not null,
  CATEGORY_ID       INTEGER      default NULL
    constraint ITEM_CATEGORY_ID_FK
    references CATEGORY,
  VERSION           VARCHAR(255) default 'NULL',
  DATA              CLOB         default 'NULL',
  DATETIME_CREATED  TIMESTAMP WITH TIME ZONE(30, 10) default NOW() not null,
  DATETIME_MODIFIED TIMESTAMP WITH TIME ZONE(30, 10) default NOW() not null,
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
  NAME        VARCHAR(255) not null
    primary key,
  CATEGORY_ID INTEGER default NULL
    constraint TAG_CATEGORY_ID_FK
    references CATEGORY,
  DATA        CLOB    default 'NULL'
);

create table TAG_COLLECTION_ASSN
(
  TAG_NAME      VARCHAR(255) not null
    constraint TAG_COLLECTION_ASSN_TAG_NAME_FK
    references TAG,
  COLLECTION_ID INTEGER      not null
    constraint TAG_COLLECTION_ASSN_COLLECTION_ID_FK
    references COLLECTION,
  constraint TAG_COLLECTION_ASSN_TAG_NAME_COLLECTION_ID_PK
  primary key (TAG_NAME, COLLECTION_ID)
);

create table TAG_ITEM_ASSN
(
  TAG_NAME VARCHAR(255) not null
    constraint TAG_ITEM_ASSN_TAG_NAME_FK
    references TAG,
  ITEM_ID  INTEGER      not null
    constraint TAG_ITEM_ASSN_ITEM_ID_FK
    references ITEM,
  constraint TAG_ITEM_ASSN_TAG_NAME_ITEM_ID_PK
  primary key (TAG_NAME, ITEM_ID)
);


