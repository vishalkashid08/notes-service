package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotesService {

    @Autowired
    private NotesRepository repository;

    // ✅ Save note (Create / Update)
    public Notes saveNotes(Notes notes) {
        return repository.save(notes);
    }

    // ✅ Get all notes
    public List<Notes> getAllNotes() {
        return repository.findAll();
    }

    // ✅ Get notes by userId (DB optimized)
    public List<Notes> getNotesByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    // ✅ Get notes by subject (case-insensitive recommended)
    public List<Notes> getNotesBySubject(String subject) {
        return repository.findBySubjectIgnoreCase(subject);
    }

    // ✅ Get single note by ID
    public Notes getNoteById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
    }

    // ✅ Update note (clean method)
    public Notes updateNote(Long id, Notes updatedNote) {
        Notes existing = getNoteById(id);

        existing.setTitle(updatedNote.getTitle());
        existing.setDescription(updatedNote.getDescription());
        existing.setSubject(updatedNote.getSubject());

        // Optional: update file URL only if provided
        if (updatedNote.getFileUrl() != null) {
            existing.setFileUrl(updatedNote.getFileUrl());
        }

        return repository.save(existing);
    }

    // ✅ Delete note with validation
    public void deleteNote(Long id) {
        Notes note = getNoteById(id);
        repository.delete(note);
    }
}