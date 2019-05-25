package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.lexer.lexems.Identifier;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringUtils;

public class LSS implements Command {
	private String code = "";
	private int offset;

	private String operand1;
	private String operand2;

	public LSS(String operand1, String operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		code = AssemblerHelper.getPrefix(operand2);
		if (!code.isEmpty()) {
			offset++;
		}
		code += "0F B2 ";
		offset += 2;

		code += AssemblerHelper.getModRM(operand1, operand2) + " ";
		code +=
				StringUtils.beautifyCode(
						Integer.toHexString(
								AssemblerHelper.getIdentifier(
										AssemblerHelper.getIdentifierNameFromOperand(operand2))
										.map(Identifier::getOffset)
										.orElse(0)), 4) + "r ";

		offset += 3;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void updateCode() {}
}
