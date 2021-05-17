create table Users
(
    id       integer
        constraint Users_pk
            primary key,
    username varchar not null,
    password varchar not null
);

create table Messages
(
    id          integer
        constraint Messages_pk
            primary key,
    sender      int
        references Users
            on update cascade on delete restrict,
    receiver    int
        references Users
            on update cascade on delete restrict,
    contentType varchar not null,
    content     varchar not null,
    timestamp   datetime default CURRENT_TIMESTAMP
);

