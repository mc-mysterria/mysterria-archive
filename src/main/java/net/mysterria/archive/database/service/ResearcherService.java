package net.mysterria.archive.database.service;

import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.database.repository.ResearcherRepository;
import net.mysterria.archive.dto.CreateResearcherRequest;
import net.mysterria.archive.dto.ResearcherDto;
import net.mysterria.archive.exception.model.DuplicateResourceException;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResearcherService {
    
    private final ResearcherRepository researcherRepository;
    
    @Autowired
    public ResearcherService(ResearcherRepository researcherRepository) {
        this.researcherRepository = researcherRepository;
    }
    
    public ResearcherDto createResearcher(CreateResearcherRequest request) {
        validateNickname(request.getNickname());
        
        if (researcherRepository.existsByNickname(request.getNickname())) {
            throw new DuplicateResourceException("Researcher with nickname '" + request.getNickname() + "' already exists");
        }
        
        ArchiveResearcher archiveResearcher = new ArchiveResearcher();
        archiveResearcher.setNickname(request.getNickname());
        
        ArchiveResearcher savedArchiveResearcher = researcherRepository.save(archiveResearcher);
        return mapToDto(savedArchiveResearcher);
    }
    
    public ResearcherDto findById(Long id) {
        ArchiveResearcher archiveResearcher = researcherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + id));
        return mapToDto(archiveResearcher);
    }
    
    public ResearcherDto findByNickname(String nickname) {
        ArchiveResearcher archiveResearcher = researcherRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with nickname: " + nickname));
        return mapToDto(archiveResearcher);
    }
    
    public List<ResearcherDto> findAll() {
        return researcherRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteById(Long id) {
        if (!researcherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Researcher not found with id: " + id);
        }
        researcherRepository.deleteById(id);
    }
    
    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new ValidationException("Nickname cannot be null or empty");
        }
        if (nickname.length() < 2 || nickname.length() > 50) {
            throw new ValidationException("Nickname must be between 2 and 50 characters");
        }
    }
    
    private ResearcherDto mapToDto(ArchiveResearcher archiveResearcher) {
        ResearcherDto dto = new ResearcherDto();
        dto.setId(archiveResearcher.getId());
        dto.setNickname(archiveResearcher.getNickname());
        dto.setCreatedAt(archiveResearcher.getCreatedAt());
        return dto;
    }
}