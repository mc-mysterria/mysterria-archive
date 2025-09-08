package net.mysterria.archive;

import net.mysterria.archive.database.repository.ItemRepository;
import net.mysterria.archive.database.repository.PathwayRepository;
import net.mysterria.archive.database.repository.PlayerRepository;
import net.mysterria.archive.database.repository.TypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

@Order(0)
@SpringBootApplication
public class MysterriaArchiveApplication implements CommandLineRunner {

    public final ApplicationContext applicationContext;

    public MysterriaArchiveApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(MysterriaArchiveApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from Mysterria Archive!");


        ItemRepository itemRepository = applicationContext.getBean(ItemRepository.class);

        PlayerRepository playerRepository = applicationContext.getBean(PlayerRepository.class);

        TypeRepository typeRepository = applicationContext.getBean(TypeRepository.class);

        PathwayRepository pathwayRepository = applicationContext.getBean(PathwayRepository.class);
    }
}
