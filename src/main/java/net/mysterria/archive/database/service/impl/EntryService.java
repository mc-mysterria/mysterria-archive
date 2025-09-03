package net.mysterria.archive.database.service.impl;

import net.mysterria.archive.database.repository.EntryRepository;
import net.mysterria.archive.database.service.EntryServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class EntryService implements EntryServiceInterface {

    public EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public void init() {
        System.out.println("init");
    }
}
