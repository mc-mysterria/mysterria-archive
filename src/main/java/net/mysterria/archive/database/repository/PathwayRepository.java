package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchivePathway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PathwayRepository extends JpaRepository<ArchivePathway, Long> {

    Optional<ArchivePathway> findByName(String name);
    
    List<ArchivePathway> findByNameContainingIgnoreCase(String name);
    
    boolean existsByName(String name);
    
    List<ArchivePathway> findBySequenceCountGreaterThanEqual(Integer sequenceCount);
}
