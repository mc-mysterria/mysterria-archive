package net.mysterria.archive.runner;

import net.mysterria.archive.config.HardstuckPlatinum;
import net.mysterria.archive.database.entity.ArchiveEntry;
import net.mysterria.archive.database.entity.ArchiveType;
import net.mysterria.archive.database.repository.EntryRepository;
import net.mysterria.archive.database.repository.TypeRepository;
import net.mysterria.archive.database.service.EntryServiceInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component(value = "firstCommandRunner")
@Order(1)
public class FirstCommandRunner implements CommandLineRunner {

    private final EntryRepository entryRepository;
    private final TypeRepository typeRepository;
    private final HardstuckPlatinum hardstuckPlatinum;

    private final EntryServiceInterface entryService;

    public FirstCommandRunner(EntryRepository entryRepository, TypeRepository typeRepository, HardstuckPlatinum hardstuckPlatinum, EntryServiceInterface entryService) {
        this.entryRepository = entryRepository;
        this.typeRepository = typeRepository;
        this.hardstuckPlatinum = hardstuckPlatinum;
        this.entryService = entryService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from first runner!");

        ArchiveType archiveType = new ArchiveType();
        archiveType.setName("Aura!");
        typeRepository.save(archiveType);

        ArchiveEntry entry = new ArchiveEntry();
        entry.setDescription("bebra");
        entry.setName("Bebra!");
        entry.setType(archiveType);

        entryRepository.save(entry);

        hardstuckPlatinum.cryLoud();
    }

}
