import javax.swing.*;
import java.awt.event.*;
import javax.swing.JApplet;
import java.awt.BorderLayout;
import java.util.*;
/* Esta clase contiene todo el juego ...
La mayor parte de la logica del juego se encuentra en la clase Board
pero esto crea el GUI y captura del raton y el teclado,
asi como controles de los estados de juego. */
public class Pacman extends JApplet implements MouseListener, KeyListener{
    /* Estos temporizadores se utilizan para acabar el titulo y poner el
    juego encima, y las pantallas de victoria despues de un periodo de
    inactividad conjunto(5 segundos). */
    long titleTimer = -1;
    long timer = -1;
    /* Crea un nuevo tablero*/
    Board b=new Board(); 
    /* Este temporizador se utiliza para hacer solicitud
    de nuevos marcos pueden extraer.*/
    javax.swing.Timer frameTimer;
    /* ESTE CONTRUCTOR CREA EL JUEGO EN ESENCIA */   
    public Pacman(){
        b.requestFocus();
        /* Crear y configura marco de la ventana. */
        JFrame f=new JFrame(); 

        f.setSize(420,460);
        /* Anade el tablero a la estructura */
        f.add(b,BorderLayout.CENTER);
        b.addMouseListener(this);  
        b.addKeyListener(this);  
        /* Hace visible el marco, desactivar el cambio de tamano */
        f.setVisible(true);
        f.setResizable(false);
        b.New=1;
        stepFrame(true);
        /* Crear un temporizador que llama STEPFRAME cada 30 milisegundos. */
        frameTimer = new javax.swing.Timer(35,new ActionListener(){
            public void actionPerformed(ActionEvent e){
              stepFrame(false);
            }
          });
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* Comienza el timer */
        frameTimer.start();
        b.requestFocus();
    }
    /* Esta funcion repaint vuelve a dibujar solo las partes de la pantalla que puede haber cambiado.
    Es decir, el area alrededor de cada jugador fantasma y las barras de menu.
    */
    public void repaint(){
        if (b.player.teleport==true){
          b.repaint(b.player.lastX-20,b.player.lastY-20,80,80);
          b.player.teleport=false;
        }
        b.repaint(0,0,600,20);
        b.repaint(0,420,600,40);
        b.repaint(b.player.x-20,b.player.y-20,80,80);
        b.repaint(b.ghost1.x-20,b.ghost1.y-20,80,80);
        b.repaint(b.ghost2.x-20,b.ghost2.y-20,80,80);
        b.repaint(b.ghost3.x-20,b.ghost3.y-20,80,80);
        b.repaint(b.ghost4.x-20,b.ghost4.y-20,80,80);
        //b.repaint(b.ghost5.x-20,b.ghost5.y-20,80,80);
        //b.repaint(b.ghost6.x-20,b.ghost6.y-20,80,80);
        //b.repaint(b.ghost7.x-20,b.ghost7.y-20,80,80);
        //b.repaint(b.ghost8.x-20,b.ghost8.y-20,80,80);

    }
    /* Los pasos de la pantalla hacia adelante un cuadro. */
    public void stepFrame(boolean New){
        /* Si no estamos en una pantalla especial,los temporizadores se establecen en -1 para desactivarlas. */
        if (!b.titleScreen && !b.winScreen && !b.overScreen){
          timer = -1;
          titleTimer = -1;
        }
        /* Si estamos jugando la animacion de morir, seguir avanzando marcos hasta que la animacion es completa. */
        if (b.dying>0){
          b.repaint();
          return;
        }
        New = New || (b.New !=0) ;
        /*Si esta es la pantalla de titulo, se asegura de quedarse solo en la pantalla de titulo durante 5 segundos.
        Si despues de 5 segundos, el usuario no ha comenzado un juego, iniciar el modo de demostracion.*/
        if (b.titleScreen){
            if (titleTimer == -1){
                titleTimer = System.currentTimeMillis();
            }
            long currTime = System.currentTimeMillis();
            if (currTime - titleTimer >= 5000){
                b.titleScreen = false;
                b.demo = true;
                titleTimer = -1;
            }
            b.repaint();
            return;
        }
        /* Si esta es la pantalla de victoria o juego sobre la pantalla, se asegura de quedarse solo en la pantalla durante 5 segundos.
        Si despues de 5 segundos, el usuario no ha pulsado una tecla, se va a la pantalla de titulo. */
        else if (b.winScreen || b.overScreen){
            if (timer == -1){
              timer = System.currentTimeMillis();
            }
            long currTime = System.currentTimeMillis();
            if (currTime - timer >= 5000){
              b.winScreen = false;
              b.overScreen = false;
              b.titleScreen = true;
              timer = -1;
            }
            b.repaint();
            return;
        }
        /* If we have a normal game state, move all pieces and update pellet status */
        if (!New){
          /* The pacman player has two functions, demoMove if we're in demo mode and move if we're in
             user playable mode.  Call the appropriate one here */
          if (b.demo){
            b.player.demoMove();
          }else{
            b.player.move();
          }
          /* Also move the ghosts, and update the pellet states */
          b.ghost1.getPosition(b.player);
          b.ghost1.move1(); 
          b.ghost2.getPosition(b.player);
          b.ghost2.move1();
          b.ghost3.getPosition(b.player);
          b.ghost3.move1();
          //b.ghost4.getPosition(b.player);
          //b.ghost4.move1();

          b.ghost4.getPosition(b.player);
          b.ghost4.move();
        
          //b.ghost5.getPosition(b.player);
          //b.ghost5.move1();
          //b.ghost6.getPosition(b.player);
          //b.ghost6.move1();
          //b.ghost7.getPosition(b.player);
          //b.ghost7.move1();
          //b.ghost8.getPosition(b.player);
          //b.ghost8.move1();

          b.player.updatePellet();
          b.ghost1.updatePellet();
          b.ghost2.updatePellet();
          b.ghost3.updatePellet();
          b.ghost4.updatePellet();
          //b.ghost5.updatePellet();
          //b.ghost6.updatePellet();
          //b.ghost7.updatePellet();
          //b.ghost8.updatePellet();

        }
        
        /* We either have a new game or the user has died, either way we have to reset the board */
        if (b.stopped || New){
            /*Temporarily stop advancing frames */
            frameTimer.stop();
            /* If user is dying ... */
            while (b.dying >0){
              /* Play dying animation. */
              stepFrame(false);
            }

            /* Move all game elements back to starting positions and orientations */
            b.player.currDirection='L';
            b.player.direction='L';
            b.player.desiredDirection='L';
            b.player.x = 200;
            b.player.y = 300;
            
            b.ghost1.x = 180;
            b.ghost1.y = 180;
            
            b.ghost2.x = 200;
            b.ghost2.y = 180;
            
            b.ghost3.x = 220;
            b.ghost3.y = 180;
            
            b.ghost4.x = 220;
            b.ghost4.y = 180;
            
            //b.ghost5.x = 220;
            //b.ghost5.y = 180;

            //b.ghost6.x = 220;
            //b.ghost6.y = 180;
            
            //b.ghost7.x = 220;
            //b.ghost7.y = 180;
            
            //b.ghost8.x = 220;
            //b.ghost8.y = 180;

            /* Advance a frame to display main state */
            b.repaint(0,0,600,600);
            /* Iniciar avanzar marcos, una vez mas. */
            b.stopped=false;
            frameTimer.start();
        }
        else{
            repaint(); 
        }
    }

