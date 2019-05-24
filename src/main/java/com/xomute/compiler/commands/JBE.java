package com.xomute.compiler.commands;

import com.xomute.compiler.Compiler;
import com.xomute.compiler.interfaces.Command;
import com.xomute.compiler.utils.CompilerUtils;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringConstants;
import com.xomute.utils.StringUtils;

public class JBE implements Command {
	private String code = "";
	private int offset;

	private String label;

	public JBE(String label) {
		this.label = label;
		code = "76 ";
		if (CompilerUtils.isJumpBack(label)) {
			String offset = CompilerUtils.getOffset(Compiler.getOffset());
			String sum = AssemblerHelper.addHexToHex(offset, CompilerUtils.getLabelOffset(label));
			code += StringUtils.beautifyCode(AssemblerHelper.subHexFromHex("FE", sum) + " ", 2); // FF - (jbeOff + labelOff) - 1
			this.offset = 2;
		} else {
			code = StringConstants.SECOND_GT;
			this.offset = 4;
		}
	}

	@Override
	public void updateCode() { // in case of forward jump
		code = "76 ";
		String offset = CompilerUtils.getOffset(Compiler.getOffset());
		String diff = AssemblerHelper.subHexFromHex(CompilerUtils.getLabelOffset(label), offset);
		diff = StringUtils.beautifyCode(AssemblerHelper.subHexFromHex(diff, "2"), 2);
		code += diff;
		code += " 90 90";
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}
}
