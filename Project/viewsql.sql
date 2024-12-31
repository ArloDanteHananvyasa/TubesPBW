CREATE VIEW view_homepage AS
SELECT 
    movies.movie_id,
    movies.title,
    STRING_AGG(genres.name, ', ') AS genres_names,
    movies.release_year,
    movies.landscapeposter,
    movies.portraitposter
FROM 
    movie_genres 
JOIN 
    movies ON movies.movie_id = movie_genres.movie_id
JOIN 
    genres ON genres.genre_id = movie_genres.genre_id
GROUP BY
    movies.movie_id, 
    movies.title, 
    movies.release_year, 
    movies.landscapeposter, 
    movies.portraitposter;