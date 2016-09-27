package a6;

public abstract class ChessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9074733751004568193L;

	protected ChessException(String message) {
		super(message);
	}
}

class IllegalMove extends ChessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7924515212946695380L;

	public IllegalMove(ChessPiece p, ChessPosition from, ChessPosition to) {
		super("Illegal move: piece " + p.toString() + " can not move from " + from.toString() + " to " + to.toString());
	}
}