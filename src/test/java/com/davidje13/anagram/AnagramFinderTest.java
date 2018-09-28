package com.davidje13.anagram;

import com.davidje13.testutil.TestUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class AnagramFinderTest {
	private final AnagramFinder finder = new AnagramFinder();

	@Test
	public void findAnagrams_returnsSetOfIdentifiedAnagrams() {
		finder.loadWords(Stream.of("ab", "ba"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("ab", "ba")
		)));
	}

	@Test
	public void findAnagrams_returnsNothingIfInputIsEmpty() {
		finder.loadWords(Stream.empty());

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, hasSize(0));
	}

	@Test
	public void findAnagrams_excludesWordsWithoutAnagrams() {
		finder.loadWords(Stream.of("ab", "aa"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, hasSize(0));
	}

	@Test
	public void findAnagrams_isCaseInsensitive() {
		finder.loadWords(Stream.of("ab", "BA"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("ab", "BA")
		)));
	}

	@Test
	public void findAnagrams_doesNotConsiderDuplicatesAnagrams() {
		finder.loadWords(Stream.of("ab", "ab"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, hasSize(0));
	}

	@Test
	public void findAnagrams_doesNotConsiderMixedCaseDuplicatesAnagrams() {
		finder.loadWords(Stream.of("ab", "AB"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, hasSize(0));
	}

	@Test
	public void findAnagrams_returnsAllCaseFormatsIfAnagramsAreFound() {
		finder.loadWords(Stream.of("ab", "AB", "ba"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("ab", "AB", "ba")
		)));
	}

	@Test
	public void findAnagrams_combinesAllAnagramsOfTheSameWord() {
		finder.loadWords(Stream.of("abc", "cba", "bca"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("abc", "cba", "bca")
		)));
	}

	@Test
	public void findAnagrams_returnsMultipleSetsOfAnagrams() {
		finder.loadWords(Stream.of("abc", "dog", "cba", "nah", "bca", "god"));

		Set<Set<String>> anagrams = finder.findAnagrams().collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("abc", "cba", "bca"),
				containsInAnyOrder("dog", "god")
		)));
	}

	@Test
	public void findAnagrams_runsInLinearTime() {
		int smallSize = 1000;
		int growthFactor = 100;

		double smallSetMillis = TestUtils.averageTimeTakenMillis(50, () -> {
			Stream<String> words = IntStream.range(0, smallSize)
					.mapToObj((v) -> "value-" + v);
			AnagramFinder finder = new AnagramFinder();
			finder.loadWords(words);
			finder.findAnagrams();
		});

		assertThat(smallSetMillis, lessThan(10.0));

		double largeSetMillis = TestUtils.averageTimeTakenMillis(5, () -> {
			Stream<String> words = IntStream.range(0, smallSize * growthFactor)
					.mapToObj((v) -> "value-" + v);
			AnagramFinder finder = new AnagramFinder();
			finder.loadWords(words);
			finder.findAnagrams();
		});

		double expectedMaxTime = smallSetMillis * growthFactor * 1.5;
		assertThat(largeSetMillis, lessThan(expectedMaxTime));
	}

	@Test
	public void findMultiWordAnagrams_returnsWordsFormingAnagramsOfTheInput() {
		finder.loadWords(Stream.of("abc", "def", "ghi", "jkl"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("ihgfed", 3)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("def"),
						containsInAnyOrder("ghi")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_ignoresCase() {
		finder.loadWords(Stream.of("abc", "def", "ghi", "jkl"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("iHgFED", 3)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("def"),
						containsInAnyOrder("ghi")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_returnsAllWordAnagrams() {
		finder.loadWords(Stream.of("abc", "def", "fed", "ghi", "jkl"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("ihgfed", 3)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("def", "fed"),
						containsInAnyOrder("ghi")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_returnsAllCombinations() {
		finder.loadWords(Stream.of("abc", "def", "ghi", "jkl", "defg", "hi"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("ihgfed", 3)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("def"),
						containsInAnyOrder("ghi")
				)),
				containsInAnyOrder(asList(
						containsInAnyOrder("defg"),
						containsInAnyOrder("hi")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_allowsRepetitions() {
		finder.loadWords(Stream.of("abc", "def", "ghi", "jkl", "defg", "hi"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("aabbcc", 3)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("abc"),
						containsInAnyOrder("abc")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_returnsCombinationsOfAllWordCounts() {
		finder.loadWords(Stream.of("abc", "def", "ab", "cd", "ef", "abcdef"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("fedcba", 3)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("abcdef")
				)),
				containsInAnyOrder(asList(
						containsInAnyOrder("abc"),
						containsInAnyOrder("def")
				)),
				containsInAnyOrder(asList(
						containsInAnyOrder("ab"),
						containsInAnyOrder("cd"),
						containsInAnyOrder("ef")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_allowsLimitingMaxWordCount() {
		finder.loadWords(Stream.of("abc", "def", "ab", "cd", "ef", "abcdef"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("fedcba", 2)
				.collect(toList());

		assertThat(choices, containsInAnyOrder(asList(
				containsInAnyOrder(asList(
						containsInAnyOrder("abcdef")
				)),
				containsInAnyOrder(asList(
						containsInAnyOrder("abc"),
						containsInAnyOrder("def")
				))
		)));
	}

	@Test
	public void findMultiWordAnagrams_returnsNothingIfNoAnagramsAreFound() {
		finder.loadWords(Stream.of("abc", "def", "ghi", "jkl"));

		Collection<Collection<Set<String>>> choices = finder
				.findMultiWordAnagrams("xyz", 3)
				.collect(toList());

		assertThat(choices, hasSize(0));
	}
}
