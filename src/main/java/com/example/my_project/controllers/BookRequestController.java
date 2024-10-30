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
@RequestMapping("/requests")
public class BookRequestController {

  @Autowired
  private BookRequestService bookRequestService;
  private UserBookService userBookService;
  private UserService userService;
  
  @Autowired
  public BookRequestController(UserBookService userBookService,BookRequestService bookRequestService,UserService userService){
    this.userBookService = userBookService;
    this.bookRequestService = bookRequestService;
    this.userService = userService;
  }

  @PostMapping("/approve/{id}")
  public String approveRequest(@PathVariable Long id,Principal principal) {
    User user = userService.findUser(principal.getName()) ;
    BookRequest request=bookRequestService.findById(id);
    UserBook userBook=userBookService.findById(request.getUserBookId());

    bookRequestService.updateStatus(id,RequestStatus.ACCEPTED); 
    Long adminId=24L;
    UserBook newUserBook=userBookService.findByUserIdAndBookId(adminId,userBook.getBookId());
    if(newUserBook!=null){
      userBookService.updateAvailableCopies(newUserBook.getId(), request.getRequestedCopies());
      userBookService.updateTotalCopies(newUserBook.getId(), request.getRequestedCopies());
    }
    else{
        newUserBook = new UserBook();
        newUserBook.setUserId(adminId);
        newUserBook.setBookId(userBook.getBookId());
        newUserBook.setTotalCopies(request.getRequestedCopies());
        newUserBook.setAvailableCopies(request.getRequestedCopies());
        userBookService.saveUserBook(newUserBook);
    }
    userBookService.updateAvailableCopies(userBook.getId(),-request.getRequestedCopies());
    userBookService.updateTotalCopies(userBook.getId(),-request.getRequestedCopies());
    return "redirect:/books"; 
  }

  @PostMapping("/deny/{id}")
  public String denyRequest(@PathVariable Long id) {
    bookRequestService.updateStatus(id,RequestStatus.REJECTED); 
    return "redirect:/books"; 
  }

}