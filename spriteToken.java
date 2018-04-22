package sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import pong.*;
import invariant.*;

public class spriteToken implements ConstantesDuJeu {
	
	public static PingPong pong;
	
	public int x, y, position, X;
	
	public Color gold = new Color(218, 165, 32), whiteSmoke = new Color(245, 245, 245), silver = new Color (182,182,182);
	
	public spriteToken(int x, int y, int choix){
		this.x = x - LARGEUR_BORD_FENETRE_X;
		this.y = y - HAUTEUR_BANDEAU_FENETRE_Y;
		this.position = choix;
		
		/* Position du token sur la barre
		 * Si 3 choix, prend valeur 0, 5 ou 9
		 * Si 4 choix, prend valeur 0, 3, 6 ou 9 */
	}
	
	public void paintComponent (Graphics2D designToken) {
		
		switch(position) {
		case 0:
			this.X = x - 6;
			break;
		case 3:
			this.X = x + 27;
			break;
		case 5:
			this.X = x + 44;
			break;
		case 6:
			this.X = x + 60;
			break;
		case 9:
			this.X = x + 94;
			break;
		}
		
		designToken.setStroke(new BasicStroke(1f));
		designToken.setColor(Color.darkGray);	// Ombre
		designToken.fillOval(X + 2, y - 4, 24, 24);	
		designToken.setColor(silver);	// Surface
		designToken.fillOval(X, y -5, 24, 24);
		designToken.setColor(whiteSmoke);		// Intérieur bague
		designToken.fillOval(X + 2, y -2, 16, 16);
		designToken.setStroke(new BasicStroke(3f));
		designToken.setColor(gold);				// Bague
		designToken.drawOval(X + 3, y - 1, 14, 14);
		designToken.setStroke(new BasicStroke(1f));
		designToken.setColor(Color.yellow);				// Bague liséré
		designToken.drawOval(X + 3, y - 1, 14, 14);

	}
}
