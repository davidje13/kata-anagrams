package com.davidje13;

import com.davidje13.OptionParser.Options;
import com.davidje13.anagram.AnagramFinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
	public static void main(String[] args) {
		Options options = new OptionParser().parseOptions(args);

		String wordListFile = options.getWordListFile();
		if (wordListFile == null) {
			showUsage();
			return;
		}

		Stream<String> words;
		try {
			words = Files.lines(new File(wordListFile).toPath(), UTF_8);
		} catch (IOException e) {
			System.err.println("Failed to load word list from " + wordListFile);
			return;
		}

		AnagramFinder finder = new AnagramFinder();

		finder.findAnagrams(words, 1048576)
				.sorted(options.getSortOrder())
				.map((group) -> String.join(" ", group))
				.forEach(System.out::println);
	}

	private static void showUsage() {
		System.err.println("Finds anagrams in a given dictionary.");
		System.err.println();
		System.err.println("Usage:");
		System.err.println("  ./program <path_to_word_list>");
	}
}
