package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<ArchiveType, Long> {

    @Override
    List<ArchiveType> findAll();

    List<ArchiveType> findByType(String type);

    @Query(value = "SELECT * FROM archive_item WHERE item_type = ?1", nativeQuery = true)
    List<ArchiveItem> findItemsOfType(ArchiveType type);
}
