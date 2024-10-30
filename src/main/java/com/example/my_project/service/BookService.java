package com.example.my_project.service;

import com.example.my_project.dao.BookDao;
import com.example.my_project.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookDao bookDao;

    @Autowired
    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public Book findBookById(Long id) {
        return bookDao.findBookById(id);
    }

    public Book saveBook(Book book) {
      return bookDao.saveBook(book);
    }

    public int deleteBook(Long id) {
      return bookDao.deleteBook(id);
    }
}
