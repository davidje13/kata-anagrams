package com.davidje13;

import com.davidje13.OptionParser.Options;
import com.davidje13.anagram.AnagramFinder;
import com.davidje13.permutation.PermutationExpander;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
	public static void main(String[] args) {
		Options options = new OptionParser().parseOptions(args);

		String wordListFile = options.getWordListFile();
		if (wordListFile == null) {
			showUsage();
			return;
		}

		AnagramFinder finder = new AnagramFinder();
		PermutationExpander expander = new PermutationExpander();

		try {
			finder.loadWords(
					Files.lines(new File(wordListFile).toPath(), UTF_8),
					1048576
			);
		} catch (IOException e) {
			System.err.println("Failed to load word list from " + wordListFile);
			return;
		}

		if (options.getWords().isEmpty()) {
			finder.findAnagrams()
					.sorted(options.getSortOrder())
					.map((group) -> String.join(" ", group))
					.forEach(System.out::println);
		} else {
			for (String word : options.getWords()) {
				finder.findMultiWordAnagrams(word, 3)
						.flatMap(expander::expand)
						.sorted(options.getSortOrder())
						.map((group) -> String.join(" ", group))
						.forEach(System.out::println);
			}
		}
	}

	private static void showUsage() {
		System.err.println("Finds anagrams in a given dictionary.");
		System.err.println();
		System.err.println("Usage:");
		System.err.println("  ./program <path_to_word_list>");
		System.err.println("  ./program <path_to_word_list> <word>");
	}
}
