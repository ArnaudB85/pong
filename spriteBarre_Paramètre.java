package sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import pong.*;
import invariant.*;

public class spriteBarre_Paramètre implements ConstantesDuJeu {

public static PingPong pong;
	
	public int x, y, nombreDeGraduation;
	
	public Color gold = new Color(218, 165, 32), forestGreen = new Color(34, 139, 34);
	public Color silver = new Color (182,182,182), dimGray = new Color(105, 105, 105);
	
	public spriteBarre_Paramètre(int x, int y, int nombreDeGraduation){
		this.x = x - LARGEUR_BORD_FENETRE_X;
		this.y = y - HAUTEUR_BANDEAU_FENETRE_Y;
		this.nombreDeGraduation = nombreDeGraduation;
		// Prend valeur 3 ou 4 selon le nombre de positions sur la barre de réglages
	}
	
	public void paint (Graphics2D designBarre) {
		// Barre	
		designBarre.setStroke(new BasicStroke(1f));
		designBarre.setColor(Color.white);	// Surface extérieure
		designBarre.fillRoundRect(x, y, 112, 16, 12, 12);
		designBarre.setColor(dimGray);	// Bordure
		designBarre.drawRoundRect(x, y, 112, 16, 12, 12);
		designBarre.setColor(silver);	// Surface intérieure
		designBarre.fillRoundRect(x + 5, y + 2, 102, 12, 10, 10);
		designBarre.setColor(dimGray);	// Fond
		designBarre.fillRoundRect(x + 8, y + 6, 99, 7, 6, 6);
		
		// Support de token
		if (nombreDeGraduation == 3) {
			// position 1/2
			designBarre.setStroke(new BasicStroke(1f));
			designBarre.setColor(Color.darkGray);	// Surface extérieure
			designBarre.fillRect(x + 54, y + 19, 4, 8);
			designBarre.setColor(Color.red);	// Zone cliquable
			designBarre.drawRect(x + 42, y - 7, 28, 28);
		}
		
		else if(nombreDeGraduation == 4) {
			// position 1/3
			designBarre.setStroke(new BasicStroke(1f));
			designBarre.setColor(Color.darkGray);	// Surface extérieure
			designBarre.fillRect(x + 37, y + 19, 4, 8);
			designBarre.setColor(Color.red);	// Zone cliquable
			designBarre.drawRect(x + 25, y - 7, 28, 28);
			
			//position 2/3
			designBarre.setStroke(new BasicStroke(1f));
			designBarre.setColor(Color.darkGray);	// Surface extérieure
			designBarre.fillRect(x + 70, y + 19, 4, 8);
			designBarre.setColor(Color.red);	// Zone cliquable
			designBarre.drawRect(x + 58, y - 7, 28, 28);
		}
		
		// position gauche
		designBarre.setStroke(new BasicStroke(1f));
		designBarre.setColor(Color.darkGray);	// Surface extérieure
		designBarre.fillRect(x + 4, y + 19, 4, 8);
		designBarre.setColor(Color.red);	// Zone cliquable
		designBarre.drawRect(x - 8, y - 7, 28, 28);
		
		// position droite
		designBarre.setStroke(new BasicStroke(1f));
		designBarre.setColor(Color.darkGray);	// Surface extérieure
		designBarre.fillRect(x + 104, y + 19, 4, 8);
		designBarre.setColor(Color.red);	// Zone cliquable
		designBarre.drawRect(x + 92, y - 7, 28, 28);
		
	}
}
