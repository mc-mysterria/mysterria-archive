package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchivePathway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PathwayRepository extends JpaRepository<ArchivePathway, Long> {

    @Override
    List<ArchivePathway> findAll();

    List<ArchivePathway> findByPath(String path);

    List<ArchivePathway> findByLevel(int level);

    @Query(value = "SELECT * FROM archive_item WHERE item_path = ?1", nativeQuery = true)
    List<ArchiveItem> findItemsByPath(ArchivePathway path);
}
