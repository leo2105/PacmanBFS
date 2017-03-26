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

/* clase fantasma. */
class Ghost extends Mover{
    final int MAX = 400;
    int ady[][] = new int[ MAX ][ MAX ];     //matriz de adyacencia
    Scanner sc = new Scanner( System.in );
    int V, prev[] = new int[ MAX ];
    int[][] mP = new int[20][20];
            
    int posX,posY,x0,y0;
    /*direccion del fantasma */
    char direction;

  /*posicion anterior*/
    int lastX;
    int lastY;

  /* posicion actual */
    int x;
    int y;
  
    public int px;
    public int py;
          
    int pelletX,pelletY;

    int lastPelletX,lastPelletY;

  /*constructor de ghost*/  
    public Ghost(int x, int y){
        mP[0][0]=1;   mP[0][1]=1;  mP[0][2]=1;  mP[0][3]=1;  mP[0][4]=1;  mP[0][5]=1;   mP[0][6]=1;   mP[0][7]=1;  mP[0][8]=1;  mP[0][9]=-1; mP[0][10]=1;  mP[0][11]=1;  mP[0][12]=1;  mP[0][13]=1;  mP[0][14]=1;  mP[0][15]=1;  mP[0][16]=1;  mP[0][17]=1;  mP[0][18]=1;  mP[0][19]=-1;
        mP[1][0]=1;   mP[1][1]=-1; mP[1][2]=-1; mP[1][3]=-1; mP[1][4]=1;  mP[1][5]=-1;  mP[1][6]=-1;  mP[1][7]=-1; mP[1][8]=1;  mP[1][9]=-1; mP[1][10]=1;  mP[1][11]=-1; mP[1][12]=-1; mP[1][13]=-1; mP[1][14]=1;  mP[1][15]=-1; mP[1][16]=-1; mP[1][17]=-1; mP[1][18]=1;  mP[1][19]=-1; 
        mP[2][0]=1;   mP[2][1]=1;  mP[2][2]=1;  mP[2][3]=1;  mP[2][4]=1;  mP[2][5]=1;   mP[2][6]=1;   mP[2][7]=1;  mP[2][8]=1;  mP[2][9]=1;  mP[2][10]=1;  mP[2][11]=1;  mP[2][12]=1;  mP[2][13]=1;  mP[2][14]=1;  mP[2][15]=1;  mP[2][16]=1;  mP[2][17]=1;  mP[2][18]=1;  mP[2][19]=-1;
        mP[3][0]=1;   mP[3][1]=-1; mP[3][2]=-1; mP[3][3]=-1; mP[3][4]=1;  mP[3][5]=-1;  mP[3][6]=1;   mP[3][7]=-1; mP[3][8]=-1; mP[3][9]=-1; mP[3][10]=-1; mP[3][11]=-1; mP[3][12]=1;  mP[3][13]=-1; mP[3][14]=1;  mP[3][15]=-1; mP[3][16]=-1; mP[3][17]=-1; mP[3][18]=1;  mP[3][19]=-1; 
        mP[4][0]=1;   mP[4][1]=1;  mP[4][2]=1;  mP[4][3]=1;  mP[4][4]=1;  mP[4][5]=-1;  mP[4][6]=1;   mP[4][7]=1;  mP[4][8]=1;  mP[4][9]=-1; mP[4][10]=1;  mP[4][11]=1;  mP[4][12]=1;  mP[4][13]=-1; mP[4][14]=1;  mP[4][15]=1;  mP[4][16]=1;  mP[4][17]=1;  mP[4][18]=1;  mP[4][19]=-1;
        mP[5][0]=-1;  mP[5][1]=-1; mP[5][2]=-1; mP[5][3]=-1; mP[5][4]=1;  mP[5][5]=-1;  mP[5][6]=-1;  mP[5][7]=-1; mP[5][8]=1;  mP[5][9]=-1; mP[5][10]=1;  mP[5][11]=-1; mP[5][12]=-1; mP[5][13]=-1; mP[5][14]=1;  mP[5][15]=-1; mP[5][16]=-1; mP[5][17]=-1; mP[5][18]=-1; mP[5][19]=-1;
        mP[6][0]=-1;  mP[6][1]=-1; mP[6][2]=-1; mP[6][3]=-1; mP[6][4]=1;  mP[6][5]=-1;  mP[6][6]=1;   mP[6][7]=1;  mP[6][8]=1;  mP[6][9]=1;  mP[6][10]=1;  mP[6][11]=1;  mP[6][12]=1;  mP[6][13]=-1; mP[6][14]=1;  mP[6][15]=-1; mP[6][16]=-1; mP[6][17]=-1; mP[6][18]=-1; mP[6][19]=-1;
        mP[7][0]=-1;  mP[7][1]=-1; mP[7][2]=-1; mP[7][3]=-1; mP[7][4]=1;  mP[7][5]=-1;  mP[7][6]=1;   mP[7][7]=-1; mP[7][8]=-1; mP[7][9]=1;  mP[7][10]=-1; mP[7][11]=-1; mP[7][12]=1;  mP[7][13]=-1; mP[7][14]=1;  mP[7][15]=-1; mP[7][16]=-1; mP[7][17]=-1; mP[7][18]=-1; mP[7][19]=-1; 
        mP[8][0]=1;   mP[8][1]=1;  mP[8][2]=1;  mP[8][3]=1;  mP[8][4]=1;  mP[8][5]=1;   mP[8][6]=1;   mP[8][7]=-1; mP[8][8]=1;  mP[8][9]=1;  mP[8][10]=1;  mP[8][11]=-1; mP[8][12]=1;  mP[8][13]=1;  mP[8][14]=1;  mP[8][15]=1;  mP[8][16]=1;  mP[8][17]=1;  mP[8][18]=1;  mP[8][19]=-1;
        mP[9][0]=-1;  mP[9][1]=-1; mP[9][2]=-1; mP[9][3]=-1; mP[9][4]=1;  mP[9][5]=-1;  mP[9][6]=1;   mP[9][7]=-1; mP[9][8]=-1; mP[9][9]=-1; mP[9][10]=-1; mP[9][11]=-1; mP[9][12]=1;  mP[9][13]=-1; mP[9][14]=1;  mP[9][15]=-1; mP[9][16]=-1; mP[9][17]=-1; mP[9][18]=-1; mP[9][19]=-1;
        mP[10][0]=-1; mP[10][1]=-1;mP[10][2]=-1;mP[10][3]=-1;mP[10][4]=1; mP[10][5]=-1; mP[10][6]=1;  mP[10][7]=1; mP[10][8]=1; mP[10][9]=1; mP[10][10]=1; mP[10][11]=1; mP[10][12]=1; mP[10][13]=-1;mP[10][14]=1; mP[10][15]=-1;mP[10][16]=-1;mP[10][17]=-1;mP[10][18]=-1;mP[10][19]=-1; 
        mP[11][0]=-1; mP[11][1]=-1;mP[11][2]=-1;mP[11][3]=-1;mP[11][4]=1; mP[11][5]=-1; mP[11][6]=1;  mP[11][7]=-1;mP[11][8]=-1;mP[11][9]=-1;mP[11][10]=-1;mP[11][11]=-1;mP[11][12]=1; mP[11][13]=-1;mP[11][14]=1; mP[11][15]=-1;mP[11][16]=-1;mP[11][17]=-1;mP[11][18]=-1;mP[11][19]=-1; 
        mP[12][0]=1;  mP[12][1]=1; mP[12][2]=1; mP[12][3]=1; mP[12][4]=1; mP[12][5]=1;  mP[12][6]=1;  mP[12][7]=1; mP[12][8]=1; mP[12][9]=-1;mP[12][10]=1; mP[12][11]=1; mP[12][12]=1; mP[12][13]=1; mP[12][14]=1; mP[12][15]=1; mP[12][16]=1; mP[12][17]=1; mP[12][18]=1; mP[12][19]=-1;
        mP[13][0]=1;  mP[13][1]=-1;mP[13][2]=-1;mP[13][3]=-1;mP[13][4]=1; mP[13][5]=-1; mP[13][6]=-1; mP[13][7]=-1;mP[13][8]=1; mP[13][9]=-1;mP[13][10]=1; mP[13][11]=-1;mP[13][12]=-1;mP[13][13]=-1;mP[13][14]=1; mP[13][15]=-1;mP[13][16]=-1;mP[13][17]=-1;mP[13][18]=1; mP[13][19]=-1; 
        mP[14][0]=1;  mP[14][1]=1; mP[14][2]=1; mP[14][3]=-1;mP[14][4]=1; mP[14][5]=1;  mP[14][6]=1;  mP[14][7]=1; mP[14][8]=1; mP[14][9]=1; mP[14][10]=1; mP[14][11]=1; mP[14][12]=1; mP[14][13]=1; mP[14][14]=1; mP[14][15]=-1;mP[14][16]=1; mP[14][17]=1; mP[14][18]=1; mP[14][19]=-1;
        mP[15][0]=-1; mP[15][1]=-1;mP[15][2]=1; mP[15][3]=-1;mP[15][4]=1; mP[15][5]=-1; mP[15][6]=1;  mP[15][7]=-1;mP[15][8]=-1;mP[15][9]=-1;mP[15][10]=-1;mP[15][11]=-1;mP[15][12]=1; mP[15][13]=-1;mP[15][14]=1; mP[15][15]=-1;mP[15][16]=1; mP[15][17]=-1;mP[15][18]=-1;mP[15][19]=-1; 
        mP[16][0]=1;  mP[16][1]=1; mP[16][2]=1; mP[16][3]=1; mP[16][4]=1; mP[16][5]=-1; mP[16][6]=1;  mP[16][7]=1; mP[16][8]=1; mP[16][9]=-1;mP[16][10]=1; mP[16][11]=1; mP[16][12]=1; mP[16][13]=-1;mP[16][14]=1; mP[16][15]=1; mP[16][16]=1; mP[16][17]=1; mP[16][18]=1; mP[16][19]=-1; 
        mP[17][0]=1;  mP[17][1]=-1;mP[17][2]=-1;mP[17][3]=-1;mP[17][4]=-1;mP[17][5]=-1; mP[17][6]=-1; mP[17][7]=-1;mP[17][8]=1; mP[17][9]=-1;mP[17][10]=1; mP[17][11]=-1;mP[17][12]=-1;mP[17][13]=-1;mP[17][14]=-1;mP[17][15]=-1;mP[17][16]=-1;mP[17][17]=-1;mP[17][18]=1; mP[17][19]=-1; 
        mP[18][0]=1;  mP[18][1]=1; mP[18][2]=1; mP[18][3]=1; mP[18][4]=1; mP[18][5]=1;  mP[18][6]=1;  mP[18][7]=1; mP[18][8]=1; mP[18][9]=1; mP[18][10]=1; mP[18][11]=1; mP[18][12]=1; mP[18][13]=1; mP[18][14]=1; mP[18][15]=1; mP[18][16]=1; mP[18][17]=1; mP[18][18]=1; mP[18][19]=-1;   
        mP[19][0]=-1; mP[19][1]=-1;mP[19][2]=-1;mP[19][3]=-1;mP[19][4]=-1;mP[19][5]=-1; mP[19][6]=-1; mP[19][7]=-1;mP[19][8]=-1;mP[19][9]=-1;mP[19][10]=-1;mP[19][11]=-1;mP[19][12]=-1;mP[19][13]=-1;mP[19][14]=-1;mP[19][15]=-1;mP[19][16]=-1;mP[19][17]=-1;mP[19][18]=-1;mP[19][19]=-1;
    
        direction='U';
        pelletX=x/gridSize-1;
        pelletY=y/gridSize-1;
        lastPelletX=pelletX;
        lastPelletY=pelletY;
        this.lastX = x;
        this.lastY = y;
        this.x = x;
        this.y = y;
    }

