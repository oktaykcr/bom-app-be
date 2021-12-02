create table users
(
    id varchar(36) not null,
    email varchar(320) not null,
    username varchar(64) not null,
    password varchar(255) not null,
    enabled boolean default true,
    updated_date timestamp,
    created_date timestamp
);

create unique index users_email_uindex
	on users (email);

create unique index users_id_uindex
	on users (id);

create unique index users_username_uindex
	on users (username);

alter table users
    add constraint users_pk
        primary key (id);