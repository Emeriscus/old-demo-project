CREATE TABLE audios (
id BIGINT AUTO_INCREMENT,
library_items_id BIGINT,
title VARCHAR(255),
performers VARCHAR(255),
composers VARCHAR(255),
year_of_publication int,
CONSTRAINT pk_audios PRIMARY KEY (id)
);