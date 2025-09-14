package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.database.entity.ArchiveAction;
import net.mysterria.archive.database.service.ActionService;
import net.mysterria.archive.dto.ActionDto;
import net.mysterria.archive.dto.CreateActionRequest;
import net.mysterria.archive.dto.ResearcherDto;
import net.mysterria.archive.dto.UpdateActionRequest;
import net.mysterria.archive.enums.ActionType;
import net.mysterria.archive.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/actions")
public class ActionController {

    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:ADMIN')")
    public ResponseEntity<ActionDto> createAction(
            @Valid @RequestBody CreateActionRequest request) {
        ArchiveAction action = actionService.recordAction(request.getResearcherId(), request.getActionType());
        ActionDto actionDto = mapToDto(action);
        return new ResponseEntity<>(actionDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:READ')")
    public ResponseEntity<ActionDto> getActionById(@PathVariable Long id) {
        ArchiveAction action = actionService.getActionById(id)
                .orElseThrow(() -> new RuntimeException("Action not found with id: " + id));
        ActionDto actionDto = mapToDto(action);
        return ResponseEntity.ok(actionDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:READ')")
    public ResponseEntity<List<ActionDto>> getAllActions() {
        List<ArchiveAction> actions = actionService.getAllActions();
        List<ActionDto> actionDtos = actions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(actionDtos);
    }

    @GetMapping("/researcher/{researcherId}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:READ')")
    public ResponseEntity<List<ActionDto>> getActionsByResearcher(@PathVariable Long researcherId) {
        List<ArchiveAction> actions = actionService.getActionsByResearcher(researcherId);
        List<ActionDto> actionDtos = actions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(actionDtos);
    }

    @GetMapping("/type/{actionType}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:READ')")
    public ResponseEntity<List<ActionDto>> getActionsByType(@PathVariable ActionType actionType) {
        List<ArchiveAction> actions = actionService.getActionsByType(actionType);
        List<ActionDto> actionDtos = actions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(actionDtos);
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:READ')")
    public ResponseEntity<List<ActionDto>> getActionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ArchiveAction> actions = actionService.getActionsByDateRange(startDate, endDate);
        List<ActionDto> actionDtos = actions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(actionDtos);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:READ')")
    public ResponseEntity<List<ActionDto>> getRecentActions(@RequestParam(defaultValue = "10") int limit) {
        List<ArchiveAction> actions = actionService.getRecentActions(limit);
        List<ActionDto> actionDtos = actions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(actionDtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:ADMIN')")
    public ResponseEntity<ActionDto> updateAction(
            @PathVariable Long id,
            @Valid @RequestBody UpdateActionRequest request) {
        ArchiveAction action = actionService.updateAction(id, request.getActionType());
        ActionDto actionDto = mapToDto(action);
        return ResponseEntity.ok(actionDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:MODERATE')")
    public ResponseEntity<Void> deleteAction(@PathVariable Long id) {
        actionService.deleteAction(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/researcher/{researcherId}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:ADMIN')")
    public ResponseEntity<Void> deleteActionsByResearcher(@PathVariable Long researcherId) {
        actionService.deleteActionsByResearcher(researcherId);
        return ResponseEntity.noContent().build();
    }

    private ActionDto mapToDto(ArchiveAction action) {
        ActionDto dto = new ActionDto();
        dto.setId(action.getId());
        dto.setActionType(action.getActionType());
        dto.setCreatedAt(action.getCreatedAt());

        if (action.getArchiveResearcher() != null) {
            ResearcherDto researcherDto = new ResearcherDto();
            researcherDto.setId(action.getArchiveResearcher().getId());
            researcherDto.setNickname(action.getArchiveResearcher().getNickname());
            dto.setResearcher(researcherDto);
        }

        return dto;
    }
}