package net.mysterria.archive.database.service;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.database.repository.CommentRepository;
import net.mysterria.archive.database.repository.ItemRepository;
import net.mysterria.archive.database.repository.ResearcherRepository;
import net.mysterria.archive.dto.*;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final ResearcherRepository researcherRepository;
    private final CommentRepository commentRepository;
    
    @Autowired
    public ItemService(ItemRepository itemRepository, ResearcherRepository researcherRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.researcherRepository = researcherRepository;
        this.commentRepository = commentRepository;
    }
    
    public ItemDto createItem(CreateItemRequest request) {
        validateItemRequest(request);
        
        ArchiveResearcher archiveResearcher = researcherRepository.findById(request.getResearcherId())
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + request.getResearcherId()));
        
        ArchiveItem archiveItem = new ArchiveItem();
        archiveItem.setName(request.getName());
        archiveItem.setDescription(request.getDescription());
        archiveItem.setPurpose(request.getPurpose());
        archiveItem.setArchiveResearcher(archiveResearcher);
        
        ArchiveItem savedArchiveItem = itemRepository.save(archiveItem);
        return mapToDto(savedArchiveItem);
    }
    
    public ItemDto findById(Long id) {
        ArchiveItem archiveItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return mapToDto(archiveItem);
    }
    
    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<ItemDto> searchItems(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return itemRepository.searchItems(searchTerm).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<ItemDto> findByResearcherId(Long researcherId) {
        ArchiveResearcher archiveResearcher = researcherRepository.findById(researcherId)
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + researcherId));
        
        return itemRepository.findByResearcher(archiveResearcher).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public ItemDto updateItem(Long id, UpdateItemRequest request) {
        validateUpdateRequest(request);
        
        ArchiveItem archiveItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            archiveItem.setName(request.getName());
        }
        if (request.getDescription() != null) {
            archiveItem.setDescription(request.getDescription());
        }
        if (request.getPurpose() != null) {
            archiveItem.setPurpose(request.getPurpose());
        }
        
        ArchiveItem savedArchiveItem = itemRepository.save(archiveItem);
        return mapToDto(savedArchiveItem);
    }
    
    public void deleteById(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }
    
    private void validateItemRequest(CreateItemRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("Item name cannot be null or empty");
        }
        if (request.getName().length() < 2 || request.getName().length() > 100) {
            throw new ValidationException("Item name must be between 2 and 100 characters");
        }
        if (request.getResearcherId() == null) {
            throw new ValidationException("Researcher ID cannot be null");
        }
    }
    
    private void validateUpdateRequest(UpdateItemRequest request) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            if (request.getName().length() < 2 || request.getName().length() > 100) {
                throw new ValidationException("Item name must be between 2 and 100 characters");
            }
        }
    }
    
    private ItemDto mapToDto(ArchiveItem archiveItem) {
        ItemDto dto = new ItemDto();
        dto.setId(archiveItem.getId());
        dto.setName(archiveItem.getName());
        dto.setDescription(archiveItem.getDescription());
        dto.setPurpose(archiveItem.getPurpose());
        dto.setCreatedAt(archiveItem.getCreatedAt());
        dto.setUpdatedAt(archiveItem.getUpdatedAt());
        
        ResearcherDto researcherDto = new ResearcherDto();
        researcherDto.setId(archiveItem.getArchiveResearcher().getId());
        researcherDto.setNickname(archiveItem.getArchiveResearcher().getNickname());
        researcherDto.setCreatedAt(archiveItem.getArchiveResearcher().getCreatedAt());
        dto.setResearcher(researcherDto);
        
        List<CommentDto> comments = commentRepository.findByItemOrderByCreatedAtAsc(archiveItem).stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setContent(comment.getContent());
                    commentDto.setCreatedAt(comment.getCreatedAt());
                    
                    ResearcherDto commentResearcherDto = new ResearcherDto();
                    commentResearcherDto.setId(comment.getArchiveResearcher().getId());
                    commentResearcherDto.setNickname(comment.getArchiveResearcher().getNickname());
                    commentResearcherDto.setCreatedAt(comment.getArchiveResearcher().getCreatedAt());
                    commentDto.setResearcher(commentResearcherDto);
                    
                    return commentDto;
                })
                .collect(Collectors.toList());
        dto.setComments(comments);
        
        return dto;
    }
}