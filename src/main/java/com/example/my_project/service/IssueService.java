package com.example.my_project.service;

import com.example.my_project.dao.IssueDao;
import com.example.my_project.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueDao issueDao;

    @Autowired
    public IssueService(IssueDao issueDao) {
        this.issueDao = issueDao;
    }

    public void createIssue(Issue issue) {
        issueDao.save(issue);
    }

    public List<Issue> getIssuedBooksByUser(Long userId) {
        return issueDao.findBooksIssuedToUser(userId);
    }
    public void updateIssue(Issue issue) {
        issueDao.updateIssue(issue);
    }

    public Issue getLastIssuedBookForUser(Long userId, Long bookId) {
        return issueDao.findLastIssuedBookForUser(userId, bookId);
    }

}
