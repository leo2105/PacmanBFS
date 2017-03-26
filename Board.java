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

/*This board class contains the player, ghosts, pellets, and most of the game logic.*/

public class Board extends JPanel
{
  Image pacmanImage = Toolkit.getDefaultToolkit().getImage("img/pacman.jpg"); 
  /*Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg"); 
  Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg"); 
  Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg"); 
  Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg"); */
  Image ghost10 = Toolkit.getDefaultToolkit().getImage("img/ghost10.jpg"); 
  Image ghost20 = Toolkit.getDefaultToolkit().getImage("img/ghost20.jpg"); 
  Image ghost30 = Toolkit.getDefaultToolkit().getImage("img/ghost30.jpg"); 
  Image ghost40 = Toolkit.getDefaultToolkit().getImage("img/ghost40.jpg"); 
  Image ghost50 = Toolkit.getDefaultToolkit().getImage("img/ghost10.jpg"); 
  Image ghost60 = Toolkit.getDefaultToolkit().getImage("img/ghost20.jpg"); 
  Image ghost70 = Toolkit.getDefaultToolkit().getImage("img/ghost30.jpg"); 
  Image ghost80 = Toolkit.getDefaultToolkit().getImage("img/ghost40.jpg"); 

  Image ghost11 = Toolkit.getDefaultToolkit().getImage("img/ghost11.jpg"); 
  Image ghost21 = Toolkit.getDefaultToolkit().getImage("img/ghost21.jpg"); 
  Image ghost31 = Toolkit.getDefaultToolkit().getImage("img/ghost31.jpg"); 
  Image ghost41 = Toolkit.getDefaultToolkit().getImage("img/ghost41.jpg"); 
  Image ghost51 = Toolkit.getDefaultToolkit().getImage("img/ghost11.jpg"); 
  Image ghost61 = Toolkit.getDefaultToolkit().getImage("img/ghost21.jpg"); 
  Image ghost71 = Toolkit.getDefaultToolkit().getImage("img/ghost31.jpg"); 
  Image ghost81 = Toolkit.getDefaultToolkit().getImage("img/ghost41.jpg"); 

  Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg"); 
  Image gameOverImage = Toolkit.getDefaultToolkit().getImage("img/gameOver.jpg"); 
  Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

  /* crea un player y los 4 fantasmas */
  Player player = new Player(200,300);
  Ghost ghost1 = new Ghost(180,180);
  Ghost ghost2 = new Ghost(200,180);
  Ghost ghost3 = new Ghost(220,180);
  Ghost ghost4 = new Ghost(220,180);
  //Ghost ghost5 = new Ghost(220,180);
  //Ghost ghost6 = new Ghost(220,180);
  //Ghost ghost7 = new Ghost(220,180);
  //Ghost ghost8 = new Ghost(220,180);

  
  long timer = System.currentTimeMillis();

  int dying=0;
 
  /*informacion del puntaje */
  int currScore;
  int highScore;

  /*si es true se cambia de score*/
  boolean clearHighScores= false;

  int numLives=2;

  /*el estado del mapas*/
  boolean[][] state;

  /*estado de cada bolita*/
  boolean[][] pellets;

  /* dimensiones del juego */
  int gridSize;
  int max;

  /* banderas de estado*/
  boolean stopped;
  boolean titleScreen;
  boolean winScreen = false;
  boolean overScreen = false;
  boolean demo = false;
  int New;

  /* para los sonidos */
  GameSounds sounds;

  int lastPelletEatenX = 0;
  int lastPelletEatenY=0;

  Font font = new Font("Monospaced",Font.BOLD, 12);

  public Board() 
  {
    initHighScores();
    sounds = new GameSounds();
    currScore=0;
    stopped=false;
    max=400;
    gridSize=20;
    New=0;
    titleScreen = true;
  }

