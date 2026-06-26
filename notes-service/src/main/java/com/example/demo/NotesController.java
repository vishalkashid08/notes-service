package com.example.demo;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "*")
public class NotesController {

    @Autowired
    private NotesService service;

    // ✅ Upload Note (FILE + DATA)
    @PostMapping("/upload")
    public Notes uploadNote(
            @RequestParam("file") MultipartFile file,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String subject,
            @RequestParam Long userId
    ) {
        try {
            System.out.println("UPLOAD API CALLED ✅");

            // ✅ FIXED: Absolute path (VERY IMPORTANT)
            String uploadDir = "C:/uploads/";   // create this folder manually once

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // ✅ Clean filename
            String originalFileName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFileName.replaceAll("\\s+", "_");

            String filePath = uploadDir + fileName;

            // ✅ Save file
            file.transferTo(new File(filePath));

            // ✅ Save in DB
            Notes note = new Notes();
            note.setTitle(title);
            note.setDescription(description);
            note.setSubject(subject);
            note.setUserId(userId);

            // IMPORTANT: this URL must match your static config
            note.setFileUrl("http://3.110.167.15:8084/uploads/" + fileName);

            return service.saveNotes(note);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed!");
        }
    }

    // ✅ Add note (without file)
    @PostMapping
    public Notes addNotes(@RequestBody Notes notes) {
        return service.saveNotes(notes);
    }

    // ✅ Get all notes
    @GetMapping
    public List<Notes> getAllNotes() {
        return service.getAllNotes();
    }

    // ✅ Get notes by userId
    @GetMapping("/user/{userId}")
    public List<Notes> getNotesByUser(@PathVariable Long userId) {
        return service.getNotesByUserId(userId);
    }

    // ✅ Get notes by subject
    @GetMapping("/subject/{subject}")
    public List<Notes> getNotesBySubject(@PathVariable String subject) {
        return service.getNotesBySubject(subject);
    }

    // ✅ Get single note
    @GetMapping("/{id}")
    public Notes getNoteById(@PathVariable Long id) {
        return service.getNoteById(id);
    }

    // ✅ Update note
    @PutMapping("/{id}")
    public Notes updateNote(@PathVariable Long id, @RequestBody Notes updatedNote) {
        Notes existing = service.getNoteById(id);

        if (existing != null) {
            existing.setTitle(updatedNote.getTitle());
            existing.setDescription(updatedNote.getDescription());
            existing.setSubject(updatedNote.getSubject());
            existing.setFileUrl(updatedNote.getFileUrl());
            return service.saveNotes(existing);
        } else {
            return null;
        }
    }

    // ✅ Delete note
    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Long id) {
        service.deleteNote(id);
        return "Note deleted successfully!";
    }
}