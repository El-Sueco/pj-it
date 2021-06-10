CREATE TABLE similarity (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    file1_id bigint(20) NOT NULL,
    file2_id bigint(20) NOT NULL,
    score double NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;