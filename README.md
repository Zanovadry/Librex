# Librex – system do obsługi biblioteki (Backend)

Ten projekt to backend systemu bibliotecznego realizowanego w ramach laboratoriów AGH.  
Całość została zbudowana w **Spring Boot + PostgreSQL** i spełnia wymagania modułu M1 + M2 + M3:

M1:
- pełny model obiektowy + bazodanowy,
- rejestracja użytkownika,
- autentykacja i autoryzacja,
- operacje CRUD na użytkownikach,
- role systemowe (CUSTOMER, LIBRARIAN, ADMIN).

M2:
- operacje CRUD na książkach
- wypożyczanie książki
- rezerwacje
- powiadomienia
- recenzje

M3:
- wysyłanie emaili do użykowników
- raporty statystyczne
- baza danych wypełniona danymi
- prolognowanie rezerwacji

---

# Endpointy dostępne w API

## Auth (publiczne)
GET  /api/auth/me  
POST /api/auth/register

## Użytkownicy (ADMIN / LIBRARIAN)
GET    /api/users  
GET    /api/users/{id}  
PUT    /api/users/{id}
DELETE /api/users/{id}

## Książki (BookTitle)
GET    /api/books  
POST   /api/books
GET    /api/books/{id}    
PUT    /api/books/{id}  
DELETE /api/books/{id}
GET    /api/books/{titleId}/copies

## Autorzy
GET    /api/authors  
POST   /api/authors  
GET    /api/authors/{id}  
PUT    /api/authors/{id}  
DELETE /api/authors/{id}

## Recenzje
POST /api/reviews  
GET  /api/reviews/title/{titleId}  
GET  /api/reviews/user  
GET  /api/reviews/user/title/{titleId}

## Powiadomienia
GET   /api/notifications  
PATCH /api/notifications/{id}

## Wypożyczenia (Reservations)
POST /api/reservations/borrow  
POST /api/reservations/return

> NOWE!!!

POST /api/reservations/prolong

## Kolejka oczekujących (Waitlist)
POST /api/waitlist/join

## Panel bibliotekarza
GET /api/librarian/users/search  
GET /api/librarian/users/{userId}

## Statystyki

> NOWE!!!

GET /api/statistics/reservations    
GET /api/statistics/users


---

# Jak uruchomić projekt

## Wymagania
- Java 21+
- IntelliJ IDEA / Gradle 8+

Wystarczy pobrać pliki projektu i uruchomić go, np. za pomocą Intellij\
Aplikacja wraz z frontendem będzie wówczas dostępna pod adresem `http://localhost:8080/index.html`
> **Nie wszystkie endpointy są zaimplementowane do frontendu**


# Nowości dla M3

- Potwierdzenie rejestracji poprzez wiadomość email    
<img width="493" height="35" alt="obraz" src="https://github.com/user-attachments/assets/e675ec55-4c1f-4000-bb11-eb86c602dc36" />

- Powiadomienia się wysyłane również jako wiadomość email
- Stworzone dodatkowe endopointy dla statystyk:
    - Użytkowników
    - Rezerwacji
- Stworzony dodatkowy endopoint do prolongowania rezerwacji
- Drobne poprawki w kodzie i schemacie bazy danych
- Wprowadzone dane do bazy
- Rozbudowany Front End


# Nowości dla M2

- Baza jest postawiona online `supabase.com`, nie trzeba nic konfigurować lokalnie
- Poprawione logowanie i tworzenie konta
- Stworzone dodatkowe CRUD
    - Książki
    - Autorzy
    - Wypożyczanie
    - Zapisywanie na waitlistę
    - Zarządzanie użytkownikami
    - Powiadomienia
    - Opinie
- Przejrzysty frontend
- Zaaktualizowany schemat bazy danych
    - Tabela `Notification`
    - Tabela `Rewiev`


<img width="1470" height="1024" alt="Librex-2026-01-09_16-23" src="https://github.com/user-attachments/assets/b4911c63-88c1-452a-b44d-3e222b4f0b2f" />

---

# !!!PONIŻEJ STARA DOKUMENTACJA (do M1)!!!

# 1. Co było wcześniej

## 1.1. Problemy z modelem danych

Poprzednia wersja miała m.in.:

- bazę H2 zamiast PostgreSQL,
- nazwy pól (`PermissionID`, `Firstname`, `Surname`, `CountryID`),
- daty przechowywane jako `String`,
- hasła przechowywane w czystym tekście,
- brak DTO — API wystawiało encje bezpośrednio,
- brak unikalności email i username.

## 1.2. Problemy architektoniczne

- brak walidacji pól wejściowych,
- brak obsługi wyjątków,
- kontrolery powiązane bezpośrednio z encjami,
- brak jakiejkolwiek autoryzacji,
- brak testów integracyjnych.

