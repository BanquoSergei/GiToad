drop table if exists accounts cascade;
create table accounts (id varchar(255) not null, authorities smallint array, jwt bytea, primary key (id));