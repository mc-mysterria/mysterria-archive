package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.database.service.TypeService;
import net.mysterria.archive.dto.CreateTypeRequest;
import net.mysterria.archive.dto.TypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archive/types")
public class TypeController {

    private final TypeService typeService;

    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @PostMapping
    public ResponseEntity<TypeDto> createType(@Valid @RequestBody CreateTypeRequest request) {
        TypeDto type = typeService.createType(request);
        return new ResponseEntity<>(type, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDto> getTypeById(@PathVariable Long id) {
        TypeDto type = typeService.findById(id);
        return ResponseEntity.ok(type);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TypeDto> getTypeByName(@PathVariable String name) {
        TypeDto type = typeService.findByName(name);
        return ResponseEntity.ok(type);
    }

    @GetMapping
    public ResponseEntity<List<TypeDto>> getAllTypes(@RequestParam(required = false) String search) {
        List<TypeDto> types;
        if (search != null && !search.trim().isEmpty()) {
            types = typeService.searchTypes(search);
        } else {
            types = typeService.findAll();
        }
        return ResponseEntity.ok(types);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        typeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}