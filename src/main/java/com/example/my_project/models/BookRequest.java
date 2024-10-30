package com.example.my_project.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookRequest {
    private Long id;               
    private Long userBookId;            
    private int requestedCopies;    
    private RequestStatus status;   
    private LocalDateTime requestDate; 
    private LocalDateTime responseDate; 

    public BookRequest() {
        this.status = RequestStatus.PENDING;
    }
}
