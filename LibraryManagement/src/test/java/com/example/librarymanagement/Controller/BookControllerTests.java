package com.example.librarymanagement.Controller;
import com.example.librarymanagement.Module.Book;
import com.example.librarymanagement.Module.User;
import com.example.librarymanagement.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc
@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
public class BookControllerTests {

    @MockBean
    private BookService bookService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void getBookById_successCase() throws Exception {

        Book book = Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();

        when(bookService.findBookByID(1)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.borrower").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.borrowed").value(false));

        assertNotNull(book);
    }
    @Test
    public void getAllBooks_successCase() throws Exception {
        List<Book> bookList = Arrays.asList(
                Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build(),
                Book.builder().id(2).name("Beloved").author("Toni Morrison").borrower(null).borrowed(false).build()
        );

        when(bookService.findAllBooks()).thenReturn(bookList);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookList)));

        assertNotNull(bookList);
        assertEquals(2,bookList.size());
    }
    @Test
    public void addBook_successCase() throws Exception{
        Book book = Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();

        when(bookService.saveBook(book)).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk());

        assertNotNull(book);
    }
    @Test
    public void updateBook_successCase() throws Exception{
        Book book = Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();

        when(bookService.saveBook(book)).thenReturn(book);

        mockMvc.perform(put("/api/books/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk());

        assertNotNull(book);
    }
    @Test
    public void deleteBook_successCase() throws Exception {
        Book book = Book.builder().id(1).name("HarryPotter").author("J.K. Rowling").borrower(null).borrowed(false).build();

        when(bookService.findBookByID(1)).thenReturn(book);
        doNothing().when(bookService).deleteBookById(1);
        mockMvc.perform(delete("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(book)))
                        .andExpect(status().isOk());

    }
    @Test
    public void borrowBook_first_successCase() throws Exception
    {
        Book book = Book.builder().id(1).name("Harry Potter").borrower(null).borrowed(false).build();
        User user = User.builder().id(1).name("Khushi").phn_no(87584346L).build();
        Book savedbook = Book.builder().id(1).name("Harry Potter").borrower(user).borrowed(true).build();
        when(bookService.borrowBook(book.getId(), user.getId())).thenReturn(savedbook);

        mockMvc.perform(post("/api/books/{bookId}/borrow/{userId}",1,1))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully."));

        Book actual = bookService.borrowBook(book.getId(), user.getId());
        assertTrue(actual.isBorrowed());
        assertEquals(user, actual.getBorrower());
    }
    @Test
    public void AlreadyBorrowBook_successCase() throws Exception
    {
        User user = User.builder().id(1).name("Khushi").phn_no(87584346L).build();
        Book book = Book.builder().id(1).name("Harry Potter").borrower(user).borrowed(true).build();
        when(bookService.borrowBook(book.getId(), user.getId())).thenReturn(book);

        mockMvc.perform(post("/api/books/{bookId}/borrow/{userId}",1,1))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully."));

        assertTrue(book.isBorrowed());
        assertEquals(user, book.getBorrower());
    }
    @Test
    public void returnBook_SuccessCase() throws Exception {
        User user = User.builder().id(1).name("Khushi").phn_no(87584346L).build();
        Book book = Book.builder().id(1).name("Harry Potter").borrower(user).borrowed(true).build();
        Book returnedBook = Book.builder().id(1).name("Harry Potter").borrower(null).borrowed(false).build();

        when(bookService.returnBook(book.getId())).thenReturn(returnedBook);

        mockMvc.perform(post("/api/books/{bookId}/return", 1));

        assertFalse(returnedBook.isBorrowed());
    }

}
