package Structures;

public class MemoryHandlerResponse {
	int destRegisterNumber = 0;
	int value=0;
	boolean doWriteBack = true;
	
	public boolean isDoWriteBack() {
		return doWriteBack;
	}
	public void setDoWriteBack(boolean doWriteBack) {
		this.doWriteBack = doWriteBack;
	}
	public int getDestRegisterNumber() {
		return destRegisterNumber;
	}
	public void setDestRegisterNumber(int destRegisterNumber) {
		this.destRegisterNumber = destRegisterNumber;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
