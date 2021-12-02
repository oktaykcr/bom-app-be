create table bom
(
    id varchar(36) not null,
    title varchar(64) not null,
    description varchar(255),
    user_id varchar(36) not null
        constraint user_fk
            references users,
    created_date timestamp not null,
    updated_date timestamp not null
);

create unique index bom_id_uindex
	on bom (id);

alter table bom
    add constraint bom_pk
        primary key (id);