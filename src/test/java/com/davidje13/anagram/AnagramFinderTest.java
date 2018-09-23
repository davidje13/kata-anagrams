package com.davidje13.anagram;

import com.davidje13.testutil.TestUtils;
import org.junit.Test;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
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
		Stream<String> words = Stream.of("ab", "ba");

		Set<Set<String>> anagrams = finder.findAnagrams(words).collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("ab", "ba")
		)));
	}

	@Test
	public void findAnagrams_returnsNothingIfInputIsEmpty() {
		Stream<String> words = Stream.empty();

		Set<Set<String>> anagrams = finder.findAnagrams(words).collect(toSet());

		assertThat(anagrams, hasSize(0));
	}

	@Test
	public void findAnagrams_excludesWordsWithoutAnagrams() {
		Stream<String> words = Stream.of("ab", "aa");

		Set<Set<String>> anagrams = finder.findAnagrams(words).collect(toSet());

		assertThat(anagrams, hasSize(0));
	}

	@Test
	public void findAnagrams_isCaseInsensitive() {
		Stream<String> words = Stream.of("ab", "BA");

		Set<Set<String>> anagrams = finder.findAnagrams(words).collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("ab", "BA")
		)));
	}

	@Test
	public void findAnagrams_combinesAllAnagramsOfTheSameWord() {
		Stream<String> words = Stream.of("abc", "cba", "bca");

		Set<Set<String>> anagrams = finder.findAnagrams(words).collect(toSet());

		assertThat(anagrams, containsInAnyOrder(asList(
				containsInAnyOrder("abc", "cba", "bca")
		)));
	}

	@Test
	public void findAnagrams_returnsMultipleSetsOfAnagrams() {
		Stream<String> words = Stream.of(
				"abc",
				"dog",
				"cba",
				"nah",
				"bca",
				"god"
		);

		Set<Set<String>> anagrams = finder.findAnagrams(words).collect(toSet());

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
			finder.findAnagrams(words);
		});

		assertThat(smallSetMillis, lessThan(10.0));

		double largeSetMillis = TestUtils.averageTimeTakenMillis(5, () -> {
			Stream<String> words = IntStream.range(0, smallSize * growthFactor)
					.mapToObj((v) -> "value-" + v);
			finder.findAnagrams(words);
		});

		double expectedMaxTime = smallSetMillis * growthFactor * 1.5;
		assertThat(largeSetMillis, lessThan(expectedMaxTime));
	}
}
