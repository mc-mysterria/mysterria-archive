package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<ArchiveType, Long> {

}
