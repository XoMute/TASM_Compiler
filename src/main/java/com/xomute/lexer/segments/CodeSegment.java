package com.xomute.lexer.segments;

import com.xomute.lexer.lexems.Label;
import com.xomute.utils.StringConstants;

import java.util.ArrayList;
import java.util.List;

public class CodeSegment implements Segment{

	List<String> USER_IDENTIFIERS = new ArrayList<>();

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
	public void addIdentifier(String identifier) {
		USER_IDENTIFIERS.add(identifier);
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
}
