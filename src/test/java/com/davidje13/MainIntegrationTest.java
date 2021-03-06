package com.davidje13;

import com.davidje13.testutil.IntegrationTestUtils.Output;
import org.junit.Test;

import java.util.List;

import static com.davidje13.testutil.IntegrationTestUtils.getOutputFrom;
import static com.davidje13.testutil.IntegrationTestUtils.getResource;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class MainIntegrationTest {
	@Test
	public void main_reportsCollectionsOfWordsWhichAreAnagrams() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("simple.txt").getPath()
		}));

		assertThat(output.out, anyOf(
				equalTo("foo oof\n"),
				equalTo("oof foo\n")
		));
		assertThat(output.err, equalTo(""));
	}

	@Test
	public void main_reportsMultipleCollections() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("multiple.txt").getPath()
		}));

		assertThat(output.out, containsString("ab"));
		assertThat(output.out, containsString("foo"));
		assertThat(output.out, containsString("ba"));
		assertThat(output.out, containsString("OOF"));
		assertThat(output.out, not(containsString("nope")));
	}

	@Test
	public void main_sortsMostWordsLast() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("word-count.txt").getPath(),
				"--words-asc"
		}));

		String[] lines = output.out.split("\n");
		assertThat(lines[0], containsString("foo"));
		assertThat(lines[1], containsString("abc"));
	}

	@Test
	public void main_sortsMostWordsFirst() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("word-count.txt").getPath(),
				"--words-desc"
		}));

		String[] lines = output.out.split("\n");
		assertThat(lines[0], containsString("abc"));
		assertThat(lines[1], containsString("foo"));
	}

	@Test
	public void main_sortsLongestWordsLast() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("word-length.txt").getPath(),
				"--length-asc"
		}));

		String[] lines = output.out.split("\n");
		assertThat(lines[0], containsString("ab"));
		assertThat(lines[1], containsString("abc"));
	}

	@Test
	public void main_sortsLongestWordsFirst() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("word-length.txt").getPath(),
				"--length-desc"
		}));

		String[] lines = output.out.split("\n");
		assertThat(lines[0], containsString("abc"));
		assertThat(lines[1], containsString("ab"));
	}

	@Test
	public void main_reportsAnagramsOfTheGivenWordIfSpecified() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("simple.txt").getPath(),
				"abr"
		}));

		assertThat(output.out, equalTo("bar\n"));
		assertThat(output.err, equalTo(""));
	}

	@Test
	public void main_reportsNothingIfTheGivenWordHasNoAnagrams() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("simple.txt").getPath(),
				"no"
		}));

		assertThat(output.out, equalTo(""));
		assertThat(output.err, equalTo(""));
	}

	@Test
	public void main_reportsMultiWordAnagramsOfTheGivenWordIfSpecified() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("simple.txt").getPath(),
				"fabroo"
		}));

		List<String> lines = asList(output.out.split("\n"));
		assertThat(lines, hasItem(anyOf(equalTo("foo bar"), equalTo("bar foo"))));
		assertThat(lines, hasItem(anyOf(equalTo("oof bar"), equalTo("bar oof"))));
	}

	@Test
	public void main_reportsLongestWordsWithinASource() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				getResource("simple.txt").getPath(),
				"azbrz",
				"--length-desc"
		}));

		assertThat(output.out, equalTo("bar\n"));
		assertThat(output.err, equalTo(""));
	}

	@Test
	public void main_reportsAnErrorIfTheWordListIsNotFound() {
		Output output = getOutputFrom(() -> Main.main(new String[]{"nope"}));

		assertThat(output.out, equalTo(""));
		assertThat(output.err, equalTo(
				"Failed to load word list from nope\n"
		));
	}

	@Test
	public void main_printsUsageInformationIfNoInputGiven() {
		Output output = getOutputFrom(() -> Main.main(new String[] {}));

		assertThat(output.out, equalTo(""));
		assertThat(output.err, containsString("Usage:"));
	}

	@Test
	public void main_printsFormulaWhenCalledWithNumbers() {
		Output output = getOutputFrom(() -> Main.main(new String[]{
				"2",
				"3",
				"5"
		}));

		assertThat(output.out, equalTo(
				"3 + 2 = 5\n" +
				"= 5\n"
		));
		assertThat(output.err, equalTo(""));
	}
}
