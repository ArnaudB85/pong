package sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import pong.*;
import invariant.*;

public class spriteTouche implements ConstantesDuJeu {
	
	public static PingPong pong;
	
	public int x, y;
	public String toucheLabel, toucheDefinition;
	public int font;
	
	public Color gold = new Color(218, 165, 32), whiteSmoke = new Color(245, 245, 245), silver = new Color (182,182,182);
	
	public spriteTouche(int x, int y, String stringLabel, String stringDefinition, int font) {
		this.x = x - LARGEUR_BORD_FENETRE_X;
		this.y = y - HAUTEUR_BANDEAU_FENETRE_Y;
		
		this.toucheLabel = stringLabel;
		this.toucheDefinition = stringDefinition;
		this.font = font;
	}
	
	public void paint (Graphics2D designTouche) {
		
		// Bouton Echap principalement
		if (font == 1) {
			// Dessin de la touche en relief
			designTouche.setColor(Color.darkGray);
			designTouche.setStroke(new BasicStroke(2f));
			designTouche.drawRect(x, y, 20, 20);
			designTouche.setColor(new Color(244, 244, 244));
			designTouche.fillRect(x, y, 20, 20);
			
			// Description de la touche
			designTouche.setColor(Color.GRAY);
			designTouche.setFont(new Font("Arial", 1, 10));
			designTouche.drawString(toucheLabel, x + 1, y + 15);
			designTouche.drawString(toucheDefinition, x + 25, y + 15);
		}
		
		// Boutons servant à jouer
		if (font == 2) {
			// Dessin de la touche en relief
			designTouche.setColor(Color.darkGray);
			designTouche.setStroke(new BasicStroke(2f));
			designTouche.drawRect(x, y, 20, 20);
			designTouche.setColor(new Color(244, 244, 244));
			designTouche.fillRect(x, y, 20, 20);
					
			// Description de la touche
			designTouche.setColor(Color.GRAY);
			designTouche.setFont(new Font("Arial", 1, 12));
			designTouche.drawString(toucheLabel, x + 7, y + 15);
			designTouche.drawString(toucheDefinition, x + 27, y + 15);
		}
		
		// Gros bouton du menu principal
		if (font == 3) {
			// Dessin de la touche en relief
			designTouche.setColor(gold);
			designTouche.fillRect(x, y, 212, 42);
			designTouche.setStroke(new BasicStroke(1f));
			designTouche.setColor(Color.yellow);
			designTouche.drawRect(x + 3, y + 3, 206, 36);
			designTouche.setColor(new Color(253, 245, 230));
			designTouche.fillRoundRect(x + 6, y + 6, 200, 30, 12, 12);
					
			// Description de la touche
			designTouche.setColor(silver);
			designTouche.setFont(new Font("Arial", 1, 24));
			designTouche.drawString(toucheDefinition, x + 18, y + 30);
			designTouche.setColor(Color.GRAY);
			designTouche.drawString(toucheDefinition, x + 16, y + 29);
		}
	}
}
