package com.wrestling.controller;

import com.wrestling.model.Wrestler;
import com.wrestling.service.WrestlingAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/wrestling")
public class WrestlingController {

    @Autowired
    private WrestlingAnalyticsService analyticsService;

    @GetMapping("/wrestlers")
    public List<Wrestler> getAllWrestlers() {
        return analyticsService.getAllWrestlers();
    }

    @GetMapping("/wrestlers/top-by-rating")
    public List<Wrestler> getWrestlersByRating() {
        return analyticsService.getWrestlersByRatingDesc();
    }

    @GetMapping("/wrestlers/top-performer")
    public Optional<Wrestler> getTopPerformer() {
        return analyticsService.getTopWrestlerByWinPercentage();
    }

    @GetMapping("/wrestlers/second-highest-rated")
    public Optional<Wrestler> getSecondHighestRated() {
        return analyticsService.getSecondHighestRatedWrestler();
    }

    @GetMapping("/analytics/partition-by-winrate")
    public Map<Boolean, List<Wrestler>> partitionByWinRate(@RequestParam(defaultValue = "70.0") double threshold) {
        return analyticsService.partitionWrestlersByWinRate(threshold);
    }

    @GetMapping("/analytics/ratings-starting-with/{digit}")
    public List<String> getRatingsStartingWith(@PathVariable String digit) {
        return analyticsService.findWrestlersWithRatingsStartingWith(digit);
    }

    @GetMapping("/analytics/category-stats")
    public Map<String, Long> getCategoryStats() {
        return analyticsService.getWrestlerCountByCategory();
    }

    @GetMapping("/analytics/duplicate-letters/{name}")
    public Map<Character, Long> getDuplicateLetters(@PathVariable String name) {
        return analyticsService.findDuplicateLettersInName(name);
    }

    @GetMapping("/analytics/first-unique-char")
    public Character getFirstUniqueCharacter() {
        return analyticsService.findFirstNonRepeatedCharacterInAllNames();
    }

    @GetMapping("/analytics/character-frequency")
    public Map<String, Long> getCharacterFrequency() {
        return analyticsService.getCharacterCountInAllNames();
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "Wrestling Complex Scenarios API",
            "message", "Ready to wrestle with complex data scenarios!"
        );
    }
}