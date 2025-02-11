create database project;
use project;

drop table user;
drop table board;
drop table board_heart;
drop table reply;
drop table favorite_board_item;
drop table reply;
drop table recipe;
drop table recipe_input_keyword;

create table user 
( 
   	user_seq bigint auto_increment, 
	user_id varchar(255), 
	user_name VARCHAR(255), 
	user_email VARCHAR(255) unique, 
	created_at TIMESTAMP default CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP, 
	is_deleted BOOLEAN default false,
	constraint user_PK primary key (user_seq),
	unique key(user_id)
 );


create table board
(
    board_seq bigint auto_increment,
    board_writer varchar(50) not null,
    board_title varchar(200) default "Untitled",
    board_content varchar(4000),
    hit_count int default 0, -- 조회수
    create_date datetime default current_timestamp,
    update_date datetime default current_timestamp,
    original_file_name varchar(2000),
    saved_file_name varchar(2000),
    is_deleted BOOLEAN DEFAULT false,
    constraint board_PK primary key (board_seq)
);

CREATE table board_heart
(
    user_seq bigint not null,
    board_seq bigint not null,
    board_heart_seq bigint,
    is_hearted boolean default true,
	constraint board_heart_PK primary key (board_heart_seq),
    foreign key(board_seq) references board(board_seq),
    foreign key(user_seq) references user(user_seq) on delete cascade,
    unique key(board_seq, user_seq)
);



CREATE table favorite_board_item 
(
    user_seq bigint not null,
    board_seq bigint not null,
    favorite_seq bigint,
    is_favorited boolean default true,
    constraint favorite_board_item_PK primary key (favorite_seq),
    foreign key(board_seq) references board(board_seq),
    foreign key(user_seq) references user(user_seq) on delete cascade,
    unique key(board_seq, user_seq)
);




create table reply
(
    reply_seq bigint auto_increment,
    board_seq bigint,
    user_seq bigint not null,
    reply_content varchar(1000) not null,
    is_deleted BOOLEAN default FALSE,
    create_date datetime default current_timestamp,
    constraint reply_pk primary key (reply_seq),
    constraint reply_board_fk foreign key (board_seq) references board(board_seq) on Delete cascade,
	constraint reply_user_fk foreign key (user_seq) references user(user_seq)
);



Create table recipe
(
	recipe_seq bigint auto_increment,
	user_seq bigint not null, 
	recipe_output_content varchar(5000) not null,
	created_at datetime default current_timestamp,
	constraint recipe_PK primary key (recipe_seq),    
	constraint recipe_user_fk foreign key (user_seq) references user(user_seq) 
);


drop table recipe_input_keyword;

create table recipe_input_keyword
(
	recipe_seq bigint auto_increment,
	keyword varchar(100) not null,
	constraint recipe_input_keyword_pk primary key (recipe_seq, keyword),
    constraint recipe_input_keyword_fk foreign key(recipe_seq) references recipe(recipe_seq)     
);






