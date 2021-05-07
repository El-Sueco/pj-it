INSERT INTO algo (name, friendly_name)
VALUES ('LongestCommonSubsequenceDistance', 'Longest Common Subsequence Distance'),
       ('FuzzyScore', 'Fuzzy Score');
UPDATE algo SET active = 0 WHERE name = 'CosineSimilarity';