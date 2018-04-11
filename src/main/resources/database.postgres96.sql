-- we don't know how to generate database collectioneer (class Database) :(
create table category
(
  id        serial not null
    constraint category_pkey
    primary key,
  parent_id integer
    constraint category_category_id_fk
    references category,
  name      text
);

create table collection
(
  id                serial                                 not null
    constraint collection_pkey
    primary key,
  name              text                                   not null,
  category_id       integer
    constraint collection_category_id_fk
    references category,
  description       text,
  datetime_created  timestamp with time zone default now() not null,
  datetime_modified timestamp with time zone default now() not null,
  deleted           boolean default false                  not null,
  active            boolean default true                   not null
);

create index collection_name_index
  on collection (name);

create index collection_deleted_index
  on collection (deleted);

create index collection_active_index
  on collection (active);

create table item
(
  id                serial                                 not null
    constraint item_pkey
    primary key,
  name              text                                   not null,
  category_id       integer
    constraint item_category_id_fk
    references category,
  version           text,
  data              text,
  datetime_created  timestamp with time zone default now() not null,
  datetime_modified timestamp with time zone default now() not null,
  deleted           boolean default false                  not null,
  active            boolean default true                   not null
);

create index item_name_index
  on item (name);

create index item_version_index
  on item (version);

create index item_name_version_index
  on item (name, version);

create index item_deleted_index
  on item (deleted);

create index item_active_index
  on item (active);

create table collection_item_assn
(
  collection_id integer           not null
    constraint collection_item_assn_collection_id_fk
    references collection,
  item_id       integer           not null
    constraint collection_item_assn_item_id_fk
    references item,
  quantity      integer default 1 not null,
  constraint collection_item_assn_collection_id_item_id_pk
  primary key (collection_id, item_id)
);

create table collection_parent_collection_assn
(
  collection_id        integer               not null
    constraint collection_parent_collection_assn_collection_id_fk
    references collection,
  parent_collection_id integer               not null
    constraint collection_parent_collection_assn_collection_id_fk_2
    references collection,
  exclusive            boolean default false not null,
  constraint collection_parent_collection_assn_collection_id_parent_collecti
  primary key (collection_id, parent_collection_id)
);

create table tag
(
  id          serial not null
    constraint tag_pkey
    primary key,
  category_id integer
    constraint tag_category_id_fk
    references category,
  name        text   not null,
  data        text
);

create table tag_collection_assn
(
  tag_id        integer not null
    constraint tag_collection_assn_tag_id_fk
    references tag,
  collection_id integer not null
    constraint tag_collection_assn_collection_id_fk
    references collection,
  constraint tag_collection_assn_tag_id_collection_id_pk
  primary key (tag_id, collection_id)
);

create table tag_item_assn
(
  tag_id  integer not null
    constraint tag_item_assn_tag_id_fk
    references tag,
  item_id integer not null
    constraint tag_item_assn_item_id_fk
    references item,
  constraint tag_item_assn_tag_id_item_id_pk
  primary key (tag_id, item_id)
);


