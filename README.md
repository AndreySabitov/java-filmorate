# ER-diagram filmorate
![ER-diagram filmorate](ER-диаграмма%20filmorate.png)
## Примеры запросов в БД
### Получить список всех фильмов:
```sql
SELECT *
FROM films f;
```
### Получить фильм по id:
```sql
SELECT *
FROM films f 
WHERE f.film_id = 1;
```
### Получить список самых популярных фильмов:
```sql
SELECT f.title,
       COUNT(ul.user_id) as likes
FROM films f
LEFT JOIN users_likes ul on f.film_id = ul.film_id
GROUP BY f.title
ORDER BY likes desc
LIMIT 10;
```
### Получить жанры фильмов:
```sql
SELECT f.title,
       g.genre_name
FROM films f
JOIN films_genres fg on f.film_id = fg.film_id
JOIN genre g ON fg.genre_id = g.genre_id;
```
### Получить рейтинг фильмов:
```sql
SELECT f.title,
       r.rating_name
FROM films f
JOIN rating r on f.rating_id = r.rating_id;
```
### Получить всех пользователей:
```sql
SELECT *
FROM users u;
```
### Получить пользователя по id:
```sql
SELECT *
FROM users u 
WHERE u.user_id = 2;
```
### Получить список друзей пользователя
```sql
SELECT *
FROM users u
WHERE u.user_id in (select f.user_id2
                    from users u2
                    LEFT JOIN friendship f on u2.user_id = f.user_id1
                    where u2.user_id = 1);
```
### Получить список общих друзей
```sql
SELECT *
FROM users u, friendship f, friendship o
WHERE u.user_id = f.user_id2
  AND u.user_id = o.user_id2
  AND f.user_id1 = ?
  AND o.user_id1 = ?;
```
