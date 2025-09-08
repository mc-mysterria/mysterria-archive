package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<ArchiveType, Long> {

    Optional<ArchiveType> findByName(String name);
    
    List<ArchiveType> findByNameContainingIgnoreCase(String name);
    
    boolean existsByName(String name);
}
