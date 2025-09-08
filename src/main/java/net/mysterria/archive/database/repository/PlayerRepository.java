package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchivePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<ArchivePlayer, String> {

    @Override
    List<ArchivePlayer> findAll();

    List<ArchivePlayer> findByPlayerName(String playerName);

    List<ArchivePlayer> findByPlayerUUID(String playerUUID);

    @Query(value = "SELECT * FROM archive_item WHERE player_found = ?1", nativeQuery = true)
    List<ArchiveItem> findItemsFoundByPlayer(ArchivePlayer player);
}
