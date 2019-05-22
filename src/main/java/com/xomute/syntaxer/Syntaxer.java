package com.xomute.syntaxer;

import com.sun.istack.internal.Nullable;
import com.xomute.lexer.SourceLine;

import java.util.ArrayList;
import java.util.List;

public class Syntaxer {

	private List<SourceLine> linesOfSrc;

	public Syntaxer() {
		this.linesOfSrc = new ArrayList<>();
	}

	public Syntaxer(List<SourceLine> linesOfSrc) {
		this.linesOfSrc = new ArrayList<>(linesOfSrc);
	}

	/////////////////// PRINTING PART ////////////////

	/**
	 * can pass null as parameter. In this case syntaxer will work with internal list of SourceLines
	 * @param lines
	 */
	public void print(@Nullable List<SourceLine> lines) {
		if (lines != null) {
			this.linesOfSrc = new ArrayList<>(lines);
		}

		for (SourceLine line : linesOfSrc) {
			printLine(line);
		}

	}

	public void printLine(SourceLine line) {
    System.out.println("Синтаксичний аналіз:");
    System.out.println("  (Поле міток/імені) Номер лексеми поля: " + line.getNameIndex());
    System.out.println("  (Поле мнемокоду) Номер першої лексеми: " + line.getMnemocodeIndex());
    System.out.println("  (Поле мнемокоду) Кількість лексем поля: " + line.getMnemocodeLexemNumber());
    System.out.println("  (1-й операнд) Номер першої лексеми операнда: " + line.getFirstOperandIndex());
    System.out.println("  (1-й операнд) Кількість лексем операнда: " + line.getFirstOperandLexemNumber());
    System.out.println("  (1-й операнд) Номер першох лексеми операнда: " + line.getSecondOperandIndex());
    System.out.println("  (2-й операнд) Кількість лексем операнда: " + line.getSecondOperandLexemNumber());
    System.out.println();
	}
}
