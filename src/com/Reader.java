package com;

import java.util.Scanner;

class Reader {
	private Profile profile = new Profile();

	public boolean hasParam() {
		try {
			this.askCommands();
			this.askLoop();
			this.askMode();
			this.askTimeout();
		} catch (ReaderException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public Profile getProfile() {
		return this.profile;
	}

	private void askCommands() throws ReaderException {
		this.profile.setCommand(this.takeUserInstruction("comando(s)"));
		if (!this.profile.isValid())
			throw new ReaderException("Aplicação finalizada!");
			
	}

	private void askLoop() {
		if (!this.profile.isMulti()) {
			try {
				this.profile.setLoop(Integer.parseInt(takeUserInstruction("loopes")));
			} catch (NumberFormatException e) {
				System.out.println("Somente numeros!");
				this.askLoop();
			}
		}
	}

	private void askMode() {
		if (this.profile.isMulti()) {
			String input = takeUserInstruction("[P]aralelo ou [S]equencial");
			if (input.toUpperCase().charAt(0) == 'P')
				this.profile.setMode(true);
		}
	}

	private void askTimeout() {
		try {
			this.profile.setTimeout(Integer.parseInt(takeUserInstruction("timeout")));
		} catch (NumberFormatException e) {
			System.out.println("Somente numeros!");
			this.askTimeout();
		}
	}

	private String takeUserInstruction(String label) {
		System.out.print(label + "> ");
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}

}
