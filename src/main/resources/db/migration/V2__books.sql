CREATE TABLE books (
id BIGINT AUTO_INCREMENT,
library_items_id bigint,
title VARCHAR(255),
authors VARCHAR(255),
year_of_publication int,
CONSTRAINT pk_books PRIMARY KEY (id)
);
