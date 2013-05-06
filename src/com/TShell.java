package com;

public class TShell {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Reader reader = new Reader();
		Controller control = new Controller();

		while (reader.hasParam())
			control.process(reader.getProfile());
		
	}

}