    /*posicion del jugador*/
    public void getPosition(Player p){
        this.px = p.x;
        this.py = p.y;
    }
  /* actualiza las bolitas */
    public void updatePellet(){
        int tempX,tempY;
        tempX = x/gridSize-1;
        tempY = y/gridSize-1;
        if (tempX != pelletX || tempY != pelletY){
            lastPelletX = pelletX;
            lastPelletY = pelletY;
            pelletX=tempX;
            pelletY = tempY;
        }
    } 
    public boolean isChoiceDest(){
        return x%gridSize==0&& y%gridSize==0;
    }

    //Direccion del BFS
    public char newDirectionBFS()
    {
        int ini = ((this.y)/20 - 1) * 20 + (this.x / 20 - 1);
        int fin = ((py)/20 - 1) * 20 + ((px) / 20 - 1);
     // Estas son las primeras posicion a donde se debe avanzar el GHOST
  
        int pasos = 0, max = 0, actual;
        boolean visitado[] = new boolean[ MAX ];
        Arrays.fill( visitado , false );
        Arrays.fill( prev , 0);
        for(int i=0;i<MAX;i++)
            for(int j=0;j<MAX;j++)
                ady[i][j]=0;
    
        prev[ ini ] = -1;
                   
        for(int j=0;j<20;j++)
            for(int i=0;i<20;i++)
                if(mP[j][i]!=-1)
                {
                    if(j-1>=0 && mP[j-1][i]!=-1) // NORTE
                    {
                        ady[20*(j)+(i)][20*(j-1)+(i)]=1;
                        ady[20*(j-1)+(i)][20*(j)+(i)]=1;
                    }
                    if(i+1<20 && mP[j][i+1]!=-1) // ESTE
                    {
                        ady[20*(j)+(i)][20*(j)+(i+1)]=1;
                        ady[20*(j)+(i+1)][20*(j)+(i)]=1;
                    }
                    if(j+1<20 && mP[j+1][i]!=-1 ) // SUR
                    {
                        ady[20*(j)+(i)][20*(j+1)+(i)]=1;
                        ady[20*(j+1)+(i)][20*(j)+(i)]=1;
                    }
                    if(i-1>=0 && mP[j][i-1]!=-1) // OESTE
                    {
                        ady[20*(j)+(i)][20*(j)+(i-1)]=1;
                        ady[20*(j)+(i-1)][20*(j)+(i)]=1;
                    }
                }

        Queue<Integer> Q = new LinkedList<Integer>();
        Q.add( ini );
        while( !Q.isEmpty() )
        {
            max = Math.max( max , Q.size() );   //ver memoria usada en cola
            actual = Q.remove();
            pasos++;
            if( actual == fin )break;   //si se llego al destino
            visitado[ actual ] = true;
            for( int i = 0 ; i < 400 ; ++i ){   //vemos adyacentes a nodo actual
                int v = ady[ actual ][ i ];
                if( v != 0 && !visitado[ i ] ){ //no visitado agregamos a cola
                    prev[ i ] = actual; //para ver recorrido de nodo inicio a fin
                    Q.add( i );
                }
            }
        }
        List<Integer> path = new ArrayList<Integer>();
        for( ;; )
        {
            path.add( fin );
            if( prev[ fin ] == -1 )
                break;
            fin = prev[ fin ];
        }

        /*Iterator<Integer> it = path.iterator();
        while(it.hasNext())
        {
            Integer elem = it.next();
            System.out.print(elem+" ");
        }
        System.out.println();
        */
        int m=path.get(path.size()-2);
        x0=(m+1)%20;
        if(x0==0) x0=20;
        y0=(m+1)-x0;
        y0=y0/20;
        y0++;

        posY = y0-1;
        posX = x0-1;
        int random;
        char backwards='U';
        int newX=x,newY=y;
        int lookX=x,lookY=y;
        Set<Character> set = new HashSet<Character>();
        
        switch(direction)
        {
          case 'L':
             backwards='R';
             break;     
          case 'R':
             backwards='L';
             break;     
          case 'U':
            backwards='D';
             break;     
          case 'D':
             backwards='U';
             break;     
        }

        int pos2X,pos2Y;
        char newDirection = backwards;
        
        while (newDirection == backwards || !isValidDest(lookX,lookY)){
            if(set.size() == 3)
            {
                newDirection = backwards;
                System.out.println("Setsize = 3");
                break;
            }
            
            newX=x;
            newY=y;
            lookX=x;
            lookY=y;
            //posX posY donde se va a mover
            //pos2X pos2Y es la pos del ghost
             
            
            pos2X = this.x/20-1;
            pos2Y = this.y/20-1;
           
            if ( pos2X - posX > 0 && pos2Y - posY == 0 ){
                newDirection = 'L';
                newX-=increment; 
                lookX-= increment;
                
            }
            else if (pos2X - posX < 0 && pos2Y - posY == 0 )
            {
                newDirection = 'R';
                newX+=increment; 
                lookX+= gridSize;
            }
            else if (pos2X - posX == 0 && pos2Y - posY > 0)
            {
                newDirection = 'U';
                newY-=increment; 
                lookY-=increment;
            }
            else if (pos2X - posX == 0 && pos2Y - posY < 0 )
            {
                newDirection = 'D';
                newY+=increment; 
                lookY+=gridSize;
            }
            
            if (newDirection == backwards)
            {
                set.add(new Character(newDirection));
            }

            //System.out.println("Nueva Direccion: "+newDirection+" y Backwards: "+backwards);
            if(newDirection == backwards)
            {
                backwards='R';
            }
        } 
        return newDirection;
    }

