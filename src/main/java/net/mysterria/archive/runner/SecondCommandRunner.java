package net.mysterria.archive.runner;

import net.mysterria.archive.database.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component(value = "secondCommandRunner")
@Order(2)
public class SecondCommandRunner implements CommandLineRunner {

    private final ItemRepository itemRepository;

    public SecondCommandRunner(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from second runner!");
    }

}
