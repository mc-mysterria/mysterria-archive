package net.mysterria.archive.database.service.impl;

import net.mysterria.archive.database.repository.ItemRepository;
import net.mysterria.archive.database.service.EntryServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class EntryService implements EntryServiceInterface {

    public ItemRepository itemRepository;

    public EntryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void init() {
        System.out.println("init");
    }
}
