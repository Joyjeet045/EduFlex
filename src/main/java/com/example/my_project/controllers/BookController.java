package com.example.my_project.controller;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.security.Principal;
import lombok.Data;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final IssueService issueService;
    private final UserBookService userBookService;

    @Autowired
    public BookController(BookService bookService,NotificationService notificationService,UserService userService,IssueService issueService,UserBookService userBookService) {
        this.bookService = bookService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.issueService = issueService;
        this.userBookService = userBookService;
    }

    @GetMapping
    public String listBooks(Model model,Principal principal) {
        User user =userService.findUser(principal.getName()) ;
        Long userId=user.getId();
        List<UserBook> userBooks = userBookService.findAllByUserId(userId);
        List<BookWithAvailability> bookWithAvailabilityList = new ArrayList<>();
        boolean isStudent = user.getRole().equals(UserRole.STUDENT);
        boolean isSeller = user.getRole().equals(UserRole.SELLER);
        boolean isAdmin = user.getRole().equals(UserRole.ADMIN);
        for (UserBook userBook : userBooks) {
            Book book = bookService.findBookById(userBook.getBookId());
            if (book != null) {
                BookWithAvailability bookWithAvailability = new BookWithAvailability();
                bookWithAvailability.setUserBookId(userBook.getId());
                bookWithAvailability.setBook(book);
                bookWithAvailability.setAvailableCopies(userBook.getAvailableCopies());
                bookWithAvailabilityList.add(bookWithAvailability);
            }
        }
        
        model.addAttribute("books", bookWithAvailabilityList);
        model.addAttribute("isStudent", isStudent);
        model.addAttribute("isSeller", isSeller);
        model.addAttribute("isAdmin", isAdmin);
        return "books-list";
    }


    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books-add"; 
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, @RequestParam("copies") int copies, Principal principal) {
        String username = principal.getName();
        Long userId=userService.findUser(username).getId();

        Book savedBook=bookService.saveBook(book);
        UserBook userBook = new UserBook();

        userBook.setUserId(userId);
        userBook.setBookId(savedBook.getId());
        userBook.setTotalCopies(copies);
        userBook.setAvailableCopies(copies);
        userBookService.saveUserBook(userBook);
        return "redirect:/books"; 
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/issue/{id}")
    public String issueBook(@PathVariable("id") Long bookId, Model model, Principal principal) {
        
        String username = principal.getName();
        Long userId=userService.findUser(username).getId();

        Notification notification = new Notification();
        notification.setBookId(bookId);
        notification.setUserId(userId);
        notification.setMessage("Request to issue book ID: " + bookId);

        notificationService.createNotification(notification);
        
        model.addAttribute("message", "Your request to issue the book has been sent for approval.");
        return "redirect:/books"; 
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable("id") Long id, Model model,Principal principal) {
        String username = principal.getName();
        Long userId=userService.findUser(username).getId();

        Issue issue=issueService.getLastIssuedBookForUser(userId,id);
        issueService.updateIssue(issue);
        userBookService.returnBook(id);
        
        model.addAttribute("message", "Book returned successfully!");
        return "redirect:/books";
    }

    @PostMapping("/sell/{id}")
    public String sellBook(@PathVariable Long id, @RequestParam int sellCopies, Principal principal) {
        boolean requestPlaced = userBookService.placeBookRequest(id, sellCopies);
        return "redirect:/books"; 
    }
}

@Data
class BookWithAvailability {
    private Long userBookId;
    private Book book;
    private int availableCopies;
}
