package com.inv.noAuth.exception;

public class BookAlreadyExistsException extends RuntimeException{
    public BookAlreadyExistsException(String title, String authorName, String authorSurname) {
        super("Book with title \"" + title + "\" and author " + authorName + " " + authorSurname + " already exists.");
    }
}
