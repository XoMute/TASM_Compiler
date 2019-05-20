package com.xomute.utils;

import com.xomute.lexer.Lexer;
import com.xomute.syntaxer.Syntaxer;

public class Printer {

	private Lexer lexer;
	private Syntaxer syntaxer;

	public Printer(Lexer lexer, Syntaxer syntaxer) {
		this.lexer = lexer;
		this.syntaxer = syntaxer;
	}

	public void printAll() {
		lexer.getLinesOfSrc().forEach(srcLine -> {
			lexer.printLine(srcLine);
			syntaxer.printLine(srcLine);
		});
	}
}
