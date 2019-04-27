package comp1110.ass2;

import comp1110.ass2.gui.Peg;
import comp1110.ass2.gui.Piece;
import java.util.*;

/**
 * This class provides the text interface for the Twist Game
 * <p>
 * The game is based directly on Smart Games' IQ-Twist game
 * (http://www.smartgames.eu/en/smartgames/iq-twist)
 */
public class TwistGame {

    /**
     * Determine whether a piece or peg placement is well-formed according to the following:
     * - it consists of exactly four characters
     * - the first character is in the range a .. l (pieces and pegs)
     * - the second character is in the range 1 .. 8 (columns)
     * - the third character is in the range A .. D (rows)
     * - the fourth character is in the range 0 .. 7 (if a piece) or is 0 (if a peg)
     *
     * @param piecePlacement A string describing a single piece or peg placement
     * @return True if the placement is well-formed
     */

  /*
  Checks if a single piece or peg is well formed using control flow and for loops by Weitong Huang
   */
    public static boolean isPlacementWellFormed(String piecePlacement) {
        if(exactlyFour(piecePlacement)){
            if(within(piecePlacement.charAt(0), pieces)){
                return within(piecePlacement.charAt(1),column)&&within(piecePlacement.charAt(2),row)&&within(piecePlacement.charAt(3),rotation);
            }else if(within(piecePlacement.charAt(0), pegs)){
                return within(piecePlacement.charAt(1),column)&&within(piecePlacement.charAt(2),row)&&within(piecePlacement.charAt(3),"0");
            }else {
                return false;
            }
        }return false;
    }
    // FIXME Task 2: determine whether a piece or peg placement is well-formed

    public static String pieces = "abcdefgh";
    public static String pegs = "ijkl";
    private static String column = "123456768";
    private static String row = "ABCD";
    private static String rotation = "01234567";

    private static boolean exactlyFour(String placement){
        return placement.length()==4;
    } // checks if length of a string is exactly four by Weitong Huang

