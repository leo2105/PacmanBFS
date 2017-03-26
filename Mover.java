import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.*;
import javax.swing.JPanel;
import java.lang.Math;
import java.io.*;
import java.util.*;

/* esta clase es heredada por ghost y player*/
class Mover{
    int frameCount=0;
    boolean[][] state;
    int gridSize;
    int max;
    int increment;
    /* constructor */
    public Mover(){
        gridSize=20;
        increment = 4;
        max = 400;
        state = new boolean[20][20];
        for(int i =0;i<20;i++){
            for(int j=0;j<20;j++){
                state[i][j] = false;
            }
        }
    }
    /* actualiza el estado del mapa */
    public void updateState(boolean[][] state){
        for(int i =0;i<20;i++){
            System.arraycopy(state[i], 0, this.state[i], 0, 20);
        }
    } 
    /* chekea si el lugar a moverse es valido*/
    public boolean isValidDest(int x, int y){
        return (((x)%20==0) || ((y)%20)==0) && 20<=x && x<400 && 20<= y && y<400 && state[x/20-1][y/20-1];
    }
}