// checkmate functionality is not workign but this is due and i have other work to do

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

//You will be implmenting a part of a function and a whole function in this document. Please follow the directions for the 
//suggested order of completion that should make testing easier.
@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
	// Resource location constants for piece images
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
	private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
	private static final String RESOURCES_WKNIGHT_PNG = "wknight.png";
	private static final String RESOURCES_BKNIGHT_PNG = "bknight.png";
	private static final String RESOURCES_WROOK_PNG = "wrook.png";
	private static final String RESOURCES_BROOK_PNG = "brook.png";
	private static final String RESOURCES_WKING_PNG = "wking.png";
	private static final String RESOURCES_BKING_PNG = "bking.png";
	private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
	private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
	private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
	private static final String RESOURCES_BPAWN_PNG = "bpawn.png";
	
	// Logical and graphical representations of board
	private final Square[][] board;
    private final GameWindow g; // use this for checkmate
 
    //contains true if it's white's turn.
    private boolean whiteTurn;
    
    // checks if game is over
    private boolean gameOver = false;

    //if the player is currently dragging a piece this variable contains it.
    private Piece currPiece;
    private Piece checkCheck;
    private Square fromMoveSquare;
    
    //used to keep track of the x/y coordinates of the mouse.
    private int currX;
    private int currY;
    

    
    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        //TO BE IMPLEMENTED FIRST
        // fully functional as of 2/24!
        // Creates the board filled of alternating light and dark squares in a checkerboard pattern
        board[0][0]= new Square(this, true, 0,0);
        for (int row = 0; row < board.length; row++){ // how many rows are there
            for(int col = 0; col < board[row].length; col++){ // how many things are in each row
                if(row % 2 == 0){ // if 1st, 3rd, 5th, or 7th row
                    if (col % 2 == 0){ // also if 1st, 3rd, 5th, or 7th col 
                        board[row][col] = new Square(this, true, row, col);
                        this.add(board[row][col]); 
                    } else {
                        board[row][col] = new Square(this, false, row, col);
                        this.add(board[row][col]); 
                    }
                } else if (col % 2 == 0){ // if in 2nd, 4th, 6th, or 8th row and 1st, 3rd, 5th, or 7th col
                    board[row][col] = new Square(this, false, row, col);
                    this.add(board[row][col]); 
                } else { // above + in 2nd, 4th, 6th, or 8th col
                    board[row][col] = new Square(this, true, row, col);
                    this.add(board[row][col]); 
                }
            }
        }


        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;

    }

    
	//set up the board such that the black pieces are on one side and the white pieces are on the other.
	//since we only have one kind of piece for now you need only set the same number of pieces on either side.
	//it's up to you how you wish to arrange your pieces.

    //Preconditions: the board must have been made
    // Postconditions: 8 pieces of each color are added to the board- white to the top and black to the bottom
    private void initializePieces() {
    	for (int i = 0; i < 8; i++){ // places a row of white and black pawns
            board[1][i].put(new Pawn(false, RESOURCES_BPAWN_PNG)); // top row of white pawns
            board[6][i].put(new Pawn(true, RESOURCES_WPAWN_PNG)); // bottom of black pawns
        }
        // rooks
        board[0][0].put(new Rook(false, RESOURCES_BROOK_PNG));
        board[0][7].put(new Rook(false, RESOURCES_BROOK_PNG)); 	
        board[7][0].put(new Rook(true, RESOURCES_WROOK_PNG));
        board[7][7].put(new Rook(true, RESOURCES_WROOK_PNG));
        // knights
        board[0][1].put(new Knight(false, RESOURCES_BKNIGHT_PNG));
        board[0][6].put(new Knight(false, RESOURCES_BKNIGHT_PNG));
        board[7][1].put(new Knight(true, RESOURCES_WKNIGHT_PNG));
        board[7][6].put(new Knight(true, RESOURCES_WKNIGHT_PNG));
        // bishops
        board[0][2].put(new Bishop(false, RESOURCES_BBISHOP_PNG));
        board[0][5].put(new Bishop(false, RESOURCES_BBISHOP_PNG));
        board[7][2].put(new Bishop(true, RESOURCES_WBISHOP_PNG));
        board[7][5].put(new Bishop(true, RESOURCES_WBISHOP_PNG));
        // kings
        board[0][4].put(new King(false, RESOURCES_BKING_PNG));
        board[7][4].put(new King(true, RESOURCES_WKING_PNG));
        // queens
        board[0][3].put(new Queen(false, RESOURCES_BQUEEN_PNG));
        board[7][3].put(new Queen(true, RESOURCES_WQUEEN_PNG));

    }

    //precondition - the board is initialized and contains a king of either color. The boolean kingColor corresponds to the color of the king we wish to know the status of.
    //postcondition - returns true of the king is in check and false otherwise.
	public boolean isInCheck(boolean kingColor){
        ArrayList<Square> opposite = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                // if the piece on board[i][j] isn't empty and is the opposite color of the king
                if (board[i][j].getOccupyingPiece()!= null && board[i][j].getOccupyingPiece().getColor() != kingColor){ // kingColor true is white, false is black
                    opposite.addAll(board[i][j].getOccupyingPiece().getControlledSquares(board, board[i][j]));
                    // add all arraylists of controlled squares for each piece to the opposite arraylist
                }
            }
        }
        for (int i = 0; i < opposite.size(); i++){ // loops thru all piece locations in opposite list
                if(opposite.get(i).getOccupyingPiece() instanceof King && opposite.get(i).getOccupyingPiece().getColor() == kingColor) { // if king is on a piece in opposite and is 
                    return true;
                }
            }
            return false;
        }

            //precondition - the board is initialized and contains a king of either color. The boolean kingColor corresponds to the color of the king we wish to know the status of.
        //postcondition - returns true of the king is in checkmate and false otherwise.
    public boolean isCheckmate(boolean kingColor){
        if(isInCheck(kingColor) == false){
            return false;
        }
        ArrayList<Square> legalMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece p = board[i][j].getOccupyingPiece(); // makes current piece respective to board square
                if (p != null & p.getColor() == kingColor){ // if piece is there and same color as king
                    fromMoveSquare = board[i][j]; // start piece or board spot
                    legalMoves = p.getLegalMoves(this, fromMoveSquare);
                    for (Square move : legalMoves){ // loops through every move in legalMoves - checks X for each move
                        Piece temp = move.getOccupyingPiece(); // saves checked piece to undo
                        move.put(p); //moves checked piece
                        fromMoveSquare.removePiece(); // removes checked piece from original location

                        boolean moveInCheck = isInCheck (kingColor); // checks if king remains checked after the move

                        //undos move if king no longer in check
                        if (moveInCheck == false){
                            fromMoveSquare.put(p);
                            move.removePiece();
                            return false;
                        }
                        // undos without returning false - tries again
                        fromMoveSquare.put(p);
                        move.removePiece();


                    }
                }
            }
        }
        return true; // remains in check after all moves
    } 
        
        
    

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
     
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[x][y];
                if(sq == fromMoveSquare)
                	 sq.setBorder(BorderFactory.createLineBorder(Color.blue));
                sq.paintComponent(g);
                
            }
        }
    	if (currPiece != null) {
            if ((currPiece.getColor() && whiteTurn)
                    || (!currPiece.getColor()&& !whiteTurn)) {
                final Image img = currPiece.getImage();
                g.drawImage(img, currX, currY, null);
            }
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) { 
        if (gameOver){
            g.checkmateOccurred(whiteTurn);
            return; // can't make moves if the game ends
        }
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            fromMoveSquare = sq;
            if (!currPiece.getColor() == whiteTurn){ // prevents the piece from being able to move on wrong turn
                currPiece = null; 
                return;
            }
         
            sq.setDisplay(false);
        }
        repaint();
    }

    //TO BE IMPLEMENTED!
    //should move the piece to the desired location only if this is a legal move.
    //use the pieces "legal move" function to determine if this move is legal, then complete it by
    //moving the new piece to it's new board location. 
    //Preconditions: board, squares and pieces must exist and be established with some white and black pieces
    //Postconditions: Piece is moved to where mouse is released and erased from previous location provided move is legal
    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver){
            g.checkmateOccurred(whiteTurn);
            return; // can't make moves if the game ends
        }
        
            if(currPiece != null && currPiece.getColor() == whiteTurn){ // prevents from dereferencing currPiece
                    // if this color == move color
                Square endSquare = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

                for (Square [] row: board){
                    for(Square z: row){
                        z.setBorder(null);
                    }
                }
                    //using currPiece
                if (currPiece.getLegalMoves(this, fromMoveSquare).contains(endSquare) && (endSquare.getOccupyingPiece() == null || endSquare.getOccupyingPiece().getColor() != currPiece.getColor())){
                    Piece captured = endSquare.getOccupyingPiece();
                    endSquare.put(currPiece);
                    fromMoveSquare.removePiece();

                    if (isInCheck(whiteTurn)){ // undos move if king is in check
                        fromMoveSquare.put(currPiece);
                        endSquare.removePiece(); 
                        if (captured != null){
                            endSquare.put(captured);
                        }
                    } else {
                        whiteTurn = !whiteTurn;
                        if (isCheckmate(whiteTurn)){
                            gameOver = true;
                            g.checkmateOccurred(whiteTurn);
                            repaint();
                            }
                        }
                    }
                
                fromMoveSquare.setDisplay(true);
                currPiece = null;
                repaint();
            }
        }

    
      //preconditions: same as before board pieces and squares must exist 
      //postconditons: when the mouse is dragged piece will visually disappear from current location, controlled squares will be highlighted in yellow
      // note legal moves are currently not shown but functionality is there if the display of controlledsquares was removed or modified
    @Override
    public void mouseDragged(MouseEvent e) {
        if (gameOver){
            g.checkmateOccurred(whiteTurn);
            return; // can't make moves if the game ends
        }
        if(currPiece != null){
        currX = e.getX() - 24;
        currY = e.getY() - 24;
        ArrayList<Square> moves = currPiece.getLegalMoves(this, fromMoveSquare);
        for(Square s: moves){
           s.setBorder(BorderFactory.createLineBorder(Color.red)); // highlights red to show legal moves
        }
        for(Square s: currPiece.getControlledSquares(board, fromMoveSquare)){
            s.setBorder(BorderFactory.createLineBorder(Color.yellow)); // highlights yellow to show controlled squares
        }
    }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}