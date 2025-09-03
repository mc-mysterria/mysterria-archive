package net.mysterria.archive.config;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public HardstuckPlatinum getHardstuckPlatinum() {
        return new HardstuckPlatinum();
    }

}
