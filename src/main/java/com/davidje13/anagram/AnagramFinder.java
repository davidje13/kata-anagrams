package com.davidje13.anagram;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class AnagramFinder {
	public Set<Set<String>> findAnagrams(Stream<String> words) {
		return words.collect(groupingBy(this::normalise, toSet()))
				.values().stream()
				.filter((group) -> group.size() > 1)
				.collect(toSet());
	}

	private String normalise(String word) {
		char[] chars = word.toLowerCase().toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}
}
