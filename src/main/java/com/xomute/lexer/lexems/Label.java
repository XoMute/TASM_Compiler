package com.xomute.lexer.lexems;

public class Label {

	private String name;
	private String offset;
	private String segment;

	public Label(String name, String offset, String segment) {
		this.name = name;
		this.offset = offset;
		this.segment = segment;
	}

	public String getName() {
		return name;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}
}