  /* Handles user key presses*/
    public void keyPressed(KeyEvent e){
        /* Pressing a key in the title screen starts a game */
        if (b.titleScreen){
          b.titleScreen = false;
          return;
        }
        /* Pressing a key in the win screen or game over screen goes to the title screen */
        else if (b.winScreen || b.overScreen){
          b.titleScreen = true;
          b.winScreen = false;
          b.overScreen = false;
          return;
        }
        /* Al pulsar una tecla durante una demostracion mata al modo de demostracion y comienza un nuevo juego. */
        else if (b.demo){
            b.demo=false;
            b.sounds.nomNomStop();
            b.New=1;
            return;
        }
        /* De lo contrario, las pulsaciones de teclas controlan el jugador. */ 
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
             b.player.desiredDirection='L';
             break;
            case KeyEvent.VK_RIGHT:
             b.player.desiredDirection='R';
             break;
            case KeyEvent.VK_UP:
             b.player.desiredDirection='U';
             break;
            case KeyEvent.VK_DOWN:
             b.player.desiredDirection='D';
             break;
        }
        repaint();
    }

  /* Esta funcion detecta el usuario hace clic en los elementos de menu en la parte inferior de la pantalla. */
    public void mousePressed(MouseEvent e){
        if (b.titleScreen || b.winScreen || b.overScreen){
          /* Si no estamos en el juego donde un menu, ignorar clics. */
          return;
        }
        /* Obtener coordenadas de clic. */
        int x = e.getX();
        int y = e.getY();
        if ( 400 <= y && y <= 460){
            if ( 100 <= x && x <= 150){
                /* Nuevo juego se ha hecho click. */
                b.New = 1;
            }else if (180 <= x && x <= 300){
                /* Puntajes altos claros ha hecho click. */
                b.clearHighScores();
            }else if (350 <= x && x <= 420){
                /* Salir ha hecho click. */
                System.exit(0);
            }
        }
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    /* Funcion principal,simplemente crea una nueva instancia de pacman.*/
    public static void main(String [] args){
        Pacman c = new Pacman();
    }
}