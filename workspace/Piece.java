
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

//you will need to implement two functions in this file.
public class Piece {
    private final boolean color;
    private BufferedImage img;
    
    public Piece(boolean isWhite, String img_file) {
        this.color = isWhite;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }
    
    

    
    public boolean getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x, y, null);
    }
    
    
    /*
     * return a list of every square that is "controlled" by this piece. A square is controlled
     * if the piece capture into it legally.
     *  This code is taken from getLegalMoves using board instead of getSquareArray
     */
    
      // the if statements just check if moves are in bounds- king controls all squares in a 1 block radius around itself

      // Preconditions: board must be an ArrayList of squares and start must be a square- referring to the 8x8 board of squares
      // Postconditions: Will return an arraylist of squares that the piece controls
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
      int row = start.getRow();
      int col = start.getCol();
      ArrayList<Square> cSquares = new ArrayList<Square>();
        // options are row + 1, row - 1, same row, left 1 col, same col, right 1 col,
        // same col

        if((row + 1 < 8)){
          // adds to moves arraylist of Squares
          cSquares.add(board[row + 1][col]);
        }
// /////// //  [row][col] goes here but redundant cuz its occupied by itself
        if((row - 1 >= 0)){
          cSquares.add(board[row - 1][col]);
        }
        // left col
        if(((row + 1 < 8) && (col - 1 >= 0))){
          cSquares.add(board[row + 1][col - 1]);
        }
        if(((row - 1 >= 0) && (col - 1 >= 0))){
          cSquares.add(board[row - 1][col - 1]);
        }
        if((col - 1 >= 0)){
          cSquares.add(board[row][col - 1]);
        }
        // right col
        if(((row + 1 < 8) && (col + 1 < 8))){
          cSquares.add(board[row + 1][col + 1]);
        }
        if(((row - 1 >= 0) && (col + 1 < 8))){
          cSquares.add(board[row - 1][col + 1]);
        }
        if((col + 1 < 8)){
          cSquares.add(board[row][col + 1]);
        }
        return cSquares;
    }
    
    /*
    * implement the move function here
    * it's up to you how the piece moves, but at the very least the rules should be logical and it should never move off the board!
    * returns an arraylist of squares which are legal to move to
    * please note that your piece must have some sort of logic. Just being able to move to every square on the board is not
    * going to score any points.
    * the piece is gonna move like a standard king in chess 
    * the King can move anywhere within a 1 square radius up, down, left, right, or diagonal
    * it can capture any piece in a square it can move into
    */

    // if statements check if the move is within the board, if there is no occupying piece, or if the occupying piece is a different color

    // Preconditions: b must be type Board and start must be type Square- with the board holding 8 columns and rows of squares
    // Postconditions: An array list of type Square containing all valid moves is returned
    public ArrayList<Square> getLegalMoves(Board b, Square start){
      int row = start.getRow();
      int col = start.getCol();
      ArrayList<Square> moves = new ArrayList<Square>();
        // options are row + 1, row - 1, same row, left 1 col, same col, right 1 col,
        // same col
        if((row + 1 < 8) && ((b.getSquareArray()[row + 1][col].getOccupyingPiece() == null) || b.getSquareArray()[row + 1][col].getOccupyingPiece().getColor() != color)){
          // adds to moves arraylist of Squares
          moves.add(b.getSquareArray()[row + 1][col]);
        }
        //    [row][col] would go here but redundant cuz its occupied by itself
        if((row - 1 >= 0) && ((b.getSquareArray()[row - 1][col].getOccupyingPiece() == null) || b.getSquareArray()[row - 1][col].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row - 1][col]);
        }
        // left col
        if(((row + 1 < 8) && (col - 1 >= 0)) && ((b.getSquareArray()[row + 1][col - 1].getOccupyingPiece() == null) || b.getSquareArray()[row + 1][col - 1].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row + 1][col - 1]);
        }
        if(((row - 1 >= 0) && (col - 1 >= 0)) && ((b.getSquareArray()[row - 1][col - 1].getOccupyingPiece() == null) || b.getSquareArray()[row - 1][col - 1].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row - 1][col - 1]);
        }
        if((col - 1 >= 0) && ((b.getSquareArray()[row][col - 1].getOccupyingPiece() == null) || b.getSquareArray()[row][col - 1].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row][col - 1]);
        }
        // right col
        if(((row + 1 < 8) && (col + 1 < 8)) && ((b.getSquareArray()[row + 1][col + 1].getOccupyingPiece() == null) || b.getSquareArray()[row + 1][col + 1].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row + 1][col + 1]);
        }
        if(((row - 1 >= 0) && (col + 1 < 8)) && ((b.getSquareArray()[row - 1][col + 1].getOccupyingPiece() == null) || b.getSquareArray()[row - 1][col + 1].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row - 1][col + 1]);
        }
        if((col + 1 < 8) && ((b.getSquareArray()[row][col + 1].getOccupyingPiece() == null) || b.getSquareArray()[row][col + 1].getOccupyingPiece().getColor() != color)){
          moves.add(b.getSquareArray()[row][col + 1]);
        }
        return moves;
      }
}