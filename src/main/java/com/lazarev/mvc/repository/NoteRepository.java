package com.lazarev.mvc.repository;


import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUser(ApplicationUser user);
}
