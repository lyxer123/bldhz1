package com.bld.project.newlyadded.config;

import org.hyperic.sigar.Sigar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SigarConfig {


    @Bean
    public Sigar sigar(){
        return new Sigar();
    }
}
