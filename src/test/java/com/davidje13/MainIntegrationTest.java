package com.davidje13;

import org.junit.Test;

import static com.davidje13.testutil.IntegrationTestUtils.getStdOutFrom;
import static com.davidje13.testutil.IntegrationTestUtils.setStdInContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class MainIntegrationTest {
	@Test
	public void main_reportsCollectionsOfWordsWhichAreAnagrams() {
		String input = "foo\nbar\noof\n";

		setStdInContent(input);
		String out = getStdOutFrom(this::runWithArguments);

		assertThat(out, anyOf(
				equalTo("foo oof\n"),
				equalTo("oof foo\n")
		));
	}

	@Test
	public void main_reportsMultipleCollections() {
		String input = "ab\nfoo\nba\nOOF\nnope\n";

		setStdInContent(input);
		String out = getStdOutFrom(this::runWithArguments);

		assertThat(out, containsString("ab"));
		assertThat(out, containsString("foo"));
		assertThat(out, containsString("ba"));
		assertThat(out, containsString("OOF"));
		assertThat(out, not(containsString("nope")));
	}

	@Test
	public void main_sortsMostWordsLast() {
		String input = "abc\nbac\ncab\nfoo\noof\n";

		setStdInContent(input);
		String out = getStdOutFrom(() -> runWithArguments("--words-asc"));

		String[] lines = out.split("\n");
		assertThat(lines[0], containsString("foo"));
		assertThat(lines[1], containsString("abc"));
	}

	@Test
	public void main_sortsMostWordsFirst() {
		String input = "abc\nbac\ncab\nfoo\noof\n";

		setStdInContent(input);
		String out = getStdOutFrom(() -> runWithArguments("--words-desc"));

		String[] lines = out.split("\n");
		assertThat(lines[0], containsString("abc"));
		assertThat(lines[1], containsString("foo"));
	}

	@Test
	public void main_sortsLongestWordsLast() {
		String input = "abc\ncba\nab\nba\n";

		setStdInContent(input);
		String out = getStdOutFrom(() -> runWithArguments("--length-asc"));

		String[] lines = out.split("\n");
		assertThat(lines[0], containsString("ab"));
		assertThat(lines[1], containsString("abc"));
	}

	@Test
	public void main_sortsLongestWordsFirst() {
		String input = "abc\ncba\nab\nba\n";

		setStdInContent(input);
		String out = getStdOutFrom(() -> runWithArguments("--length-desc"));

		String[] lines = out.split("\n");
		assertThat(lines[0], containsString("abc"));
		assertThat(lines[1], containsString("ab"));
	}

	private void runWithArguments(String... args) {
		Main.main(args);
	}
}
