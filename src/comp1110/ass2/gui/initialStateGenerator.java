package comp1110.ass2.gui;

import comp1110.ass2.TwistGame;

import java.util.HashSet;
import java.util.Set;

public class initialStateGenerator {

    public static Set<String> getNextViablePiecePlacements(String placement) {
        Set<String> viablePiecePlacementList = new HashSet<>();

        String[] allpieces = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] allpositions = {"A", "B", "C", "D"};

        for (String piece : allpieces) {
            if (placement.contains(piece)) {
                continue;
            }
            for (int i = 1; i <= 8; i++) {
                for (String position : allpositions) {
                    for (int ii = 0; ii <= 7; ii++) {
                        String toInsert = piece + Integer.toString(i) + position + Integer.toString(ii);
                        if (TwistGame.isPlacementStringValid(Piece.makeWellFormedString(placement, toInsert))) {
                            viablePiecePlacementList.add(toInsert);
                        }
                    }
                }
            } break;
        }
        return Piece.removeDuplicates(viablePiecePlacementList);
    }

    public static void main(String[] args) {
        makePlacementString("");
    }

    public static void makePlacementString(String placement){
        for(String s:getNextViablePiecePlacements(placement)){
            if(TwistGame.isSolutionValid(placement+s)){
                System.out.println(((placement+s) + " " + TwistGame.redHolePlacement + " " + TwistGame.blueHolePlacement + " " + TwistGame.greenHolePlacement + " " + TwistGame.yellowHolePlacement));
            }else if(TwistGame.isAllValidPlace(TwistGame.occupiedPlacement)){
                makePlacementString(placement+s);
            }
        }
    }
}
