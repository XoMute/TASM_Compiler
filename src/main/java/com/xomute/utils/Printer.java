package com.xomute.utils;

import com.xomute.lexer.Lexer;
import com.xomute.lexer.SourceLine;
import com.xomute.syntaxer.Syntaxer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Printer {

	private Lexer lexer;
	private Syntaxer syntaxer;

	public Printer() {
		this.lexer = new Lexer();
		this.syntaxer = new Syntaxer();
	}

	public void doTheJob(String filename) {
		FileScanner scanner = new FileScanner();
		scanner.readFile(filename).stream()
				.filter(line -> !line.isEmpty())
				.forEach(
						line -> {
							List<SourceLine> srcLines = lexer.scanOneLineForPrint(line);
							srcLines.forEach(srcLine -> {
								lexer.printLine(srcLine);
								syntaxer.print(Collections.singletonList(srcLine));
							});
							srcLines = lexer.scanOneLine(line);
							lexer.getLinesOfSrc().addAll(srcLines);
						});
	}
}
