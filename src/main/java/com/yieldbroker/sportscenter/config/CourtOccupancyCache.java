package com.yieldbroker.sportscenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Configuration
public class CourtOccupancyCache {

    @Bean
    public Map<Integer, Integer> courtOccupancy() {
        return new HashMap<>();
    }

    @Bean
    public HashSet<Integer> userOccupancy() {
        return new HashSet<>();
    }
}
