package com;

public class TShell {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TReader reader = new TReader();
		TControl control = new TControl();
		
		while(reader.hasParam())
			control.process(reader.getParams());
		
		System.out.println("\u001B31;1mhello world!");

	}

}


class TTaskData{
	private static int index = 0;
	
	public TTaskData(){
		index++;
		if(index > 9)
			index = 0;
	}
}

class TReader{
	public boolean hasParam(){
		return false;
	}
	public TTaskData[] getParams(){
		return null;
	}
}

class TControl{
	public void process(TTaskData data[]){
		
	}
}
