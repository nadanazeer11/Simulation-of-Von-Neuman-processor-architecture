package memory;

import java.sql.Array;

import javax.xml.crypto.Data;

public class Memory {
	private int[] memArray;

	public Memory() {
		super();
		this.memArray = new int[2048];
//		for (int i = 0; i < memArray.length; i++) {
//			this.memArray[i]=95;
//			
//		}
	}

	public int getData(int decAddress) {
		int dataInt = 0;
		if (decAddress >= 1024 && decAddress < 2048) {
			// System.out.println("dakhalt el get data");
			dataInt = this.memArray[decAddress];
			// System.out.println("hena " + dataInt);

		}
		return dataInt;

	}

	public void setData(int decAddress, int data) {
		if (decAddress >= 1024 && decAddress < 2048) {

			this.memArray[decAddress] = data;

		}

	}

	public int getInstruction(int decAddress) {
		int dataInt = 0;
		if (decAddress <= 1023) {

			dataInt = (memArray[decAddress]);

		}
		return dataInt;
	}

	public void setInstruction(int decAddress, int instruction) {
		if (decAddress <= 1023) {

			memArray[decAddress] = instruction;

		}

	}

	public int getsize() {
		return this.memArray.length;

	}

	public void printMemory() {
		for (int i = 0; i < this.memArray.length; i++) {
			System.out.println("Memory location : " + i + " " + this.memArray[i]);
		}
	}

}
