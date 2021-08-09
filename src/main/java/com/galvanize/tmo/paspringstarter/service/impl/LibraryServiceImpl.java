package com.galvanize.tmo.paspringstarter.service.impl;

import com.galvanize.tmo.paspringstarter.model.Book;
import com.galvanize.tmo.paspringstarter.service.LibraryService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LibraryServiceImpl implements LibraryService {

    private List<Book> listOfLibraryBooks = new ArrayList<>();
    private static int idSequence = 1;

    public LibraryServiceImpl() {
    }

    @Override
    public List<Book> getAllBooks() {
        Collections.sort(listOfLibraryBooks);
        return listOfLibraryBooks;
    }

    @Override
    public void deleteAllBooks() {
        listOfLibraryBooks.clear();
    }

    @Override
    public Book addBook(Book book) {
        book.setId(idSequence++);
        listOfLibraryBooks.add(book);
        return book;
    }

}
