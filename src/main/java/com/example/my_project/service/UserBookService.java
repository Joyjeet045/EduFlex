package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class UserBookService {

    private final UserBookDao userBookDao;
    private final BookRequestDao bookRequestDao;
    private final BookDao bookDao;

    public UserBookService(UserBookDao userBookDao, BookRequestDao bookRequestDao, BookDao bookDao) {
        this.userBookDao = userBookDao;
        this.bookRequestDao = bookRequestDao;
        this.bookDao = bookDao;
    }

    public UserBook saveUserBook(UserBook userBook) {
        return userBookDao.save(userBook);
    }

    public int updateAvailableCopies(Long id, int quantityChange) {
        return userBookDao.updateAvailableCopies(id,quantityChange);
    }

    public int updateTotalCopies(Long id, int quantityChange) {
        return userBookDao.updateTotalCopies(id,quantityChange);
    }

    public List<UserBook> findAllByUserId(Long userId) {
        return userBookDao.findAllByUserId(userId);
    }

    public UserBook findByUserIdAndBookId(Long userId, Long bookId) {
        return userBookDao.findByUserIdAndBookId(userId,bookId);
    }
    public UserBook findById(Long Id) {
        return userBookDao.findById(Id);
    }

    public boolean placeBookRequest(Long userBookId, int requestedCopies) {
        if (userBookDao.hasSufficientCopies(userBookId, requestedCopies)) {
            BookRequest bookRequest = new BookRequest();
            bookRequest.setUserBookId(userBookId);
            bookRequest.setRequestedCopies(requestedCopies);
            bookRequest.setRequestDate(LocalDateTime.now());
            bookRequestDao.save(bookRequest);
            return true; 
        }
        return false; 
    }
    
    
    public boolean issueBook(Long id) {
        BookRequest request = bookRequestDao.findById(id);
        Long Id=request.getUserBookId();
        UserBook book = userBookDao.findById(Id);
        if (book != null && book.getAvailableCopies() > 0) {
            userBookDao.updateAvailableCopies(Id, -1); 
            return true;
        }
        return false;
    }

    public void returnBook(Long id) {
        userBookDao.updateAvailableCopies(id, 1); 
    }

    public List<Book> findAllBooks() {
        List<Long> bookIds = userBookDao.findAll().stream()
                .map(UserBook::getBookId)
                .distinct()
                .collect(Collectors.toList());
        return bookIds.stream()
                .map(bookDao::findBookById)
                .collect(Collectors.toList());
    }

    public List<UserBook> findMappingByBookId(Long bookId) {
        return userBookDao.findAll().stream()
            .filter(userBook -> userBook.getBookId().equals(bookId) && !userBook.getUserId().equals(24L))
            .collect(Collectors.toList());
    }

}
