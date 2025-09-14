package net.mysterria.archive.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mysterria.archive.database.entity.ArchiveAction;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.database.repository.ActionRepository;
import net.mysterria.archive.database.repository.ActionRepositoryImpl;
import net.mysterria.archive.database.repository.ResearcherRepository;
import net.mysterria.archive.enums.ActionType;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActionService {

    private final ActionRepository actionRepository;
    private final ActionRepositoryImpl actionRepositoryImpl;
    private final ResearcherRepository researcherRepository;

    @Transactional
    public ArchiveAction recordAction(Long researcherId, ActionType actionType) {
        log.debug("Recording action {} for researcher {}", actionType, researcherId);

        ArchiveResearcher researcher = researcherRepository.findById(researcherId)
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + researcherId));

        ArchiveAction action = new ArchiveAction();
        action.setArchiveResearcher(researcher);
        action.setActionType(actionType);
        action.setCreatedAt(LocalDateTime.now());

        ArchiveAction savedAction = actionRepository.save(action);
        log.info("Recorded action {} for researcher {} with id {}", actionType, researcher.getNickname(), savedAction.getId());

        return savedAction;
    }

    @Transactional
    public void recordBulkActions(List<Long> researcherIds, ActionType actionType) {
        log.debug("Recording bulk action {} for {} researchers", actionType, researcherIds.size());

        LocalDateTime now = LocalDateTime.now();

        for (Long researcherId : researcherIds) {
            ArchiveResearcher researcher = researcherRepository.findById(researcherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + researcherId));

            ArchiveAction action = new ArchiveAction();
            action.setArchiveResearcher(researcher);
            action.setActionType(actionType);
            action.setCreatedAt(now);

            actionRepository.save(action);
        }

        log.info("Recorded bulk action {} for {} researchers", actionType, researcherIds.size());
    }

    @Transactional(readOnly = true)
    public List<ArchiveAction> getActionsByResearcher(Long researcherId) {
        log.debug("Fetching actions for researcher {}", researcherId);
        return actionRepositoryImpl.findByResearcherIdWithJdbc(researcherId);
    }

    @Transactional(readOnly = true)
    public List<ArchiveAction> getActionsByType(ActionType actionType) {
        log.debug("Fetching actions of type {}", actionType);
        return actionRepository.findByActionType(actionType);
    }

    @Transactional(readOnly = true)
    public List<ArchiveAction> getActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching actions between {} and {}", startDate, endDate);
        return actionRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<ArchiveAction> getRecentActions(int limit) {
        log.debug("Fetching {} most recent actions", limit);
        return actionRepositoryImpl.findRecentActionsWithJdbc(limit);
    }

    @Transactional(readOnly = true)
    public long getActionCountByResearcher(Long researcherId) {
        log.debug("Counting actions for researcher {}", researcherId);
        return actionRepositoryImpl.countByResearcherIdWithJdbc(researcherId);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> getCommentActivityLeaderboard(int limit) {
        log.debug("Generating comment activity leaderboard with limit {}", limit);
        return actionRepositoryImpl.getCommentActivityLeaderboard(limit);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> getCommentActivityLeaderboard(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        log.debug("Generating comment activity leaderboard between {} and {} with limit {}", startDate, endDate, limit);
        return actionRepositoryImpl.getCommentActivityLeaderboardByDateRange(startDate, endDate, limit);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> getOverallActivityLeaderboard(int limit) {
        log.debug("Generating overall activity leaderboard with limit {}", limit);
        return actionRepositoryImpl.getOverallActivityLeaderboard(limit);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> getOverallActivityLeaderboard(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        log.debug("Generating overall activity leaderboard between {} and {} with limit {}", startDate, endDate, limit);
        return actionRepositoryImpl.getOverallActivityLeaderboardByDateRange(startDate, endDate, limit);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> getActivityLeaderboardByType(ActionType actionType, int limit) {
        log.debug("Generating activity leaderboard for type {} with limit {}", actionType, limit);
        return actionRepositoryImpl.getActivityLeaderboardByActionType(actionType, limit);
    }

    @Transactional(readOnly = true)
    public Map<Long, Map<ActionType, Long>> getDetailedActivityLeaderboard(int limit) {
        log.debug("Generating detailed activity leaderboard with limit {}", limit);
        return actionRepositoryImpl.getDetailedActivityLeaderboard(limit);
    }

    @Transactional(readOnly = true)
    public long getCommentActivityCount(Long researcherId) {
        log.debug("Counting comment activity for researcher {}", researcherId);
        return actionRepositoryImpl.countCommentActivityByResearcher(researcherId);
    }

    @Transactional(readOnly = true)
    public long getCommentActivityCount(Long researcherId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Counting comment activity for researcher {} between {} and {}", researcherId, startDate, endDate);
        return actionRepositoryImpl.countCommentActivityByResearcherAndDateRange(researcherId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long getTotalActionCount() {
        log.debug("Counting total actions");
        return actionRepositoryImpl.countTotalActions();
    }

    @Transactional(readOnly = true)
    public long getTotalActionCount(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Counting total actions between {} and {}", startDate, endDate);
        return actionRepositoryImpl.countTotalActionsByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Map<ActionType, Long> getActionTypeDistribution() {
        log.debug("Getting action type distribution");
        return actionRepositoryImpl.getActionTypeDistribution();
    }

    @Transactional(readOnly = true)
    public Map<ActionType, Long> getActionTypeDistribution(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Getting action type distribution between {} and {}", startDate, endDate);
        return actionRepositoryImpl.getActionTypeDistributionByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<ArchiveAction> getActionById(Long id) {
        log.debug("Fetching action with id {}", id);
        return actionRepository.findById(id);
    }

    @Transactional
    public void deleteAction(Long id) {
        log.debug("Deleting action with id {}", id);

        if (!actionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Action not found with id: " + id);
        }

        actionRepository.deleteById(id);
        log.info("Deleted action with id {}", id);
    }

    @Transactional
    public void deleteActionsByResearcher(Long researcherId) {
        log.debug("Deleting all actions for researcher {}", researcherId);

        ArchiveResearcher researcher = researcherRepository.findById(researcherId)
                .orElseThrow(() -> new ResourceNotFoundException("Researcher not found with id: " + researcherId));

        List<ArchiveAction> actions = actionRepository.findByArchiveResearcher(researcher);
        actionRepository.deleteAll(actions);

        log.info("Deleted {} actions for researcher {}", actions.size(), researcher.getNickname());
    }

    @Transactional(readOnly = true)
    public List<ArchiveAction> getAllActions() {
        log.debug("Fetching all actions");
        return actionRepository.findAll();
    }

    @Transactional
    public ArchiveAction updateAction(Long id, ActionType newActionType) {
        log.debug("Updating action {} with new type {}", id, newActionType);

        ArchiveAction action = actionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found with id: " + id));

        action.setActionType(newActionType);
        ArchiveAction savedAction = actionRepository.save(action);

        log.info("Updated action {} with new type {}", id, newActionType);
        return savedAction;
    }
}