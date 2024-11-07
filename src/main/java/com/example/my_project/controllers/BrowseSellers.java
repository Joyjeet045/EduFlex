package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.security.Principal;


@Controller
@RequestMapping("/sellers")

public class BrowseSellers {
    
    @Autowired
    private UserBookService userBookService;
    
    @Autowired
    public BrowseSellers(UserBookService userBookService){
        this.userBookService = userBookService;
    }

    @GetMapping("/browse")
    public String browseSellers(Model model) {
        List<Book> books = userBookService.findAllBooks();
        Map<Long, List<UserBook>> bookSellersMap = new HashMap<>();
        for (Book book : books) {
            List<UserBook> userBooks = userBookService.findMappingByBookId(book.getId());
            bookSellersMap.put(book.getId(), userBooks);
        }
        model.addAttribute("books", books);
        model.addAttribute("bookSellersMap", bookSellersMap);
        return "browse-sellers";
    }
}