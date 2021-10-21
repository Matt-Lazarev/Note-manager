package com.lazarev.mvc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min=2, max=20, message = "Note should be between 2 and 20 symbols")
    @Column(name = "title")
    private String title;

    @Size(min=1, max=80, message = "Firstname should be between 1 and 80 symbols")
    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private LocalDateTime creationDateTime;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ApplicationUser user;
}
