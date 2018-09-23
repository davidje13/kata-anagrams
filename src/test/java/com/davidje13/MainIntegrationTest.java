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
		String out = getStdOutFrom(() -> Main.main(new String[] {}));

		assertThat(out, anyOf(
				equalTo("foo oof\n"),
				equalTo("oof foo\n")
		));
	}

	@Test
	public void main_reportsMultipleCollections() {
		String input = "ab\nfoo\nba\nOOF\nnope\n";

		setStdInContent(input);
		String out = getStdOutFrom(() -> Main.main(new String[] {}));

		assertThat(out, containsString("ab"));
		assertThat(out, containsString("foo"));
		assertThat(out, containsString("ba"));
		assertThat(out, containsString("OOF"));
		assertThat(out, not(containsString("nope")));
	}
}
