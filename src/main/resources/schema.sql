 drop table if exists member CASCADE;
CREATE TABLE member (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    member_email VARCHAR(100),
    member_password VARCHAR(20),
    member_name VARCHAR(20),
    member_nickname VARCHAR(20),
    member_introduce VARCHAR(255),
    role VARCHAR(255),
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    expired BOOLEAN,
    enabled BOOLEAN,
    email_confirmed BOOLEAN,
    PRIMARY KEY (id)
);