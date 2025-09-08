package net.mysterria.archive;

import net.mysterria.archive.database.repository.ItemRepository;
import net.mysterria.archive.database.repository.PathwayRepository;
import net.mysterria.archive.database.repository.ResearcherRepository;
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

        System.out.println("Starting with " + itemRepository.count() + " items");

        ResearcherRepository researcherRepository = applicationContext.getBean(ResearcherRepository.class);

        System.out.println("Starting with " + researcherRepository.count() + " researchers");

        TypeRepository typeRepository = applicationContext.getBean(TypeRepository.class);

        System.out.println("Starting with " + typeRepository.count() + " types");

        PathwayRepository pathwayRepository = applicationContext.getBean(PathwayRepository.class);

        System.out.println("Starting with " + pathwayRepository.count() + " pathways");
    }
}
