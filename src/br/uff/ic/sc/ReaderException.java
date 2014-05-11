package br.uff.ic.sc;

/**
 * Classe para a excessao no Reader
 * @author Felipe
 *
 */
class ReaderException extends Exception {
	private static final long serialVersionUID = 1893885987757324659L;

	public ReaderException(String message) {
		super(message);
	}
}