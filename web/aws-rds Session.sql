CREATE TABLE users (
    uid int not null auto_increment primary key
    username char(32),
    pwd char(36),
    role char(16),
)
