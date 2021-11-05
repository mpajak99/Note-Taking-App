package com.example.notetakingapp.service;

import com.example.notetakingapp.model.HttpResponse;
import com.example.notetakingapp.model.Note;
import com.example.notetakingapp.model.enumeration.Level;
import com.example.notetakingapp.repository.NoteRepository;
import com.example.notetakingapp.service.exception.NoteNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.notetakingapp.util.DateUtil.dateTimeFormatter;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public HttpResponse<Note> getNotes() {
        log.info("Fetching all the notes from database");
        return HttpResponse.<Note>builder()
                .notes(noteRepository.findAll())
                .message(noteRepository.count() > 0 ? noteRepository.count() + " notes retrieved" : "No notes to display")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> filterNotes(Level level) {
        List<Note> notes = noteRepository.findByLevel(level);
        log.info("Fetching all the notes by level {}", level);
        return HttpResponse.<Note>builder()
                .notes(notes)
                .message(notes.size() + " notes are of a " + level + " importance")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> saveNote(Note note) {
        log.info("Saving new note to the database");
        note.setCreatedAt(LocalDateTime.now());
        return HttpResponse.<Note>builder()
                .notes(singletonList(noteRepository.save(note)))
                .message("Note created successfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> updateNote(Note note) throws NoteNotFoundException {
        log.info("Updating note to the database");
        Optional<Note> optionalNote = ofNullable(noteRepository.findById(note.getId()))
                .orElseThrow(() -> new NoteNotFoundException("The note was not found on the server"));
        Note updateNote = optionalNote.get();
        updateNote.setTitle(note.getTitle());
        updateNote.setDescription(note.getDescription());
        updateNote.setLevel(note.getLevel());
        noteRepository.save(updateNote);
        return HttpResponse.<Note>builder()
                .notes(singletonList(updateNote))
                .message("Note updated successfully")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> deleteNote(Long id) throws NoteNotFoundException {
        log.info("Deleting note from the database by id {}", id);
        Optional<Note> optionalNote = ofNullable(noteRepository.findById(id))
                .orElseThrow(() -> new NoteNotFoundException("The note was not found on the server"));
        optionalNote.ifPresent(noteRepository::delete);
        return HttpResponse.<Note>builder()
                .notes(singletonList(optionalNote.get()))
                .message("Note deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }
}

