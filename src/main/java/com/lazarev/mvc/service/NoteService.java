package com.lazarev.mvc.service;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.entity.Note;

import java.util.List;

public interface NoteService {
    List<Note> getAllNotesByUser(ApplicationUser user);
    void updateNote(Long id, Note note);
    void deleteNote(Long id);
    Note getNoteById(Long id);
}
