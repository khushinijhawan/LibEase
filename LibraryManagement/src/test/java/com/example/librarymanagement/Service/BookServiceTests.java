package com.example.librarymanagement.Service;
import com.example.librarymanagement.ExceptionHandling.BookAlreadyBorrowed;
import com.example.librarymanagement.Module.Book;
import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Repo.BookRepo;
import com.example.librarymanagement.Repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepo bookRepo;
    @Mock
    private UserRepo userRepo;
    @Test
    public void getBookById_first_successCase()
    {
        Book book1= Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();
        Optional<Book> b=Optional.ofNullable(book1);
        when(bookRepo.findById(1)).thenReturn(b);
        Book book=bookService.findBookByID(1);
        assertNotNull(book);
        assertEquals(1,book.getId());
    }
    @Test
    public void getBookById_second_successCase()
    {
        Optional<Book> b=Optional.empty();
        when(bookRepo.findById(any())).thenReturn(b);
        Book book1=bookService.findBookByID(null);
        assertNull(book1);
    }
    @Test
    public void getBookById_third_successCase()
    {

        Book book1= Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();
        Book book2= Book.builder().id(2).name("Beloved").author("Toni Morrison").borrower(User.builder().build()).borrowed(true).build();
        when(bookRepo.findById(1)).thenReturn(Optional.ofNullable(book1));
        Book book = bookService.findBookByID(1);
        assertNotNull(book);
        assertEquals(1, book.getId());
        assertTrue(bookRepo.findById(1).isPresent());
        assertNotEquals(book1.getId(), book2.getId());
        assertEquals(1, bookRepo.findById(1).get().getId());
    }
    @Test
    public void getAllBooks_successCase()
    {
        List<Book> bookList = Arrays.asList(
                Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build(),
                Book.builder().id(2).name("Beloved").author("Toni Morrison").borrower(User.builder().build()).borrowed(true).build()
        );
        when(bookRepo.findAll()).thenReturn(bookList);
        List<Book> books = bookService.findAllBooks();
        assertNotNull(books);
        assertEquals(1, books.get(0).getId());
        assertEquals(2, books.get(1).getId());
    }
    @Test
    public void saveBooks_successCase()
    {
        Book bookToSave = Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();
        when(bookRepo.save(any(Book.class))).thenReturn(bookToSave);
        Book savedBook = bookService.saveBook(bookToSave);
        assertNotNull(savedBook);
        assertEquals("HarryPotter", savedBook.getName());
    }
    @Test
    public void deleteBookById_successCase()
    {
        Book book1= Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();
        when(bookRepo.save(any(Book.class))).thenReturn(book1);
        Book savedBook = bookService.saveBook(book1);
        doNothing().when(bookRepo).deleteById(1);
        bookService.deleteBookById(savedBook.getId());
    }
    @Test
    public void borrowBook_successCase()
    {
        Book book = Book.builder().id(1).name("Harry Potter").borrowed(false).build();

        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        User user = User.builder().id(1).name("Khushi").phn_no(87584346L).build();

        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book borrowedBook = bookService.borrowBook(1, 1);

        assertEquals(book, borrowedBook);
        assertTrue(borrowedBook.isBorrowed());
        assertEquals(user, borrowedBook.getBorrower());
    }
    @Test
    public void borrowBook_AlreadyBorrowedCase() {
        User user = User.builder().id(1).name("Khushi").phn_no(1234567890L).build();
        Book borrowedBook = Book.builder().id(1).name("The Great Gatsby").borrower(user).borrowed(true).build();

        when(bookRepo.findById(1)).thenReturn(Optional.of(borrowedBook));
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        try {
            bookService.borrowBook(1, 1);
        } catch (BookAlreadyBorrowed e) {
            assertEquals("Book Already Borrowed", e.getMessage());
        }
    }

    @Test
    public void returnBook_SuccessCase()
    {
        User user = User.builder().id(1).name("Khushi").phn_no(87584346L).build();
        Book book = Book.builder().id(1).name("Harry Potter").borrower(user).borrowed(true).build();

        when(bookRepo.findById(1)).thenReturn(Optional.of(book));

        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book returnedBook = bookService.returnBook(1);

        assertEquals(book, returnedBook);
        assertFalse(returnedBook.isBorrowed());
        assertNull(returnedBook.getBorrower());
    }

}
