CREATE TABLE IF NOT EXISTS book (
	id                   CHAR(36),
	name                 CHAR(60),
	author               CHAR(60),
	deleted              BOOLEAN,
	PRIMARY KEY (id)
);
