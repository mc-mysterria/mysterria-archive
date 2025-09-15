package net.mysterria.archive.database.repository;

import lombok.RequiredArgsConstructor;
import net.mysterria.archive.database.entity.ArchiveAction;
import net.mysterria.archive.database.entity.ArchiveResearcher;
import net.mysterria.archive.enums.ActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository("actionRepositoryImpl")
@RequiredArgsConstructor
public class ActionRepositoryImpl {

    private static final Logger logger = LoggerFactory.getLogger(ActionRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final JdbcClient jdbcClient;
    private final ResearcherRepository researcherRepository;

    private static final String INSERT_SQL = """
            INSERT INTO user_actions (researcher, action_type, created_at)
            VALUES (?, ?, ?)
            """;

    private static final String SELECT_BY_ID_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            WHERE user_actions.id = ?
            """;

    private static final String SELECT_ALL_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            ORDER BY user_actions.created_at DESC
            """;

    private static final String DELETE_BY_ID_SQL = "DELETE FROM user_actions WHERE id = ?";

    private static final String SELECT_BY_RESEARCHER_ID_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            WHERE user_actions.researcher = ?
            ORDER BY user_actions.created_at DESC
            """;

    private static final String SELECT_BY_ACTION_TYPE_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            WHERE user_actions.action_type = ?
            ORDER BY user_actions.created_at DESC
            """;

    private static final String SELECT_BY_DATE_RANGE_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            WHERE user_actions.created_at BETWEEN ? AND ?
            ORDER BY user_actions.created_at DESC
            """;

    private static final String SELECT_RECENT_ACTIONS_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            ORDER BY user_actions.created_at DESC
            LIMIT ?
            """;

    private static final String COUNT_BY_RESEARCHER_ID_SQL = """
            SELECT COUNT(*) FROM user_actions WHERE researcher = ?
            """;

    private static final String SELECT_BY_RESEARCHER_AND_TYPE_SQL = """
            SELECT user_actions.id, user_actions.researcher, user_actions.action_type, user_actions.created_at,
                   researchers.id researcher_id, researchers.nickname, researchers.backend_user_id, researchers.created_at researcher_created_at
            FROM user_actions
            LEFT JOIN researchers ON user_actions.researcher = researchers.id
            WHERE user_actions.researcher = ? AND user_actions.action_type = ?
            ORDER BY user_actions.created_at DESC
            """;

    public ArchiveAction saveWithJdbc(ArchiveAction action) {
        if (action.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                ps.setLong(1, action.getArchiveResearcher().getId());
                ps.setString(2, action.getActionType().name());
                ps.setTimestamp(3, Timestamp.valueOf(action.getCreatedAt() != null ? action.getCreatedAt() : LocalDateTime.now()));
                return ps;
            }, keyHolder);

            action.setId(keyHolder.getKey().longValue());
            return action;
        } else {
            jdbcTemplate.update("UPDATE user_actions SET action_type = ?, created_at = ? WHERE id = ?", action.getActionType().name(), Timestamp.valueOf(action.getCreatedAt()), action.getId());
            return action;
        }
    }

    public Optional<ArchiveAction> findByIdWithJdbc(Long id) {
        List<ArchiveAction> results = jdbcTemplate.query(SELECT_BY_ID_SQL, archiveActionRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<ArchiveAction> findAllWithJdbc() {
        return jdbcTemplate.query(SELECT_ALL_SQL, archiveActionRowMapper());
    }

    public void deleteByIdWithJdbc(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    public List<ArchiveAction> findByResearcherIdWithJdbc(Long researcherId) {
        return jdbcClient.sql(SELECT_BY_RESEARCHER_ID_SQL).param(researcherId).query(archiveActionRowMapper()).list();
    }

    public List<ArchiveAction> findByActionTypeWithJdbc(ActionType actionType) {
        return jdbcClient.sql(SELECT_BY_ACTION_TYPE_SQL).param(actionType.name()).query(archiveActionRowMapper()).list();
    }

    public List<ArchiveAction> findByDateRangeWithJdbc(LocalDateTime startDate, LocalDateTime endDate) {
        return jdbcClient.sql(SELECT_BY_DATE_RANGE_SQL).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).query(archiveActionRowMapper()).list();
    }

    public List<ArchiveAction> findRecentActionsWithJdbc(int limit) {
        return jdbcClient.sql(SELECT_RECENT_ACTIONS_SQL).param(limit).query(archiveActionRowMapper()).list();
    }

    public long countByResearcherIdWithJdbc(Long researcherId) {
        return jdbcClient.sql(COUNT_BY_RESEARCHER_ID_SQL).param(researcherId).query(Long.class).single();
    }

    public List<ArchiveAction> findByResearcherIdAndActionTypeWithJdbc(Long researcherId, ActionType actionType) {
        return jdbcClient.sql(SELECT_BY_RESEARCHER_AND_TYPE_SQL).param(researcherId).param(actionType.name()).query(archiveActionRowMapper()).list();
    }

    public Map<Long, Long> getCommentActivityLeaderboard(int limit) {
        String sql = """
                SELECT researcher, COUNT(*) action_count
                FROM user_actions
                WHERE action_type IN ('CREATE_COMMENT', 'EDIT_COMMENT', 'DELETE_COMMENT')
                GROUP BY researcher
                ORDER BY action_count DESC
                LIMIT ?
                """;
        return jdbcClient.sql(sql).param(limit).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<Long, Long> getCommentActivityLeaderboardByDateRange(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String sql = """
                SELECT researcher, COUNT(*) action_count
                FROM user_actions
                WHERE action_type IN ('CREATE_COMMENT', 'EDIT_COMMENT', 'DELETE_COMMENT')
                AND created_at BETWEEN ? AND ?
                GROUP BY researcher
                ORDER BY action_count DESC
                LIMIT ?
                """;
        return jdbcClient.sql(sql).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).param(limit).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<Long, Long> getOverallActivityLeaderboard(int limit) {
        String sql = """
                SELECT researcher, COUNT(*) action_count
                FROM user_actions
                GROUP BY researcher
                ORDER BY action_count DESC
                LIMIT ?
                """;
        return jdbcClient.sql(sql).param(limit).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<Long, Long> getOverallActivityLeaderboardByDateRange(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String sql = """
                SELECT researcher, COUNT(*) action_count
                FROM user_actions
                WHERE created_at BETWEEN ? AND ?
                GROUP BY researcher
                ORDER BY action_count DESC
                LIMIT ?
                """;
        return jdbcClient.sql(sql).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).param(limit).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<Long, Long> getActivityLeaderboardByActionType(ActionType actionType, int limit) {
        String sql = """
                SELECT researcher, COUNT(*) action_count
                FROM user_actions
                WHERE action_type = ?
                GROUP BY researcher
                ORDER BY action_count DESC
                LIMIT ?
                """;
        return jdbcClient.sql(sql).param(actionType.name()).param(limit).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<Long, Long> getActivityLeaderboardByActionTypeAndDateRange(ActionType actionType, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String sql = """
                SELECT researcher, COUNT(*) action_count
                FROM user_actions
                WHERE action_type = ? AND created_at BETWEEN ? AND ?
                GROUP BY researcher
                ORDER BY action_count DESC
                LIMIT ?
                """;
        return jdbcClient.sql(sql).param(actionType.name()).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).param(limit).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<Long, Map<ActionType, Long>> getDetailedActivityLeaderboard(int limit) {
        String sql = """
                SELECT researcher, action_type, COUNT(*) action_count
                FROM user_actions
                GROUP BY researcher, action_type
                ORDER BY researcher, action_count DESC
                """;

        List<Map.Entry<Long, Map.Entry<ActionType, Long>>> results = jdbcClient.sql(sql).query((rs, rowNum) -> Map.entry(rs.getLong("researcher"), Map.entry(ActionType.valueOf(rs.getString("action_type")), rs.getLong("action_count")))).list();

        Map<Long, Map<ActionType, Long>> leaderboard = new LinkedHashMap<>();

        for (Map.Entry<Long, Map.Entry<ActionType, Long>> result : results) {
            Long researcherId = result.getKey();
            ActionType actionType = result.getValue().getKey();
            Long count = result.getValue().getValue();

            leaderboard.computeIfAbsent(researcherId, k -> new HashMap<>()).put(actionType, count);
        }

        return leaderboard.entrySet().stream().sorted(Map.Entry.<Long, Map<ActionType, Long>>comparingByValue((map1, map2) -> {
            long total1 = map1.values().stream().mapToLong(Long::longValue).sum();
            long total2 = map2.values().stream().mapToLong(Long::longValue).sum();
            return Long.compare(total2, total1);
        })).limit(limit).collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public long countCommentActivityByResearcher(Long researcherId) {
        String sql = """
                SELECT COUNT(*) FROM user_actions
                WHERE researcher = ? AND action_type IN ('CREATE_COMMENT', 'EDIT_COMMENT', 'DELETE_COMMENT')
                """;
        return jdbcClient.sql(sql).param(researcherId).query(Long.class).single();
    }

    public long countCommentActivityByResearcherAndDateRange(Long researcherId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
                SELECT COUNT(*) FROM user_actions
                WHERE researcher = ? AND action_type IN ('CREATE_COMMENT', 'EDIT_COMMENT', 'DELETE_COMMENT')
                AND created_at BETWEEN ? AND ?
                """;
        return jdbcClient.sql(sql).param(researcherId).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).query(Long.class).single();
    }

    public long countTotalActions() {
        String sql = "SELECT COUNT(*) FROM user_actions";
        return jdbcClient.sql(sql).query(Long.class).single();
    }

    public long countTotalActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM user_actions WHERE created_at BETWEEN ? AND ?";
        return jdbcClient.sql(sql).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).query(Long.class).single();
    }

    public Map<ActionType, Long> getActionTypeDistribution() {
        String sql = """
                SELECT action_type, COUNT(*) action_count
                FROM user_actions
                GROUP BY action_type
                ORDER BY action_count DESC
                """;
        return jdbcClient.sql(sql).query((rs, rowNum) -> Map.entry(ActionType.valueOf(rs.getString("action_type")), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<ActionType, Long> getActionTypeDistributionByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
                SELECT action_type, COUNT(*) action_count
                FROM user_actions
                WHERE created_at BETWEEN ? AND ?
                GROUP BY action_type
                ORDER BY action_count DESC
                """;
        return jdbcClient.sql(sql).param(Timestamp.valueOf(startDate)).param(Timestamp.valueOf(endDate)).query((rs, rowNum) -> Map.entry(ActionType.valueOf(rs.getString("action_type")), rs.getLong("action_count"))).list().stream().collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    private RowMapper<ArchiveAction> archiveActionRowMapper() {
        return (rs, rowNum) -> {
            ArchiveAction action = new ArchiveAction();
            action.setId(rs.getLong("id"));
            action.setActionType(mapActionType(rs.getString("action_type")));
            action.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            ArchiveResearcher researcher = new ArchiveResearcher();
            researcher.setId(rs.getLong("researcher_id"));
            researcher.setNickname(rs.getString("nickname"));
            String backendUserIdStr = rs.getString("backend_user_id");
            if (backendUserIdStr != null) {
                researcher.setBackendUserId(UUID.fromString(backendUserIdStr));
            }
            Timestamp researcherCreatedAt = rs.getTimestamp("researcher_created_at");
            if (researcherCreatedAt != null) {
                researcher.setCreatedAt(researcherCreatedAt.toLocalDateTime());
            }
            action.setArchiveResearcher(researcher);

            return action;
        };
    }

    private ActionType mapActionType(String actionTypeValue) {
        if (actionTypeValue == null) {
            return null;
        }

        try {
            return ActionType.valueOf(actionTypeValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                int ordinal = Integer.parseInt(actionTypeValue);
                ActionType[] values = ActionType.values();
                if (ordinal >= 0 && ordinal < values.length) {
                    return values[ordinal];
                }
            } catch (NumberFormatException ex) {
            }
        }

        logger.error("Cannot map action_type value '{}' to ActionType enum. Available values: {}", actionTypeValue, Arrays.toString(ActionType.values()));
        throw new IllegalArgumentException("Cannot map action_type value '" + actionTypeValue + "' to ActionType enum");
    }
}