import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;


public class Game extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	int mouse_x, mouse_y;
	int r,g,b;
	int marge = 40;
	int rect_x, rect_y, rect_w, rect_h;
	int nbLigne = 3;
	int nbColonne = 3;
	int goalWin = 3;
	int espaceLigne, espaceColonne;
	int joueur = 1 ;
	int finished = -1; // -1 = !Fin  |  0 = Egalité | 1 = Victoire
	
	Point lastBoxAction; //derniere case joué, en attendant de savoir comment acceder a la derniere valeur de la hashtable
	
	Hashtable<Point, Integer> actions; //J'ai un point en Cles et une donnée en valeur

	
	
	public Game()
	{
		addMouseListener(this);
		setSize(600, 600); setVisible(true);
		
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	this.setLocation(d.width/2-this.getWidth()/2, d.height/2 - (this.getHeight()/2));
	actions = new Hashtable<Point, Integer>();
	}
	
	
	void creerGrille(Graphics g)
	{
	
		this.rect_w = this.getWidth()-(this.marge*2); this.rect_h = this.getHeight()-(this.marge*2);
		this.rect_x = this.marge; this.rect_y = this.marge;
		this.espaceLigne = this.rect_h/nbLigne;
	    this.espaceColonne = this.rect_w/nbColonne;
		g.drawRect(rect_x, rect_y, rect_w, rect_h);

		for(int i=1; i<nbLigne; i++)
		{
			//Lignes
			g.drawLine(this.rect_x , this.rect_y +i*espaceLigne, this.rect_w+this.marge, this.rect_y +i*espaceLigne);
			
		}
		for(int i=1; i<nbColonne; i++)
		{
			//Colonnes
			g.drawLine(this.rect_x + i*espaceColonne, this.rect_y, this.rect_x + i*espaceColonne, this.rect_h+this.marge);
		}
	}
	
	Point identifier_Case() 
	{
		int col, ligne;
		Point box = new Point();
		col = (this.mouse_x - this.marge) / (this.rect_w/this.nbColonne);
		ligne = (this.mouse_y - this.marge) / (this.rect_h/this.nbLigne);
		box.x = col+1;
		box.y = ligne+1;
		return box; //EX 1, 3
	}
	

	
	void TraceCroix_box(Point box, Graphics g)
	{
		int x1 = this.rect_x + this.espaceColonne*(box.x-1);
		int x2 = this.rect_x + this.espaceColonne*box.x;
		
		int y1 = this.rect_y + this.espaceLigne*(box.y-1);
		int y2 = this.rect_y + this.espaceLigne*box.y;
		g.drawLine(x1, y1 , x2 , y2);
		g.drawLine(x1, y2 , x2 , y1);
	}
	
	void TraceCercle_box(Point box, Graphics g)
	{
		int x = this.rect_x + this.espaceColonne*(box.x-1);
		int y = this.rect_y + this.espaceLigne*(box.y-1);
		g.drawOval(x, y, this.espaceColonne, this.espaceLigne);
	}
	
	void TraceCroix_selected(int numCol, int numLigne,Graphics g)
	{
		numCol = numCol % this.nbColonne; numLigne = numLigne % this.nbLigne;
		int x1 = this.rect_x + this.espaceColonne*(numCol-1);
		int x2 = this.rect_x + this.espaceColonne*numCol;
		
		int y1 = this.rect_y + this.espaceLigne*(numLigne-1);
		int y2 = this.rect_y + this.espaceLigne*numLigne;
		g.drawLine(x1, y1 , x2 , y2);
		g.drawLine(x1, y2 , x2 , y1);
	}
	
	void affect(int a, int b)
	{
		this.mouse_x = a;
		this.mouse_y = b;
	}
	
	public void printWhichBox(Point box)
	{
		//System.out.println("X : " + this.mouse_x + " Y : " + this.mouse_y);
		System.out.println("Joué : Collonne : " + box.x + " Ligne : " + box.y);
		
		if(this.actions.get(box) == 1)
			System.out.println("Type de jeux : Croix [X]\n");
		else
			System.out.println("Type de jeux : Croix [O]\n");
	}
	
		
	public void paint(Graphics g)
    {
	    creerGrille(g);
	    if(this.actions.isEmpty())
	    {
	    	System.out.println("Debut de la partie");
	    }else
	    if(this.actions.get(this.lastBoxAction) == 1) //si le dernier tour joué est une croix
        TraceCroix_box(this.lastBoxAction, g);//mettre en paramettre la derniere box joué
	    else TraceCercle_box(this.lastBoxAction, g);
    }
	  
	  
	public void switchPlayer()
	{
		  if (joueur == 1) {joueur = 2;} else {joueur = 1;};
		  System.out.println("Joueur" + joueur + " à toi de jouer !");
		  System.out.println();
	}
	  
	public void putAction(Point box)
		{
		//TODO vérifier si la case est vide
		/*if(actions.keys() == box)
		{
			System.out.println("Cette case a deja été joué teste1");
		}*/
		
		if(actions.containsKey(box)) //verifie dans la hastable
		{
			System.out.println("Cette case a deja été joué...");
		}
		else  if( joueur == 1)
			  {
				  this.actions.put(box, 1); //Sauvegarde une croix joué
			      switchPlayer(); //change de joueur aprés avoir jouer une action

			  }
			  else
			  {
				  this.actions.put(box, 2); //sauvegarde le cercle
			      switchPlayer(); //change de joueur aprés avoir jouer une action
			  }
		printWhichBox(box);// affiche quelle case vient dêtre joué
		}
	  
	
	
	
	boolean analyseLigne()//AUtre fonction d'analyse de ligne
	{
		int cpt= 1;

		
		Point bufferPoint = new Point();
		bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y; //mets le point a comparer egale au dernier coup joué
		//horizontal
				// <-- Gauche
				bufferPoint.x = bufferPoint.x - 1; //pour comparer à la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.x >= 0 ) //si le coup est bon 
				{ //test check 1
					bufferPoint.x = bufferPoint.x - 1; //pour comparer à la suite
					cpt++;
				}
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;
				//Droite -->
				bufferPoint.x = bufferPoint.x + 1; //pour comparer a la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.x <= this.nbColonne) //si le coup est bon 
				{					
					bufferPoint.x = bufferPoint.x + 1; //pour comparer a la suite
					cpt++;
				}
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;
				if(cpt == this.goalWin )
				{
					return true; //c'est une ligne GAGNANTE
				}
				//System.out.println("Analyse Horizontale = " + cpt);
				//RAZ
				cpt = 1; 


				
		//Vertcal
				// Haut
				bufferPoint.y = bufferPoint.y - 1; 
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.y >= 0 ) //si le coup est bon
				{
					bufferPoint.y = bufferPoint.y - 1; //pour comparer à la suite
					cpt++;
				} 
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;
				// Bas
				bufferPoint.y = bufferPoint.y + 1; //pour comparer a la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.y <= this.nbLigne) //si le coup est bon 
				{
					bufferPoint.y = bufferPoint.y + 1; //pour comparer a la suite
					cpt++;
				}
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;

				if(cpt == this.goalWin )
				{
					return true; //c'est une ligne GAGNANTE
				}
				//System.out.println("Analyse Verticale = " + cpt);
				//RAZ
				cpt = 1; 
				
		//Diagonale 1
				// Gauche haut | 
				bufferPoint.y = bufferPoint.y - 1; //pour comparer à la suite
				bufferPoint.x = bufferPoint.x - 1; //pour comparer à la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.x >= 0 && bufferPoint.y >= 0 ) //si le coup est bon 
				{
					bufferPoint.y = bufferPoint.y - 1; //pour comparer à la suite
					bufferPoint.x = bufferPoint.x - 1; //pour comparer à la suite

					cpt++;
				}
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;

				//droite Bas
				bufferPoint.y = bufferPoint.y + 1; //pour comparer à la suite
				bufferPoint.x = bufferPoint.x + 1; //pour comparer à la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.x >= this.nbColonne && bufferPoint.y >= this.nbLigne ) //si le coup est bon 
				{
					bufferPoint.y = bufferPoint.y + 1; //pour comparer à la suite
					bufferPoint.x = bufferPoint.x + 1; //pour comparer à la suite

					cpt++;
				}	
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;				

				if(cpt == this.goalWin )
				{
					return true; //c'est une ligne GAGNANTE
				}
				//System.out.println("Analyse Diagonale 1 = " + cpt);
				//RAZ
				cpt = 1; 
				
		//Diagonale 2
				// Gauche bas | 
				bufferPoint.y = bufferPoint.y + 1; //pour comparer a la suite
				bufferPoint.x = bufferPoint.x - 1; //pour comparer à la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.x >= 0 && bufferPoint.y <= this.nbLigne)//si le coup est bon 
				{
					bufferPoint.y = bufferPoint.y + 1; //pour comparer a la suite
					bufferPoint.x = bufferPoint.x - 1; //pour comparer à la suite

					cpt++;
				}
				bufferPoint.x = this.lastBoxAction.x; bufferPoint.y = this.lastBoxAction.y;

				//Droite Haut
				bufferPoint.y = bufferPoint.y - 1; //pour comparer a la suite
				bufferPoint.x = bufferPoint.x + 1; //pour comparer à la suite
				while(this.actions.get(this.lastBoxAction) == this.actions.get(bufferPoint) && bufferPoint.x <= this.nbColonne && bufferPoint.y >= 0) //si le coup est bon
				{
					bufferPoint.y = bufferPoint.y - 1; //pour comparer a la suite
					bufferPoint.x = bufferPoint.x + 1; //pour comparer à la suite

					cpt++;
				}
				
				if(cpt == this.goalWin )
				{
					return true; //c'est une ligne GAGNANTE
				}
				//System.out.println("Analyse Diagonale 2 = " + cpt);

		return false;
	}
		
	void finalResult()
	{
		
		if(this.finished == 1)
		{
			if(this.actions.get( this.lastBoxAction) == 1) //Joueur 1/Croix gagne
			{
				
				System.out.println("Fin de Partie joueur 1 gagne avec " + (this.actions.size() / 2 + 1 ) + " coups jouées");
			
			}
			else if(this.actions.get( this.lastBoxAction) == 2)
			{
				System.out.println("Fin de Partie joueur 2 gagne avec " + this.actions.size() / 2 + " coups jouées");
			
			}
		}
		else if(this.finished == 0)
		{
			System.out.println("Egalité bande de naze");
		}
	}
	
	void if_Finished()
	{
		if(analyseLigne())
		{
			this.finished = 1;
		}
		else if(this.actions.size() == (this.nbColonne * this.nbLigne))
		{
			this.finished = 0;
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		//System.out.println("clic");
		if(this.finished == -1)
		{
			this.mouse_x = arg0.getX();
			this.mouse_y = arg0.getY();
			Point box = new Point();
	        box = identifier_Case(); //box contient le Num de ligne est colonne
	        
	        this.lastBoxAction = box;
	        putAction(box);
	        
	        if_Finished(); 
	        if(this.finished != -1)
	        {
				finalResult(); 

	        }
		} 
        this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Game();
	}

}
