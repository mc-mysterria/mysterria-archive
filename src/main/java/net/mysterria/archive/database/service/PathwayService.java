package net.mysterria.archive.database.service;

import net.mysterria.archive.database.entity.ArchivePathway;
import net.mysterria.archive.database.repository.PathwayRepository;
import net.mysterria.archive.dto.CreatePathwayRequest;
import net.mysterria.archive.dto.PathwayDto;
import net.mysterria.archive.exception.model.DuplicateResourceException;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathwayService {

    private final PathwayRepository pathwayRepository;

    @Autowired
    public PathwayService(PathwayRepository pathwayRepository) {
        this.pathwayRepository = pathwayRepository;
    }

    public PathwayDto createPathway(CreatePathwayRequest request) {
        validatePathwayRequest(request);

        if (pathwayRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Pathway with name '" + request.getName() + "' already exists");
        }

        ArchivePathway pathway = new ArchivePathway();
        pathway.setName(request.getName());
        pathway.setDescription(request.getDescription());
        pathway.setSequenceCount(request.getSequenceCount());

        ArchivePathway savedPathway = pathwayRepository.save(pathway);
        return mapToDto(savedPathway);
    }

    public PathwayDto findById(Long id) {
        ArchivePathway pathway = pathwayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pathway not found with id: " + id));
        return mapToDto(pathway);
    }

    public PathwayDto findByName(String name) {
        ArchivePathway pathway = pathwayRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Pathway not found with name: " + name));
        return mapToDto(pathway);
    }

    public List<PathwayDto> findAll() {
        return pathwayRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PathwayDto> searchPathways(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return pathwayRepository.findByNameContainingIgnoreCase(searchTerm).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (!pathwayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pathway not found with id: " + id);
        }
        pathwayRepository.deleteById(id);
    }

    private void validatePathwayRequest(CreatePathwayRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("Pathway name cannot be null or empty");
        }
        if (request.getSequenceCount() != null && (request.getSequenceCount() < 1 || request.getSequenceCount() > 9)) {
            throw new ValidationException("Sequence count must be between 1 and 9");
        }
    }

    private PathwayDto mapToDto(ArchivePathway pathway) {
        PathwayDto dto = new PathwayDto();
        dto.setId(pathway.getId());
        dto.setName(pathway.getName());
        dto.setDescription(pathway.getDescription());
        dto.setSequenceCount(pathway.getSequenceCount());
        dto.setCreatedAt(pathway.getCreatedAt());
        return dto;
    }
}