package org.kmaihome.pos.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {
    @Bean
    public FlywayMigrationStrategy repairThenMigrate() {
        return flyway -> {
            try {
                flyway.repair();
            } catch (Exception ignored) {}
            flyway.migrate();
        };
    }
}
