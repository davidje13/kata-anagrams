package com.davidje13.anagram;

import com.davidje13.permutation.PermutationExpander;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class AnagramFinder {
	private final PermutationExpander expander = new PermutationExpander();
	private final Map<String, Set<String>> groupedWords = new HashMap<>();

	public void loadWords(Stream<String> words) {
		loadWords(words, 1024);
	}

	public void loadWords(Stream<String> words, int initialCapacity) {
		groupedWords.putAll(words.collect(groupingBy(
				AnagramFinder::normalise,
				() -> new HashMap<>(initialCapacity),
				toSet()
		)));
	}

	public Stream<Set<String>> findAnagrams() {
		return groupedWords
				.values().stream()
				.filter(AnagramFinder::hasDistinctValues);
	}

	public Stream<Set<String>> findWordsWithin(String word) {
		return getAllVariants(normalise(word))
				.distinct()
				.map(groupedWords::get)
				.filter(Objects::nonNull);
	}

	private Stream<String> getAllVariants(CharSequence word) {
		List<List<String>> choices = IntStream.range(0, word.length())
				.mapToObj((i) -> asList(String.valueOf(word.charAt(i)), ""))
				.collect(toList());

		return expander.expand(choices)
				.map((l) -> String.join("", l));
	}

	public Stream<Collection<Set<String>>> findMultiWordAnagrams(
			String source,
			int wordCountLimit
	) {
		MultiWordAnagramFinder finder = new MultiWordAnagramFinder(
				groupedWords.keySet(),
				countLetters(normalise(source)),
				wordCountLimit
		);

		return finder.findAll()
				.map((groups) -> groups.stream()
						.map(groupedWords::get)
						.collect(toList())
				);
	}

	private static class MultiWordAnagramFinder {
		// Sort long-to-short with consistent ordering within sizes
		private static final Comparator<String> GROUP_ORDERING = Comparator
				.comparingInt(String::length).reversed()
				.thenComparing(Comparator.naturalOrder());

		private final String[] orderedGroups;
		private final Map<Character, Long> letterCounts;
		private int lettersRemaining;

		private final int[] current;
		private int currentCursor;

		private MultiWordAnagramFinder(
				Collection<String> groupedOptions,
				Map<Character, Long> letterCounts,
				int maxDepth
		) {
			this.orderedGroups = groupedOptions.stream()
					.sorted(GROUP_ORDERING)
					.toArray(String[]::new);
			this.letterCounts = letterCounts;
			this.lettersRemaining = (int) letterCounts.values().stream()
					.mapToLong(v -> v).sum();
			this.current = new int[maxDepth];
		}

		private Stream<Collection<String>> findAll() {
			this.current[0] = 0;
			this.currentCursor = 0;

			return Stream.iterate(findNext(), (v) -> findNext())
					.takeWhile(Objects::nonNull)
					.map(this::unwrap);
		}

		@SuppressWarnings("ValueOfIncrementOrDecrementUsed")
		private int[] findNext() {
			while (true) {
				if (current[currentCursor] >= orderedGroups.length) {
					if (currentCursor == 0) {
						return null;
					}
					-- currentCursor;
					unpick(orderedGroups[current[currentCursor] ++]);
				} else if (!pick(orderedGroups[current[currentCursor]])) {
					++ current[currentCursor];
				} else if (lettersRemaining == 0) {
					int[] result = Arrays.copyOfRange(current, 0, currentCursor + 1);
					unpick(orderedGroups[current[currentCursor] ++]);
					return result;
				} else if (currentCursor + 1 < current.length) {
					current[currentCursor + 1] = Math.max(
							findGroupIndex(fromCounts(letterCounts)),
							current[currentCursor]
					);
					++ currentCursor;
				} else {
					unpick(orderedGroups[current[currentCursor] ++]);
				}
			}
		}

		private boolean pick(String word) {
			int length = word.length();
			if (length > lettersRemaining) {
				return false;
			}
			for (int i = 0; i < length; ++ i) {
				Long updated = letterCounts.computeIfPresent(word.charAt(i), (k, v) -> v - 1);
				if (updated == null || updated < 0) {
					for (int j = 0; j <= i; ++ j) {
						letterCounts.computeIfPresent(word.charAt(j), (k, v) -> v + 1);
					}
					return false;
				}
			}
			lettersRemaining -= length;
			return true;
		}

		private void unpick(String word) {
			int length = word.length();
			for (int i = 0; i < length; ++ i) {
				letterCounts.computeIfPresent(word.charAt(i), (k, v) -> v + 1);
			}
			lettersRemaining += length;
		}

		private int findGroupIndex(String group) {
			int nextCheck = Arrays.binarySearch(
					orderedGroups,
					group,
					GROUP_ORDERING
			);
			if (nextCheck < 0) {
				return -nextCheck - 1;
			} else {
				return nextCheck;
			}
		}

		private Collection<String> unwrap(int[] groups) {
			return IntStream.of(groups)
					.mapToObj((index) -> orderedGroups[index])
					.collect(toList());
		}
	}

	private static String fromCounts(Map<Character, Long> letterCounts) {
		StringBuilder builder = new StringBuilder();
		letterCounts.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.forEach((i) -> {
					for (long n = 0; n < i.getValue(); ++ n) {
						builder.append(i.getKey());
					}
				});
		return builder.toString();
	}

	private static Map<Character, Long> countLetters(CharSequence word) {
		return word.chars()
				.mapToObj((c) -> (char) c)
				.collect(groupingBy((c) -> c, counting()));
	}

	private static String normalise(String word) {
		char[] chars = word.toLowerCase().toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

	private static boolean hasDistinctValues(Collection<String> group) {
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
