package com.davidje13.permutation;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class PermutationExpanderTest {
	private final PermutationExpander expander = new PermutationExpander();

	@Test
	public void expand_createsEntriesForEachPermutation() {
		Set<Collection<String>> permutations = expander.expand(asList(
				new HashSet<>(asList("a", "b")),
				new HashSet<>(asList("c", "d"))
		)).collect(toSet());

		assertThat(permutations, containsInAnyOrder(asList(
				equalTo(asList("a", "c")),
				equalTo(asList("a", "d")),
				equalTo(asList("b", "c")),
				equalTo(asList("b", "d"))
		)));
	}

	@Test
	public void expand_handlesSingleValues() {
		Set<Collection<String>> permutations = expander.expand(asList(
				new HashSet<>(asList("a"))
		)).collect(toSet());

		assertThat(permutations, containsInAnyOrder(asList(
				equalTo(asList("a"))
		)));
	}

	@Test
	public void expand_returnsASinglePermutationGivenTheEmptySet() {
		Set<Collection<String>> permutations = expander
				.expand(Collections.<Set<String>>emptyList())
				.collect(toSet());

		assertThat(permutations, containsInAnyOrder(asList(
				equalTo(asList())
		)));
	}

	@Test
	public void expand_createsEntriesOfDifferentSizes() {
		Set<Collection<String>> permutations = expander.expand(asList(
				new HashSet<>(asList("a", "b")),
				new HashSet<>(asList("c", "d", "e")),
				new HashSet<>(asList("f"))
		)).collect(toSet());

		assertThat(permutations, containsInAnyOrder(asList(
				equalTo(asList("a", "c", "f")),
				equalTo(asList("a", "d", "f")),
				equalTo(asList("a", "e", "f")),
				equalTo(asList("b", "c", "f")),
				equalTo(asList("b", "d", "f")),
				equalTo(asList("b", "e", "f"))
		)));
	}
}
