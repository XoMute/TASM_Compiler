package com.xomute.lexer;

import com.xomute.utils.StringUtils;

import java.util.List;

public class SourceLine {

	private String line;

	public SourceLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public List<String> getLexems() {
		return StringUtils.split(line);
	}
}
