package comp1110.ass2.gui;


import java.util.Random;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class InitialPegs {
    public String placement;
    public String redHole;
    public String blueHole;
    public String greenHole;
    public String yellowHole;
    public String number;


    public InitialPegs(String placement,String redHole, String greenHole, String blueHole, String yellowHole, String number){
        this.placement=placement;
        this.redHole=redHole;
        this.blueHole=blueHole;
        this.greenHole=greenHole;
        this.yellowHole=yellowHole;
        this.number=number;
    }

    public static void generateStartingPoint(String line){
        Random rand = new Random();
        String placementString = line.split(" ")[0];
        String redPegsString = "";
        String bluePegsString = "";
        String greenPegsString = "";
        String yellowPegsString = "";
        String initialPegs = "";
        String newPeg;
        int maxLength = 28;

        if(Board.difficultyLevel==0){
            maxLength=28;
        }else if(Board.difficultyLevel==1){
            maxLength=24;
        }else if(Board.difficultyLevel==2){
            int randomLength = rand.nextInt(2);
            maxLength=(randomLength+4)*4;
        }else if(Board.difficultyLevel==3){
            maxLength=12;
        }


        while(initialPegs.length()<maxLength){
            int randomNum = rand.nextInt(4)+1;
            if(randomNum==1){
                int randIndex = rand.nextInt(2)*2;
                newPeg = "i" + line.split(" ")[randomNum].substring(randIndex,randIndex+2)+"0";
                if(redPegsString.length()<4){
                    redPegsString+=newPeg;
                }
            }else if(randomNum==2){
                int randIndex = rand.nextInt(2)*2;
                newPeg = "j" + line.split(" ")[randomNum].substring(randIndex,randIndex+2)+"0";
                if(bluePegsString.length()<8&&!bluePegsString.contains(newPeg)){
                    bluePegsString+=newPeg;
                }
            }else if(randomNum==3){
                int randIndex = rand.nextInt(3)*2;
                newPeg = "k" + line.split(" ")[randomNum].substring(randIndex,randIndex+2)+"0";
                if(greenPegsString.length()<8&&!greenPegsString.contains(newPeg)){
                    greenPegsString+=newPeg;
                }
            }else if(randomNum==4){
                int randIndex = rand.nextInt(3)*2;
                newPeg = "l" + line.split(" ")[randomNum].substring(randIndex,randIndex+2)+"0";
                if(yellowPegsString.length()<8&&!yellowPegsString.contains(newPeg)){
                    yellowPegsString+=newPeg;
                }
            }
            initialPegs=redPegsString+bluePegsString+greenPegsString+yellowPegsString;
        }

        Board.solution=placementString;
        Board.initialPegs=initialPegs;
    }


    public static String randomReadFromtxt(){
        String line="";
        try {
            FileReader fr = new FileReader("solutionSet.txt");
            BufferedReader br = new BufferedReader(fr);

            int count = 1;
            Random rand = new Random();
            int randomLineNum = rand.nextInt(5992);

            while ((line = br.readLine()) != null && count<=randomLineNum) {
                count++;
            }
            br.close();
            fr.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return line;
    }

    public static void main(String[] args) {
        randomReadFromtxt();
    }

    // does this method mean that we return piece of that specific place in solution? if so, problem
    // is that user may have placed thins in different order and it's not the same order as solution
    // which means we could possibly cancel user's right placement in a wrong order.
    public static String getHint(String placementString,String solution){
        for(int i = 0;i<solution.length();i+=4){
            if(!placementString.contains(solution.substring(i,i+1))){
                return solution.substring(i,i+4);
            }
        }
        return "";
    }

    // this method returns next step as a string, and parameters are automatically passed to it through oo programming
    public static String getNextStep(){
        String slt = Board.solution;
        int size = Board.PiecePlacement.length();

        for(int i = 0; i < size; i += 4) {
            slt = slt.replace(Board.PiecePlacement.substring(i,i+4),"");
        } return slt.substring(0,4);
    }

    // this method is used to replace/insert a hint into the previous placement string
    public static String getNewPlacement(String previous, String next) {
        if(!previous.contains(""+next.charAt(0))) {
            return Piece.makeWellFormedString(previous, next);
        } else {
            int preIndex = previous.indexOf(""+next.charAt(0));
            previous = previous.replace(previous.substring(preIndex,preIndex+4),next);
            return previous;
        }
    }
}