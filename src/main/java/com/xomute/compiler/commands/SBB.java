package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;

@Deprecated
public class SBB implements Command {
	private String code = "";
	private int offset;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void updateCode() {
		// todo: implement
	}
}
