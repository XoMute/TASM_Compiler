package com.xomute.lexer;

import com.xomute.utils.FileScanner;
import com.xomute.utils.StringConstants;
import com.xomute.utils.StringUtils;

import java.util.stream.Collectors;

public class Lexer {

	public void scan(String filename) {
		FileScanner scanner = new FileScanner();
		scanner.readFile(filename).stream().map(this::scanOneLine);
	}

	public String scanOneLine(String line) {
		StringBuilder parsedLine = new StringBuilder(line);
		StringUtils.split(line).stream().map(this::scanOneWord).collect(Collectors.toList());
		return parsedLine.toString();
	}

	public String scanOneWord(String word) {

	}
}
