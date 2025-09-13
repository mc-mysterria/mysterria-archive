package net.mysterria.archive.database.service;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchivePathway;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.database.entity.ArchiveType;
import net.mysterria.archive.database.repository.*;
import net.mysterria.archive.dto.*;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ResearcherRepository researcherRepository;
    private final ResearcherService researcherService;
    private final CommentRepository commentRepository;
    private final PathwayRepository pathwayRepository;
    private final TypeRepository typeRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, ResearcherRepository researcherRepository,
                       ResearcherService researcherService, CommentRepository commentRepository,
                       PathwayRepository pathwayRepository, TypeRepository typeRepository) {
        this.itemRepository = itemRepository;
        this.researcherRepository = researcherRepository;
        this.researcherService = researcherService;
        this.commentRepository = commentRepository;
        this.pathwayRepository = pathwayRepository;
        this.typeRepository = typeRepository;
    }

    public ItemDto createItem(CreateItemRequest request, UUID backendUserId) {
        validateItemRequest(request);

        ArchiveResearcher archiveResearcher = researcherService.findOrCreateByBackendUserId(
                backendUserId,
                "User_" + backendUserId.toString().substring(0, 8) // Default nickname
        );

        ArchivePathway archivePathway = null;
        if (request.getPathwayId() != null) {
            archivePathway = pathwayRepository.findById(request.getPathwayId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pathway not found with id: " + request.getPathwayId()));
        }

        ArchiveType archiveType = null;
        if (request.getTypeId() != null) {
            archiveType = typeRepository.findById(request.getTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Type not found with id: " + request.getTypeId()));
        }

        ArchiveItem archiveItem = new ArchiveItem();
        archiveItem.setName(request.getName());
        archiveItem.setDescription(request.getDescription());
        archiveItem.setPurpose(request.getPurpose());
        archiveItem.setArchiveResearcher(archiveResearcher);
        archiveItem.setArchivePathway(archivePathway);
        archiveItem.setArchiveType(archiveType);
        archiveItem.setSequenceNumber(request.getSequenceNumber());
        archiveItem.setRarity(request.getRarity());

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

        return itemRepository.findByArchiveResearcher(archiveResearcher).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto updateItem(Long id, UpdateItemRequest request, UUID backendUserId) {
        validateUpdateRequest(request);

        ArchiveItem archiveItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        ArchiveResearcher userResearcher = researcherService.findOrCreateByBackendUserId(
                backendUserId,
                "User_" + backendUserId.toString().substring(0, 8)
        );

        if (!archiveItem.getArchiveResearcher().getId().equals(userResearcher.getId())) {
            throw new ValidationException("You can only edit your own items");
        }

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

    public List<ItemDto> findByPathwayId(Long pathwayId) {
        ArchivePathway archivePathway = pathwayRepository.findById(pathwayId)
                .orElseThrow(() -> new ResourceNotFoundException("Pathway not found with id: " + pathwayId));

        return archivePathway.getItems().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> findByTypeId(Long typeId) {
        ArchiveType archiveType = typeRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type not found with id: " + typeId));

        return archiveType.getItems().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> findBySequenceNumber(Integer sequenceNumber) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getSequenceNumber() != null && item.getSequenceNumber().equals(sequenceNumber))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> findByRarity(String rarity) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getRarity() != null && item.getRarity().equalsIgnoreCase(rarity))
                .map(this::mapToDto)
                .collect(Collectors.toList());
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
        dto.setSequenceNumber(archiveItem.getSequenceNumber());
        dto.setRarity(archiveItem.getRarity());

        ResearcherDto researcherDto = new ResearcherDto();
        researcherDto.setId(archiveItem.getArchiveResearcher().getId());
        researcherDto.setNickname(archiveItem.getArchiveResearcher().getNickname());
        researcherDto.setCreatedAt(archiveItem.getArchiveResearcher().getCreatedAt());
        dto.setResearcher(researcherDto);

        if (archiveItem.getArchivePathway() != null) {
            PathwayDto pathwayDto = new PathwayDto();
            pathwayDto.setId(archiveItem.getArchivePathway().getId());
            pathwayDto.setName(archiveItem.getArchivePathway().getName());
            pathwayDto.setDescription(archiveItem.getArchivePathway().getDescription());
            pathwayDto.setSequenceCount(archiveItem.getArchivePathway().getSequenceCount());
            pathwayDto.setCreatedAt(archiveItem.getArchivePathway().getCreatedAt());
            dto.setPathway(pathwayDto);
        }

        if (archiveItem.getArchiveType() != null) {
            TypeDto typeDto = new TypeDto();
            typeDto.setId(archiveItem.getArchiveType().getId());
            typeDto.setName(archiveItem.getArchiveType().getName());
            typeDto.setDescription(archiveItem.getArchiveType().getDescription());
            typeDto.setIconUrl(archiveItem.getArchiveType().getIconUrl());
            typeDto.setCreatedAt(archiveItem.getArchiveType().getCreatedAt());
            dto.setType(typeDto);
        }

        List<CommentDto> comments = commentRepository.findByArchiveItemOrderByCreatedAtAsc(archiveItem).stream()
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