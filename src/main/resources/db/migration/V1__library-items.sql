CREATE TABLE library_items (
id BIGINT AUTO_INCREMENT,
title VARCHAR(255),
item_type VARCHAR(255),
available_quantity int,
CONSTRAINT pk_library_items PRIMARY KEY (id)
);
