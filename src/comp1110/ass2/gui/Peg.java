package comp1110.ass2.gui;

import comp1110.ass2.TwistGame;

// Conceptual design of a Peg, may use in later coding
public class Peg {

    // return Color of a peg originally
    public static Color getPegColor(char c){
        switch (c){
            case 'i': return Color.RED;
            case 'j': return Color.BLUE;
            case 'k': return Color.GREEN;
            case 'l': return Color.YELLOW;
        }return null;
    }

    public static boolean isPegOnValidPlace(String placement){
        String pegPlacement = "";
        for(int p = 0; p<placement.length(); p+=4){
            if(placement.charAt(p)>='i'){
                String pegPosition = placement.substring(p+1,p+3);
                if(pegPlacement.contains(placement.substring(p+1,p+3))){
                    return false;
                }else{
                    pegPlacement+=placement.substring(p+1,p+3);
                    if(TwistGame.occupiedPlacement.contains(pegPosition)){
                        if(!Piece.getColourPlacement(getPegColor(placement.charAt(p))).contains(pegPosition)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}