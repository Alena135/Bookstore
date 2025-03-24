package com.inv.withAuth.exception;

public class BookAlreadyExistsException extends RuntimeException{
    public BookAlreadyExistsException(String title, String authorName, String authorSurname) {
        super("Book with title \"" + title + "\" and author " + authorName + " " + authorSurname + " already exists.");
    }
}