  /*lee el score y lo guarda */
  public void initHighScores()
  {
    File file = new File("highScores.txt");
    Scanner sc;
    try
    {
        sc = new Scanner(file);
        highScore = sc.nextInt();
        sc.close();
    }
    catch(Exception e)
    {
    }
  }

  /*actualiza el score */
  public void updateScore(int score)
  {
    PrintWriter out;
    try
    {
      out = new PrintWriter("highScores.txt");
      out.println(score);
      out.close();
    }
    catch(Exception e)
    {
    }
    highScore=score;
    clearHighScores=true;
  }

  /* limpia los scores */
  public void clearHighScores()
  {
    PrintWriter out;
    try
    {
      out = new PrintWriter("highScores.txt");
      out.println("0");
      out.close();
    }
    catch(Exception e)
    {
    }
    highScore=0;
    clearHighScores=true;
  }

  /* reset cuando se empieza nuevo juego*/
  public void reset()
  {
    numLives=2;
    state = new boolean[20][20];
    pellets = new boolean[20][20];

    for(int i=0;i<20;i++)
    {
      for(int j=0;j<20;j++)
      {
        state[i][j]=true;
        pellets[i][j]=true;
      }
    }

    for(int i = 5;i<14;i++)
    {
      for(int j = 5;j<12;j++)
      {
        pellets[i][j]=false;
      }
    }
    pellets[9][7] = false;
    pellets[8][8] = false;
    pellets[9][8] = false;
    pellets[10][8] = false;

  }


/*actualiza el mapa*/
  public void updateMap(int x,int y, int width, int height)
  {
    for (int i =x/gridSize; i<x/gridSize+width/gridSize;i++)
    {
      for (int j=y/gridSize;j<y/gridSize+height/gridSize;j++)
      {
        state[i-1][j-1]=false;
        pellets[i-1][j-1]=false;
      }
    }
  } 

/*dibuja las vidas*/
  public void drawLives(Graphics g)
  {
    g.setColor(Color.BLACK);

    g.fillRect(0,max+5,600,gridSize);
    g.setColor(Color.WHITE);
    for(int i = 0;i<numLives;i++)
    {
      /*dibuja las vidas */
      g.fillOval(gridSize*(i+1),max+5,gridSize,gridSize);
    }
    /* El menu */
    g.setColor(Color.WHITE);
    g.setFont(font);
    g.drawString("Reset",100,max+5+gridSize);
    g.drawString("Clear High Scores",180,max+5+gridSize);
    g.drawString("Exit",350,max+5+gridSize);
  }
  
