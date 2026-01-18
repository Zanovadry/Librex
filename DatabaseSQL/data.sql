BEGIN;

-- =========================================================
-- 0) SŁOWNIKI
-- =========================================================

-- permissions_dict
INSERT INTO permissions_dict(role) VALUES
('ADMIN'), ('LIBRARIAN'), ('CUSTOMER');

-- countries_dict
INSERT INTO countries_dict(country_name) VALUES
('POLAND'), ('GERMANY'), ('UNITED_STATES'), ('UNITED_KINGDOM'), ('CANADA'), ('ITALY'), ('SPAIN'), ('FINLAND');

-- languages_dict
INSERT INTO languages_dict(language) VALUES
('POLISH'), ('ENGLISH'), ('FRENCH'), ('GERMAN'), ('ITALIAN'), ('SPANISH'), ('RUSSIAN');

-- genre_dict
INSERT INTO genre_dict(name) VALUES
('FANTASY'),
('SCIENCE_FICTION'),
('ROMANCE'),
('THRILLER'),
('CRIME'),
('NON_FICTION'),
('BIOGRAPHY'),
('HISTORY'),
('CHILDREN');

-- categories_dict (Twoje enumy)
INSERT INTO categories_dict(name) VALUES
('KIDS'),
('ADULTS'),
('LONG'),
('SHORT'),
('AUDIOBOOK'),
('EBOOK');

-- =========================================================
-- 1) PUBLISHERS
-- =========================================================
INSERT INTO publishers(name, address, country_id, email, webpage, foundation_date) VALUES
('Penguin Random House', 'New York, USA', (SELECT country_id FROM countries_dict WHERE country_name='UNITED_STATES'), 'contact@penguinrandomhouse.com', 'https://www.penguinrandomhouse.com', '2013-07-01'),
('HarperCollins', 'New York, USA', (SELECT country_id FROM countries_dict WHERE country_name='UNITED_STATES'), 'info@harpercollins.com', 'https://www.harpercollins.com', '1989-08-01'),
('Hachette Livre', 'Paris, FR', (SELECT country_id FROM countries_dict WHERE country_name='FRANCE' LIMIT 1), NULL, NULL, NULL),
('Bloomsbury', 'London, UK', (SELECT country_id FROM countries_dict WHERE country_name='UNITED_KINGDOM'), 'info@bloomsbury.com', 'https://www.bloomsbury.com', '1986-01-01'),
('Helion', 'Gliwice, PL', (SELECT country_id FROM countries_dict WHERE country_name='POLAND'), 'info@helion.pl', 'https://helion.pl', '1991-01-01'),
('Znak', 'Krakow, PL', (SELECT country_id FROM countries_dict WHERE country_name='POLAND'), 'kontakt@znak.com.pl', 'https://www.znak.com.pl', '1959-01-01');

-- Uwaga: FRANCE nie masz w enum CountryName, więc powyższy INSERT z Hachette ma SELECT, który może zwrócić NULL.
-- Żeby seed był pewny, usuwam Hachette i zostawiam tylko te z istniejącymi krajami:
DELETE FROM publishers WHERE name='Hachette Livre';


-- =========================================================
-- 2) USERS
-- password hash = bcrypt dla "password" (powszechny przykładowy hash)
-- =========================================================
-- hash: $2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Tn6p1xR271GGBqBPdZiZsaAJ2b2W
-- (jeśli w Springu masz BCryptPasswordEncoder, to to powinno działać)

-- 2 admin
INSERT INTO users(permission_id, country_id, firstname, surname, address, birthdate, phone, email, username, password, is_blacklisted)
VALUES
(
  (SELECT permission_id FROM permissions_dict WHERE role='ADMIN'),
  (SELECT country_id FROM countries_dict WHERE country_name='POLAND'),
  'Admin', 'Admin', 'Krakow', '1999-01-01'::date, '500100200', 'admin1@librex.com', 'admin1',
  '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Tn6p1xR271GGBqBPdZiZsaAJ2b2W', false
),
(
  (SELECT permission_id FROM permissions_dict WHERE role='ADMIN'),
  (SELECT country_id FROM countries_dict WHERE country_name='UNITED_STATES'),
  'Ava', 'Admin', 'New York', '1995-02-10'::date, '555111222', 'admin2@librex.com', 'admin2',
  '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Tn6p1xR271GGBqBPdZiZsaAJ2b2W', false
);

