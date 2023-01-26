package Structures;

public class ParserResponse {
	int instruction = 0;
	InstructionType instructionType;
	int returned;


	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public InstructionType getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(InstructionType instructionType) {
		this.instructionType = instructionType;
	}

}
