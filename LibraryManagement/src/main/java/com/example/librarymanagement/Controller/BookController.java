package com.example.librarymanagement.Controller;
import com.example.librarymanagement.ExceptionHandling.HandleException;
import com.example.librarymanagement.Module.Book;
import com.example.librarymanagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.librarymanagement.ExceptionHandling.*;
@RestController
@RequestMapping("/api/books")
public class BookController extends HandleException {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping
    public List<Book> getAllBooks()
    {
        return bookService.findAllBooks();
    }
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Integer id)
    {
        return bookService.findBookByID(id);
    }
    @PostMapping
    public Book addBook(@RequestBody Book book)
    {
        return bookService.saveBook(book);
    }
    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book)
    {
        return bookService.saveBook(book);
    }
    @DeleteMapping("/{id}")
    public void deleteBook(Integer id)
    {
        bookService.deleteBookById(id);
    }
    @PostMapping("/{bookId}/borrow/{userId}")
    public ResponseEntity<String> borrowBook(@PathVariable Integer bookId, @PathVariable Integer userId) {
        try {
            Book borrowedBook = bookService.borrowBook(bookId, userId); //runtime exception encountered
            if (borrowedBook != null) {
                return ResponseEntity.ok("Book borrowed successfully.");
            } else {
                return ResponseEntity.badRequest().body("Unable to borrow the book. Book or user not found.");
            }
        }catch(BookAlreadyBorrowed ex){      // catching runtime exception
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred.");
        }
    }
    @PostMapping("/{bookId}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Integer bookId)
    {
        Book returnedBook=bookService.returnBook(bookId);
        if(returnedBook!=null)
        {
            ResponseEntity.ok(returnedBook);
        }
        return ResponseEntity.badRequest().build();
    }
}
