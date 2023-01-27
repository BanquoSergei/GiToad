drop table if exists accounts cascade;
create table accounts (id varchar(255) not null, installation_token oid, jwt_token oid, oauth_token oid, password oid, username oid, primary key (id));