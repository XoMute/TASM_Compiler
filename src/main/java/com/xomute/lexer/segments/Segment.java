package com.xomute.lexer.segments;

import com.xomute.lexer.lexems.Label;

public interface Segment {

	void setName(String name);

	String getName();

	void addIdentifier(String identifier);

	void addLabel(Label label);

	String getLabelOffset(String labelName);
}
