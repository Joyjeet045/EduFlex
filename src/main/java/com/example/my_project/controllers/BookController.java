package com.example.my_project.controller;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final IssueService issueService;

    @Autowired
    public BookController(BookService bookService,NotificationService notificationService,UserService userService,IssueService issueService) {
        this.bookService = bookService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.issueService = issueService;
    }

    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books-list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books-add"; 
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
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
        bookService.returnBook(id);
        
        model.addAttribute("message", "Book returned successfully!");
        return "redirect:/books";
    }

}
