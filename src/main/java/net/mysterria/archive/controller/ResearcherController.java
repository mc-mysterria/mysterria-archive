package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.dto.CreateResearcherRequest;
import net.mysterria.archive.dto.ResearcherDto;
import net.mysterria.archive.database.service.ResearcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archive/researchers")
public class ResearcherController {
    
    private final ResearcherService researcherService;
    
    @Autowired
    public ResearcherController(ResearcherService researcherService) {
        this.researcherService = researcherService;
    }
    
    @PostMapping
    public ResponseEntity<ResearcherDto> createResearcher(@Valid @RequestBody CreateResearcherRequest request) {
        ResearcherDto researcher = researcherService.createResearcher(request);
        return new ResponseEntity<>(researcher, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResearcherDto> getResearcherById(@PathVariable Long id) {
        ResearcherDto researcher = researcherService.findById(id);
        return ResponseEntity.ok(researcher);
    }
    
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<ResearcherDto> getResearcherByNickname(@PathVariable String nickname) {
        ResearcherDto researcher = researcherService.findByNickname(nickname);
        return ResponseEntity.ok(researcher);
    }
    
    @GetMapping
    public ResponseEntity<List<ResearcherDto>> getAllResearchers() {
        List<ResearcherDto> researchers = researcherService.findAll();
        return ResponseEntity.ok(researchers);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResearcher(@PathVariable Long id) {
        researcherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}