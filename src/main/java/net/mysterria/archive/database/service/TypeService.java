package net.mysterria.archive.database.service;

import net.mysterria.archive.database.entity.ArchiveType;
import net.mysterria.archive.database.repository.TypeRepository;
import net.mysterria.archive.dto.CreateTypeRequest;
import net.mysterria.archive.dto.TypeDto;
import net.mysterria.archive.exception.model.DuplicateResourceException;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeService {
    
    private final TypeRepository typeRepository;
    
    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }
    
    public TypeDto createType(CreateTypeRequest request) {
        validateTypeRequest(request);
        
        if (typeRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Type with name '" + request.getName() + "' already exists");
        }
        
        ArchiveType type = new ArchiveType();
        type.setName(request.getName());
        type.setDescription(request.getDescription());
        type.setIconUrl(request.getIconUrl());
        
        ArchiveType savedType = typeRepository.save(type);
        return mapToDto(savedType);
    }
    
    public TypeDto findById(Long id) {
        ArchiveType type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type not found with id: " + id));
        return mapToDto(type);
    }
    
    public TypeDto findByName(String name) {
        ArchiveType type = typeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Type not found with name: " + name));
        return mapToDto(type);
    }
    
    public List<TypeDto> findAll() {
        return typeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<TypeDto> searchTypes(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return typeRepository.findByNameContainingIgnoreCase(searchTerm).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteById(Long id) {
        if (!typeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Type not found with id: " + id);
        }
        typeRepository.deleteById(id);
    }
    
    private void validateTypeRequest(CreateTypeRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("Type name cannot be null or empty");
        }
    }
    
    private TypeDto mapToDto(ArchiveType type) {
        TypeDto dto = new TypeDto();
        dto.setId(type.getId());
        dto.setName(type.getName());
        dto.setDescription(type.getDescription());
        dto.setIconUrl(type.getIconUrl());
        dto.setCreatedAt(type.getCreatedAt());
        return dto;
    }
}