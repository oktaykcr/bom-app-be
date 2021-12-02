create table component_used
(
    id varchar(36) not null,
    cost integer not null,
    lead_time integer not null,
    quantity integer not null,
    bom_id varchar(36) not null
        constraint bom_fk
            references bom,
    component_id varchar(36) not null
        constraint component_fk
            references component,
    created_date timestamp not null,
    updated_date timestamp not null
);

create unique index component_used_id_uindex
	on component_used (id);

alter table component_used
    add constraint component_used_pk
        primary key (id);

