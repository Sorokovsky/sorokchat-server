create schema if not exists sorokchat;
set search_path to sorokchat, public;

create table if not exists sorokchat.roles (
    id serial primary key,
    role varchar unique not null
);

insert into sorokchat.roles (role) values ('USER'), ('PRO'), ('ADMIN');

create table if not exists sorokchat.chat_roles (
  id serial primary key,
  name varchar unique
);

insert into sorokchat.chat_roles (name) values ('MEMBER'), ('ADMIN');

create table if not exists sorokchat.users (
    id serial primary key,
    login varchar not null unique,
    password varchar not null,
    display_name varchar not null
);

create table if not exists sorokchat.users_roles (
    id serial primary key,
    user_id int references sorokchat.users(id) not null,
    role_id int references sorokchat.roles(id) not null,
    constraint uk_user_role unique (user_id, role_id)
);

create table if not exists sorokchat.chats (
    id serial primary key,
    name varchar not null,
    description varchar
);

create table if not exists sorokchat.participants (
    id serial primary key,
    user_id int references sorokchat.users(id) not null,
    chat_id int references sorokchat.chats(id) not null,
    constraint uk_user_chat unique (chat_id, user_id)
);

create table if not exists sorokchat.participants_roles (
    id serial primary key,
    participant_id int references sorokchat.participants(id) not null,
    chat_role_id int references  sorokchat.chat_roles(id) not null,
    constraint uk_participant_role unique (participant_id, chat_role_id)
);

create table if not exists sorokchat.messages (
    id serial primary key,
    text text not null,
    signing text not null,
    author_id int references sorokchat.users(id) not null,
    chat_id int references sorokchat.chats(id) not null,
    delivered_count int default 0,
    created_at pg_catalog.timestamptz default current_timestamp
);

create or replace function sorokchat.create_message(
    user_id int,
    channel_id int,
    content text,
    created_signing text
)
returns sorokchat.messages
language plpgsql
as $$
declare
    result sorokchat.messages%rowtype;
begin
    insert into sorokchat.messages(text, signing, author_id, chat_id)
    values (content, created_signing, user_id, channel_id)
    returning * into result;
    return result;
end;
$$;

create or replace view sorokchat.users_with_roles as
select u.id, u.login, u.password, u.display_name, array_agg(r.role) as roles from sorokchat.users u
left join sorokchat.users_roles ur on u.id = ur.user_id
left join sorokchat.roles r on ur.role_id = r.id
group by u.id, u.login, u.password, u.display_name;

create or replace function sorokchat.create_user (
    new_login varchar,
    new_password varchar,
    new_display_name varchar default null
)
returns sorokchat.users_with_roles
language plpgsql
as $$
declare
    new_user_id int;
    new_role_id int;
    result sorokchat.users_with_roles;
begin
    insert into sorokchat.users (login, password, display_name)
    values (new_login, new_password, coalesce(new_display_name, new_login))
    returning id into new_user_id;
    select id into new_role_id from sorokchat.roles r where r.role = 'USER';
    insert into sorokchat.users_roles (user_id, role_id) values (new_user_id, new_role_id);
    select * into result from sorokchat.users_with_roles where id = new_user_id;
    return result;
end;
$$;