## 1.3. Problemy z kontrolerami

- brak rejestracji użytkownika,
- brak rozdziału DTO/encje,
- brak ról i uprawnień.

---

# 2. Co zostało zmienione

## 2.1. Wprowadzenie PostgreSQL

Aplikacja została przeniesiona z H2 na PostgreSQL, ponieważ:

- projekt docelowo działa na Postgresie,
- H2 zachowuje się inaczej (inne typy, odmienne zachowanie),
- testy integracyjne muszą działać na prawdziwej bazie.

Zmodyfikowane pliki:

- `application.properties`,
- dodano `application-test.properties`.

## 2.2. Przebudowa encji domenowych

Encje Country, Permission, AppUser zostały całkowicie uporządkowane:

- poprawione nazwy kolumn,
- poprawione relacje ManyToOne,
- poprawione typy pól (np. `LocalDate`),
- uporządkowane nazewnictwo,
- dodane tabele słownikowe: `countries_dict`, `permissions_dict`.

To była niezbędna refaktoryzacja, bo wcześniejszy model był trudny do utrzymania i niezgodny z JPA.

## 2.3. Dodanie DTO (RegistrationRequest, UserResponse)

Wprowadzono prawidłową warstwę API:

- `RegistrationRequest` – dane wejściowe do rejestracji + walidacja,
- `UserResponse` – dane wyjściowe, bez hasła, z rolą.

Efekt:

- nie wystawiamy encji JPA na zewnątrz,
- hasło nigdy nie opuszcza backendu,
- walidacja działa na podstawie adnotacji.

## 2.4. Dodanie modułu bezpieczeństwa (Spring Security)

Powstał plik `SecurityConfig`, który:

- włącza HTTP Basic,
- udostępnia `/api/auth/register` publicznie,
- wymaga autoryzacji na `/api/users/**`,
- obsługuje role: ADMIN, LIBRARIAN, CUSTOMER,
- używa BCrypt do hashowania haseł.

Wcześniej nie było żadnego systemu uprawnień.

## 2.5. Rejestracja użytkownika — nowy endpoint

Dodany endpoint:

POST /api/auth/register

Proces rejestracji:

- walidacja danych wejściowych,
- sprawdzenie unikalności email i username,
- automatyczne nadanie roli CUSTOMER,
- hashowanie hasła,
- zwracanie bezpiecznego DTO.

## 2.6. AppUserService – logika biznesowa

Service został rozszerzony o:

- walidację unikalności,
- pobieranie domyślnej roli,
- pełne CRUD,
- konwersję encji do DTO,
- transakcje.

Poprzednio była to jedynie cienka nakładka na repository.

## 2.7. Testy integracyjne (MockMvc + PostgreSQL)

Dodano testy sprawdzające:

- rejestrację użytkownika,
- pobieranie listy użytkowników,
- aktualizację użytkownika,
- usuwanie użytkownika.

Testy działają na tej samej bazie (PostgreSQL), a nie na H2.

---

# 3. Obecna architektura backendu

## 3.1. Warstwa prezentacji
- `AuthController`
- `AppUserController`

## 3.2. Warstwa DTO
- `RegistrationRequest`
- `UserResponse`
- `RegistrationRequestTestFactory` (dla testów)

## 3.3. Warstwa domenowa
- `AppUser`
- `Permission`, `Role`
- `Country`, `CountryName`

## 3.4. Warstwa usług
- `AppUserService`

## 3.5. Warstwa repozytoriów
- `AppUserRepository`
- `PermissionRepository`

## 3.6. Konfiguracja bezpieczeństwa
- `SecurityConfig` — role, autoryzacja, hashowanie

---

# 4. Krótki opis zmian plik po pliku

## AuthController.java
- dodano rejestrację użytkownika,
- przyjmuje DTO, zwraca DTO.

## SecurityConfig.java
- HTTPS Basic Auth,
- endpoint `/api/auth/register` jest publiczny,
- pozostałe endpointy wymagają autoryzacji,
- BCryptPasswordEncoder.

## Country / Permission
- poprawione nazwy pól,
- tabele słownikowe,
- enumy zapisywane jako STRING.

## AppUser.java
- poprawione nazwy kolumn,
- relacje ManyToOne,
- `LocalDate birthdate`,
- `passwordHash`.

## AppUserService.java
- logika rejestracji,
- walidacja unikalności,
- CRUD,
- konwersja do DTO.

## AppUserController.java
- pełny CRUD z autoryzacją,
- zwraca DTO.

## Testy integracyjne
- MockMvc,
- PostgreSQL,
- czyszczenie bazy przez `deleteAll()`.

# 5. Schemat bazy danych

<img width="1221" height="683" alt="obraz" src="https://github.com/user-attachments/assets/88db3d94-5f31-4b13-99f2-a4fe743ad124" />



