package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.dto.CommentDto;
import net.mysterria.archive.dto.CreateCommentRequest;
import net.mysterria.archive.database.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archive/comments")
public class CommentController {
    
    private final CommentService commentService;
    
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentRequest request) {
        CommentDto comment = commentService.createComment(request);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto comment = commentService.findById(id);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<CommentDto>> getCommentsByItem(@PathVariable Long itemId) {
        List<CommentDto> comments = commentService.findByItemId(itemId);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/researcher/{researcherId}")
    public ResponseEntity<List<CommentDto>> getCommentsByResearcher(@PathVariable Long researcherId) {
        List<CommentDto> comments = commentService.findByResearcherId(researcherId);
        return ResponseEntity.ok(comments);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}