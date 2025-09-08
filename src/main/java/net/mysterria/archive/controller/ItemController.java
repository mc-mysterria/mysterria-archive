package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.dto.CreateItemRequest;
import net.mysterria.archive.dto.ItemDto;
import net.mysterria.archive.dto.UpdateItemRequest;
import net.mysterria.archive.database.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    
    private final ItemService itemService;
    
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody CreateItemRequest request) {
        ItemDto item = itemService.createItem(request);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        ItemDto item = itemService.findById(id);
        return ResponseEntity.ok(item);
    }
    
    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@RequestParam(required = false) String search) {
        List<ItemDto> items;
        if (search != null && !search.trim().isEmpty()) {
            items = itemService.searchItems(search);
        } else {
            items = itemService.findAll();
        }
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/researcher/{researcherId}")
    public ResponseEntity<List<ItemDto>> getItemsByResearcher(@PathVariable Long researcherId) {
        List<ItemDto> items = itemService.findByResearcherId(researcherId);
        return ResponseEntity.ok(items);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @Valid @RequestBody UpdateItemRequest request) {
        ItemDto item = itemService.updateItem(id, request);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}