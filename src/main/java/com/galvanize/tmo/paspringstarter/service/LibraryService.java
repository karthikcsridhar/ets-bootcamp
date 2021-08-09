package com.galvanize.tmo.paspringstarter.service;

import com.galvanize.tmo.paspringstarter.model.Book;

import java.util.List;

public interface LibraryService {
    List<Book> getAllBooks();
    void deleteAllBooks();

    Book addBook(Book book);
}
