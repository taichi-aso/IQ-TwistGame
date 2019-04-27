package comp1110.ass2.gui;

import comp1110.ass2.TwistGame;

import java.util.*;

// Conceptual design for a Piece, may use in later coding
public class Piece {
    public String name;
    public List<Character> shape;

    public Piece(String name, List<Character> shape){
        this.name = name;
        this.shape = shape;
    }

    // returns Color of a piece originally written by Weitong Huang and Reformed
    public static Color getPieceColor(char c){
        if(c == 'a' || c == 'b') {return Color.RED;}
        else if(c == 'c' || c == 'd') {return Color.BLUE;}
        else if(c == 'e' || c == 'f') {return Color.GREEN;}
        else if(c == 'g' || c == 'h') {return Color.YELLOW;}
        return null;
    }

    // this method rotates an arrayList as a 4*4 matrix to 90 degrees clockwise
    private static ArrayList<Character> rotate(List<Character> pieces){
        ArrayList<Character> rotatedList = new ArrayList<>(pieces);
        for(int i = 0;i<16;i++){
            rotatedList.set(i%4*4+3-i/4,pieces.get(i));
        }
        return rotatedList;
    }

    // return an arrayList after flipping
    public static ArrayList<Character> flip(List<Character> pieces){
        ArrayList<Character> flippedList = new ArrayList<>(pieces);
        for(int i = 0; i < 16; i++){
            flippedList.set(3+ i/4*8-i,pieces.get(i));
        }
        return flippedList;
    }

    // maps every original placement of a piece as an arrayList with its name originally written by Weitong Huang and Reformed
    public static HashMap<Character, List<Character>> pieceMap = new HashMap<>();
    static {
        pieceMap.put('a', Arrays.asList('t','f','t','n','c','n','f','n',
                'n','n','n','n','n','n','n','n'));
        pieceMap.put('b', Arrays.asList('f','f','c','n','c','t','f','n',
                'n','n','n','n','n','n','n','n'));
        pieceMap.put('c', Arrays.asList('f','t','f','f','n','n','n','n',
                'n','n','n','n','n','n','n','n'));
        pieceMap.put('d', Arrays.asList('f','f','f','n','c','t','t','n',
                'n','n','n','n','n','n','n','n'));
        pieceMap.put('e', Arrays.asList('f','t','n','n','c','t','n','n',
                'n','n','n','n','n','n','n','n'));
        pieceMap.put('f', Arrays.asList('f','f','t','n','c','t','c','n',
                'n','n','n','n','n','n','n','n'));
        pieceMap.put('g', Arrays.asList('t','n','c','n','t','f','f','n',
                'c','t','c','n','n','n','n','n'));
        pieceMap.put('h', Arrays.asList('t','f','f','n','n','n','n','n',
                'n','n','n','n','n','n','n','n'));

    }

    // this method uses the hashmap defined above to get a specific placement of a piece as an arrayList
    // takes piece shape as char and this orientation and return this occupation as a list
    public static List<Character> changeOrientation(char piece, char orientation){
        List<Character> list = pieceMap.get(piece);
        switch (orientation){
            case '0': {
                return list;
            }
            case '1':{
                return  rotate(list);
            }
            case '2':{
                return rotate(rotate(list));
            }
            case '3':{
                return rotate(rotate(rotate(list)));
            }
            case '4':{
                return flip(rotate(rotate(list)));
            }
            case '5':{
                return flip(rotate(list));
            }
            case '6':{
                return flip(list);
            }
            case '7':{
                return flip(rotate(rotate(rotate(list))));
            }
        }
        return null;
    }

