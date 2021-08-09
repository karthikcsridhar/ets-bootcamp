package com.galvanize.tmo.paspringstarter.api;

import com.galvanize.tmo.paspringstarter.model.Book;
import com.galvanize.tmo.paspringstarter.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/health")
    public void health() { }

    /**
     * Get a list of all books sorted by title
     * @param book
     * @return list of all books sorted by title
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(libraryService.getAllBooks(), HttpStatus.OK);
    }

    /**
     * Add a new book to library
     * @param book
     * @return added book with it's generated id
     */
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return new ResponseEntity<>(libraryService.addBook(book), HttpStatus.CREATED);
    }

    @DeleteMapping("books")
    public ResponseEntity deleteAllBooks() {
        libraryService.deleteAllBooks();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
