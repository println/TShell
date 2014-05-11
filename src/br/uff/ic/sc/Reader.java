package br.uff.ic.sc;

import java.util.Scanner;

/**
 * Classe para realizar consultas ao usuario		
 * @author Felipe
 *
 */
class Reader {
	/**
	 * Atributo para manter um perfil de execucao dos comandos
	 */
	private Profile profile = new Profile();
	
	/**
	 * Metodo para realizar consulta ao usuário
	 * @return 
	 * verdadeiro se o usuario entrou com valor valido ou falso em caso de erro ou valor em branco
	 */
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
	
	/**
	 * Metodo para retornar o perfil de execução
	 * @return perfil
	 */
	public Profile getProfile() {
		return this.profile;
	}
	
	/**
	 * Consulta ao usuario para a entrada de comando
	 * Aceita um unico comando ou multiplos comandos, separados por um separador definido na classe Profile
	 * @throws ReaderException se entrada em branco
	 */
	private void askCommands() throws ReaderException {
		this.profile.setCommand(this.takeUserInstruction("comando(s)"));
		if (!this.profile.isValid())
			throw new ReaderException("Aplicação finalizada!");
			
	}
	
	/**
	 * Consulta ao usuario para a definicao de quantas vezes o comando ira se repetir
	 * Em caso de multiplos comandos, este metodo não ira consultar o usuario
	 */
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
	
	/**
	 * Consulta ao usuario como os comandos devem ser executados, sequenciais ou paralelos
	 * Esta consulta eh habilitada quando multiplos comandos devem ser executados
	 */
	private void askMode() {
		if (this.profile.isMulti()) {
			String input = takeUserInstruction("[P]aralelo ou [S]equencial");
			if (input.toUpperCase().charAt(0) == 'P')
				this.profile.setMode(true);
		}
	}
	
	/**
	 * Consulta ao usuario para a definir o tempo maximo de execução de um comando 
	 */
	private void askTimeout() {
		try {
			this.profile.setTimeout(Integer.parseInt(takeUserInstruction("timeout")));
		} catch (NumberFormatException e) {
			System.out.println("Somente numeros!");
			this.askTimeout();
		}
	}
	
	/**
	 * Metodo generico de consultas, utilizado por todos os metodos "ask"
	 * @param label, e a frase que o usuario deve ver
	 * @return o dado de entrada do usuario  
	 */
	private String takeUserInstruction(String label) {
		System.out.print(label + "> ");
		Scanner scan = new Scanner(System.in);
		String text = scan.nextLine();
		scan.close();
		return text;
	}

}
