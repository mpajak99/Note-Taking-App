package com.example.notetakingapp.repository;

import com.example.notetakingapp.model.Note;
import com.example.notetakingapp.model.enumeration.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByLevel(Level level);
    void deleteNoteById(Long id);
}
