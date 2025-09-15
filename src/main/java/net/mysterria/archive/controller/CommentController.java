package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.annotation.LogAction;
import net.mysterria.archive.database.service.CommentService;
import net.mysterria.archive.dto.CommentDto;
import net.mysterria.archive.dto.CreateCommentRequest;
import net.mysterria.archive.enums.ActionType;
import net.mysterria.archive.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:WRITE')")
    @LogAction(ActionType.CREATE_COMMENT)
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CommentDto comment = commentService.createComment(request, userPrincipal.getId());
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
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:MODERATE')")
    @LogAction(ActionType.DELETE_COMMENT)
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}