# Backend do projektu PAI

Autor: Agata Wrześniewska

## Informacje o projekcie

Projekt utworzony przy pomocy Spring Boot (Spring 2.7.7, Java 11);  
Autoryzacja za pomocą tokena JWT; 
Baza danych MySQL

## Dostępne endpointy

### User
/register - rejestracja
/login - logowanie
/user/edit - zmiana hasła
/user/get-self - pobranie danych o zalogowanym użytkowniku
/user/delete - usunięcie użytkownika

### Post
/post/add - dodanie posta
/post/get-all-user - pobranie postów użytkownika
/post/get-all - pobranie wszystkich postów
/post/filter-date/{date} - filtrowanie po dacie utworzenia posta (format daty: YYYY-MM-DD)
/post/edit/{id} - edycja posta (id jako int)
/post/delete/{id} - edycja posta (id jako int)

### Comment
/comment/add/{post} - dodanie komentarza (id posta jako int)
/comment/get-all-comments/{post} - pobranie wszystkich komentarzy do posta (id posta jako int)
/comment/delete/{id} - usunięcie komentarza (id komentarza jako int)
