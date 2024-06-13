package com.example.librarymanagement.Service;
import com.example.librarymanagement.ExceptionHandling.BookAlreadyBorrowed;
import com.example.librarymanagement.ExceptionHandling.HandleException;
//import com.example.librarymanagement.ExceptionHandling.InvalidBookIdException;
//import com.example.librarymanagement.ExceptionHandling.InvalidUserIdException;
import com.example.librarymanagement.ExceptionHandling.MissingDataException;
import com.example.librarymanagement.Module.Book;
import com.example.librarymanagement.Repo.BookRepo;
import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService extends HandleException {

    private final BookRepo bookRepo;
    private final UserRepo userRepo;
    @Autowired
    public BookService(BookRepo bookRepo, UserRepo userRepo)
    {
        this.bookRepo = bookRepo;
        this.userRepo=userRepo;
    }
    public List<Book> findAllBooks(){

        log.info("Request to get all books");
        return bookRepo.findAll();
    }
    public Book findBookByID(Integer id){
        log.info("Request to get book with ID: {}", id);
        Optional<Book> user= bookRepo.findById(id);
        return user.orElse(null);
    }
    public Book saveBook(Book book){
        log.info("Request to save book: {}", book);
        return bookRepo.save(book);
    }
    public void deleteBookById(Integer id)
    {
        log.info("Request to delete book with ID: {}", id);
        bookRepo.deleteById(id);
    }
    public Book borrowBook(Integer bookId, Integer userId) {
        try {
            //log.info("Request to borrow book with ID {} by user with ID {}", bookId, userId);
            Optional<Book> book= bookRepo.findById(bookId);
            if(book.isEmpty()){
                throw new MissingDataException("Book not found");
            }
            Optional<User> user= userRepo.findById(bookId);
            if(user.isEmpty()){
                throw new MissingDataException("User not found");
            }
            if (!book.get().isBorrowed()) {
                book.get().setBorrower(user.get());
                book.get().setBorrowed(true);
                logBorrowDetails(book.get());
                return saveBook(book.get());
            } else {
                throw new BookAlreadyBorrowed("Unable to borrow the book");
            }
        }catch(BookAlreadyBorrowed e){
            log.error("Book already borrowed",e);
            throw new BookAlreadyBorrowed("Book Already Borrowed");
        }
        catch(MissingDataException e){
            log.error("Error occurred", e);
            throw new BookAlreadyBorrowed(e.getMessage());
        }
        catch (Exception e) {
            // Catch any other exceptions and translate them into a specific RuntimeException
            handleException(e);
            log.error("Internal server error", e);
            throw new RuntimeException("An internal server error occurred.");
        }
    }
    public Book returnBook(Integer bookId)
    {
        log.info("Request to return book with ID: {}", bookId);
        Book book=findBookByID(bookId);
        if(book!=null && book.isBorrowed())
        {
            book.setBorrowed(false);
            book.setBorrower(null);
            log.info("Book returned successfully with id: {}", bookId);
            return saveBook(book);
        }
        log.error("Failed to return book with ID: {}", bookId);
        return null;
    }
    private void logBorrowDetails(Book book) {
        log.trace("Borrow details - Book: {}, User: {}", book, book.getBorrower());
    }
}
