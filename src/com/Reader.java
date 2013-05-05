package com;

import java.util.Scanner;

class Reader {
	private Profile profile = new Profile();

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

	public Profile getProfile() {
		return this.profile;
	}

	private void getCommands() throws ReaderException {
		String cmd = collector("comando(s)");
		if (cmd.length() == 0)
			throw new ReaderException("não foi possivel ler comandos");
		else
			this.profile.setCommand(cmd);
	}

	private void takeLoop() {
		if (!profile.isMulti()) {
			try {
				this.profile.setLoop(Integer.parseInt(collector("loopes")));
			} catch (NumberFormatException e) {
				System.out.println("Somente números!");
				this.takeLoop();
			}
		}
	}

	private void getMode() {
		String input = collector("[P]aralelo ou [S]equencial");
		if (input.toUpperCase().charAt(0) == 'P')
			this.profile.setMode(true);
	}

	private void setTimeout() {
		try {
			this.profile.setTimeout(Integer.parseInt(collector("timeout")));
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

}