    private static boolean within(char c, String s){
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i)==c){
                return true;
            }
        }return false;
    } // check if a char is in a specific string by Weitong Huang

    /**
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N four-character piece placements (where N = 1 .. 15);
     * - each piece or peg placement is well-formed
     * - each piece or peg placement occurs in the correct alphabetical order (duplicate pegs can be in either order)
     * - no piece or red peg appears more than once in the placement
     * - no green, blue or yellow peg appears more than twice in the placement
     *
     * @param placement A string describing a placement of one or more pieces and pegs
     * @return True if the placement is well-formed
     */

  /*
  Checks if a whole String of placement is well formed using combination of multiple boolean expressions by Weitong Huang
   */
    public static boolean isPlacementStringWellFormed(String placement) {
        return isInGroups(placement) && eachWellFormed(placement) && isInOrder(extract(placement)) && pegUnique(placement) && pieceUnique(placement);
    }// FIXME Task 3: determine whether a placement is well-formed

    private static boolean isInGroups(String s){
        return (s.length()%4==0);
    }

    private static boolean eachWellFormed(String s){
        for(int i = 0; i < s.length(); i += 4){
            if(isPlacementWellFormed(s.substring(i,i+4))){
            }
            else return false;
        }return true;
    }

    private static String extract(String s){
        String initials = "";
        for(int i = 0; i < s.length(); i += 4){
            initials += s.charAt(i);
        }
        return initials;
    }

    private static boolean isInOrder(String s){
        for(int i = 0; i < s.length(); i++){
            if(i+1 == s.length()){
                return true;
            }
            if(s.charAt(i) > s.charAt(i+1)){
                return false;
            }
        }return false;
    }

    private static int countX(String s, char c){
        int count = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i)==c){
                count++;
            }
        }return count;
    } // returns the number of some char c in a specific string s by Weitong Huang

    private static boolean pegUnique(String s){
        return countX(s,'i')<=1 && countX(s,'j')<=2 && countX(s,'k')<=2 && countX(s,'l')<=2;
    }

    private static boolean pieceUnique(String s){
        return countX(s,'a')<=1 && countX(s,'b')<=1 && countX(s,'c')<=1 && countX(s,'d')<=1 && countX(s,'e')<=1 && countX(s,'f')<=1 && countX(s,'g')<=1 && countX(s,'h')<=1;
    }



    /**
     * Determine whether a placement string is valid.  To be valid, the placement
     * string must be well-formed and each piece placement must be a valid placement
     * according to the rules of the game.
     * - pieces must be entirely on the board
     * - pieces must not overlap each other
     * - pieces may only overlap pegs when the a) peg is of the same color and b) the
     *   point of overlap in the piece is a hole.
     *
     * @param placement A placement sequence string
     * @return True if the placement sequence is valid
     */

    public static String occupiedPlacement = "";
    public static String redHolePlacement = "";
    public static String blueHolePlacement = "";
    public static String greenHolePlacement = "";
    public static String yellowHolePlacement = "";

    public static boolean isPlacementStringValid(String placement) {
        for(int i = 0; i < placement.length(); i += 4) {
            if(!isPlacementStringWellFormed(placement.substring(i,i+4))) {
                return false;
            }
        }
        occupiedPlacement = "";
        redHolePlacement = "";
        blueHolePlacement = "";
        greenHolePlacement = "";
        yellowHolePlacement = "";
        if(!Piece.isOnBoard(placement)){
            return false;
        }else if(!Peg.isPegOnValidPlace(placement)){
            return false;
        }
        return true;
    }// FIXME Task 5: determine whether a placement string is valid

    public static void main(String[] args) {
        System.out.println(isPlacementStringValid("a5B2b1C4c5D0d1A2e3C1g6A2f3A0"));
        }

    /**
     * Given a string describing a placement of pieces and pegs, return a set
     * of all possible next viable piece placements.   To be viable, a piece
     * placement must be a valid placement of a single piece.  The piece must
     * not have already been placed (ie not already in the placement string),
     * and its placement must be valid.   If there are no valid piece placements
     * for the given placement string, return null.
     *
     * When symmetric placements of the same piece are viable, only the placement
     * with the lowest rotation should be included in the set.
     *
     * @param placement A valid placement string (comprised of peg and piece placements)
     * @return An set of viable piece placements, or null if there are none.
     */
    // as required above by Taichi Aso and Weitong Huang
    public static Set<String> getViablePiecePlacements(String placement) {
        Set<String> viablePiecePlacementList = new HashSet<>();

        String [] allpieces = {"a","b","c","d","e","f","g","h"};
        String [] allpositions = {"A","B","C","D"};

        for(String piece: allpieces){
            if(placement.contains(piece)){
                continue;
            }
            for(int i = 1; i <= 8; i++){
                for(String position: allpositions){
                    for(int ii = 0; ii<=7; ii++){
                        String toInsert = piece +Integer.toString(i)+position+Integer.toString(ii);
                        if(isPlacementStringValid(Piece.makeWellFormedString(placement,toInsert))){
                            viablePiecePlacementList.add(toInsert);
                        }
                    }
                }
            }
        }
        if(viablePiecePlacementList.isEmpty()){
            return null;
        }
        return Piece.removeDuplicates(viablePiecePlacementList);

    } // FIXME Task 6: determine the set of valid next piece placements

    /**
     * Return an array of all unique solutions for a given starting placement.
     *
     * Each solution should be a 32-character string giving the placement sequence
     * of all eight pieces, given the starting placement.
     *
     * The set of solutions should not include any symmetric piece placements.
     *
     * In the IQ-Twist game, valid challenges can have only one solution, but
     * other starting placements that are not valid challenges may have more
     * than one solution.  The most obvious example is the unconstrained board,
     * which has very many solutions.
     *
     * @param placement A valid piece placement string.
     * @return An array of strings, each 32-characters long, describing a unique
     * unordered solution to the game given the starting point provided by placement.
     */

    // as required above by Taichi Aso
    public static String[] getSolutions(String placement) {
        Set<String> solutions = new HashSet<>();
        addSolutions(placement,solutions);
        int size = solutions.size();
        return solutions.toArray(new String[size]);

        // FIXME Task 9: determine all solutions to the game, given a particular starting placement
    }

    public static boolean isSolutionValid(String placement){
        if(isPlacementStringValid(placement) && placement.contains("a") && placement.contains("b") && placement.contains("c") &&
                placement.contains("d") && placement.contains("e") && placement.contains("f") &&
                placement.contains("g") && placement.contains("h") ){
            return true;
        }else{
            return false;
        }
    }

    // through recursion getting a solution set by Taichi Aso
    private static void addSolutions(String placement,Set<String> solutions){
        String newplacement = placement;
        if(getViablePiecePlacements(placement)!=null){
            for(String next: getViablePiecePlacements(placement)){
                newplacement = Piece.makeWellFormedString(placement,next);
                if(isSolutionValid(newplacement)){
                    solutions.add(newplacement.substring(0,32));
                    break;
                }else if(isAllValidPlace(occupiedPlacement)){
                    addSolutions(newplacement,solutions);
                }
            }
        }
    }

    // determine whether it is impossible placement to reach a solution, such as placement which has two separated spaces
    public static boolean isAllValidPlace(String occupiedString){
        for(int i = 1;i<=8;i++){
            for(char c ='A';c<='D';c++){
                int count=0;
                int nextcount=0;
                String placement = ""+i+c;
                if(occupiedString.contains(placement)){
                    continue;
                }else {
                    if(i-1<1||occupiedString.contains(""+(i-1)+c)) {
                        count++;
                    }else if((i-2<1||occupiedString.contains(""+(i-2)+c))&&(c-1<'A'||occupiedString.contains(""+(i-1)+(char)(c-1)))&&(c+1>'D'||occupiedString.contains(""+(i-1)+(char)(c+1)))){
                        nextcount++;
                    }
                    if(i+1>8||occupiedString.contains(""+(i+1)+c)) {
                        count++;
                    }else if((i+2<1||occupiedString.contains(""+(i+2)+c))&&(c-1<'A'||occupiedString.contains(""+(i+1)+(char)(c-1)))&&(c+1>'D'||occupiedString.contains(""+(i+1)+(char)(c+1)))){
                        nextcount++;
                    }
                    if(c-1<'A'||occupiedString.contains(""+i+(char)(c-1))) {
                        count++;
                    }else if((c-2<'A'||occupiedString.contains(""+i+(char)(c-2)))&&(i-1<1||occupiedString.contains(""+(i-1)+(char)(c-1)))&&(i+1>8||occupiedString.contains(""+(i+1)+(char)(c-1)))){
                        nextcount++;
                    }
                    if(c+1>'D'||occupiedString.contains(""+i+(char)(c+1))) {
                        count++;
                    }else if((c+2>'D'||occupiedString.contains(""+i+(char)(c+2)))&&(i-1<1||occupiedString.contains(""+(i-1)+(char)(c+1)))&&(i+1>8||occupiedString.contains(""+(i+1)+(char)(c+1)))){
                        nextcount++;
                    }
                }
                if(count==4){
                    return false;
                }else if(count==3&&nextcount==1){
                    return false;
                }
            }
        }
        return true;
    }
}