  /*aqui se dibuja el mapa*/
  public void drawBoard(Graphics g)
  {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,600);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,420,420);
        
        g.setColor(Color.BLACK);
        g.fillRect(0,0,20,600);
        g.fillRect(0,0,600,20);
        g.setColor(Color.WHITE);
        g.drawRect(19,19,382,382);
        g.setColor(Color.BLUE);

        g.fillRect(40,40,60,20);
          updateMap(40,40,60,20);
        g.fillRect(120,40,60,20);
          updateMap(120,40,60,20);
        g.fillRect(200,20,20,40);
          updateMap(200,20,20,40);
        g.fillRect(240,40,60,20);
          updateMap(240,40,60,20);
        g.fillRect(320,40,60,20);
          updateMap(320,40,60,20);
        g.fillRect(40,80,60,20);
          updateMap(40,80,60,20);
        g.fillRect(160,80,100,20);
          updateMap(160,80,100,20);
        g.fillRect(200,80,20,60);
          updateMap(200,80,20,60);
        g.fillRect(320,80,60,20);
          updateMap(320,80,60,20);

        g.fillRect(20,120,80,60);
          updateMap(20,120,80,60);
        g.fillRect(320,120,80,60);
          updateMap(320,120,80,60);
        g.fillRect(20,200,80,60);
          updateMap(20,200,80,60);
        g.fillRect(320,200,80,60);
          updateMap(320,200,80,60);

        g.fillRect(160,160,40,20);
          updateMap(160,160,40,20);
        g.fillRect(220,160,40,20);
          updateMap(220,160,40,20);
        g.fillRect(160,180,20,20);
          updateMap(160,180,20,20);
        g.fillRect(160,200,100,20);
          updateMap(160,200,100,20);
        g.fillRect(240,180,20,20);
          updateMap(240,180,20,20);
        g.setColor(Color.BLUE);

        //#AQUI
        g.fillRect(20, 180, 80, 20);
          updateMap(20, 180,80,20);
        g.fillRect(320,180,80,20);
          updateMap(320,180,80,20);
        
        g.fillRect(120,120,60,20);
          updateMap(120,120,60,20);
        g.fillRect(120,80,20,100);
          updateMap(120,80,20,100);
        g.fillRect(280,80,20,100);
          updateMap(280,80,20,100);
        g.fillRect(240,120,60,20);
          updateMap(240,120,60,20);

        g.fillRect(280,200,20,60);
          updateMap(280,200,20,60);
        g.fillRect(120,200,20,60);
          updateMap(120,200,20,60);
        g.fillRect(160,240,100,20);
          updateMap(160,240,100,20);
        g.fillRect(200,260,20,40);
          updateMap(200,260,20,40);

        g.fillRect(120,280,60,20);
          updateMap(120,280,60,20);
        g.fillRect(240,280,60,20);
          updateMap(240,280,60,20);

        g.fillRect(40,280,60,20);
          updateMap(40,280,60,20);
        g.fillRect(80,280,20,60);
          updateMap(80,280,20,60);
        g.fillRect(320,280,60,20);
          updateMap(320,280,60,20);
        g.fillRect(320,280,20,60);
          updateMap(320,280,20,60);

        g.fillRect(20,320,40,20);
          updateMap(20,320,40,20);
        g.fillRect(360,320,40,20);
          updateMap(360,320,40,20);
        g.fillRect(160,320,100,20);
          updateMap(160,320,100,20);
        g.fillRect(200,320,20,60);
          updateMap(200,320,20,60);

        g.fillRect(40,360,140,20);
          updateMap(40,360,140,20);
        g.fillRect(240,360,140,20);
          updateMap(240,360,140,20);
        g.fillRect(280,320,20,40);
          updateMap(280,320,20,60);
        g.fillRect(120,320,20,60);
          updateMap(120,320,20,60);
        drawLives(g);
  } 


  /*dibuja las bolitas */
  public void drawPellets(Graphics g)
  {
        g.setColor(Color.WHITE);
        for (int i=1;i<20;i++)
        {
          for (int j=1;j<20;j++)
          {
            if ( pellets[i-1][j-1])
            g.fillOval(i*20+8,j*20+8,4,4);
          }
        }
      
  }

  /* dibuja cada bolita*/
  public void fillPellet(int x, int y, Graphics g)
  {
    g.setColor(Color.WHITE);
    g.fillOval(x*20+28,y*20+28,4,4);
  }

  /* esta funcion dibuja todo el juego */
  public void paint(Graphics g)
  { 
    if (dying > 0)
    {
      sounds.nomNomStop();

      /* dibujar player */
      g.drawImage(pacmanImage,player.x,player.y,Color.BLACK,null);
      g.setColor(Color.BLACK);
      
      /* cuando muere pacman */
      if (dying == 4)
        g.fillRect(player.x,player.y,20,7);
      else if ( dying == 3)
        g.fillRect(player.x,player.y,20,14);
      else if ( dying == 2)
        g.fillRect(player.x,player.y,20,20); 
      else if ( dying == 1)
      {
        g.fillRect(player.x,player.y,20,20); 
      }
     
      long currTime = System.currentTimeMillis();
      long temp;
      if (dying != 1)
        temp = 100;
      else
        temp = 2000;
      if (currTime - timer >= temp)
      {
        dying--;
        timer = currTime;
        if (dying == 0)
        {
          if (numLives==-1)
          {
            /* modo demo tiene infitas vidas*/
            if (demo)
              numLives=2;
            else
            {
            /*cuando es gameover*/
              if (currScore > highScore)
              {
                updateScore(currScore);
              }
              overScreen=true;
            }
          }
        }
      }
      return;
    }
/*imagen deportada */
    if (titleScreen)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,600);
      g.drawImage(titleScreenImage,0,0,Color.BLACK,null);

      sounds.nomNomStop();
      New = 1;
      return;
    } 

    /*imagen cuando ganas */
    else if (winScreen)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,600);
      g.drawImage(winScreenImage,0,0,Color.BLACK,null);
      New = 1;
      sounds.nomNomStop();
      return;
    }

    /* cuando pieres*/
    else if (overScreen)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,600);
      g.drawImage(gameOverImage,0,0,Color.BLACK,null);
      New = 1;
      sounds.nomNomStop();
      return;
    }

    /* redibujar el menu para actualizar el highscore */
    if (clearHighScores)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,18);
      g.setColor(Color.WHITE);
      g.setFont(font);
      clearHighScores= false;
      if (demo)
        g.drawString("Presionar una tecla\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);
    }
   
    /*oops es true cuando se pierde una vida */ 
    boolean oops=false;
    
    /*empezar nuevo juego */
    if (New==1)
    {
      reset();
      player = new Player(200,300);
      ghost1 = new Ghost(180,180);
      ghost2 = new Ghost(200,180);
      ghost3 = new Ghost(220,180);
      ghost4 = new Ghost(220,180);
      //ghost5 = new Ghost(220,180);
      //ghost6 = new Ghost(220,180);
      //ghost7 = new Ghost(220,180);
      //ghost8 = new Ghost(220,180);
      currScore = 0;
      drawBoard(g);
      drawPellets(g);
      drawLives(g);
      /* actualizar el map\E1 */
      player.updateState(state);
      /* player no puede entrar en la caja de los fantasmas*/
      player.state[9][7]=false; 
      ghost1.updateState(state);
      ghost2.updateState(state);
      ghost3.updateState(state);
      ghost4.updateState(state);
      //ghost5.updateState(state);
	  //ghost6.updateState(state);
      //ghost7.updateState(state);
      //ghost8.updateState(state);
   
      /*dibujar el menu de arriba*/
      g.setColor(Color.WHITE);
      g.setFont(font);
      if (demo)
        g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);
      New++;
    }
    else if (New == 2)
    {
      New++;
    }
    else if (New == 3)
    {
      New++;
      sounds.newGame();
      timer = System.currentTimeMillis();
      return;
    }
    else if (New == 4)
    {
      long currTime = System.currentTimeMillis();
      if (currTime - timer >= 5000)
      {
        New=0;
      }
      else
        return;
    }
    
    g.copyArea(player.x-20,player.y-20,80,80,0,0);
    g.copyArea(ghost1.x-20,ghost1.y-20,80,80,0,0);
    g.copyArea(ghost2.x-20,ghost2.y-20,80,80,0,0);
    g.copyArea(ghost3.x-20,ghost3.y-20,80,80,0,0);
    g.copyArea(ghost4.x-20,ghost4.y-20,80,80,0,0);
    //g.copyArea(ghost5.x-20,ghost5.y-20,80,80,0,0);
    //g.copyArea(ghost6.x-20,ghost6.y-20,80,80,0,0);
    //g.copyArea(ghost7.x-20,ghost7.y-20,80,80,0,0);
    //g.copyArea(ghost8.x-20,ghost8.y-20,80,80,0,0);

    /* Detectar colisiones */
    if (player.x == ghost1.x && Math.abs(player.y-ghost1.y) < 10)
      oops=true;
    else if (player.x == ghost2.x && Math.abs(player.y-ghost2.y) < 10)
      oops=true;
    else if (player.x == ghost3.x && Math.abs(player.y-ghost3.y) < 10)
      oops=true;
    else if (player.x == ghost4.x && Math.abs(player.y-ghost4.y) < 10)
      oops=true;
    /*else if (player.x == ghost5.x && Math.abs(player.y-ghost5.y) < 10)
      oops=true;	
    else if (player.x == ghost6.x && Math.abs(player.y-ghost6.y) < 10)
      oops=true;
    else if (player.x == ghost7.x && Math.abs(player.y-ghost7.y) < 10)
      oops=true;
    else if (player.x == ghost8.x && Math.abs(player.y-ghost8.y) < 10)
	 oops=true;
    */else if (player.y == ghost1.y && Math.abs(player.x-ghost1.x) < 10)
      oops=true;
    else if (player.y == ghost2.y && Math.abs(player.x-ghost2.x) < 10)
      oops=true;
    else if (player.y == ghost3.y && Math.abs(player.x-ghost3.x) < 10)
      oops=true;
    else if (player.y == ghost4.y && Math.abs(player.x-ghost4.x) < 10)
      oops=true;
    /*else if (player.y == ghost5.y && Math.abs(player.x-ghost5.x) < 10)
      oops=true;
    else if (player.y == ghost6.y && Math.abs(player.x-ghost6.x) < 10)
      oops=true;
    else if (player.y == ghost7.y && Math.abs(player.x-ghost7.x) < 10)
      oops=true;
    else if (player.y == ghost8.y && Math.abs(player.x-ghost8.x) < 10)
      oops=true;
    */
    /* cuandp muere player */
    if (oops && !stopped)
    {
      dying=4;
      
      sounds.death();
      
      sounds.nomNomStop();

      /*actualizar el numero de vidas */
      numLives--;
      stopped=true;
      drawLives(g);
      timer = System.currentTimeMillis();
    }

    /* borrar jugador y fantasmas */
    g.setColor(Color.BLACK);
    g.fillRect(player.lastX,player.lastY,20,20);
    g.fillRect(ghost1.lastX,ghost1.lastY,20,20);
    g.fillRect(ghost2.lastX,ghost2.lastY,20,20);
    g.fillRect(ghost3.lastX,ghost3.lastY,20,20);
    g.fillRect(ghost4.lastX,ghost4.lastY,20,20);
    /*g.fillRect(ghost5.lastX,ghost5.lastY,20,20);
    g.fillRect(ghost6.lastX,ghost6.lastY,20,20);
    g.fillRect(ghost7.lastX,ghost7.lastY,20,20);
    g.fillRect(ghost8.lastX,ghost8.lastY,20,20);
    */
    /* comer bolitas */
    if ( pellets[player.pelletX][player.pelletY] && New!=2 && New !=3)
    {
      lastPelletEatenX = player.pelletX;
      lastPelletEatenY = player.pelletY;

      /* Play eating sound */
      sounds.nomNom();
      
      /* incrementar el numero de bolas comidas */
      player.pelletsEaten++;

      /* borrar una  bolita*/
      pellets[player.pelletX][player.pelletY]=false;

      /* Incrementar el  score */
      currScore += 50;

      /* actualizar el score */
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,20);
      g.setColor(Color.WHITE);
      g.setFont(font);
      if (demo)
        g.drawString("Presiona una tecla\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);

      if (player.pelletsEaten == 165)
      {
        if (!demo)
        {
          if (currScore > highScore)
          {
            updateScore(currScore);
          }
          winScreen = true;
        }
        else
        {
          titleScreen = true;
        }
        return;
      }
    }

    /* si no hay bolitas no hay sonido */
    else if ( (player.pelletX != lastPelletEatenX || player.pelletY != lastPelletEatenY ) || player.stopped)
    {
      /* detener sonido de comida */
      sounds.nomNomStop();
    }


    /* para reemplazar las bolitas cuando el fantasma esta encima */
    if ( pellets[ghost1.lastPelletX][ghost1.lastPelletY])
      fillPellet(ghost1.lastPelletX,ghost1.lastPelletY,g);
    if ( pellets[ghost2.lastPelletX][ghost2.lastPelletY])
      fillPellet(ghost2.lastPelletX,ghost2.lastPelletY,g);
    if ( pellets[ghost3.lastPelletX][ghost3.lastPelletY])
      fillPellet(ghost3.lastPelletX,ghost3.lastPelletY,g);
    if ( pellets[ghost4.lastPelletX][ghost4.lastPelletY])
      fillPellet(ghost4.lastPelletX,ghost4.lastPelletY,g);
    /*if ( pellets[ghost5.lastPelletX][ghost5.lastPelletY])
      fillPellet(ghost5.lastPelletX,ghost5.lastPelletY,g);
    if ( pellets[ghost6.lastPelletX][ghost6.lastPelletY])
      fillPellet(ghost6.lastPelletX,ghost6.lastPelletY,g);
    if ( pellets[ghost7.lastPelletX][ghost7.lastPelletY])
      fillPellet(ghost7.lastPelletX,ghost7.lastPelletY,g);
    if ( pellets[ghost8.lastPelletX][ghost8.lastPelletY])
      fillPellet(ghost8.lastPelletX,ghost8.lastPelletY,g);
    */

    /*Dibuja los fantasmas */
    if (ghost1.frameCount < 5)
    {
      /*primer frame de los fantasmas */
      g.drawImage(ghost10,ghost1.x,ghost1.y,Color.BLACK,null);
      g.drawImage(ghost20,ghost2.x,ghost2.y,Color.BLACK,null);
      g.drawImage(ghost30,ghost3.x,ghost3.y,Color.BLACK,null);
      g.drawImage(ghost40,ghost4.x,ghost4.y,Color.BLACK,null);
      /*g.drawImage(ghost50,ghost5.x,ghost5.y,Color.BLACK,null);
      g.drawImage(ghost60,ghost6.x,ghost6.y,Color.BLACK,null);
      g.drawImage(ghost70,ghost7.x,ghost7.y,Color.BLACK,null);
      g.drawImage(ghost80,ghost8.x,ghost8.y,Color.BLACK,null);
    */
      ghost1.frameCount++;
    }
    else
    {
      /* segundo frame de los fantasmas */
      g.drawImage(ghost11,ghost1.x,ghost1.y,Color.BLACK,null);
      g.drawImage(ghost21,ghost2.x,ghost2.y,Color.BLACK,null);
      g.drawImage(ghost31,ghost3.x,ghost3.y,Color.BLACK,null);
      g.drawImage(ghost41,ghost4.x,ghost4.y,Color.BLACK,null);
      //g.drawImage(ghost51,ghost5.x,ghost5.y,Color.BLACK,null);
      //g.drawImage(ghost61,ghost6.x,ghost6.y,Color.BLACK,null);
      //g.drawImage(ghost71,ghost7.x,ghost7.y,Color.BLACK,null);
      //g.drawImage(ghost81,ghost8.x,ghost8.y,Color.BLACK,null);
      if (ghost1.frameCount >=10)
        ghost1.frameCount=0;
      else
        ghost1.frameCount++;
    }

    /* dibuja a pacman */
    if (player.frameCount < 5)
    {
      /* boca cerrada */
      g.drawImage(pacmanImage,player.x,player.y,Color.BLACK,null);
    }else{
        /* dibuja al pacman*/
        if (player.frameCount >=10)
          player.frameCount=0;

        g.drawImage(pacmanImage,player.x,player.y,Color.BLACK,null);

    }

    /* para corregir el borde */
    g.setColor(Color.WHITE);
    g.drawRect(19,19,382,382);

  }
}
