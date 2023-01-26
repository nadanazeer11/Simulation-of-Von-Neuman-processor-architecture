package Structures;

public class Register{
	private int name;
	private int data;

	public Register(int i,String Name) {
		this.name = name;
		this.data = 0;
	}

	public int getName() {
		return name;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public void setName(int name) {
		this.name = name;
	}
	
}
