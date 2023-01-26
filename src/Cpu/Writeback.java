package cpu;

import Structures.MemoryHandlerResponse;
import memory.RegisterFile;

public class Writeback {
	RegisterFile reg;
	private int number = 1;

	public Writeback(RegisterFile reg) {
		super();
		this.reg = reg;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void writeBack(MemoryHandlerResponse response) {
		// System.out.println(response.isDoWriteBack());
		if (response.isDoWriteBack() == true) {
			System.out.println("WritingBack Instruction Number : " + (number));

			int registerName = response.getDestRegisterNumber();
			int value = response.getValue();

			reg.setValueinRegister(registerName, value);
			if (registerName == 0) {
				System.out.println("Register number " + registerName + " is not updated to " + value);
				System.out.println();

			} else {
				System.out.println("Register number " + registerName + " updated to " + value);
				System.out.println();
			}

			number = number + 1;
		} else {
			System.out.println("WritingBack Instruction Number : " + (number));
			System.out.println();
			response.setDoWriteBack(true);
			number = number + 1;
		}

	}

}
