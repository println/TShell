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
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public Profile getProfile() {
		return this.profile;
	}

	private void getCommands() throws ReaderException {
		this.profile.setCommand(this.getUserInstruction("comando(s)"));
		if (!this.profile.isValid())
			throw new ReaderException("Aplicação finalizada!");
			
	}

	private void takeLoop() {
		if (!this.profile.isMulti()) {
			try {
				this.profile.setLoop(Integer.parseInt(getUserInstruction("loopes")));
			} catch (NumberFormatException e) {
				System.out.println("Somente numeros!");
				this.takeLoop();
			}
		}
	}

	private void getMode() {
		if (this.profile.isMulti()) {
			String input = getUserInstruction("[P]aralelo ou [S]equencial");
			if (input.toUpperCase().charAt(0) == 'P')
				this.profile.setMode(true);
		}
	}

	private void setTimeout() {
		try {
			this.profile.setTimeout(Integer.parseInt(getUserInstruction("timeout")));
		} catch (NumberFormatException e) {
			System.out.println("Somente numeros!");
			this.setTimeout();
		}
	}

	private String getUserInstruction(String label) {
		System.out.print(label + "> ");
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}

}
