create database post_project;
use post_project;

CREATE TABLE users (
  user_id int NOT NULL AUTO_INCREMENT,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY email (email)
);

create table posts(
	post_id int auto_increment primary key,
    user_id int not null,
    title varchar(255),
    content varchar(500),
    author varchar(100),
    create_at datetime,
    foreign key (user_id) references users(user_id)
);