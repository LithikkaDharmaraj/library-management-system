package com.library.service;

import com.library.config.PostgresTestBase;
import com.library.entity.Book;
import com.library.entity.User;
import com.library.entity.Issued;
import com.library.entity.Fine;
import com.library.repository.BookRepository;
import com.library.repository.FineRepository;
import com.library.repository.IssuedRepository;
import com.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LibraryServiceIntegrationTest extends PostgresTestBase {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssuedService issuedService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssuedRepository issuedRepository;

    @Autowired
    private FineRepository fineRepository;

    @Test
    void contextLoads() {
        assertNotNull(bookService);
        assertNotNull(userService);
        assertNotNull(issuedService);
    }

    @Test
    void addBookPersistsAndDuplicateIsbnRejected() {
        Book book = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        Book saved = bookService.addBook(book);

        assertNotNull(saved.getId());
        assertEquals("Clean Code", saved.getName());
        assertTrue(bookRepository.countByIsbn("978-0132350884") == 1);

        Book duplicate = new Book("Clean Code 2", "Robert C. Martin", "978-0132350884");
        assertThrows(RuntimeException.class, () -> bookService.addBook(duplicate));
    }

    @Test
    void addUserPersistsAndDuplicateEmpRollRejected() {
        User user = new User("John Doe", "student", "STU001");
        User saved = userService.addUser(user);

        assertNotNull(saved.getId());
        assertEquals("STU001", saved.getEmpRollNo());
        assertTrue(userRepository.countByEmpRollNo("STU001") == 1);

        User duplicate = new User("Jane Doe", "student", "STU001");
        assertThrows(RuntimeException.class, () -> userService.addUser(duplicate));
    }

    @Test
    void issueAndReturnBookComputesFine() {
        Book book = bookService.addBook(new Book("Spring in Action", "Craig Walls", "978-1617294945"));
        User user = userService.addUser(new User("Alice", "faculty", "FAC001"));

        Issued issued = issuedService.issueBook(book.getId(), user.getId(), 5);

        assertNotNull(issued.getId());
        assertEquals("ISSUED", issued.getStatus());

        // Re-issuing the same book should be rejected while it is still issued
        assertThrows(RuntimeException.class,
                () -> issuedService.issueBook(book.getId(), user.getId(), 5));

        // Return the book -> fine should be computed and set on the fine record
        Issued returned = issuedService.returnBook(issued.getId());
        assertEquals("RETURNED", returned.getStatus());

        Fine fine = fineRepository.findByIssuedId(issued.getId()).orElseThrow();
        assertNotNull(fine.getFineAmount());
        assertNotNull(fine.getAllowedDays());
        assertEquals(5, fine.getAllowedDays());
    }
}
