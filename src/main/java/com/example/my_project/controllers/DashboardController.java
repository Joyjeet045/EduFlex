package com.example.my_project.controllers; 

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import java.sql.*;

@Controller
public class DashboardController {

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final BookService bookService;
    private final IssueService issueService;
    private final BookRequestService bookRequestService;

    @Autowired
    public DashboardController(EnrollmentService enrollmentService, CourseService courseService,UserService userService, NotificationService notificationService,BookService bookService,IssueService issueService,BookRequestService bookRequestService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.userService= userService;
        this.notificationService = notificationService;
        this.bookService=bookService;
        this.issueService=issueService;
        this.bookRequestService = bookRequestService;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Principal principal) {
        User user =userService.findUser(principal.getName());
        Long userId=user.getId();
        String role=user.getRole().name();

        List<BookRequest> allRequests = bookRequestService.getAllRequests(); 
        List<BookRequest> pendingRequests = allRequests.stream()  
        .filter(request -> request.getStatus() == RequestStatus.PENDING)
        .collect(Collectors.toList());

        if ("ADMIN".equalsIgnoreCase(role)) {
            List<Notification> pendingNotifications = notificationService.getPendingNotifications(); 
            model.addAttribute("pendingNotifications", pendingNotifications); 
            model.addAttribute("pendingRequests", pendingRequests);
            return "admin-dashboard"; 
        }    
        if ("TEACHER".equalsIgnoreCase(role)) {
            List<Course> createdCourses = courseService.getAllCoursesByInstructor(userId);
            model.addAttribute("createdCourses", createdCourses);
        }
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(userId);
        List<Course> enrolledCourses = enrollments.stream()
                                    .map(enrollment -> courseService.findCourseById(enrollment.getCourseId()))
                                    .collect(Collectors.toList());
        List<Issue> issuedBooks = issueService.getIssuedBooksByUser(userId);
        List<IssuedBookDTO> issuedBookDetails = issuedBooks.stream()
            .filter(issue -> issue.getReturnDate() == null)  
            .map(issue -> new IssuedBookDTO(
                bookService.findBookById(issue.getBookId()),
                issue
            ))
            .collect(Collectors.toList());
        boolean isStudent=(user.getRole().equals(UserRole.STUDENT));
        boolean isTeacher=(user.getRole().equals(UserRole.TEACHER));

        model.addAttribute("issuedBooks", issuedBookDetails);
        model.addAttribute("enrolledCourses", enrolledCourses);
        model.addAttribute("isStudent", isStudent);
        model.addAttribute("isTeacher", isTeacher);
        return "dashboard";
    }
}


@Data
class IssuedBookDTO {
    private Book book;
    private Timestamp issueDate;
    private Timestamp returnDate;
    
    public IssuedBookDTO(Book book, Issue issue) {
        this.book = book;
        this.issueDate = issue.getIssueDate();
        this.returnDate = issue.getReturnDate();
    }
}
