package com.example.notetakingapp.service.exception;

public class NoteNotFoundException extends Exception {
    public NoteNotFoundException(String message) {
        super(message);
    }
}
