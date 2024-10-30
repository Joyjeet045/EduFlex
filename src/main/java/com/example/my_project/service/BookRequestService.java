package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookRequestService {
    private final BookRequestDao bookRequestDao;
    
    @Autowired
    public BookRequestService(BookRequestDao bookRequestDao) {
        this.bookRequestDao = bookRequestDao;
    }

    public List<BookRequest> getAllRequests() {
        return bookRequestDao.findAll();
    }

    public BookRequest findById(Long id) {
        return bookRequestDao.findById(id);
    }

    public List<BookRequest> getRequestsByUserId(Long userId) {
        return bookRequestDao.findByUserId(userId);
    }

    public void updateStatus(Long requestId, RequestStatus status) {
        bookRequestDao.updateStatus(requestId, status);
    }
}
