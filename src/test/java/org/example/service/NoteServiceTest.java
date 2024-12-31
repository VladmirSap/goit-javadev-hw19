package org.example.service;

import org.example.model.Note;
import org.example.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;
    private Note note;

    @BeforeEach
    void setUp() {
        note = new Note("Test title", "Test content");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAll() {
        when(noteRepository.findAll()).thenReturn(List.of(note));
        List<Note> notes = noteService.listAll();
        assertEquals(1, notes.size());
        assertEquals("Test title", notes.getFirst().getTitle());
    }

    @Test
    void testAdd() {
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        Note savedNote = noteService.add(note);
        assertNotNull(savedNote);
        assertEquals("Test title", savedNote.getTitle());

    }

    @Test
    void deleteById() {
        noteService.deleteById(1L);
        verify(noteRepository, times(1)).deleteById(1L);


    }

    @Test
    void testUpdate_existingNote() {
        when(noteRepository.existsById(note.getId())).thenReturn(true);
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        note.setContent("Updated Content");
        noteService.update(note);
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testUpdate_noteNotFound() {
        when(noteRepository.existsById(note.getId())).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            noteService.update(note);
        });
        assertEquals("Note not found", exception.getMessage());
    }

    @Test
    void testGetById_existingNote() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        Note retrievedNote = noteService.getById(1L);
        assertNotNull(retrievedNote);
        assertEquals("Test title", retrievedNote.getTitle());
    }

    @Test
    void testGetById_noteNotFound() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            noteService.getById(1L);
        });
        assertEquals("Note not found", exception.getMessage());
    }


}
