package com.xomute.lexer.segments;

public interface Segment {

	void setName(String name);

	String getName();

	void addIdentifier(String identifier);
}
