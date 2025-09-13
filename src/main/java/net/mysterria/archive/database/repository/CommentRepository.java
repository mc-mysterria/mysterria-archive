package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveComment;
import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<ArchiveComment, Long> {

    List<ArchiveComment> findByArchiveItemOrderByCreatedAtAsc(ArchiveItem archiveItem);

    List<ArchiveComment> findByArchiveResearcher(ArchiveResearcher archiveResearcher);

    List<ArchiveComment> findByArchiveItemAndArchiveResearcher(ArchiveItem archiveItem, ArchiveResearcher archiveResearcher);
}