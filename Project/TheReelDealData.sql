DROP TABLE IF EXISTS transaction_details CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS movie_genres CASCADE;
DROP TABLE IF EXISTS movie_actors CASCADE;
DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS admins CASCADE;

-- Create Tables:
CREATE TABLE users (
    phone VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
	name VARCHAR (50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
	role VARCHAR(255) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE movies (
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
	description VARCHAR(500) NOT NULL,
	release_year VARCHAR(4) NOT NULL,
    duration INT NOT NULL,  -- Duration in minutes
    base_price NUMERIC NOT NULL,  -- Price per day of rental
    landscapePoster VARCHAR(500),
    portraitPoster VARCHAR(500),
	stock INT NOT NULL, --stock
    deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE genres (
    genre_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE movie_genres (
    movie_id INT,
    genre_id INT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);


CREATE TABLE actors (
    actor_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE movie_actors (
    movie_id INT,
    actor_id INT,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES actors(actor_id) ON DELETE CASCADE
);

CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    phone VARCHAR(20),
    base_fee NUMERIC NOT NULL,
    pickup_date DATE NOT NULL,
	isPickedUp BOOLEAN DEFAULT False,
    due_date DATE NOT NULL,
	return_date DATE,
    transaction_date DATE NOT NULL,
	days INT NOT NULL,
	late_fee NUMERIC DEFAULT 0,
    FOREIGN KEY (phone) REFERENCES users(phone) ON DELETE CASCADE
);




CREATE TABLE transaction_details (
    transaction_id INT,
    movie_id INT,
	PRIMARY KEY (movie_id, transaction_id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE
);



-- Dummy Data
INSERT INTO users (phone, username, name, email, password, role, deleted) VALUES
-- ('0812987328', 'd8Ty', 'Arlo', 'john.doe@example.com', 'd8ty', 'user', FALSE),
('08781827391', 'jeyii', 'Junita', 'jeyii@example.com', 'jeyii', 'user', FALSE),
('080981233125', 'Ska', 'Renggana', 'ska@example.com', 'ska', 'user', FALSE),
('08158092834', 'AirPls', 'Radif', 'airpls@example.com', 'airpls', 'user', FALSE),
('0812387123', 'admin_1', 'ADMIN1', 'admin.one@example.com', 'adminpass1', 'admin', FALSE),
('0812937918', 'admin_2', 'ADMIN2', 'admin.two@example.com', 'adminpass2', 'admin', FALSE);

INSERT INTO genres (genre_id, name) VALUES
(1, 'Action'),
(2, 'Comedy'),
(3, 'Adventure'),
(4, 'Drama'),
(5, 'Thriller'),
(6, 'Sci-Fi'),
(7, 'Fantasy'),
(8, 'Horror'),
(9, 'Fantasy'),
(10, 'Animation');


INSERT INTO movies (movie_id, title, description, release_year, duration, base_price, landscapePoster, portraitPoster, stock, deleted) VALUES
(1, 'Red Notice', 'A globetrotting heist film starring Dwayne Johnson, Gal Gadot, and Ryan Reynolds.', '2021', 118, 21000, '/Horizontal/red-notice-h.jpg', '/Vertical/red-notice-v.jpg', 3, FALSE),
(2, 'Wonder Woman 1984', 'Wonder Woman faces new threats in the 1980s, including the villainous Cheetah.', '2020', 151, 21000, '/Horizontal/wonder-woman-1984-h.jpg', '/Vertical/wonder-woman-1984-v.jpg', 3, FALSE),
(3, 'Deadpool', 'A former Special Forces operative becomes the antihero Deadpool after being subjected to a dangerous experiment.', '2016', 108, 25000, '/Horizontal/deadpool-h.jpg', '/Vertical/deadpool-v.jpg', 3, FALSE),
(4, 'Deadpool & Wolverine', 'Deadpool unites with his would-be pal, Wolverine, to complete the mission and save his world from an existential threat.', '2024', 127, 25000, '/Horizontal/deadpool-wolverine-h.jpg', '/Vertical/deadpool-wolverine.png', 3, FALSE),
(5, 'The Terminator', 'A cyborg assassin from the future is sent to kill Sarah Connor, whose son will lead a resistance movement.', '1984', 107, 29000, '/Horizontal/terminator-h.png', '/Vertical/terminator-v.jpg', 3, FALSE),
(6, 'Aliens', 'The crew of a space station must fight for survival after encountering alien creatures.', '1986', 137, 24000, '/Horizontal/aliens-h.jpg', '/Vertical/aliens-v.jpg', 3, FALSE),
(7, 'The Matrix', 'A hacker discovers that reality as we know it is a simulated construct controlled by machines.', '1999', 136, 24000, '/Horizontal/the-matrix-h.jpg', '/Vertical/the-matrix-v.jpg', 3, FALSE),
(8, 'Coco', 'An aspiring young guitar player, whose family has a classic hate for music, tries to find answers of his great Grandfather.', '2017', 109, 24000, '/Horizontal/coco-h.jpg', '/Vertical/coco-v.jpg', 3, FALSE),
(9, 'Annabelle', 'A doll with a sinister presence terrorizes a young couple after being brought into their home.', '2014', 99, 20000, '/Horizontal/annabelle-h.png', '/Vertical/annabelle-v.png', 3, FALSE),
(10, 'Annabelle: Creation', 'A prequel to Annabelle, showing the origins of the possessed doll.', '2017', 109, 20000, '/Horizontal/annabelle-creation-h.jpg', '/Vertical/annabelle-creation-v.png', 3, FALSE),
(11, 'Annabelle Comes Home', 'The doll wreaks havoc when it is brought into the home of a paranormal investigator.', '2019', 106, 20000, '/Horizontal/annabelle-comes-home-h.jpg', '/Vertical/annabelle-comes-home-v.png', 3, FALSE),
(12, 'The Nun', 'A priest and a novice must confront a malevolent force that manifests itself as a nun.', '2018', 96, 20000, '/Horizontal/the-nun-h.jpg', '/Vertical/the-nun-v.jpg', 3, FALSE),
(13, 'The Nun II', 'The terrifying sequel to The Nun, set in the 1950s with an even darker evil force.', '2023', 110, 25000, '/Horizontal/the-nun-2-h.jpg', '/Vertical/the-nun-2-v.jpg', 3, FALSE),
(14, 'The Conjuring', 'Paranormal investigators Ed and Lorraine Warren tackle one of their most horrifying cases in a haunted house.', '2013', 112, 25000, '/Horizontal/the-conjuring-h.jpg', '/Vertical/the-conjuring-v.jpg', 3, FALSE),
(15, 'The Conjuring 2', 'The Warrens travel to England to investigate a haunting in a family home.', '2016', 134, 25000, '/Horizontal/the-conjuring-2-h.jpg', '/Vertical/the-conjuring-2-v.jpg', 3, FALSE),
(16, 'IT Chapter One', 'A group of outcast children battle a malevolent entity that takes the form of a terrifying clown.', '2017', 135, 25000, '/Horizontal/it-1-h.jpg', '/Vertical/it-1-v.jpg', 3, FALSE),
(17, 'IT Chapter Two', 'The now-adult group of outcasts must face their childhood fears when the evil clown returns.', '2019', 169, 30000, '/Horizontal/it-2-h.jpg', '/Vertical/it-2-v.jpg', 3, FALSE),
(18, 'The Incredible Hulk', 'The Avengers attempt to reverse the damage caused by Thanos and save the universe.', '2019', 181, 40000, '', '', 3, TRUE),
(19, 'Star Wars: The Empire Strikes Back', 'The Rebel Alliance faces off against the Empire in one of the greatest battles in galactic history.', '1980', 124, 30000, '/Horizontal/star-wars-empire-h.png', '/Vertical/star-wars-empire-v.jpg', 3, FALSE),
(20, 'John Wick', 'A retired hitman comes out of retirement to avenge the death of his dog.', '2014', 101, 25000, '/Horizontal/john-wick-h.jpg', '/Vertical/john-wick-v.jpg', 3, FALSE),
(21, 'Jumanji: Welcome to the Jungle', 'Four teenagers are transported into a video game world where they must work together to survive.', '2017', 119, 30000, '/Horizontal/jumanji-h.jpg', '/Vertical/jumanji-v.jpg', 3, FALSE),
(22, 'The Lord of the Rings: The Fellowship of the Ring', 'A young hobbit sets out on a perilous journey to destroy a powerful ring and save Middle-earth.', '2001', 178, 35000, '/Horizontal/the-lord-of-the-rings-h.jpg', '/Vertical/the-lord-of-the-rings-v.jpg', 3, FALSE),
(23, 'Iron Man', 'The story of a genius billionaire who builds a suit of armor and becomes the superhero Iron Man.', '2008', 126, 30000, '/Horizontal/iron-man-1-h.jpg', '/Vertical/iron-man-1-v.jpg', 3, FALSE),
(24, 'Iron Man 2', 'Tony Stark faces new challenges and enemies while coming to terms with his newfound fame as Iron Man.', '2010', 124, 30000, '/Horizontal/iron-man-2-h.jpg', '/Vertical/iron-man-2-v.jpg', 3, FALSE),
(25, 'Thor', 'The story of the Norse god Thor, who is banished to Earth and must prove himself worthy of his powers.', '2011', 115, 30000, '/Horizontal/thor-h.jpg', '/Vertical/thor-v.jpg', 3, FALSE),
(26, 'Captain America: The First Avenger', 'The origin story of Captain America, from his transformation into a super soldier to his battle against Hydra.', '2011', 124, 30000, '/Horizontal/captain-america-h.jpg', '/Vertical/captain-america-v.jpg', 3, FALSE),
(27, 'The Avengers', 'Earth''s mightiest heroes must unite to defeat an alien invasion led by the villain Loki.', '2012', 143, 35000, '/Horizontal/the-avengers-h.png', '/Vertical/the-avengers-v.jpg', 3, FALSE),
(28, 'Iron Man 3', 'Tony Stark faces a new villain and must confront the consequences of the events from The Avengers.', '2013', 130, 30000, '/Horizontal/iron-man-3-h.jpg', '/Vertical/iron-man-3-v.jpg', 3, FALSE),
(29, 'Captain America: The Winter Soldier', 'Captain America teams up with Black Widow to uncover a conspiracy while battling a mysterious assassin.', '2014', 136, 35000, '/Horizontal/captain-america-winter-soldier-h.jpg', '/Vertical/captain-america-winter-soldier-v.jpg', 3, FALSE),
(30, 'Guardians of the Galaxy', 'A group of intergalactic misfits come together to stop a warlord from taking over the galaxy.', '2014', 121, 35000, '/Horizontal/guardians-of-the-galaxy-h.jpg', '/Vertical/guardians-of-the-galaxy-v.jpg', 3, FALSE),
(31, 'Avengers: Age of Ultron', 'The Avengers face a new threat in the form of Ultron, an artificial intelligence bent on human extinction.', '2015', 141, 35000, '/Horizontal/avengers-age-of-ultron-h.jpg', '/Vertical/avengers-age-of-ultron-v.jpg', 3, FALSE),
(32, 'Ant-Man', 'A thief must use a shrinking suit to stop a former mentor from destroying the world.', '2015', 117, 30000, '/Horizontal/ant-man-h.jpg', '/Vertical/ant-man-v.jpg', 3, FALSE),
(33, 'Captain America: Civil War', 'The Avengers are divided over a new law regulating their actions, leading to a battle between Iron Man and Captain America.', '2016', 147, 35000, '/Horizontal/captain-america-civil-war-h.jpg', '/Vertical/captain-america-civil-war-v.jpg', 3, FALSE),
(34, 'Doctor Strange', 'A brilliant surgeon discovers a hidden world of magic after a life-changing accident.', '2016', 115, 30000, '/Horizontal/doctor-strange-h.jpg', '/Vertical/doctor-strange-v.jpg', 3, FALSE),
(35, 'Guardians of the Galaxy Vol. 2', 'The Guardians struggle to understand the true nature of their leader Star-Lord''s parentage.', '2017', 136, 35000, '/Horizontal/guardians-of-the-galaxy-2-h.jpg', '/Vertical/guardians-of-the-galaxy-2-v.jpg', 3, FALSE),
(36, 'Spider-Man: Homecoming', 'A young Spider-Man must face a new villain while balancing high school life and superhero duties.', '2017', 133, 30000, '/Horizontal/spiderman-homecoming-h.jpg', '/Vertical/spiderman-homecoming.jpg', 3, FALSE),
(37, 'Thor: Ragnarok', 'Thor must escape from a distant planet and prevent the end of Asgard in a showdown with his sister, Hela.', '2017', 130, 30000, '/Horizontal/thor-ragnarok-h.jpg', '/Vertical/thor-ragnarok-v.jpg', 3, FALSE),
(38, 'Black Panther', 'The newly crowned king of Wakanda must protect his people from a dangerous foe seeking to exploit the nation''s resources.', '2018', 134, 35000, '/Horizontal/black-panther-h.jpg', '/Vertical/black-panther-v.jpg', 3, FALSE),
(40, 'Captain Marvel', 'Carol Danvers becomes the superhero Captain Marvel and joins the fight against an alien invasion.', '2019', 123, 30000, '/Horizontal/captain-marvel-h.jpg', '/Vertical/captain-marvel-v.jpg', 3, FALSE),
(41, 'Spider-Man: Far From Home', 'Peter Parker goes on a European school trip while trying to balance his life as Spider-Man.', '2019', 129, 30000, '/Horizontal/spiderman-far-from-home-h.jpg', '/Vertical/spiderman-far-from-home-v.jpg', 3, FALSE),
(42, 'Black Widow', 'Black Widow faces her past while fighting new enemies after the events of Avengers: Endgame.', '2021', 134, 30000, '/Horizontal/black-widow-h.jpg', '/Vertical/black-widow-v.jpg', 3, FALSE),
(43, 'Shang-Chi and the Legend of the Ten Rings', 'Shang-Chi must confront his father and the deadly Ten Rings organization.', '2021', 132, 30000, '/Horizontal/shangchi-h.jpg', '/Vertical/shangchi-v.jpg', 3, FALSE),
(44, 'Eternals', 'A group of immortal heroes must come together to stop a threat that has been hidden from humanity for centuries.', '2021', 157, 30000, '/Horizontal/eternals-h.png', '/Vertical/eternals-v.jpg', 3, FALSE),
(45, 'Spider-Man: No Way Home', 'Peter Parker must deal with the consequences of his secret identity being revealed.', '2021', 148, 40000, '/Horizontal/spiderman-no-way-home-h.jpg', '/Vertical/spiderman-no-way-home-v.jpg', 3, FALSE),
(46, 'Doctor Strange in the Multiverse of Madness', 'Doctor Strange must navigate alternate realities to stop a multiverse-threatening villain.', '2022', 126, 30000, '/Horizontal/doctor-strange-mom-h.jpg', '/Vertical/doctor-strange-mom-v.jpg', 3, FALSE),
(47, 'Black Panther: Wakanda Forever', 'The people of Wakanda fight to protect their nation after the death of their king.', '2022', 161, 40000, '/Horizontal/black-panther-wakanda-forever-h.jpg', '/Vertical/black-panther-wakanda-forever-v.jpg', 3, FALSE),
(48, 'The Lord of the Rings: The Two Towers', 'The journey continues as Frodo and Sam trek towards Mount Doom while Aragorn, Legolas, and Gimli battle the forces of Saruman.', '2002', 179, 35000, '/Horizontal/the-lord-of-the-rings-two-towers-h.jpg', '/Vertical/the-lord-of-the-rings-two-towers-v.jpg', 3, FALSE),
(49, 'The Lord of the Rings: The Return of the King', 'The final battle for Middle-earth begins as Frodo and Sam approach Mount Doom to destroy the One Ring.', '2003', 201, 35000, '/Horizontal/the-lord-of-the-rings-return-of-kings-h.jpg', 'D:/Codes/TubesPBW/TheReelDeal/Vertical/the-lord-of-the-rings-return-of-kings-v.jpg', 3, FALSE),
(50, 'Indiana Jones and the Raiders of the Lost Ark', 'Archaeologist Indiana Jones races against the Nazis to find the Ark of the Covenant.', '1981', 115, 23000, '/Horizontal/indiana-jones-lost-ark-h.jpg', '/Vertical/indiana-jones-lost-ark-v.jpg', 3, FALSE),
(51, 'Indiana Jones and the Temple of Doom', 'Indiana Jones must rescue a group of children from an evil cult in India.', '1984', 118, 25000, '/Horizontal/indiana-jones-temple-of-doom-h.jpg', '/Vertical/indiana-jones-temple-of-doom-v.jpg', 3, FALSE),
(52, 'Indiana Jones and the Last Crusade', 'Indiana Jones teams up with his father to find the Holy Grail before the Nazis can get to it.', '1989', 127, 24000, '/Horizontal/indiana-jones-last-crusade-h.jpg', '/Vertical/indiana-jones-last-crusade-v.jpg', 3, FALSE),
(53, 'Indiana Jones and the Kingdom of the Crystal Skull', 'Indiana Jones discovers a mysterious crystal skull with extraordinary powers.', '2008', 122, 27000, '/Horizontal/indiana-jones-crystal-skull-h.jpg', '/Vertical/indiana-jones-crystal-skull-v.jpg', 3, FALSE);
(54, 'Avengers: Infinity War', 'The Avengers team up with the Guardians of the Galaxy to stop Thanos from collecting the Infinity Stones.', '2018', 149, 40000, '/Horizontal/avengers-infinity-war-h.jpg', '/Vertical/avengers-infinity-war-v.jpg', 3, FALSE),

INSERT INTO movie_genres (movie_id, genre_id) VALUES
(1, 1), -- Red Notice: Action, Thriller
(1, 5),
(2, 1), -- Wonder Woman 1984: Action, Fantasy, Sci-Fi
(2, 7),
(2, 6),
(3, 1), -- Deadpool: Action, Comedy
(3, 2),
(4, 1), -- Deadpool&Wolverine: Action, Comedy
(4, 2),
(5, 1), -- The Terminator: Action, Sci-Fi, Thriller
(5, 6),
(5, 5),
(6, 1), -- Aliens: Action, Sci-Fi, Thriller
(6, 6),
(6, 5),
(7, 1), -- The Matrix: Action, Sci-Fi, Thriller
(7, 6),	
(7, 5),
(8,10), --Coco: Animation, Adventure, Fantasy
(8,3),
(8,9),
(9, 8), -- Annabelle: Horror, Thriller
(9, 5),
(10, 8), -- Annabelle: Creation: Horror, Thriller
(10, 5),
(11, 8), -- Annabelle Comes Home: Horror, Thriller
(11, 5),
(12, 8), -- The Nun: Horror, Thriller
(12, 5),
(13, 8), -- The Nun II: Horror, Thriller
(13, 5),
(14, 8), -- The Conjuring: Horror, Thriller
(14, 5),
(15, 8), -- The Conjuring 2: Horror, Thriller
(15, 5),
(16, 8), -- IT Chapter One: Horror, Thriller
(16, 5),
(17, 8), -- IT Chapter Two: Horror, Thriller
(17, 5),
(18, 1), -- The Incredible Hulk: Action, Adventure, Sci-Fi
(18, 3),
(18, 6),
(19, 1), -- Star Wars: The Empire Strikes Back: Action, Adventure, Sci-Fi
(19, 3),
(19, 6),
(20, 1), -- John Wick: Action, Thriller
(20, 5),
(21, 1), -- Jumanji: Welcome to the Jungle: Action, Adventure, Comedy
(21, 3),
(21, 2),
(22, 1), -- The Lord of the Rings: The Fellowship of the Ring: Action, Adventure, Fantasy
(22, 3),
(22, 7),
(23, 1), -- Iron Man: Action, Sci-Fi
(23, 6),
(24, 1), -- Iron Man 2: Action, Sci-Fi
(24, 6),
(25, 1), -- Thor: Action, Adventure, Fantasy
(25, 3),
(25, 7),
(26, 1), -- Captain America: The First Avenger: Action, Adventure
(26, 3),
(27, 1), -- The Avengers: Action, Adventure, Sci-Fi
(27, 3),
(27, 6),
(28, 1), -- Iron Man 3: Action, Sci-Fi
(28, 6),
(29, 1), -- Captain America: The Winter Soldier: Action, Thriller
(29, 5),
(30, 1), -- Guardians of the Galaxy: Action, Adventure, Sci-Fi
(30, 3),
(30, 6),
(31, 1), -- Avengers: Age of Ultron: Action, Adventure, Sci-Fi
(31, 3),
(31, 6),
(32, 1), -- Ant-Man: Action, Adventure, Sci-Fi
(32, 3),
(32, 6),
(33, 1), -- Captain America: Civil War: Action, Adventure, Sci-Fi
(33, 3),
(33, 6),
(34, 1), -- Doctor Strange: Action, Adventure, Sci-Fi
(34, 3),
(34, 6),
(35, 1), -- Guardians of the Galaxy Vol. 2: Action, Adventure, Sci-Fi
(35, 3),
(35, 6),
(36, 1), -- Spider-Man: Homecoming: Action, Adventure
(36, 3),
(37, 1), -- Thor: Ragnarok: Action, Adventure, Fantasy
(37, 3),
(37, 7),
(38, 1), -- Black Panther: Action, Drama, Sci-Fi
(38, 4),
(38, 6),
(54, 1), -- Avengers: Infinity War: Action, Adventure, Sci-Fi
(54, 3),
(54, 6),
(40, 1), -- Captain Marvel: Action, Adventure, Sci-Fi
(40, 3),
(40, 6),
(41, 1), -- Spider-Man: Far From Home: Action, Adventure
(41, 3),
(42, 1), -- Black Widow: Action, Thriller
(42, 5),
(43, 1), -- Shang-Chi and the Legend of the Ten Rings: Action, Adventure, Fantasy
(43, 3),
(43, 7),
(44, 1), -- Eternals: Action, Adventure, Sci-Fi
(44, 3),
(44, 6),
(45, 1), -- Spider-Man: No Way Home: Action, Adventure
(45, 3),
(46, 1), -- Doctor Strange in the Multiverse of Madness: Action, Adventure, Sci-Fi
(46, 3),
(46, 6),
(47, 1), -- Black Panther: Wakanda Forever: Action, Drama, Sci-Fi
(47, 4),
(47, 6),
(48, 1), -- The Lord of the Rings: The Two Towers: Action, Adventure, Fantasy
(48, 3),
(48, 7),
(49, 1), -- The Lord of the Rings: The Return of the King: Action, Adventure, Fantasy
(49, 3),
(49, 7),
(50, 1), -- Indiana Jones and the Raiders of the Lost Ark: Action, Adventure
(50, 3),
(51, 1), -- Indiana Jones and the Temple of Doom: Action, Adventure
(51, 3),
(52, 1), -- Indiana Jones and the Last Crusade: Action, Adventure
(52, 3),
(53, 1), -- Indiana Jones and the Kingdom of the Crystal Skull: Action, Adventure
(53, 3);

-- Inserting actors
INSERT INTO actors (actor_id, name, deleted) VALUES
(1, 'Ryan Reynolds', FALSE),
(2, 'Dwayne Johnson', FALSE),
(3, 'Gal Gadot', FALSE),
(4, 'Kristen Wiig', FALSE),
(5, 'Chris Hemsworth', FALSE),
(6, 'Robert Downey Jr.', FALSE),
(7, 'Chris Evans', FALSE),
(8, 'Scarlett Johansson', FALSE),
(9, 'Tom Hiddleston', FALSE),
(10, 'Chris Pratt', FALSE),
(11, 'Mark Ruffalo', FALSE),
(12, 'Chadwick Boseman', FALSE),
(13, 'Tom Holland', FALSE),
(14, 'Benedict Cumberbatch', FALSE),
(15, 'Zoe Saldana', FALSE),
(16, 'Will Smith', FALSE),
(17, 'Milla Jovovich', FALSE),
(18, 'Vera Farmiga', FALSE),
(19, 'Patrick Wilson', FALSE),
(20, 'Jessica Chastain', FALSE),
(21, 'Gael García Bernal', FALSE),
(22, 'Anthony Gonzalez', FALSE),
(23, 'Pedro Pascal', FALSE),
(24, 'Bill Skarsgård', FALSE),
(25, 'Mark Hamill', FALSE),
(26, 'Carrie Fisher', FALSE),
(27, 'Hugh Jackman', FALSE),
(28, 'Keanu Reeves', FALSE),
(29, 'Arnold Schwarzenegger', FALSE),
(30, 'Sigourney Weaver', FALSE),
(31, 'Carrie-Anne Moss', FALSE),
(32, 'Vin Diesel', FALSE),
(33, 'Ian McKellen', FALSE),
(34, 'Elijah Wood', FALSE),
(35, 'Orlando Bloom', FALSE),
(36, 'Harrison Ford', FALSE),
(37, 'Paul Rudd', FALSE),
(38, 'Simu Liu', FALSE),
(39, 'Gemma Chan', FALSE),
(40, 'Richard Madden', FALSE),
(41, 'Angelina Jolie', FALSE),
(42, 'Benjamin Bratt', FALSE),
(43, 'Michael Biehn', FALSE),
(44, 'Lance Henriksen', FALSE),
(45, 'Ian McShane', FALSE),
(46, 'Lance Reddick', FALSE),
(47, 'Jack Black', FALSE),
(48, 'Brie Larson', FALSE),
(49, 'Awkwafina', FALSE),
(50, 'Tony Leung', FALSE),
(51, 'Pedro Pascal', FALSE);

INSERT INTO movie_actors (movie_id, actor_id) VALUES
-- Red Notice
(1, 1), (1, 2), (1, 3),

-- Wonder Woman 1984
(2, 3), (2, 4), (2, 23),

-- Deadpool
(3, 1), (3, 17), (3, 24),

-- Deadpool & Wolverine
(4, 1), (4, 27), (4, 17),

-- The Terminator
(5, 29), (5, 18), (5, 43), -- Michael Biehn (added to actors)

-- Aliens
(6, 30), (6, 43), (6, 44), -- Lance Henriksen (added to actors)

-- The Matrix
(7, 28), (7, 31), (7, 19),

-- Coco
(8, 21), (8, 22), (8, 42),

-- Annabelle
(9, 18), (9, 19),

-- Annabelle: Creation
(10, 18), (10, 19),

-- Annabelle Comes Home
(11, 18), (11, 19),

-- The Nun
(12, 18), (12, 19),

-- The Nun II
(13, 18), (13, 19),

-- The Conjuring
(14, 18), (14, 19),

-- The Conjuring 2
(15, 18), (15, 19),

-- IT Chapter One
(16, 20), (16, 23), (16, 24),

-- IT Chapter Two
(17, 20), (17, 23), (17, 24),

-- Incredible Hulk
(18, 5), (18, 6), (18, 7), (18, 8), (18, 9),

-- Star Wars: The Empire Strikes Back
(19, 36), (19, 25), (19, 26),

-- John Wick
(20, 28), (20, 45), -- Ian McShane (added to actors)
(20, 46), -- Lance Reddick (added to actors)

-- Jumanji: Welcome to the Jungle
(21, 2), (21, 1), (21, 47), -- Jack Black (added to actors)

-- The Lord of the Rings: The Fellowship of the Ring
(22, 33), (22, 34), (22, 35),

-- Iron Man
(23, 6), (23, 8), (23, 5),

-- Iron Man 2
(24, 6), (24, 8), (24, 5),

-- Thor
(25, 5), (25, 9), (25, 8),

-- Captain America: The First Avenger
(26, 7), (26, 8), (26, 5),

-- The Avengers
(27, 5), (27, 6), (27, 7), (27, 8), (27, 9),

-- Iron Man 3
(28, 6), (28, 8), (28, 5),

-- Captain America: The Winter Soldier
(29, 7), (29, 8), (29, 5),

-- Guardians of the Galaxy
(30, 10), (30, 15), (30, 32),

-- Avengers: Age of Ultron
(31, 5), (31, 6), (31, 7), (31, 8),

-- Ant-Man
(32, 37), (32, 6), (32, 8),

-- Captain America: Civil War
(33, 7), (33, 6), (33, 5), (33, 8),

-- Doctor Strange
(34, 14), (34, 8), (34, 6),

-- Guardians of the Galaxy Vol. 2
(35, 10), (35, 15), (35, 32),

-- Spider-Man: Homecoming
(36, 13), (36, 8), (36, 6),

-- Thor: Ragnarok
(37, 5), (37, 9), (37, 8),

-- Black Panther
(38, 12), (38, 8), (38, 14),

-- Avengers: Infinity War
(54, 5), (54, 6), (54, 7), (54, 8),

-- Captain Marvel
(40, 48), -- Brie Larson (added to actors)
(40, 8), (40, 11),

-- Spider-Man: Far From Home
(41, 13), (41, 8), (41, 6),

-- Black Widow
(42, 8), (42, 9), (42, 6),

-- Shang-Chi and the Legend of the Ten Rings
(43, 38), -- Simu Liu
(43, 49), -- Awkwafina (added to actors)
(43, 50), -- Tony Leung (added to actors)

-- Eternals
(44, 39), (44, 40), (44, 41),

-- Spider-Man: No Way Home
(45, 13), (45, 14), (45, 8),

-- Doctor Strange in the Multiverse of Madness
(46, 14), (46, 8), (46, 6),

-- Black Panther: Wakanda Forever
(47, 12), (47, 14), (47, 8),

-- The Lord of the Rings: The Two Towers
(48, 33), (48, 34), (48, 35),

-- The Lord of the Rings: The Return of the King
(49, 33), (49, 34), (49, 35),

-- Indiana Jones and the Raiders of the Lost Ark
(50, 36), (50, 8), (50, 32),

-- Indiana Jones and the Temple of Doom
(51, 36), (51, 8), (51, 32),

-- Indiana Jones and the Last Crusade
(52, 36), (52, 8), (52, 32),

-- Indiana Jones and the Kingdom of the Crystal Skull
(53, 36), (53, 8), (53, 32);


INSERT INTO transactions (transaction_id, phone, base_fee, pickup_date, isPickedUp, due_date, return_date, transaction_date, days, late_fee) VALUES
(1,'08781827391', 84000,'2024-01-01', true, '2024-01-04', '2024-01-04', '2024-01-01', 4,0), --1, tepat waktu
(2,'080981233125', 46000, '2024-02-03', true, '2024-02-04', '2024-02-04','2024-01-02', 1, 0),--2,3; multiple films, tepat waktu
(3,'080981233125', 25000, '2024-01-02', true,'2024-01-04','2024-01-05', '2024-01-02',1,10000),--4, late
(4,'08158092834', 58000,'2024-03-01', true, '2024-03-03','2024-03-02', '2024-02-29',2,0 ), --5, early
(5,'08158092834', 21000, '2024-03-01', true, '2024-03-02', null,'2024-03-01',1, null);--1, not returned, late


INSERT INTO transaction_details(transaction_id, movie_id) VALUES
(1,1),
(2,2),
(2,3),
(3,4),
(4,5),
(5,1);