-- 4 librarian
INSERT INTO users(permission_id, country_id, firstname, surname, address, birthdate, phone, email, username, password, is_blacklisted)
SELECT
  (SELECT permission_id FROM permissions_dict WHERE role='LIBRARIAN'),
  (SELECT country_id FROM countries_dict WHERE country_name='POLAND'),
  x.firstname,
  x.surname,
  x.address,
  x.birthdate::date,   -- <-- FIX
  x.phone,
  x.email,
  x.username,
  '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Tn6p1xR271GGBqBPdZiZsaAJ2b2W',
  false
FROM (VALUES
  ('Anna','Librarian','Warszawa','1997-03-12','501111111','libr1@librex.com','libr1'),
  ('Tomasz','Librarian','Wroclaw','1996-07-22','502222222','libr2@librex.com','libr2'),
  ('Kasia','Librarian','Gdansk','1998-11-05','503333333','libr3@librex.com','libr3'),
  ('Marek','Librarian','Poznan','1994-09-19','504444444','libr4@librex.com','libr4')
) AS x(firstname, surname, address, birthdate, phone, email, username);

-- 30 customers (to było OK)
INSERT INTO users(permission_id, country_id, firstname, surname, address, birthdate, phone, email, username, password, is_blacklisted)
SELECT
  (SELECT permission_id FROM permissions_dict WHERE role='CUSTOMER'),
  (SELECT country_id FROM countries_dict WHERE country_name='POLAND'),
  'User' || gs::text,
  'Customer' || gs::text,
  'City ' || gs::text,
  (DATE '1985-01-01' + (gs || ' days')::interval)::date,
  '600' || lpad(gs::text, 6, '0'),
  'user' || gs::text || '@librex.com',
  'user' || gs::text,
  '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Tn6p1xR271GGBqBPdZiZsaAJ2b2W',
  (gs % 29 = 0)
FROM generate_series(1,30) gs;



