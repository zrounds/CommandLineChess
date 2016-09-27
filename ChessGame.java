package a6;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
public class ChessGame extends Observable implements Observer{

	private ChessBoard board;
	private ChessPlayer player1;
	private ChessPlayer player2;	
	private ArrayList<ChessMove> log = new ArrayList<ChessMove>(0);
	
	public ChessGame(ChessPlayer player1, ChessPlayer player2) {
		this.player1 = player1;
		this.player2 = player2;
		board = new ChessBoard();
		
		new Rook(player1, this, new ChessPosition(0,0));
		new Knight(player1, this, new ChessPosition(1,0));
		new Bishop(player1, this, new ChessPosition(2,0));
		new Queen(player1, this, new ChessPosition(3,0));
		new King(player1, this, new ChessPosition(4,0));
		new Bishop(player1, this, new ChessPosition(5,0));
		new Knight(player1, this, new ChessPosition(6,0));
		new Rook(player1, this, new ChessPosition(7,0));

		for (int i=0; i<8; i++) {
			new Pawn(player1, this, new ChessPosition(i,1));
		}

		new Rook(player2, this, new ChessPosition(0,7));
		new Knight(player2, this, new ChessPosition(1,7));
		new Bishop(player2, this, new ChessPosition(2,7));
		new Queen(player2, this, new ChessPosition(3,7));
		new King(player2, this, new ChessPosition(4,7));
		new Bishop(player2, this, new ChessPosition(5,7));
		new Knight(player2, this, new ChessPosition(6,7));
		new Rook(player2, this, new ChessPosition(7,7));

		for (int i=0; i<8; i++) {
			new Pawn(player2, this, new ChessPosition(i,6));
		}		
	}
	
	public ChessPlayer getPlayer1() {
		return player1;
	}
	
	public ChessPlayer getPlayer2() {
		return player2;
	}

	public ChessBoard getBoard() {
		return board;
	}
	
	public int getLogSize(){
		return log.size();
	}
	
	public ChessMove[] getMoves(int num){
		int size = log.size();
		ChessMove[] moves = new ChessMove[size];
		if (num == 0 || Math.abs(num) >= size){
			for (int i=0; i<size; i++){
				moves[i] = log.get(i);
			}
		} else if (num > 0){
			moves = new ChessMove[num];
			for (int i=0; i<num; i++){
				moves[i] = log.get(i);
			}
		} else if (num < 0){
			num=-num;
			moves = new ChessMove[num];
			int count = size - num;
			for (int i=0; i<num; i++){
				moves[i] = log.get(count);
				count++;
			}
		}
		return moves;
	}
	
	public void update(Observable o, Object movePassed){
		ChessMove move = (ChessMove)movePassed;
		log.add(move);
		setChanged();
		notifyObservers(movePassed);
	}
	
	public void undo(){
		int size = log.size();
		if (size > 0){
			ChessMove lastMove = log.get(size - 1);
			log.remove(size - 1);
			board.removePieceFrom(lastMove.getTo());
			board.placePieceAt(lastMove.getPiece(), lastMove.getFrom());
			if (lastMove.pieceWasCaptured()){
				board.placePieceAt(lastMove.getCaptured(), lastMove.getTo());
			}
			setChanged();
			notifyObservers(lastMove);
		} else {
			System.out.println("There are no moves to undo!");
		}
	}
	
}