    /*da una direcccion random*/
    public char newDirection1()
    {
    int random;
    char backwards='U';
    int newX=x,newY=y;
    int lookX=x,lookY=y;
    Set<Character> set = new HashSet<Character>();
    switch(direction)
    {
      case 'L':
         backwards='R';
         break;     
      case 'R':
         backwards='L';
         break;     
      case 'U':
         backwards='D';
         break;     
      case 'D':
         backwards='U';
         break;     
    }

    char newDirection = backwards;
    while (newDirection == backwards || !isValidDest(lookX,lookY))
    {
      if (set.size()==3)
      {
        newDirection=backwards;
        break;
      }

      newX=x;
      newY=y;
      lookX=x;
      lookY=y;
      /*escoge una direccion random*/
      random = (int)(Math.random()*4) + 1;
      if (random == 1)
      {
        newDirection = 'L';
        newX-=increment; 
        lookX-= increment;
      }
      else if (random == 2)
      {
        newDirection = 'R';
        newX+=increment; 
        lookX+= gridSize;
      }
      else if (random == 3)
      {
        newDirection = 'U';
        newY-=increment; 
        lookY-=increment;
      }
      else if (random == 4)
      {
        newDirection = 'D';
        newY+=increment; 
        lookY+=gridSize;
      }
      if (newDirection != backwards)
      {
        set.add(new Character(newDirection));
      }
    } 
    return newDirection;
  }
    /*movimiento random*/
    public void move1()
  {
    lastX=x;
    lastY=y;
 
    
    if (isChoiceDest())
    {
      direction = newDirection1();
    }
    
    /* si la direccion es vaida moverse */
    switch(direction)
    {
      case 'L':
         if ( isValidDest(x-increment,y))
           x -= increment;
         break;     
      case 'R':
         if ( isValidDest(x+gridSize,y))
           x+= increment;
         break;     
      case 'U':
         if ( isValidDest(x,y-increment))
           y-= increment;
         break;     
      case 'D':
         if ( isValidDest(x,y+gridSize))
           y+= increment;
         break;     
    }
  }

    public void move()
    {
        lastX=x;
        lastY=y;
 
        if (isChoiceDest())
        {
            direction = newDirectionBFS();
        }
    
        System.out.println(direction);
    
    /* si la direccion es valida moverse */
        //System.out.println("Hola a todo el mun do en especial a leonardo");
        switch(direction)
        {
           
            case 'L':
                if ( isValidDest(x-increment,y))
                    x -= increment;
                break;     
            case 'R':
                if ( isValidDest(x+gridSize,y))
                    x+= increment;
                break;     
            case 'U':
                if ( isValidDest(x,y-increment))
                    y-= increment;
                break;         
            case 'D':
                if ( isValidDest(x,y+gridSize))
                    y+= increment;
                break;  
            default:
                System.out.print("chau");   
        }
    }
}