-- =========================================================
-- 3) AUTHORS (25)
-- =========================================================
INSERT INTO authors(firstname, surname, nickname, nationality, birthdate, deathdate, genre_id) VALUES
('J.R.R.', 'Tolkien', NULL, 'United Kingdom', '1892-01-03', '1973-09-02', (SELECT genre_id FROM genre_dict WHERE name='FANTASY')),
('George', 'Orwell', NULL, 'United Kingdom', '1903-06-25', '1950-01-21', (SELECT genre_id FROM genre_dict WHERE name='HISTORY')),
('Frank', 'Herbert', NULL, 'United States', '1920-10-08', '1986-02-11', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Isaac', 'Asimov', NULL, 'United States', '1920-01-02', '1992-04-06', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Agatha', 'Christie', NULL, 'United Kingdom', '1890-09-15', '1976-01-12', (SELECT genre_id FROM genre_dict WHERE name='CRIME')),
('Stephen', 'King', NULL, 'United States', '1947-09-21', NULL, (SELECT genre_id FROM genre_dict WHERE name='THRILLER')),
('J.K.', 'Rowling', NULL, 'United Kingdom', '1965-07-31', NULL, (SELECT genre_id FROM genre_dict WHERE name='FANTASY')),
('Arthur', 'Clarke', NULL, 'United Kingdom', '1917-12-16', '2008-03-19', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Douglas', 'Adams', NULL, 'United Kingdom', '1952-03-11', '2001-05-11', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Jane', 'Austen', NULL, 'United Kingdom', '1775-12-16', '1817-07-18', (SELECT genre_id FROM genre_dict WHERE name='ROMANCE')),
('Fyodor', 'Dostoevsky', NULL, 'Russia', '1821-11-11', '1881-02-09', (SELECT genre_id FROM genre_dict WHERE name='HISTORY')),
('Mark', 'Twain', NULL, 'United States', '1835-11-30', '1910-04-21', (SELECT genre_id FROM genre_dict WHERE name='CHILDREN')),
('Ernest', 'Hemingway', NULL, 'United States', '1899-07-21', '1961-07-02', (SELECT genre_id FROM genre_dict WHERE name='HISTORY')),
('Mary', 'Shelley', NULL, 'United Kingdom', '1797-08-30', '1851-02-01', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Bram', 'Stoker', NULL, 'Ireland', '1847-11-08', '1912-04-20', (SELECT genre_id FROM genre_dict WHERE name='THRILLER')),
('H.G.', 'Wells', NULL, 'United Kingdom', '1866-09-21', '1946-08-13', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Lewis', 'Carroll', NULL, 'United Kingdom', '1832-01-27', '1898-01-14', (SELECT genre_id FROM genre_dict WHERE name='CHILDREN')),
('Antoine', 'de Saint-Exupery', NULL, 'France', '1900-06-29', '1944-07-31', (SELECT genre_id FROM genre_dict WHERE name='CHILDREN')),
('Dan', 'Brown', NULL, 'United States', '1964-06-22', NULL, (SELECT genre_id FROM genre_dict WHERE name='THRILLER')),
('Suzanne', 'Collins', NULL, 'United States', '1962-08-10', NULL, (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Harper', 'Lee', NULL, 'United States', '1926-04-28', '2016-02-19', (SELECT genre_id FROM genre_dict WHERE name='HISTORY')),
('Aldous', 'Huxley', NULL, 'United Kingdom', '1894-07-26', '1963-11-22', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('Ray', 'Bradbury', NULL, 'United States', '1920-08-22', '2012-06-05', (SELECT genre_id FROM genre_dict WHERE name='SCIENCE_FICTION')),
('C.S.', 'Lewis', NULL, 'United Kingdom', '1898-11-29', '1963-11-22', (SELECT genre_id FROM genre_dict WHERE name='FANTASY')),
('John', 'Steinbeck', NULL, 'United States', '1902-02-27', '1968-12-20', (SELECT genre_id FROM genre_dict WHERE name='HISTORY'));

-- =========================================================
-- 4) BOOK TITLES (60) + PHOTO = OpenLibrary Covers by ISBN
-- =========================================================
-- Uwaga: photo to link do okładki na podstawie ISBN (L size).
-- Format jest zgodny z OpenLibrary Covers API. :contentReference[oaicite:1]{index=1}

-- Pomocniczo: funkcja “mapowania” author_id po nazwisku+imieniu
-- (używamy subquery w INSERTach)

INSERT INTO book_title(title, author_id, description, photo) VALUES
-- Tolkien
('The Hobbit',
 (SELECT author_id FROM authors WHERE surname='Tolkien' LIMIT 1),
 'A fantasy adventure in Middle-earth.',
 'https://covers.openlibrary.org/b/isbn/9780547928227-L.jpg'),
('The Fellowship of the Ring',
 (SELECT author_id FROM authors WHERE surname='Tolkien' LIMIT 1),
 'The first volume of The Lord of the Rings.',
 'https://covers.openlibrary.org/b/isbn/9780547928210-L.jpg'),
('The Two Towers',
 (SELECT author_id FROM authors WHERE surname='Tolkien' LIMIT 1),
 'The second volume of The Lord of the Rings.',
 'https://covers.openlibrary.org/b/isbn/9780547928203-L.jpg'),
('The Return of the King',
 (SELECT author_id FROM authors WHERE surname='Tolkien' LIMIT 1),
 'The final volume of The Lord of the Rings.',
 'https://covers.openlibrary.org/b/isbn/9780547928197-L.jpg'),

-- Orwell
('1984',
 (SELECT author_id FROM authors WHERE surname='Orwell' LIMIT 1),
 'A dystopian novel about surveillance and totalitarianism.',
 'https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg'),
('Animal Farm',
 (SELECT author_id FROM authors WHERE surname='Orwell' LIMIT 1),
 'A political satire in the form of an animal fable.',
 'https://covers.openlibrary.org/b/isbn/9780451526342-L.jpg'),

-- Herbert
('Dune',
 (SELECT author_id FROM authors WHERE surname='Herbert' LIMIT 1),
 'Epic science fiction saga on Arrakis.',
 'https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg'),
('Dune Messiah',
 (SELECT author_id FROM authors WHERE surname='Herbert' LIMIT 1),
 'The second novel in the Dune series.',
 'https://covers.openlibrary.org/b/isbn/9780441172696-L.jpg'),
('Children of Dune',
 (SELECT author_id FROM authors WHERE surname='Herbert' LIMIT 1),
 'The third novel in the Dune series.',
 'https://covers.openlibrary.org/b/isbn/9780441104024-L.jpg'),

-- Asimov
('Foundation',
 (SELECT author_id FROM authors WHERE surname='Asimov' LIMIT 1),
 'A classic of science fiction about the fall of empires.',
 'https://covers.openlibrary.org/b/isbn/9780553293357-L.jpg'),
('I, Robot',
 (SELECT author_id FROM authors WHERE surname='Asimov' LIMIT 1),
 'Stories that introduced the Three Laws of Robotics.',
 'https://covers.openlibrary.org/b/isbn/9780553382563-L.jpg'),

-- Christie
('Murder on the Orient Express',
 (SELECT author_id FROM authors WHERE surname='Christie' LIMIT 1),
 'Hercule Poirot solves a murder on a train.',
 'https://covers.openlibrary.org/b/isbn/9780062693662-L.jpg'),
('And Then There Were None',
 (SELECT author_id FROM authors WHERE surname='Christie' LIMIT 1),
 'Ten strangers are trapped and killed one by one.',
 'https://covers.openlibrary.org/b/isbn/9780062073488-L.jpg'),
('The ABC Murders',
 (SELECT author_id FROM authors WHERE surname='Christie' LIMIT 1),
 'Poirot faces a serial killer with an alphabetic pattern.',
 'https://covers.openlibrary.org/b/isbn/9780062073563-L.jpg'),

-- King
('The Shining',
 (SELECT author_id FROM authors WHERE surname='King' LIMIT 1),
 'A horror classic set in the Overlook Hotel.',
 'https://covers.openlibrary.org/b/isbn/9780307743657-L.jpg'),
('Carrie',
 (SELECT author_id FROM authors WHERE surname='King' LIMIT 1),
 'A tale of telekinesis and high school horror.',
 'https://covers.openlibrary.org/b/isbn/9780307743664-L.jpg'),
('It',
 (SELECT author_id FROM authors WHERE surname='King' LIMIT 1),
 'A shape-shifting horror terrorizes Derry.',
 'https://covers.openlibrary.org/b/isbn/9781501142970-L.jpg'),
('Misery',
 (SELECT author_id FROM authors WHERE surname='King' LIMIT 1),
 'A writer is held captive by an obsessed fan.',
 'https://covers.openlibrary.org/b/isbn/9781501143106-L.jpg'),

-- Rowling
('Harry Potter and the Sorcerer''s Stone',
 (SELECT author_id FROM authors WHERE surname='Rowling' LIMIT 1),
 'The first Harry Potter novel.',
 'https://covers.openlibrary.org/b/isbn/9780590353427-L.jpg'),
('Harry Potter and the Chamber of Secrets',
 (SELECT author_id FROM authors WHERE surname='Rowling' LIMIT 1),
 'The second Harry Potter novel.',
 'https://covers.openlibrary.org/b/isbn/9780439064873-L.jpg'),
('Harry Potter and the Prisoner of Azkaban',
 (SELECT author_id FROM authors WHERE surname='Rowling' LIMIT 1),
 'The third Harry Potter novel.',
 'https://covers.openlibrary.org/b/isbn/9780439136365-L.jpg'),
('Harry Potter and the Goblet of Fire',
 (SELECT author_id FROM authors WHERE surname='Rowling' LIMIT 1),
 'The fourth Harry Potter novel.',
 'https://covers.openlibrary.org/b/isbn/9780439139601-L.jpg'),
('Harry Potter and the Order of the Phoenix',
 (SELECT author_id FROM authors WHERE surname='Rowling' LIMIT 1),
 'The fifth Harry Potter novel.',
 'https://covers.openlibrary.org/b/isbn/9780439358071-L.jpg'),

-- Clarke
('2001: A Space Odyssey',
 (SELECT author_id FROM authors WHERE surname='Clarke' LIMIT 1),
 'A space epic about evolution and AI.',
 'https://covers.openlibrary.org/b/isbn/9780451452733-L.jpg'),

-- Adams
('The Hitchhiker''s Guide to the Galaxy',
 (SELECT author_id FROM authors WHERE surname='Adams' LIMIT 1),
 'Comedy sci-fi classic.',
 'https://covers.openlibrary.org/b/isbn/9780345391803-L.jpg'),

-- Austen
('Pride and Prejudice',
 (SELECT author_id FROM authors WHERE surname='Austen' LIMIT 1),
 'A classic romance novel.',
 'https://covers.openlibrary.org/b/isbn/9780141439518-L.jpg'),
('Sense and Sensibility',
 (SELECT author_id FROM authors WHERE surname='Austen' LIMIT 1),
 'A classic romance about the Dashwood sisters.',
 'https://covers.openlibrary.org/b/isbn/9780141439662-L.jpg'),

-- Dostoevsky
('Crime and Punishment',
 (SELECT author_id FROM authors WHERE surname='Dostoevsky' LIMIT 1),
 'A psychological novel about guilt and redemption.',
 'https://covers.openlibrary.org/b/isbn/9780140449136-L.jpg'),
('The Brothers Karamazov',
 (SELECT author_id FROM authors WHERE surname='Dostoevsky' LIMIT 1),
 'A profound philosophical novel.',
 'https://covers.openlibrary.org/b/isbn/9780374528379-L.jpg'),

-- Twain
('The Adventures of Tom Sawyer',
 (SELECT author_id FROM authors WHERE surname='Twain' LIMIT 1),
 'Classic coming-of-age story.',
 'https://covers.openlibrary.org/b/isbn/9780143039563-L.jpg'),
('Adventures of Huckleberry Finn',
 (SELECT author_id FROM authors WHERE surname='Twain' LIMIT 1),
 'A classic American novel.',
 'https://covers.openlibrary.org/b/isbn/9780142437179-L.jpg'),

-- Hemingway
('The Old Man and the Sea',
 (SELECT author_id FROM authors WHERE surname='Hemingway' LIMIT 1),
 'A fisherman battles a giant marlin.',
 'https://covers.openlibrary.org/b/isbn/9780684801223-L.jpg'),

-- Shelley
('Frankenstein',
 (SELECT author_id FROM authors WHERE surname='Shelley' LIMIT 1),
 'A foundational gothic science fiction novel.',
 'https://covers.openlibrary.org/b/isbn/9780143131847-L.jpg'),

-- Stoker
('Dracula',
 (SELECT author_id FROM authors WHERE surname='Stoker' LIMIT 1),
 'Classic vampire novel.',
 'https://covers.openlibrary.org/b/isbn/9780141439846-L.jpg'),

-- Wells
('The Time Machine',
 (SELECT author_id FROM authors WHERE surname='Wells' LIMIT 1),
 'A scientist travels into the far future.',
 'https://covers.openlibrary.org/b/isbn/9780553213515-L.jpg'),
('The War of the Worlds',
 (SELECT author_id FROM authors WHERE surname='Wells' LIMIT 1),
 'Martian invasion of Earth.',
 'https://covers.openlibrary.org/b/isbn/9780553213386-L.jpg'),

-- Carroll
('Alice''s Adventures in Wonderland',
 (SELECT author_id FROM authors WHERE surname='Carroll' LIMIT 1),
 'A surreal children''s classic.',
 'https://covers.openlibrary.org/b/isbn/9780141321073-L.jpg'),

-- Saint-Exupery
('The Little Prince',
 (SELECT author_id FROM authors WHERE surname='de Saint-Exupery' LIMIT 1),
 'A poetic tale about life and friendship.',
 'https://covers.openlibrary.org/b/isbn/9780156012195-L.jpg'),

-- Dan Brown
('The Da Vinci Code',
 (SELECT author_id FROM authors WHERE surname='Brown' LIMIT 1),
 'A thriller involving secret societies and codes.',
 'https://covers.openlibrary.org/b/isbn/9780307474278-L.jpg'),
('Angels & Demons',
 (SELECT author_id FROM authors WHERE surname='Brown' LIMIT 1),
 'A thriller set around the Vatican.',
 'https://covers.openlibrary.org/b/isbn/9780743493468-L.jpg'),

-- Collins
('The Hunger Games',
 (SELECT author_id FROM authors WHERE surname='Collins' LIMIT 1),
 'Dystopian YA novel.',
 'https://covers.openlibrary.org/b/isbn/9780439023481-L.jpg'),
('Catching Fire',
 (SELECT author_id FROM authors WHERE surname='Collins' LIMIT 1),
 'Second novel in The Hunger Games trilogy.',
 'https://covers.openlibrary.org/b/isbn/9780439023498-L.jpg'),
('Mockingjay',
 (SELECT author_id FROM authors WHERE surname='Collins' LIMIT 1),
 'Final novel in The Hunger Games trilogy.',
 'https://covers.openlibrary.org/b/isbn/9780439023511-L.jpg'),

-- Harper Lee
('To Kill a Mockingbird',
 (SELECT author_id FROM authors WHERE surname='Lee' LIMIT 1),
 'A novel about justice and race in the Deep South.',
 'https://covers.openlibrary.org/b/isbn/9780061120084-L.jpg'),

-- Huxley
('Brave New World',
 (SELECT author_id FROM authors WHERE surname='Huxley' LIMIT 1),
 'A dystopian science fiction novel.',
 'https://covers.openlibrary.org/b/isbn/9780060850524-L.jpg'),

-- Bradbury
('Fahrenheit 451',
 (SELECT author_id FROM authors WHERE surname='Bradbury' LIMIT 1),
 'A future where books are burned.',
 'https://covers.openlibrary.org/b/isbn/9781451673319-L.jpg'),

-- C.S. Lewis (Narnia)
('The Lion, the Witch and the Wardrobe',
 (SELECT author_id FROM authors WHERE surname='Lewis' AND firstname='C.S.' LIMIT 1),
 'A classic children''s fantasy (Narnia).',
 'https://covers.openlibrary.org/b/isbn/9780064404990-L.jpg'),
('Prince Caspian',
 (SELECT author_id FROM authors WHERE surname='Lewis' AND firstname='C.S.' LIMIT 1),
 'Second published Narnia book.',
 'https://covers.openlibrary.org/b/isbn/9780064409452-L.jpg'),

-- Steinbeck
('Of Mice and Men',
 (SELECT author_id FROM authors WHERE surname='Steinbeck' LIMIT 1),
 'A story about friendship and hardship.',
 'https://covers.openlibrary.org/b/isbn/9780140177398-L.jpg'),
('The Grapes of Wrath',
 (SELECT author_id FROM authors WHERE surname='Steinbeck' LIMIT 1),
 'A novel of the Great Depression.',
 'https://covers.openlibrary.org/b/isbn/9780143039433-L.jpg'),

-- Dodatkowe tytuły, żeby dobić do 60 (różni autorzy już istnieją)
('The Silmarillion',
 (SELECT author_id FROM authors WHERE surname='Tolkien' LIMIT 1),
 'Mythopoeic stories of Middle-earth.',
 'https://covers.openlibrary.org/b/isbn/9780618126989-L.jpg'),
('The Murder of Roger Ackroyd',
 (SELECT author_id FROM authors WHERE surname='Christie' LIMIT 1),
 'A famous Poirot mystery with a twist.',
 'https://covers.openlibrary.org/b/isbn/9780062073570-L.jpg'),
('The Stand',
 (SELECT author_id FROM authors WHERE surname='King' LIMIT 1),
 'Post-apocalyptic horror novel.',
 'https://covers.openlibrary.org/b/isbn/9780307743688-L.jpg'),
('The Gunslinger',
 (SELECT author_id FROM authors WHERE surname='King' LIMIT 1),
 'The Dark Tower series begins.',
 'https://covers.openlibrary.org/b/isbn/9780452284692-L.jpg'),
('Foundation and Empire',
 (SELECT author_id FROM authors WHERE surname='Asimov' LIMIT 1),
 'Second Foundation novel.',
 'https://covers.openlibrary.org/b/isbn/9780553293371-L.jpg'),
('Second Foundation',
 (SELECT author_id FROM authors WHERE surname='Asimov' LIMIT 1),
 'Third Foundation novel.',
 'https://covers.openlibrary.org/b/isbn/9780553293364-L.jpg'),
('The Martian Chronicles',
 (SELECT author_id FROM authors WHERE surname='Bradbury' LIMIT 1),
 'Stories of Mars colonization.',
 'https://covers.openlibrary.org/b/isbn/9781451678192-L.jpg'),
('The Illustrated Man',
 (SELECT author_id FROM authors WHERE surname='Bradbury' LIMIT 1),
 'A collection of sci-fi stories.',
 'https://covers.openlibrary.org/b/isbn/9781451678185-L.jpg'),
('Emma',
 (SELECT author_id FROM authors WHERE surname='Austen' LIMIT 1),
 'A classic novel of manners.',
 'https://covers.openlibrary.org/b/isbn/9780141439587-L.jpg'),
('Persuasion',
 (SELECT author_id FROM authors WHERE surname='Austen' LIMIT 1),
 'A late Austen romance.',
 'https://covers.openlibrary.org/b/isbn/9780141439686-L.jpg'),
('The Time Traveler''s Wife',
 (SELECT author_id FROM authors WHERE surname='Austen' LIMIT 1),
 'Placeholder author to keep schema consistent.',
 'https://covers.openlibrary.org/b/isbn/9780156029438-L.jpg'); -- UWAGA: tytuł realny, ale autor nie pasuje (nie łamie FK, tylko "merytorykę")
-- Jeśli chcesz: podmienimy na prawdziwego autora (Niffenegger) + dopiszemy autora do authors.

-- Żeby utrzymać "bez zmyślania" i merytoryczną spójność autor-książka:
-- Usuwam ostatni wiersz i zamiast tego dodaję autora Niffenegger + poprawny rekord.
DELETE FROM book_title WHERE title='The Time Traveler''s Wife';

INSERT INTO authors(firstname, surname, nickname, nationality, birthdate, deathdate, genre_id) VALUES
('Audrey', 'Niffenegger', NULL, 'United States', '1963-06-13', NULL, (SELECT genre_id FROM genre_dict WHERE name='ROMANCE'));

INSERT INTO book_title(title, author_id, description, photo) VALUES
('The Time Traveler''s Wife',
 (SELECT author_id FROM authors WHERE surname='Niffenegger' LIMIT 1),
 'A romantic novel with time travel elements.',
 'https://covers.openlibrary.org/b/isbn/9780156029438-L.jpg');

-- Sprawdzenie ilości:
-- SELECT COUNT(*) FROM book_title;

-- =========================================================
-- 5) BOOK EDITIONS (1 per title) + BOOK COPIES (2 per edition)
-- =========================================================

-- Edycje: isbn = unikalny, language = ENGLISH / POLISH, publisher = losowo
INSERT INTO book_edition(title_id, publisher_id, isbn, language_id, pages, publish_year, is_hard_cover, value, notes)
SELECT
 t.title_id,
 (SELECT publisher_id FROM publishers ORDER BY random() LIMIT 1),
 regexp_replace(split_part(t.photo, '/b/isbn/', 2), '-L.jpg', ''),  -- ISBN wyciągnięty z photo linku
 (SELECT language_id FROM languages_dict WHERE language='ENGLISH'),
 (200 + (random()*500)::int),
 (1950 + (random()*75)::int),
 (random() > 0.5),
 round((10 + random()*90)::numeric, 2),
 'Seed edition for ' || t.title
FROM book_title t;

-- Egzemplarze: 2 per edition, inventory_num unikalny
INSERT INTO book_copy(inventory_num, edition_id, condition, defects, is_available)
SELECT
 'INV-' || e.edition_id || '-A',
 e.edition_id,
 (ARRAY['New','Good','Used','Worn'])[1 + (random()*3)::int],
 NULL,
 (random() > 0.2)
FROM book_edition e;

INSERT INTO book_copy(inventory_num, edition_id, condition, defects, is_available)
SELECT
 'INV-' || e.edition_id || '-B',
 e.edition_id,
 (ARRAY['New','Good','Used','Worn'])[1 + (random()*3)::int],
 NULL,
 (random() > 0.2)
FROM book_edition e;

-- =========================================================
-- 6) BOOK CATEGORIES: 1-2 kategorie na tytuł
-- =========================================================
-- Prosto: wszystkie "KIDS" dla dziecięcych, reszta "ADULTS", + LONG/SHORT na podstawie pages

INSERT INTO book_categories(category_id, title_id)
SELECT
 (SELECT category_id FROM categories_dict WHERE name='ADULTS'),
 t.title_id
FROM book_title t;

-- część jako KIDS (wybrane tytuły)
INSERT INTO book_categories(category_id, title_id)
SELECT
 (SELECT category_id FROM categories_dict WHERE name='KIDS'),
 t.title_id
FROM book_title t
WHERE t.title IN (
  'The Hobbit',
  'Alice''s Adventures in Wonderland',
  'The Little Prince',
  'The Lion, the Witch and the Wardrobe',
  'Prince Caspian',
  'Harry Potter and the Sorcerer''s Stone',
  'Harry Potter and the Chamber of Secrets',
  'Harry Potter and the Prisoner of Azkaban'
);

-- LONG/SHORT po pages (z edycji)
INSERT INTO book_categories(category_id, title_id)
SELECT
 CASE WHEN e.pages >= 350
      THEN (SELECT category_id FROM categories_dict WHERE name='LONG')
      ELSE (SELECT category_id FROM categories_dict WHERE name='SHORT')
 END,
 e.title_id
FROM book_edition e;

-- =========================================================
-- 7) REVIEWS: dużo opinii (np. 300)
-- =========================================================
INSERT INTO review(content, create_date, stars, title_id, user_id)
SELECT
 CASE WHEN random() > 0.25
      THEN 'Review #' || gs || ': ' ||
           (ARRAY[
              'Bardzo dobra książka, wrócę do niej.',
              'Fajna historia, ale momentami dłużyzny.',
              'Świetna okładka i klimat.',
              'Jedna z lepszych pozycji w tym gatunku.',
              'Nie moje klimaty, ale rozumiem hype.',
              'Mega wciąga, polecam!'
           ])[1 + (random()*5)::int]
      ELSE NULL
 END,
 now() - ((random()*3650)::int || ' days')::interval,
 (1 + (random()*4)::int),
 (SELECT title_id FROM book_title ORDER BY random() LIMIT 1),
 (SELECT user_id FROM users WHERE username LIKE 'user%' ORDER BY random() LIMIT 1)
FROM generate_series(1,300) gs;

-- =========================================================
-- 8) RESERVATIONS: 80 wypożyczeń (część aktywna, część oddana)
-- =========================================================
-- Wybieramy kopie; jeśli wylosuje się niedostępna, to i tak rezerwacja może istnieć,
-- ale dla sensu: weźmy is_available=false dla części i ustawmy return_date NULL.

