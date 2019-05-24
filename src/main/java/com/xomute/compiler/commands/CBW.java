package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;

public class CBW implements Command {

	private String code = "98";
	private int offset = 1;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	@Deprecated
	public void updateCode() {

	}
}
