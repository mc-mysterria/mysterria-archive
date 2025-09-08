package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveResearcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResearcherRepository extends JpaRepository<ArchiveResearcher, Long> {

    Optional<ArchiveResearcher> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

}