-- 40 aktywnych (return_date NULL) na losowych kopiach
INSERT INTO reservations(user_id, create_date, return_date, expected_return_date, damage_details, copy_id)
SELECT
 (SELECT user_id FROM users WHERE username LIKE 'user%' ORDER BY random() LIMIT 1),
 (current_date - (1 + (random()*60)::int)),
 NULL,
 (current_date + (7 + (random()*21)::int)),
 CASE WHEN random() < 0.10 THEN 'Minor scratches' ELSE NULL END,
 c.copy_id
FROM book_copy c
ORDER BY random()
LIMIT 40;

-- 40 zakończonych (return_date != NULL), część spóźniona, część uszkodzona
INSERT INTO reservations(user_id, create_date, return_date, expected_return_date, damage_details, copy_id)
SELECT
 (SELECT user_id FROM users WHERE username LIKE 'user%' ORDER BY random() LIMIT 1),
 (current_date - (120 + (random()*900)::int)),
 (current_date - (random()*30)::int),
 (current_date - (60 + (random()*600)::int)),
 CASE WHEN random() < 0.15 THEN 'Torn page / cover wear' ELSE NULL END,
 c.copy_id
FROM book_copy c
ORDER BY random()
LIMIT 40;

-- Ustaw dostępność kopii: jeśli jest aktywna rezerwacja (return_date NULL) -> is_available=false
UPDATE book_copy bc
SET is_available = false
WHERE EXISTS (
  SELECT 1 FROM reservations r
  WHERE r.copy_id = bc.copy_id AND r.return_date IS NULL
);

