DROP VIEW IF EXISTS view_moviegenres;
DROP VIEW IF EXISTS view_movieactors;

CREATE VIEW view_moviegenres AS
SELECT 
    movies.movie_id,
    movies.title,
	movies.description,
    STRING_AGG(genres.name, ' • ') AS genres_names,
    movies.release_year,
	movies.duration,
    movies.landscapeposter,
    movies.portraitposter,
	movies.deleted
FROM 
    movie_genres 
JOIN 
    movies ON movies.movie_id = movie_genres.movie_id
JOIN 
    genres ON genres.genre_id = movie_genres.genre_id
GROUP BY
    movies.movie_id, 
    movies.title,
	movies.description,
    movies.release_year,
	movies.duration,
    movies.landscapeposter, 
    movies.portraitposter;

CREATE VIEW view_movieactors AS
SELECT
	movies.movie_id,
    movies.title,
	movies.description,
	STRING_AGG(actors.name, ' • ') AS actors_names
FROM
	movie_actors
JOIN
	movies ON movies.movie_id = movie_actors.movie_id
JOIN
	actors ON actors.actor_id = movie_actors.actor_id
GROUP BY
	movies.movie_id, 
    movies.title,
	movies.description;


