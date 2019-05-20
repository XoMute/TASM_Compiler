package com.xomute.lexer.segments;

import java.util.ArrayList;
import java.util.List;

public class DataSegment implements Segment{

	private List<String> USER_IDENTIFIERS = new ArrayList<>();
	private String name;

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

}
