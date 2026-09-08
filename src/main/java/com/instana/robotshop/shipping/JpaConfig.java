package com.instana.robotshop.shipping;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    private static final Logger logger = LoggerFactory.getLogger(JpaConfig.class);

    @Bean
    public DataSource getDataSource() {

        String dbHost = System.getenv("DB_HOST");

        if (dbHost == null || dbHost.isBlank()) {
            dbHost = "mysql";
        }

        String jdbcUrl = String.format(
                "jdbc:mysql://%s/cities?useSSL=false&autoReconnect=true",
                dbHost);

        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");

        logger.info("Creating database connection for host {}", dbHost);

        DataSourceBuilder<?> builder = DataSourceBuilder.create();

        builder.driverClassName("com.mysql.cj.jdbc.Driver");
        builder.url(jdbcUrl);
        builder.username(dbUsername);
        builder.password(dbPassword);

        return builder.build();
    }
}
