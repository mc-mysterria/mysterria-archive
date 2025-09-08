package net.mysterria.archive.runner;

import net.mysterria.archive.config.HardstuckPlatinum;
import net.mysterria.archive.database.entity.ArchiveItem;
import net.mysterria.archive.database.entity.ArchivePlayer;
import net.mysterria.archive.database.repository.ItemRepository;
import net.mysterria.archive.database.repository.PathwayRepository;
import net.mysterria.archive.database.repository.PlayerRepository;
import net.mysterria.archive.database.repository.TypeRepository;
import net.mysterria.archive.database.service.EntryServiceInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component(value = "firstCommandRunner")
@Order(1)
public class FirstCommandRunner implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final PlayerRepository playerRepository;
    private final TypeRepository typeRepository;
    private final PathwayRepository pathwayRepository;
    private final HardstuckPlatinum hardstuckPlatinum;

    private final EntryServiceInterface entryService;

    public FirstCommandRunner(ItemRepository itemRepository, PlayerRepository playerRepository, TypeRepository typeRepository, PathwayRepository pathwayRepository, HardstuckPlatinum hardstuckPlatinum, EntryServiceInterface entryService) {
        this.itemRepository = itemRepository;
        this.playerRepository = playerRepository;
        this.typeRepository = typeRepository;
        this.pathwayRepository = pathwayRepository;
        this.hardstuckPlatinum = hardstuckPlatinum;
        this.entryService = entryService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from first runner!");
    }

}
