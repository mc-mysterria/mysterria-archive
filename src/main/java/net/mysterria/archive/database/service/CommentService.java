package net.mysterria.archive.database.service;

import net.mysterria.archive.database.entity.ArchiveComment;
import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.database.repository.CommentRepository;
import net.mysterria.archive.database.repository.ItemRepository;
import net.mysterria.archive.database.repository.ResearcherRepository;
import net.mysterria.archive.dto.CommentDto;
import net.mysterria.archive.dto.CreateCommentRequest;
import net.mysterria.archive.dto.ResearcherDto;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ResearcherRepository researcherRepository;
    private final ResearcherService researcherService;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          ItemRepository itemRepository,
                          ResearcherRepository researcherRepository,
                          ResearcherService researcherService) {
        this.commentRepository = commentRepository;
        this.itemRepository = itemRepository;
        this.researcherRepository = researcherRepository;
        this.researcherService = researcherService;
    }

    public CommentDto createComment(CreateCommentRequest request, UUID backendUserId) {
        validateCommentRequest(request);

        ArchiveItem archiveItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + request.getItemId()));

        ArchiveResearcher archiveResearcher = researcherService.findOrCreateByBackendUserId(
                backendUserId,
                "User_" + backendUserId.toString().substring(0, 8)
        );

        ArchiveComment archiveComment = new ArchiveComment();
        archiveComment.setContent(request.getContent());
        archiveComment.setArchiveItem(archiveItem);
        archiveComment.setArchiveResearcher(archiveResearcher);

        ArchiveComment savedArchiveComment = commentRepository.save(archiveComment);
        return mapToDto(savedArchiveComment);
    }

    public CommentDto findById(Long id) {
        ArchiveComment archiveComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        return mapToDto(archiveComment);
    }

    public List<CommentDto> findByItemId(Long itemId) {
        ArchiveItem archiveItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        return commentRepository.findByArchiveItemOrderByCreatedAtAsc(archiveItem).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> findByResearcherId(Long researcherId) {
        ArchiveResearcher archiveResearcher = researcherRepository.findById(researcherId)
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + researcherId));

        return commentRepository.findByArchiveResearcher(archiveResearcher).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    private void validateCommentRequest(CreateCommentRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new ValidationException("Comment content cannot be null or empty");
        }
        if (request.getContent().length() > 2000) {
            throw new ValidationException("Comment content cannot exceed 2000 characters");
        }
        if (request.getItemId() == null) {
            throw new ValidationException("Item ID cannot be null");
        }
    }

    private CommentDto mapToDto(ArchiveComment archiveComment) {
        CommentDto dto = new CommentDto();
        dto.setId(archiveComment.getId());
        dto.setContent(archiveComment.getContent());
        dto.setCreatedAt(archiveComment.getCreatedAt());

        ResearcherDto researcherDto = new ResearcherDto();
        researcherDto.setId(archiveComment.getArchiveResearcher().getId());
        researcherDto.setNickname(archiveComment.getArchiveResearcher().getNickname());
        researcherDto.setCreatedAt(archiveComment.getArchiveResearcher().getCreatedAt());
        dto.setResearcher(researcherDto);

        return dto;
    }
}