DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(50) NOT NULL UNIQUE,
    login varchar(50) NOT NULL UNIQUE,
    user_name varchar(50),
    birthday DATE NOT NULL,
    CONSTRAINT constr_login CHECK(login <> ' ')
    );

CREATE TABLE IF NOT EXISTS friendship (
    user_id1 INTEGER REFERENCES users(user_id),
    user_id2 INTEGER REFERENCES users(user_id),
    PRIMARY KEY (user_id1, user_id2)
);

CREATE TABLE IF NOT EXISTS rating (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar(10) NOT NULL CHECK(rating_name IN('G', 'PG', 'PG-13', 'R', 'NC-17'))
);

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar(50) NOT NULL,
    description varchar(200),
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    rating_id INTEGER NOT NULL REFERENCES rating(rating_id),
    CONSTRAINT constr_films CHECK(title <> ' ' AND duration > 0)
);

CREATE TABLE IF NOT EXISTS user_likes (
    film_id INTEGER REFERENCES films(film_id),
    user_id INTEGER REFERENCES users(user_id),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(25) NOT NULL
     CHECK(genre_name IN('Комедия', 'Драма', 'Мультфильм', 'Триллер', 'Документальный', 'Боевик'))
);

CREATE TABLE IF NOT EXISTS films_genres (
    film_id INTEGER REFERENCES films(film_id),
    genre_id INTEGER REFERENCES genre(genre_id),
    PRIMARY KEY (film_id, genre_id)
);