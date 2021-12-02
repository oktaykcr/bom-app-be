create table inventory
(
    id varchar(36) not null,
    user_id varchar(36) not null
        constraint users_fk
            references users,
    created_date timestamp not null,
    updated_date timestamp not null
);

create unique index inventory_id_uindex
	on inventory (id);

alter table inventory
    add constraint inventory_pk
        primary key (id);

