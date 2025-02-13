CREATE DATABASE project;
USE project;

-- Drop tables in correct order to avoid FK constraint issues
DROP TABLE IF EXISTS recipe_output_content;
DROP TABLE IF EXISTS recipe_input_keyword;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS board_heart;
DROP TABLE IF EXISTS board_image;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS user;

-- User Table
CREATE TABLE user 
( 
    user_seq BIGINT AUTO_INCREMENT, 
    user_id VARCHAR(30) NOT NULL, 
    user_name VARCHAR(50) NOT NULL, 
    user_password VARCHAR(60),
    user_email VARCHAR(254), 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,  # java의 localDateTime은 timezone이 없으므로 timestamp보다는 DATETIME으로
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    roles VARCHAR(20) DEFAULT 'ROLE_USER',
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT user_PK PRIMARY KEY (user_seq),
    CONSTRAINT user_roles_CHK CHECK (roles IN ('ROLE_USER', 'ROLE_ADMIN')),
    CONSTRAINT user_id_UK UNIQUE (user_id),
    CONSTRAINT user_email_UK UNIQUE (user_email),
    CONSTRAINT user_name_UK UNIQUE (user_name)
);

-- Board Table
CREATE TABLE board
(
    board_seq BIGINT AUTO_INCREMENT,
    user_seq BIGINT, 
    board_title VARCHAR(100) DEFAULT "Untitled",
    board_content VARCHAR(4000),
    hit_count INT DEFAULT 0,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT board_PK PRIMARY KEY (board_seq),
    CONSTRAINT board_user_FK FOREIGN KEY (user_seq) REFERENCES user(user_seq) ON DELETE SET null #user가 없어져도 board는 남겨야한다. 대신 user는 null로 들어가게 됨.
);

-- Board Image Table
CREATE TABLE board_image
(	
    image_seq BIGINT AUTO_INCREMENT,
    board_seq BIGINT NOT NULL,
    original_file_name VARCHAR(255),
    saved_file_name VARCHAR(255),
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT board_image_PK PRIMARY KEY (image_seq),
    CONSTRAINT board_image_board_FK FOREIGN KEY (board_seq) REFERENCES board(board_seq) ON DELETE CASCADE
);

-- Board Heart (Likes) Table
CREATE TABLE board_heart
(
    board_heart_seq BIGINT AUTO_INCREMENT,
    user_seq BIGINT NOT NULL,
    board_seq BIGINT NOT NULL,
    is_hearted BOOLEAN DEFAULT FALSE,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT board_heart_PK PRIMARY KEY (board_heart_seq),
    CONSTRAINT board_heart_board_FK FOREIGN KEY (board_seq) REFERENCES board(board_seq) ON DELETE CASCADE,
    CONSTRAINT board_heart_user_FK FOREIGN KEY (user_seq) REFERENCES user(user_seq) ON DELETE CASCADE, # 한 게시물에 복수 개의 공감. user 지워진다고 모두 null로 바꾸면 (1, null) 등의 row가 여러개  생겨 unique key 조건 위반이됨.
    UNIQUE KEY (board_seq, user_seq)
);

-- Reply Table
CREATE TABLE reply
(
    reply_seq BIGINT AUTO_INCREMENT,
    board_seq BIGINT NOT NULL,
    user_seq BIGINT,
    reply_content VARCHAR(300) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT reply_PK PRIMARY KEY (reply_seq),
    CONSTRAINT reply_board_FK FOREIGN KEY (board_seq) REFERENCES board(board_seq) ON DELETE CASCADE,
    CONSTRAINT reply_user_FK FOREIGN KEY (user_seq) REFERENCES user(user_seq)ON DELETE SET null #user가 없어져도 reply는 남겨야한다. 대신 user는 null로 들어가게 됨.
);

-- Recipe Table
CREATE TABLE recipe
(
    recipe_seq BIGINT AUTO_INCREMENT,
    user_seq BIGINT NOT NULL, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT recipe_PK PRIMARY KEY (recipe_seq),    
    CONSTRAINT recipe_user_FK FOREIGN KEY (user_seq) REFERENCES user(user_seq) ON DELETE set null 
);

-- Recipe Input Keywords Table
CREATE TABLE recipe_input_keyword
(
    recipe_seq BIGINT NOT NULL,
    keyword VARCHAR(30) NOT NULL,
    CONSTRAINT recipe_input_keyword_PK PRIMARY KEY (recipe_seq, keyword),
    CONSTRAINT recipe_input_keyword_FK FOREIGN KEY (recipe_seq) REFERENCES recipe(recipe_seq) ON DELETE CASCADE
);

-- Recipe Output Content Table (Fixed PK Name)
CREATE TABLE recipe_output_content
(
    recipe_output_content_seq BIGINT AUTO_INCREMENT,
    recipe_seq BIGINT NOT NULL,
    output_content VARCHAR(300) NOT NULL,
    CONSTRAINT recipe_output_content_PK PRIMARY KEY (recipe_output_content_seq),
    CONSTRAINT recipe_output_content_recipe_FK FOREIGN KEY (recipe_seq) REFERENCES recipe(recipe_seq) ON DELETE CASCADE
);
