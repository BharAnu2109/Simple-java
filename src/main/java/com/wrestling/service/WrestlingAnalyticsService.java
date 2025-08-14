package com.wrestling.service;

import com.wrestling.model.Wrestler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WrestlingAnalyticsService {

    private final List<Wrestler> wrestlers = Arrays.asList(
        new Wrestler("Stone Cold Steve Austin", 1998, "Heavyweight", 245, 89),
        new Wrestler("The Rock", 1995, "Heavyweight", 231, 112),
        new Wrestler("John Cena", 2005, "Heavyweight", 189, 67),
        new Wrestler("Undertaker", 1991, "Heavyweight", 167, 98),
        new Wrestler("Hulk Hogan", 1984, "Heavyweight", 156, 123),
        new Wrestler("Rey Mysterio", 2003, "Cruiserweight", 134, 145),
        new Wrestler("Eddie Guerrero", 2001, "Cruiserweight", 123, 89),
        new Wrestler("Daniel Bryan", 2010, "Technical", 119, 76),
        new Wrestler("CM Punk", 2011, "Technical", 115, 81),
        new Wrestler("Shawn Michaels", 1996, "Technical", 111, 94)
    );

    /**
     * Find duplicate characters in wrestler names (adapted from FindDuplicates.java)
     */
    public Map<Character, Long> findDuplicateLettersInName(String wrestlerName) {
        return wrestlerName.toLowerCase().chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c != ' ') // ignore spaces
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Find wrestlers with ratings starting with specific digit (adapted from ElementsStartingWithOne.java)
     */
    public List<String> findWrestlersWithRatingsStartingWith(String digit) {
        return wrestlers.stream()
                .map(w -> w.getRating())
                .map(String::valueOf)
                .filter(rating -> rating.startsWith(digit))
                .collect(Collectors.toList());
    }

    /**
     * Partition wrestlers into winners and others based on win percentage (adapted from Java8Code.java)
     */
    public Map<Boolean, List<Wrestler>> partitionWrestlersByWinRate(double threshold) {
        return wrestlers.stream()
                .collect(Collectors.partitioningBy(w -> w.getWinPercentage() >= threshold));
    }

    /**
     * Sort wrestlers by rating in descending order (adapted from SortDescending.java)
     */
    public List<Wrestler> getWrestlersByRatingDesc() {
        return wrestlers.stream()
                .sorted(Comparator.comparing(Wrestler::getRating).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Find the first non-repeated character in combined wrestler names (adapted from FirstNonRepeated.java)
     */
    public Character findFirstNonRepeatedCharacterInAllNames() {
        String allNames = wrestlers.stream()
                .map(Wrestler::getName)
                .collect(Collectors.joining());

        return allNames.toLowerCase().chars()
                .mapToObj(s -> Character.valueOf((char) s))
                .filter(c -> c != ' ')
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1L)
                .map(entry -> entry.getKey())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get character count for all wrestler names (adapted from findCountOfChars.java)
     */
    public Map<String, Long> getCharacterCountInAllNames() {
        String allNames = wrestlers.stream()
                .map(Wrestler::getName)
                .collect(Collectors.joining());

        return Arrays.stream(allNames.toLowerCase().split(""))
                .filter(c -> !c.equals(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Find the wrestler with highest win percentage (adapted from FindMaxElement.java concept)
     */
    public Optional<Wrestler> getTopWrestlerByWinPercentage() {
        return wrestlers.stream()
                .max(Comparator.comparing(Wrestler::getWinPercentage));
    }

    /**
     * Get second highest rated wrestler (adapted from SecondHighestNumber.java concept)
     */
    public Optional<Wrestler> getSecondHighestRatedWrestler() {
        return wrestlers.stream()
                .sorted(Comparator.comparing(Wrestler::getRating).reversed())
                .skip(1)
                .findFirst();
    }

    /**
     * Group wrestlers by category and count (complex scenario)
     */
    public Map<String, Long> getWrestlerCountByCategory() {
        return wrestlers.stream()
                .collect(Collectors.groupingBy(Wrestler::getCategory, Collectors.counting()));
    }

    /**
     * Get all wrestlers for testing
     */
    public List<Wrestler> getAllWrestlers() {
        return new ArrayList<>(wrestlers);
    }
}