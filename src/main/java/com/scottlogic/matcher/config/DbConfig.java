package com.scottlogic.matcher.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("!container")
@EnableMongoRepositories(basePackages = "com.scottlogic.matcher")
public class DbConfig extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    public String host;

    @Value("${spring.data.mongodb.port}")
    public String port;

    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder
            .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%s/%s", host, port, databaseName)));
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
