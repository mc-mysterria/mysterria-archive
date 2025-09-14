package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.database.service.PathwayService;
import net.mysterria.archive.dto.CreatePathwayRequest;
import net.mysterria.archive.dto.PathwayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pathways")
public class PathwayController {

    private final PathwayService pathwayService;

    @Autowired
    public PathwayController(PathwayService pathwayService) {
        this.pathwayService = pathwayService;
    }

    @PostMapping
    public ResponseEntity<PathwayDto> createPathway(@Valid @RequestBody CreatePathwayRequest request) {
        PathwayDto pathway = pathwayService.createPathway(request);
        return new ResponseEntity<>(pathway, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PathwayDto> getPathwayById(@PathVariable Long id) {
        PathwayDto pathway = pathwayService.findById(id);
        return ResponseEntity.ok(pathway);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PathwayDto> getPathwayByName(@PathVariable String name) {
        PathwayDto pathway = pathwayService.findByName(name);
        return ResponseEntity.ok(pathway);
    }

    @GetMapping
    public ResponseEntity<List<PathwayDto>> getAllPathways(@RequestParam(required = false) String search) {
        List<PathwayDto> pathways;
        if (search != null && !search.trim().isEmpty()) {
            pathways = pathwayService.searchPathways(search);
        } else {
            pathways = pathwayService.findAll();
        }
        return ResponseEntity.ok(pathways);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePathway(@PathVariable Long id) {
        pathwayService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}