-- =========================================================
-- 9) PENALTIES: dla części zakończonych rezerwacji (np. 15)
-- =========================================================
INSERT INTO penalties(reservation_id, amount, days_late, created_at, is_paid, paid_at)
SELECT
 r.reservation_id,
 round((0.50 * (1 + (random()*20)::int) + CASE WHEN r.damage_details IS NOT NULL THEN 10 ELSE 0 END)::numeric, 2),
 (1 + (random()*20)::int),
 now() - ((random()*365)::int || ' days')::interval,
 (random() > 0.5),
 CASE WHEN random() > 0.5 THEN now() - ((random()*200)::int || ' days')::interval ELSE NULL END
FROM reservations r
WHERE r.return_date IS NOT NULL
ORDER BY random()
LIMIT 15;

-- =========================================================
-- 10) NOTIFICATIONS: 60 powiadomień
-- =========================================================
INSERT INTO notification(content, created_at, read_at, title, user_id)
SELECT
 'Powiadomienie #' || gs || ': ' ||
 (ARRAY[
   'Twoja rezerwacja została zaktualizowana.',
   'Książka z waitlisty jest dostępna.',
   'Przypomnienie o terminie zwrotu.',
   'Nowa recenzja pojawiła się przy tytule, który obserwujesz.',
   'Zaległa kara do opłacenia.'
  ])[1 + (random()*4)::int],
 now() - ((random()*500)::int || ' days')::interval,
 CASE WHEN random() > 0.6 THEN now() - ((random()*200)::int || ' days')::interval ELSE NULL END,
 (ARRAY[
   'Aktualizacja',
   'Książka dostępna',
   'Przypomnienie',
   'Nowość',
   'Kara'
  ])[1 + (random()*4)::int],
 (SELECT user_id FROM users ORDER BY random() LIMIT 1)
FROM generate_series(1,60) gs;

-- =========================================================
-- 11) WAITLIST: 40 pozycji (dla tytułów, często 1-3 osoby)
-- =========================================================
-- Rozbijamy na "grupy" po tytule, pozycje rosną.
WITH picked_titles AS (
  SELECT title_id FROM book_title ORDER BY random() LIMIT 15
),
wl AS (
  SELECT
    pt.title_id,
    u.user_id,
    row_number() OVER (PARTITION BY pt.title_id ORDER BY random()) AS pos
  FROM picked_titles pt
  JOIN LATERAL (
    SELECT user_id FROM users WHERE username LIKE 'user%' ORDER BY random() LIMIT (1 + (random()*2)::int)
  ) u ON true
)
INSERT INTO waitlist(user_id, title_id, is_active, create_date, position)
SELECT
 user_id,
 title_id,
 true,
 now() - ((random()*120)::int || ' days')::interval,
 pos
FROM wl;

COMMIT;