    // checks whether pieces' placement are all on the board and no overlapping
    public static boolean isOnBoard(String placement){
        for(int p = 0; p<placement.length() && placement.charAt(p)<='h'; p+=4){
            List<Character> pieceShapeList = changeOrientation(placement.charAt(p),placement.charAt(p+3));
            int startIndex = 0;
            for (int s = 0; s < pieceShapeList.size(); s++) {
                if (pieceShapeList.get(s) != 'n') {
                    startIndex = s;
                    break;
                }
            }
            for (int i = 0; i < 16; i++) {
                if (pieceShapeList.get(i) == 't' || pieceShapeList.get(i) == 'f') {
                    int positionX = (placement.charAt(p+1)+i-i/4*4-(startIndex-startIndex/4*4));
                    int positionY = (placement.charAt(p+2)+i/4-startIndex/4);
                    String piecePosition = ""+ (char)positionX+(char)positionY;
                    if(!TwistGame.occupiedPlacement.contains(piecePosition)&& positionY>=65 && positionY<=68 && positionX>=49 && positionX<=56){
                        TwistGame.occupiedPlacement+=piecePosition;
                        if(pieceShapeList.get(i) == 't'){
                            makeHolePlacement(placement.charAt(p),piecePosition);
                        }
                    }else{
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void makeHolePlacement(Character piece, String position){
        if(getPieceColor(piece)==Color.RED){
            TwistGame.redHolePlacement+=position;
        }else if(getPieceColor(piece)==Color.BLUE){
            TwistGame.blueHolePlacement+=position;
        }else if(getPieceColor(piece)==Color.GREEN){
            TwistGame.greenHolePlacement+=position;
        }else if(getPieceColor(piece)==Color.YELLOW){
            TwistGame.yellowHolePlacement+=position;
        }
    }

    public static String getColourPlacement (Color c){
        if(c==Color.RED){
            return TwistGame.redHolePlacement;
        }else if(c==Color.BLUE){
            return TwistGame.blueHolePlacement;
        }else if(c==Color.GREEN){
            return TwistGame.greenHolePlacement;
        }else if(c==Color.YELLOW){
            return TwistGame.yellowHolePlacement;
        }
        return "";
    }

    // this method removes duplicates from a well-formed solution set
    public static Set<String> removeDuplicates(Set<String> viablePiecePlacementList){
        ArrayList<String> newArray = new ArrayList<>(viablePiecePlacementList);
        String[][] duplicatesList = new String[][] {
                {"b0","b2"},{"b1","b3"},{"b4","b6"},{"b5","b7"},
                {"c0","c2"},{"c0","c4"},{"c0","c6"},{"c2","c6"},
                {"c1","c3"},{"c1","c5"},{"c1","c7"},{"c3","c7"},
                {"e0","e7"},{"e1","e4"},{"e2","e5"},{"e3","e6"},
                {"f0","f6"},{"f1","f7"},{"f2","f4"},{"f3","f5"},
                {"h0","h2"},{"h0","h4"},{"h0","h6"},{"h2","h6"},
                {"h1","h3"},{"h1","h5"},{"h1","h7"},{"h3","h7"}};
        for(String piece:viablePiecePlacementList){
            for(String[] d:duplicatesList){
                if(piece.charAt(0)<d[0].charAt(0)){break;}
                String pieceShape = piece.substring(0,1)+piece.substring(3);
                String pieceLocation = piece.substring(1,3);
                if(pieceShape.equals(d[0])){
                    String duplicatePiece = d[1].substring(0,1)+pieceLocation+d[1].substring(1);
                    if(newArray.contains(duplicatePiece)){
                        newArray.remove(duplicatePiece);
                    }
                }
            }
        }
        return new HashSet<>(newArray);
    }

    // this method inserts a four-bit long placement String into a well-formed String and return a new well-formed String
    // e.g "a7A7c1A3","b6A7" -> "a7A7b6A7c1A3"
    public static String makeWellFormedString(String previous, String toInsert) {
        if(previous.length()==0) {
            return toInsert;
        }
        for(int i = 0; i < previous.length(); i+=4) {
            if(previous.charAt(0) > toInsert.charAt(0)) {
                return toInsert + previous;
            } else if(previous.charAt(i) > toInsert.charAt(0)) {
                return previous.substring(0,i) + toInsert + previous.substring(i,previous.length());
            } else if(i == previous.length() - 4 && previous.charAt(i) < toInsert.charAt(0)) {
                return previous + toInsert;
            }
        }return null;
    }
}