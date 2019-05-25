package com.xomute.lexer.segments;

import com.xomute.lexer.lexems.Identifier;
import com.xomute.lexer.lexems.Label;

import java.util.Optional;

public interface Segment {

	void setName(String name);

	String getName();

	void addIdentifier(Identifier identifier);

	boolean hasIdentifier(String identifierName);

	Optional<Identifier> getIdentifier(String identifierName);

	void addLabel(Label label);

	String getLabelOffset(String labelName);
}
