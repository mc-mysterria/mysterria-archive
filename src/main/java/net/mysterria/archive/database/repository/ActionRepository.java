package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveAction;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<ArchiveAction, Long> {

    List<ArchiveAction> findByArchiveResearcher(ArchiveResearcher researcher);

    List<ArchiveAction> findByActionType(ActionType actionType);

    List<ArchiveAction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    ArchiveAction saveWithJdbc(ArchiveAction action);

    Optional<ArchiveAction> findByIdWithJdbc(Long id);

    List<ArchiveAction> findAllWithJdbc();

    void deleteByIdWithJdbc(Long id);

    List<ArchiveAction> findByResearcherIdWithJdbc(Long researcherId);

    List<ArchiveAction> findByActionTypeWithJdbc(ActionType actionType);

    List<ArchiveAction> findByDateRangeWithJdbc(LocalDateTime startDate, LocalDateTime endDate);

    List<ArchiveAction> findRecentActionsWithJdbc(int limit);

    long countByResearcherIdWithJdbc(Long researcherId);

    List<ArchiveAction> findByResearcherIdAndActionTypeWithJdbc(Long researcherId, ActionType actionType);

    Map<Long, Long> getCommentActivityLeaderboard(int limit);

    Map<Long, Long> getCommentActivityLeaderboardByDateRange(LocalDateTime startDate, LocalDateTime endDate, int limit);

    Map<Long, Long> getOverallActivityLeaderboard(int limit);

    Map<Long, Long> getOverallActivityLeaderboardByDateRange(LocalDateTime startDate, LocalDateTime endDate, int limit);

    Map<Long, Long> getActivityLeaderboardByActionType(ActionType actionType, int limit);

    Map<Long, Long> getActivityLeaderboardByActionTypeAndDateRange(ActionType actionType, LocalDateTime startDate, LocalDateTime endDate, int limit);

    Map<Long, Map<ActionType, Long>> getDetailedActivityLeaderboard(int limit);

    long countCommentActivityByResearcher(Long researcherId);

    long countCommentActivityByResearcherAndDateRange(Long researcherId, LocalDateTime startDate, LocalDateTime endDate);

    long countTotalActions();

    long countTotalActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Map<ActionType, Long> getActionTypeDistribution();

    Map<ActionType, Long> getActionTypeDistributionByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
