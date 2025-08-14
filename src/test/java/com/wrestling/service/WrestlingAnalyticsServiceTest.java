package com.wrestling.service;

import com.wrestling.model.Wrestler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WrestlingAnalyticsServiceTest {

    private final WrestlingAnalyticsService service = new WrestlingAnalyticsService();

    @Test
    void testGetAllWrestlers() {
        List<Wrestler> wrestlers = service.getAllWrestlers();
        assertEquals(10, wrestlers.size());
    }

    @Test
    void testFindDuplicateLettersInName() {
        Map<Character, Long> duplicates = service.findDuplicateLettersInName("Stone Cold");
        assertEquals(1, duplicates.size());
        assertTrue(duplicates.containsKey('o'));
        assertEquals(2L, duplicates.get('o'));
    }

    @Test
    void testPartitionWrestlersByWinRate() {
        Map<Boolean, List<Wrestler>> partitioned = service.partitionWrestlersByWinRate(70.0);
        
        assertTrue(partitioned.containsKey(true));
        assertTrue(partitioned.containsKey(false));
        
        // Should have wrestlers with >= 70% win rate
        List<Wrestler> highPerformers = partitioned.get(true);
        assertFalse(highPerformers.isEmpty());
        
        for (Wrestler wrestler : highPerformers) {
            assertTrue(wrestler.getWinPercentage() >= 70.0);
        }
    }

    @Test
    void testGetTopWrestlerByWinPercentage() {
        Optional<Wrestler> topWrestler = service.getTopWrestlerByWinPercentage();
        assertTrue(topWrestler.isPresent());
        
        // Verify it's actually the wrestler with highest win percentage
        double maxWinRate = service.getAllWrestlers().stream()
                .mapToDouble(Wrestler::getWinPercentage)
                .max()
                .orElse(0.0);
        
        assertEquals(maxWinRate, topWrestler.get().getWinPercentage(), 0.01);
    }

    @Test
    void testGetCategoryStats() {
        Map<String, Long> categoryStats = service.getWrestlerCountByCategory();
        
        assertFalse(categoryStats.isEmpty());
        assertTrue(categoryStats.containsKey("Heavyweight"));
        assertTrue(categoryStats.containsKey("Technical"));
        assertTrue(categoryStats.containsKey("Cruiserweight"));
    }
}