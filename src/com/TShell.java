package com;

public class TShell {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Reader reader = new Reader();
		Controller control = new Controller();
		
		while(reader.hasParam())
			control.process(reader.getParams());
		
		System.out.println("\u001B31;1mhello world!");

	}

}


class TData{
	private static int index = 0;
	
	public TData(){
		index++;
		if(index > 9)
			index = 0;
	}
}

class Reader{
	public boolean hasParam(){
		return false;
	}
	public TData[] getParams(){
		return null;
	}
}

class Controller{
	public void process(TData data[]){
		
	}
}
