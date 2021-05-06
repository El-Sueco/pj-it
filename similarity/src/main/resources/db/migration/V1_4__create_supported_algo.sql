CREATE TABLE algo (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  friendly_name varchar(100) NOT NULL,
  active boolean DEFAULT TRUE,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO algo (name, friendly_name)
VALUES ('HammingDistance', 'Hamming Distance'),
       ('CosineDistance', 'Cosine Distance'),
       ('CosineSimilarity', 'Cosine Similarity'),
       ('JaroWinklerDistance', 'Jaro Winkler Distance'),
       ('JaroWinklerSimilarity', 'Jaro Winkler Similarity'),
       ('LevenshteinDistance', 'Levenshtein Distance');