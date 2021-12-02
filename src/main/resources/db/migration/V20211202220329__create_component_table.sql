create table component
(
    id varchar(36) not null,
    description varchar(255),
    image_url varchar(255),
    manufacturer_name varchar(255),
    part_number varchar(255),
    quantity_on_hand integer not null,
    supplier_link varchar(255),
    inventory_id varchar(36) not null
        constraint inventory_fk
            references inventory,
    created_date timestamp not null,
    updated_date timestamp not null
);

create unique index component_id_uindex
	on component (id);

alter table component
    add constraint component_pk
        primary key (id);

