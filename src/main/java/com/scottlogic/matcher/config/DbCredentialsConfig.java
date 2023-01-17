package com.scottlogic.matcher.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("container")
@EnableMongoRepositories(basePackages = "com.scottlogic.matcher")
public class DbCredentialsConfig extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    public String host;

    @Value("${spring.data.mongodb.port}")
    public String port;

    @Value("${spring.data.mongodb.username}")
    public String mongoUsername = "";

    @Value("${spring.data.mongodb.password}")
    public String mongoPassword = "";

    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        MongoCredential credential = MongoCredential.createCredential(mongoUsername, databaseName, mongoPassword.toCharArray());
        builder
            .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%s/%s", host, port, databaseName))).credential(credential);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
