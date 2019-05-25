package com.xomute.lexer.lexems;

public class Identifier {

	private String name = "";
	private int offset;
	private boolean hex;

	public Identifier() {}

	public Identifier(String name, int offset, boolean hex) {
		this.name = name;
		this.offset = offset;
		this.hex = hex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isHex() {
		return hex;
	}

	public void setHex(boolean hex) {
		this.hex = hex;
	}
}
