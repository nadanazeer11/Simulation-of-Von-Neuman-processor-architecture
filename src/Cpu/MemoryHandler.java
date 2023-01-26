package cpu;

import Structures.*;
import memory.Memory;

public class MemoryHandler {

	private Memory mem;
	private int number = 1;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public MemoryHandler(Memory mem) {
		super();
		this.mem = mem;
	}

	public int loadWord(int address) {

		int x = mem.getData(address);
		return x;

	}

	public void storeWord(int data, int address) {
		mem.setData(address, data);
	}

	public MemoryHandlerResponse handle(ExecuterResponse response) {
		System.out.println("Executing instruction Number : " + (number) + " in Memory");
		MemoryHandlerResponse resp = new MemoryHandlerResponse();
		if (response.getOperation().equals(Operation.LW)) {

			resp.setDestRegisterNumber(response.getDestReg());
			int loadedIndex = response.getResult();
			System.out.println("Loading Word from  " + loadedIndex);
			System.out.println();
			int loadedValue = loadWord(loadedIndex);

			resp.setValue(loadedValue);
			resp.setDoWriteBack(response.isDoWriteBack());

		} else if (response.getOperation().equals(Operation.SW)) {

			storeWord(response.getResult(), response.getDestReg());
			System.out.println(
					"Storing word from  " + response.getDestReg() + " in memory address " + response.getResult());
			System.out.println();
			resp.setDestRegisterNumber(99);// dummy values
			resp.setValue(99);
			resp.setDoWriteBack(response.isDoWriteBack());
			// dummy values no need to enter writeback
		} else {
			resp.setDestRegisterNumber(response.getDestReg());
			resp.setValue(response.getResult());
			resp.setDoWriteBack(response.isDoWriteBack());

		}
		number = number + 1;
		return resp;
	}
}
