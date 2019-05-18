package com.xomute.lexer.lexems;

public class Label {

	private String label;

	public Label(String line) {
		label = line.replace(":", "");
	}
}
