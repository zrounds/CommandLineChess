package a6;
import java.util.Observable;

public abstract class ChessPiece extends Observable{

	private ChessPlayer owner;
	private ChessGame game;
	private ChessPosition position;
	protected char mark;
	
	protected ChessPiece(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		this.owner = owner;
		this.game = game;
		this.position = null;
		this.addObserver(game);
		
		game.getBoard().placePieceAt(this, init_position);
	}
	
	public ChessPlayer getOwner() {
		return owner;
	}
	
	public ChessGame getGame() {
		return game;
	}
	
		
	public ChessPosition getPosition() {
		return position;
	}
	
	public void setPosition(ChessPosition new_position) {
		position = new_position;
	}
	
	
	public void moveTo(ChessPosition destination) throws IllegalMove {
		ChessBoard b = getGame().getBoard();
		
		if(getPosition().equals(destination)){
			throw new IllegalMove(this, getPosition(), destination);
		} else if(b.getPieceAt(destination) != null) {
			if (getOwner().equals(b.getPieceAt(destination).getOwner())){
				throw new IllegalMove(this, getPosition(), destination);
			}
		}
		
		checkLineOfSight(getPosition(), destination);
		
		ChessMove confirmedMove = new ChessMove(this, getPosition(), destination, b.getPieceAt(destination));
		b.removePieceFrom(destination);
		b.removePieceFrom(getPosition());
		b.placePieceAt(this, destination);
		setChanged();
		notifyObservers(confirmedMove);
	}
	
	public void checkLineOfSight(ChessPosition start, ChessPosition end) throws IllegalMove{
		if (getMark() == 'n' || getMark() == 'N'){
			return;
		}
		int dX = end.getX() - start.getX();
		int dY = end.getY() - start.getY();
		if (!( dX == 0 || dY == 0 || Math.abs(dX/dY) == 1)){
			throw new IllegalMove(this, start, end);
		}
		if (dX != 0) dX = dX/Math.abs(dX);
		if (dY != 0) dY = dY/Math.abs(dY);
		ChessPosition check = new ChessPosition(start.getX() + dX,start.getY() + dY);
		while(!check.equals(end)){
			if (getGame().getBoard().getPieceAt(check) != null){
				throw new IllegalMove(this, start, end);
			}
			check = new ChessPosition(check.getX() + dX, check.getY() + dY);
		}
	}
	
	public char getMark() {
		return mark;
	}
}

class Rook extends ChessPiece {
	public Rook(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'r';
		} else {
			mark = 'R';
		}
	}
	
		@Override
		public void moveTo(ChessPosition destination) throws IllegalMove {
			int dX = destination.getX() - getPosition().getX();
			int dY = destination.getY() - getPosition().getY();
			
			if(Math.abs(dX) > 0 && Math.abs(dY) > 0){
				throw new IllegalMove(this, getPosition(), destination);
			}
			
			super.moveTo(destination);
		}
		
		@Override
		public String toString(){
			return "rook";
		}
	}	


class Bishop extends ChessPiece {
	public Bishop(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'b';
		} else {
			mark = 'B';
		}
	}
	
	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove {
		int dX = destination.getX() - getPosition().getX();
		int dY = destination.getY() - getPosition().getY();
		
		if (dX == 0 || dY == 0){
			throw new IllegalMove(this, getPosition(), destination);
		} else if (Math.abs(dX / dY) != 1){
			throw new IllegalMove(this, getPosition(), destination);
		}
		
		super.moveTo(destination);
	}
	
	@Override
	public String toString(){
		return "bishop";
	}
}

class Knight extends ChessPiece {
	public Knight(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'n';
		} else {
			mark = 'N';
		}
	}
	
	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove {
		int dX = Math.abs(destination.getX() - getPosition().getX());
		int dY = Math.abs(destination.getY() - getPosition().getY());
		
		if (dX == 0 || dY == 0 || dX > 2 || dY > 2 || !(dX / dY == 1/2 || dX / dY == 2)){
			throw new IllegalMove(this, getPosition(), destination);
		}
		
		super.moveTo(destination);
	}
	
	@Override
	public String toString(){
		return "knight";
	}
}

class Queen extends ChessPiece {
	public Queen(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'q';
		} else {
			mark = 'Q';
		}
	}	

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove {
		int dX = destination.getX() - getPosition().getX();
		int dY = destination.getY() - getPosition().getY();
		
		if (dX > 0 && dY > 0){
			if (Math.abs(dX / dY) != 1){
				throw new IllegalMove(this, getPosition(), destination);
			}
		}
		super.moveTo(destination);
	}
	
	@Override
	public String toString(){
		return "queen";
	}
}

class King extends ChessPiece {
	public King(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'k';
		} else {
			mark = 'K';
		}
	}

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove {
		int dX = destination.getX() - getPosition().getX();
		int dY = destination.getY() - getPosition().getY();
		
		if (Math.abs(dX) > 1 || Math.abs(dY) > 1){
			throw new IllegalMove(this, getPosition(), destination);
		}
		
		super.moveTo(destination);
	}
	
	@Override
	public String toString(){
		return "king";
	}
}

class Pawn extends ChessPiece {
	public Pawn(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'p';
		} else {
			mark = 'P';
		}
	}

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int dX = destination.getX() - x;
		int dY = destination.getY() - y;
		
		if (getOwner() == getGame().getPlayer1()){
			if (y != 1 && dY > 1 || dY < 0 || dY > 2){
				throw new IllegalMove(this,getPosition(),destination);
			}
		} else {
			if (y != 6 && dY < -1 || dY > 0 || dY < -2){
				throw new IllegalMove(this,getPosition(),destination);
			}
		}
		
		if (dX != 0 && getGame().getBoard().getPieceAt(destination) == null || Math.abs(dX) > 1){
			throw new IllegalMove(this,getPosition(),destination);
		}
		
		if(getGame().getBoard().getPieceAt(destination) != null){
			if (! getOwner().equals(getGame().getBoard().getPieceAt(destination).getOwner()) && dX == 0){
				throw new IllegalMove(this, getPosition(), destination);
			}
		}
		
		super.moveTo(destination);
	}
	
	@Override
	public String toString(){
		return "pawn";
	}
}