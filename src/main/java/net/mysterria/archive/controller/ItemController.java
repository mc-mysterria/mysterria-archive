package net.mysterria.archive.controller;

import jakarta.validation.Valid;
import net.mysterria.archive.dto.CreateItemRequest;
import net.mysterria.archive.dto.ItemDto;
import net.mysterria.archive.dto.UpdateItemRequest;
import net.mysterria.archive.database.service.ItemService;
import net.mysterria.archive.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archive/items")
public class ItemController {
    
    private final ItemService itemService;
    
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:WRITE')")
    public ResponseEntity<ItemDto> createItem(
            @Valid @RequestBody CreateItemRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ItemDto item = itemService.createItem(request, userPrincipal.getId());
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
    
    @GetMapping("/pathway/{pathwayId}")
    public ResponseEntity<List<ItemDto>> getItemsByPathway(@PathVariable Long pathwayId) {
        List<ItemDto> items = itemService.findByPathwayId(pathwayId);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<ItemDto>> getItemsByType(@PathVariable Long typeId) {
        List<ItemDto> items = itemService.findByTypeId(typeId);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/sequence/{sequenceNumber}")
    public ResponseEntity<List<ItemDto>> getItemsBySequence(@PathVariable Integer sequenceNumber) {
        List<ItemDto> items = itemService.findBySequenceNumber(sequenceNumber);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<List<ItemDto>> getItemsByRarity(@PathVariable String rarity) {
        List<ItemDto> items = itemService.findByRarity(rarity);
        return ResponseEntity.ok(items);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:WRITE')")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateItemRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ItemDto item = itemService.updateItem(id, request, userPrincipal.getId());
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_ARCHIVE:MODERATE')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}