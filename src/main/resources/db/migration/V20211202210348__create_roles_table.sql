create table roles
(
    id varchar(36) not null,
    name varchar(64) not null,
    created_date timestamp,
    updated_date timestamp
);

create unique index roles_id_uindex
	on roles (id);

create unique index roles_name_uindex
	on roles (name);

alter table roles
    add constraint roles_pk
        primary key (id);