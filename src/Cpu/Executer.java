package cpu;

import java.util.ArrayList;

import Structures.*;

public class Executer {
	DecoderResponse decoderResponse;
	ExecuterResponse executerResponse;
	private int number = 1;

	public Executer() {
		super();

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ExecuterResponse execute(DecoderResponse resp, int pc) {
		System.out.println("Executing instruction Number : " + (number));
		if (resp.getInstructionType().equals(InstructionType.rType)) {

			return execRType(resp);
		}
		if (resp.getInstructionType().equals(InstructionType.iType)) {

			return execIType(resp, pc);
		}

		if (resp.getInstructionType().equals(InstructionType.jType)) {

			return execJType(resp, pc);
		}

		return null;
	}

	public ExecuterResponse execRType(DecoderResponse resp) {

		int firstSourceValue = resp.getFirstSourceValue();
		int secSourceValue = resp.getSecondSourceValue();
		int dest = resp.getDestinationRegisterNumber();
		int answer;

		if (resp.getOperation().equals(Operation.ADD)) {
			answer = firstSourceValue + secSourceValue;
			System.out.println("Adding first operand " + firstSourceValue + " + second operand " + secSourceValue);
			System.out.println();

			executerResponse = new ExecuterResponse(dest, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.SUB)) {
			answer = firstSourceValue - secSourceValue;
			System.out.println("Subtracting first operand " + firstSourceValue + " + second operand " + secSourceValue);
			System.out.println();

			executerResponse = new ExecuterResponse(dest, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.SLL)) {
//			System.out.println("ana henaaaa");
			int shamt = resp.getShamt();
			answer = firstSourceValue << shamt;
			System.out.println("Shifting first operand to the left logical " + firstSourceValue + " by " + shamt);
			System.out.println();

			executerResponse = new ExecuterResponse(dest, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.SRL)) {
			int shamt = resp.getShamt();
			answer = firstSourceValue >>> shamt;
			System.out.println("Shifting first operand to the right logical " + firstSourceValue + " by " + shamt);
			System.out.println();
			executerResponse = new ExecuterResponse(dest, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		return null;

	}

	private ExecuterResponse execJType(DecoderResponse resp, int pc) {
		int address = resp.getAddress();
		String binaryStringAddress = Integer.toBinaryString(address);
		int newPc = (pc - 1) & 0b11110000000000000000000000000000;
		String binaryStringPc = Integer.toBinaryString(newPc);
		String StringfinalPc = binaryStringPc + binaryStringAddress;
		int integerFinalPc = Integer.parseInt(StringfinalPc, 2);
		System.out.println("Jumping to PC " + integerFinalPc);
		System.out.println();
		executerResponse = new ExecuterResponse(1000, integerFinalPc, resp.getOperation());// 1000 dih dummy value
		executerResponse.setNewPc(integerFinalPc);
		executerResponse.setInstrunumber(number);
		executerResponse.setDoMemory(false);// mesh haye3mel haga fel memory
		executerResponse.setDoWriteBack(false);
		executerResponse.setJumpOrBranch(true);// mesh haye3mel haga fel writeback
		number = number + 1;
		return executerResponse;
	}

	private ExecuterResponse execIType(DecoderResponse resp, int pc) {
		int firstSourceValue = resp.getFirstSourceValue();
		int immValue = resp.getSecondSourceValue();
		int destName = resp.getDestinationRegisterNumber();// r5

		if (resp.getOperation().equals(Operation.ADDI)) {
			int answer = firstSourceValue + immValue;
			System.out.println("Adding first operand " + firstSourceValue + " + second operand " + immValue);
			System.out.println();

			executerResponse = new ExecuterResponse(destName, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.MULI)) {
			int answer = firstSourceValue * immValue;
			System.out.println("Multiplying first operand " + firstSourceValue + " * second operand " + immValue);
			System.out.println();

			executerResponse = new ExecuterResponse(destName, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.ANDI)) {
			int answer = firstSourceValue & immValue;
			System.out.println("Bitwise Anding first operand " + firstSourceValue + " & second operand " + immValue);
			System.out.println();

			executerResponse = new ExecuterResponse(destName, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.ORI)) {
			int answer = firstSourceValue | immValue;
			System.out.println("Bitwise Anding first operand " + firstSourceValue + " | second operand " + immValue);
			System.out.println();
			executerResponse = new ExecuterResponse(destName, answer, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}
		if (resp.getOperation().equals(Operation.BNE)) { // number wala pc law number hashil el fetcher men hena
			if (resp.getDestinationRegisterValue() != firstSourceValue) {
				int newPc = pc + immValue;
				System.out.println("Branching to PC " + newPc);
				System.out.println();
				executerResponse = new ExecuterResponse(99, 99, resp.getOperation());
				executerResponse.setNewPc(newPc);
				executerResponse.setInstrunumber(number);

				executerResponse.setDoWriteBack(false);// mesh hate3mel haga fel write back
				executerResponse.setDoMemory(false);
				executerResponse.setJumpOrBranch(true);
				number = number + 1;
				return executerResponse;
			} else {
				System.out.println("Condition not satisfied, didn't branch ");
				System.out.println();
				executerResponse = new ExecuterResponse(99, 99, resp.getOperation());
				executerResponse.setDoWriteBack(false);// mesh hate3mel haga fel write back
				executerResponse.setDoMemory(false);
				executerResponse.setJumpOrBranch(false);
				executerResponse.setInstrunumber(number);
				number = number + 1;
				return executerResponse;
			}

		}
		if (resp.getOperation().equals(Operation.SW)) { // 3ayza a7ot el fe register value fe makan el index fel memory
			int index = firstSourceValue + immValue;
			System.out.println("Memory Address " + index + " to store word in ");
			System.out.println();
			executerResponse = new ExecuterResponse(index, resp.getDestinationRegisterValue(), resp.getOperation());
			executerResponse.setDoWriteBack(false);
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;

		}
		if (resp.getOperation().equals(Operation.LW)) {// 3ayza a7ot el fe el index fel memory fel register
			int index = firstSourceValue + immValue;
			System.out.println("Memory Address " + index + "   to load from ");
			System.out.println();
			executerResponse = new ExecuterResponse(destName, index, resp.getOperation());
			executerResponse.setInstrunumber(number);
			number = number + 1;
			return executerResponse;
		}

		return null;

	}

}