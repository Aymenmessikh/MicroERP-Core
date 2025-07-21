-- V1__Create_Tables.sql

create table if not exists authority_type(
    id BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    libelle varchar(255) not null unique,
    actif BOOLEAN not null
    );
create table if not exists groupe(
    id  BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    libelle varchar(255) not null unique,
    actif BOOLEAN not null
    );
create table if not exists module(
    id  BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    module_name varchar(255) not null unique,
    module_code varchar(255) not null unique,
    uri varchar(255) not null,
    color varchar(255) not null,
    icon varchar(255) not null,
    actif BOOLEAN not null
    );
create table if not exists role(
    id BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    libelle varchar(255) not null unique,
    actif BOOLEAN not null,
    module_id BIGINT not null references module(id)
    );
create table  if not exists authority(
    id BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    libelle varchar(255) not null unique,
    actif BOOLEAN not null,
    module_id BIGINT not null references module(id),
    authority_Type_id BIGINT not null references authority_type(id)
    );
create table if not exists _user(
    id BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    email varchar(255) not null unique,
    user_name varchar(255) not null unique,
    uuid varchar(255) not null unique,
    phone_number varchar(255) not null unique,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    actif BOOLEAN not null
    );
create table if not exists profile(
    id BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    libelle varchar(255) not null,
    actif BOOLEAN not null,
    user_id BIGINT not null references _user(id),
    groupe_id BIGINT references groupe(id)
    );
create table if not exists profile_authority(
    id BIGINT GENERATED ALWAYS AS IDENTITY not null unique primary key,
    granted BOOLEAN not null,
    profile_id BIGINT not null references profile(id),
    authority_id BIGINT not null references authority(id)
    );
create table if not exists role_authority (
    role_id BIGINT not null references role(id),
    authority_id BIGINT not null references authority(id),
    PRIMARY KEY (role_id, authority_id)
    );
create table if not exists profile_role (
    profile_id BIGINT not null references profile(id),
    role_id BIGINT not null references role(id),
    PRIMARY KEY (profile_id, role_id)
    );
create table if not exists profile_module (
    profile_id BIGINT not null references profile(id),
    module_id BIGINT not null references module(id),
    PRIMARY KEY (profile_id, module_id)
    );
alter table if exists _user
    add column actif_profile_id BIGINT  unique references profile(id)
