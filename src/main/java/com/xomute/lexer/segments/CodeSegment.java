package com.xomute.lexer.segments;

import com.xomute.lexer.lexems.Identifier;
import com.xomute.lexer.lexems.Label;
import com.xomute.utils.StringConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodeSegment implements Segment{

	List<Identifier> userIdentifiers = new ArrayList<>();

	private String name;

	private List<Label> labels = new ArrayList<>();

	public CodeSegment(String name) {
		this.name = name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addIdentifier(Identifier identifier) {
		userIdentifiers.add(identifier);
	}

	@Override
	public void addLabel(Label label) {
		labels.add(label);
	}

	@Override
	public String getLabelOffset(String labelName) {
		for (Label label : labels) {
			if (label.getName().equals(labelName)) {
				return label.getOffset();
			}
		}
		return StringConstants.NO_SUCH_LABEL;
	}

	@Override
	public boolean hasIdentifier(String identifierName) {
		for(Identifier identifier : userIdentifiers) {
			if (identifier.getName().equals(identifierName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Optional<Identifier> getIdentifier(String identifierName) {
		if (!hasIdentifier(identifierName)) {
			return Optional.empty();
		}

		for(Identifier identifier : userIdentifiers) {
			if (identifier.getName().equals(identifierName)) {
				return Optional.of(identifier);
			}
		}
		return Optional.empty();
	}
}
