--
--ТАБЛИЦА 'КНИГИ' (ТЕСТОВАЯ ВЕРСИЯ; ВСЕ ЗНАЧЕНИЯ ДОЛЖНЫ БЫТЬ ЗАПОЛНЕНЫ, НО ПОКА ТУТ
--ПРАВИЛА БУДУТ ПОПРОЩЕ)
--

CREATE TABLE 'book' (
  'idBook' int(11) NOT NULL,
  'ISBN' varchar(13),
  'title' varchar(128),
  'author' varchar(128),
  'publisher' varchar(128),
  'publishYear' int(4),
  'genre' varchar(128),
  'language' varchar(128),
  'pagesAmount' int (10),
) DEFAULT CHARSET=utf8mb4;

--
--ДОБАВЛЯЕМ ПЕРВИЧНЫЙ КЛЮЧ
--

ALTER TABLE 'book'
  ADD PRIMARY KEY (`idBook`);