package com.davidje13.testutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class IntegrationTestUtils {
	public static void setStdInContent(String input) {
		System.setIn(new ByteArrayInputStream(input.getBytes()));
	}

	public static String getStdOutFrom(Runnable runnable) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream stdout = System.out;

		try {
			System.setOut(new PrintStream(out));
			runnable.run();
			return out.toString();
		} finally {
			System.setOut(stdout);
		}
	}
}
