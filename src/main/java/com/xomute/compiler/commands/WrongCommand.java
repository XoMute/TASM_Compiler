package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;

public class WrongCommand implements Command {
	@Override
	public String getCode() {
		return "ERROR: Wrong command";
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public void updateCode() {

	}
}
