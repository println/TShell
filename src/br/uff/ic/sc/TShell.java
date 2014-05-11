package br.uff.ic.sc;
/**
 * Classe inicial TShell
 * @author Felipe
 *
 */
public class TShell {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * Instancia o leitor
		 */
		Reader reader = new Reader();
		
		/**
		 * Instancia o controlador dos threads
		 */
		Controller control = new Controller();
		
		/**
		 * loop para manter a aplicacao interagindo com o usuario
		 * "enter" sem entrar dados para sair
		 */
		while (reader.hasParam())
			control.process(reader.getProfile());
		
	}

}