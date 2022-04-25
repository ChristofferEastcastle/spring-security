create sequence hibernate_sequence start 1;

create table animals(
    id serial primary key,
    name varchar(50),
    type varchar(20),
    age int,
    health int
);

create table users(
    id serial primary key,
    username varchar(50),
    password varchar(100)
);

create table authorities(
    id serial primary key,
    name varchar(20)
);