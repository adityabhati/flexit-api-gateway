package com.flexit.api_gateway.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IniConfiguration {

    @Bean
    public org.apache.commons.configuration.Configuration getIniConfig(@Autowired ApplicationArguments applicationArguments) throws ConfigurationException {
        return new PropertiesConfiguration(applicationArguments.getOptionValues("Dpath").getFirst());

    }
}