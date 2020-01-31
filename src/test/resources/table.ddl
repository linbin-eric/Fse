create schema if not exists inst01 collate utf8_general_ci;

create table if not exists account
(
	id int auto_increment
		primary key,
	amount int null,
	froze int null
);

create table if not exists fasttcc
(
	global_id varchar(100) not null,
	branch_id varchar(100) not null,
	create_time bigint not null,
	identifier varchar(50) null
);

create index fasttcc_global_id_idx
	on fasttcc (global_id);

