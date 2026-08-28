package com.library.service;

import com.library.entity.Book;
import com.library.entity.Fine;
import com.library.entity.Issued;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.FineRepository;
import com.library.repository.IssuedRepository;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class IssuedService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final IssuedRepository issuedRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;

    public IssuedService(IssuedRepository issuedRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository,
                         FineRepository fineRepository) {
        this.issuedRepository = issuedRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.fineRepository = fineRepository;
    }

    public List<Issued> getAllIssues() {
        return issuedRepository.findAll();
    }

    public Issued getIssueById(Long id) {
        return issuedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue record not found with id: " + id));
    }

    @Transactional
    public Issued issueBook(Long bookId, Long userId, Integer allowedDays) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        issuedRepository.findByBookIdAndStatus(bookId, "ISSUED")
                .ifPresent(i -> {
                    throw new RuntimeException("Book is already issued and not yet returned.");
                });

        int days = allowedDays == null ? 10 : allowedDays;

        Issued issued = new Issued(
                bookId,
                userId,
                LocalDate.now().format(FORMATTER),
                null,
                "ISSUED",
                0.0
        );
        issued = issuedRepository.save(issued);

        Fine fine = new Fine(issued.getId(), days, 1.0, 0.0);
        fineRepository.save(fine);

        return issued;
    }

    @Transactional
    public Issued returnBook(Long issuedId) {
        Issued issued = getIssueById(issuedId);

        if ("RETURNED".equalsIgnoreCase(issued.getStatus())) {
            throw new RuntimeException("Book has already been returned.");
        }

        issued.setReturnDate(LocalDate.now().format(FORMATTER));
        issued.setStatus("RETURNED");
        issued = issuedRepository.save(issued);

        calculateFine(issued);

        return issued;
    }

    private void calculateFine(Issued issued) {
        fineRepository.findByIssuedId(issued.getId()).ifPresent(fine -> {
            LocalDate issueDate = LocalDate.parse(issued.getIssueDate(), FORMATTER);
            LocalDate returnDate = LocalDate.parse(issued.getReturnDate(), FORMATTER);

            long daysHeld = java.time.temporal.ChronoUnit.DAYS.between(issueDate, returnDate);
            int allowedDays = fine.getAllowedDays() == null ? 10 : fine.getAllowedDays();
            double perDay = fine.getFinePerDay() == null ? 1.0 : fine.getFinePerDay();

            double amount = 0.0;
            if (daysHeld > allowedDays) {
                amount = (daysHeld - allowedDays) * perDay;
            }

            fine.setFineAmount(amount);
            fineRepository.save(fine);

            issued.setFineAmount(amount);
            issuedRepository.save(issued);
        });
    }

    public List<Issued> getIssuesByUser(Long userId) {
        return issuedRepository.findByUserId(userId);
    }
}
