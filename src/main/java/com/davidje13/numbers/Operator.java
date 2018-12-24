package com.davidje13.numbers;

interface Operator {
	String getName();

	boolean supports(int a, int b);

	int apply(int a, int b);
}
