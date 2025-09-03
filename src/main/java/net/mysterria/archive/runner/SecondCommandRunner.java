package net.mysterria.archive.runner;

import net.mysterria.archive.database.entity.ArchiveEntry;
import net.mysterria.archive.database.repository.EntryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "secondCommandRunner")
@Order(2)
public class SecondCommandRunner implements CommandLineRunner {

    private final EntryRepository entryRepository;

    public SecondCommandRunner(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from second runner!");

        List<ArchiveEntry> entry = entryRepository.findAll();
        for (ArchiveEntry e : entry) {
            System.out.println(e.toString());
        }

        List<ArchiveEntry> entry1 = entryRepository.findByName("Bebra!");
        if (!entry1.isEmpty()) {
            System.out.println("Found by name: ");
            System.out.println(entry1.getFirst());
        } else {
            System.out.println("No entry found!");
        }

        List<ArchiveEntry> entry2 = entryRepository.findByNameAndDescription("Bebra!", "bebra");
        if (!entry2.isEmpty()) {
            System.out.println("Found by name & desc: ");
            System.out.println(entry2.getFirst());
        } else {
            System.out.println("No entry found!");
        }

        List<ArchiveEntry> aura = entryRepository.findByAura();
        if (!aura.isEmpty()) {
            System.out.println("Found by aura: ");
            System.out.println(aura.getFirst());
        } else {
            System.out.println("No entry found!");
        }
    }

}
