package com.lazarev.mvc.service;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.entity.Note;
import com.lazarev.mvc.repository.NoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public List<Note> getAllNotesByUser(ApplicationUser user) {
        return noteRepository.findAllByUser(user);
    }

    @Override
    public void updateNote(Long id, Note note) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if(optionalNote.isPresent()){
            Note retrievedNote = optionalNote.get();
            retrievedNote.setDescription(note.getDescription());
            retrievedNote.setTitle(note.getTitle());
            noteRepository.save(retrievedNote);
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find note with id '%s'", id));
            log.error("Cannot find note by id", ex);
            throw ex;
        }
    }

    @Override
    public void deleteNote(Long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if(noteOptional.isPresent()){
            noteRepository.delete(noteOptional.get());
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find note with id '%s'", id));
            log.error("Cannot delete note", ex);
            throw ex;
        }

    }

    @Override
    public Note getNoteById(Long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if(noteOptional.isPresent()){
            return noteOptional.get();
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find note with id '%s'", id));
            log.error("Cannot find note by id", ex);
            throw ex;
        }
    }
}
