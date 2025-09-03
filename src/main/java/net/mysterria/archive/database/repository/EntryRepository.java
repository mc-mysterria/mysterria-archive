package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<ArchiveEntry, Long> {

    List<ArchiveEntry> findByName(String name);

    List<ArchiveEntry> findByNameAndDescription(String name, String description);

    @Query(value = "SELECT * FROM archive_entry WHERE description LIKE '%bebra%'", nativeQuery = true)
    List<ArchiveEntry> findByAura();

}
