package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ArchiveItem, Long> {
    
    List<ArchiveItem> findByNameContainingIgnoreCase(String name);
    
    List<ArchiveItem> findByDescriptionContainingIgnoreCase(String description);
    
    List<ArchiveItem> findByPurposeContainingIgnoreCase(String purpose);

    List<ArchiveItem> findByArchiveResearcher(ArchiveResearcher archiveResearcher);
    
    @Query("SELECT i FROM ArchiveItem i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.purpose) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ArchiveItem> searchItems(@Param("searchTerm") String searchTerm);
}
