package com.example.springbootapp.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamUtils {

    /**
     * Generic method to filter and map a collection using streams
     */
    public static <T, R> List<R> filterAndMap(Collection<T> collection, 
                                            Predicate<T> filter, 
                                            Function<T, R> mapper) {
        return collection.stream()
                .filter(filter)
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Convert list to map using method reference
     */
    public static <T, K> Map<K, T> toMap(List<T> list, Function<T, K> keyMapper) {
        return list.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * Group by using method reference
     */
    public static <T, K> Map<K, List<T>> groupBy(List<T> list, Function<T, K> classifier) {
        return list.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    /**
     * Find first matching element using predicate
     */
    public static <T> T findFirst(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if any element matches predicate
     */
    public static <T> boolean anyMatch(List<T> list, Predicate<T> predicate) {
        return list.stream().anyMatch(predicate);
    }

    /**
     * Check if all elements match predicate
     */
    public static <T> boolean allMatch(List<T> list, Predicate<T> predicate) {
        return list.stream().allMatch(predicate);
    }

    /**
     * Count elements matching predicate
     */
    public static <T> long count(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).count();
    }

    /**
     * Distinct elements using method reference
     */
    public static <T> List<T> distinct(List<T> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Sort using comparator lambda
     */
    public static <T> List<T> sort(List<T> list, java.util.Comparator<T> comparator) {
        return list.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Limit and skip operations
     */
    public static <T> List<T> paginate(List<T> list, int page, int size) {
        return list.stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * Transform list using function
     */
    public static <T, R> List<R> transform(List<T> list, Function<T, R> transformer) {
        return list.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }
} 