package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import com.example.my_project.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class RecommendedService {
  private final RecommendedDao recommendedDao;
  private final UserService userService;
  private final BookDao bookDao;
  
  @Autowired
  public RecommendedService(RecommendedDao recommendedDao,UserService userService,BookDao bookDao) {
    this.recommendedDao = recommendedDao;
    this.userService = userService;
    this.bookDao = bookDao;
  }

  public List<RecommendedBookDto> getRecommendedBooks(Long courseId) {
    List<RecommendedBook> recommendedBooks = recommendedDao.findByCourseId(courseId);
    
    if (!recommendedBooks.isEmpty()) {
        return recommendedBooks.stream()
                .map(recommendedBook -> {
                    Book book = bookDao.findBookById(recommendedBook.getBookId());
                    if (book != null) {
                        RecommendedBookDto dto = new RecommendedBookDto();
                        dto.setId(recommendedBook.getId());
                        dto.setBookId(book.getId());
                        dto.setTitle(book.getTitle());
                        dto.setAuthor(book.getAuthor());
                        dto.setThumbnail(book.getThumbnail());
                        dto.setNotes(recommendedBook.getNotes());
                        return dto;
                    }
                    return null; 
                })
                .filter(dto -> dto != null) 
                .collect(Collectors.toList());
    }
    return List.of();
  }
  public void addRecommendedBook(RecommendedBook recommendedBook) {
        try {
              if (recommendedBook == null || recommendedBook.getBookId() == null) {
              throw new IllegalArgumentException("Invalid recommended book data.");
              }

            recommendedDao.addRecommendedBook(recommendedBook);
        } catch (Exception e) {
            System.out.println("Error adding recommended book: " + e.getMessage());
            throw new RuntimeException("Failed to add recommended book", e);
        }
    }
}