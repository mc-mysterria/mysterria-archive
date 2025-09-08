package net.mysterria.archive.database.repository;

import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchivePathway;
import net.mysterria.archive.database.entity.ArchivePlayer;
import net.mysterria.archive.database.entity.ArchiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ArchiveItem, Long> {

    @Override
    List<ArchiveItem> findAll();

    List<ArchiveItem> findByItemName(String itemName);

    List<ArchiveItem> findByItemLore(String itemLore);

    List<ArchiveItem> findByPlayerFound(ArchivePlayer playerFound);

    List<ArchiveItem> findByItemType(ArchiveType itemType);

    List<ArchiveItem> findByItemPath(ArchivePathway itemPath);
}
