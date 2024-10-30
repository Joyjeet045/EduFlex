package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;


@Controller
@RequestMapping("/sellers")

public class BrowseSellers {
    @Autowired
    private BookService bookService; 
    @GetMapping("/browse")
    public String browseSellers(Model model, Principal principal) {
      List<Book> books = bookService.getAllBooks();      
      model.addAttribute("books", books);
      return "browse-sellers"; 
    }
}