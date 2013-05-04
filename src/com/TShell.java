package com;

import java.util.*;

public class TShell {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Reader reader = new Reader();
		Controller control = new Controller();

		while (reader.hasParam())
			control.process(reader.getParams());

	}

}

class TData {
	private String command;
	private int loop;
	private int timeout;
	private boolean mode;

	private static int index = 0;

	public TData(String command, int loop, boolean mode, int timeout) {
		this.command = command;
		this.loop = loop;
		this.mode = mode;
		this.timeout = timeout;

		index++;
		if (index > 9)
			index = 0;
	}
}

class Reader {
	private String command;
	private int loop;
	private int timeout;
	private boolean mode;

	public boolean hasParam() {
		try {
			this.getCommands();
			this.takeLoop();
			this.getMode();
			this.setTimeout();
		} catch (ReaderException e) {
			System.out.print("bug do milenio");
			return false;
		}
		return true;
	}

	public TData[] getParams() {
		String[] cmds = this.command.split(";");

		TData[] data = new TData[cmds.length];

		for (int i = 0; i < cmds.length; i++) 
			data[i] = new TData(cmds[i], this.loop, this.mode, this.timeout);

		this.resetValues();
		return data;
	}

	private void getCommands() throws ReaderException {
		this.command = collector("comando(s)");
		if (this.command.length() == 0)
			throw new ReaderException("não foi possivel ler comandos");
	}

	private void takeLoop() {
		try {
			this.loop = Integer.parseInt(collector("loopes"));
		} catch (NumberFormatException e) {
			System.out.println("Somente números!");
			this.takeLoop();
		}
	}

	private void getMode() {
		String input = collector("[P]aralelo ou [S]equencial");
		if (input.toUpperCase().charAt(0) == 'P')
			this.mode = true;
	}

	private void setTimeout() {
		try {
			this.timeout = Integer.parseInt(collector("timeout"));
		} catch (NumberFormatException e) {
			System.out.println("Somente números!");
			this.setTimeout();
		}
	}

	private String collector(String label) {
		System.out.print(label + "> ");
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}

	private void resetValues() {
		this.command = null;
		this.loop = 0;
		this.timeout = 0;
		this.mode = false;
	}
}

class ReaderException extends Exception {
	private static final long serialVersionUID = 1893885987757324659L;

	public ReaderException(String message) {
		super(message);
	}
}

class Controller {
	public void process(TData data[]) {

	}
}
