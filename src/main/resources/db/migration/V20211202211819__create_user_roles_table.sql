create table user_roles
(
    user_id varchar(36) not null
        constraint users_fk
            references users,
    role_id varchar(36) not null
        constraint roles_fk
            references roles,
    constraint user_roles_pkey
        primary key (user_id, role_id)
);