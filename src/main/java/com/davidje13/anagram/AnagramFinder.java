package com.davidje13.anagram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class AnagramFinder {
	public Stream<Set<String>> findAnagrams(Stream<String> words) {
		return words.collect(groupingBy(this::normalise, toSet()))
				.values().stream()
				.filter(this::hasDistinctValues);
	}

	private String normalise(String word) {
		char[] chars = word.toLowerCase().toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

	private boolean hasDistinctValues(Collection<String> group) {
		if (group.size() < 2) {
			return false;
		}

		long distinctWords = group.stream()
				.map(String::toLowerCase)
				.distinct()
				.count();

		return distinctWords > 1;
	}
}
