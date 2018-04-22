package sprite;

import java.awt.Color;
import java.awt.Graphics;

import pong.*;
import invariant.*;

public class spriteRaquette implements ConstantesDuJeu, parametres {
	
	public static spriteBalle balle;
	public static PingPong pong;
	
	public int x, y;
	public int indiceJoueurRaquette, positionRaquettesService;
	public int tailleRaquette, longueurRaquette_y;
	
	public int translationRaquette_Y = INCREMENT_RAQUETTE_Y;
	
	public int score;
	
	public boolean raquetteMontée;
	
	public spriteRaquette(int indiceJoueurRaquette, int tailleRaquette) {
		this.indiceJoueurRaquette = indiceJoueurRaquette;
		this.tailleRaquette = tailleRaquette;
		
		if (tailleRaquette == 1) {
			longueurRaquette_y = LONGUEUR_RAQUETTE_Y_6;
		}
		else if (tailleRaquette == 2) {
			longueurRaquette_y = LONGUEUR_RAQUETTE_Y_7;
		}
		else if (tailleRaquette == 3) {
			longueurRaquette_y = LONGUEUR_RAQUETTE_Y_8;
		}
		
		if (indiceJoueurRaquette == 1) {
			positionRaquette(1, 1);
		}
		
		if (indiceJoueurRaquette == 2) {
			positionRaquette(2, 1);
		}
	}
	
	public void positionRaquette (int indiceJoueurRaquette, int positionRaquettesService) {
		this.indiceJoueurRaquette = indiceJoueurRaquette;
		this.positionRaquettesService = positionRaquettesService;
		
		
		
		if (positionRaquettesService == 1 || positionRaquettesService == 4) {
			if (indiceJoueurRaquette == 1) {
				this.x = RAQUETTE_1_X_DEPART;
				this.y = RAQUETTE_Y_DEPART_HAUT - longueurRaquette_y/2;
			}
			if (indiceJoueurRaquette == 2) {
				this.x = RAQUETTE_2_X_DEPART;
				this.y = RAQUETTE_Y_DEPART_BAS - longueurRaquette_y/2;
			}
		}
		
		if (positionRaquettesService == 2 || positionRaquettesService == 3) {
			if (indiceJoueurRaquette == 1) {
				this.x = RAQUETTE_1_X_DEPART;
				this.y = RAQUETTE_Y_DEPART_BAS - longueurRaquette_y/2;
			}
			if (indiceJoueurRaquette == 2) {
				this.x = RAQUETTE_2_X_DEPART;
				this.y = RAQUETTE_Y_DEPART_HAUT - longueurRaquette_y/2;
			}			
		}
	}

	public void paint(Graphics designRaquette) {
		
		if (this.x < TABLE_X_MIN + LONGUEUR_TABLE_X/2) {
			designRaquette.setColor(Color.white);
			designRaquette.fillRect(x + 4, y - 2, EPAISSEUR_RAQUETTE_X, longueurRaquette_y);
			designRaquette.setColor(Color.white);
			designRaquette.fillRect(x + 2, y - 1, EPAISSEUR_RAQUETTE_X, longueurRaquette_y);
		}
		else {
			designRaquette.setColor(Color.white);
			designRaquette.fillRect(x - 4, y - 2, EPAISSEUR_RAQUETTE_X, longueurRaquette_y);
			designRaquette.setColor(Color.white);
			designRaquette.fillRect(x - 2, y - 1, EPAISSEUR_RAQUETTE_X, longueurRaquette_y);
		}
		designRaquette.setColor(new Color(204, 0 , 0));
		designRaquette.fillRect(x, y, EPAISSEUR_RAQUETTE_X, longueurRaquette_y);
	}

	public void monterRaquette(boolean monter) {
		
		if(monter) {
			if (y > TABLE_Y_MIN + translationRaquette_Y) {
				y -= translationRaquette_Y;
			} else {
				y = TABLE_Y_MIN; 
			}
			raquetteMontée = true;
		}
		else {
			if (y < TABLE_Y_MIN + HAUTEUR_TABLE_Y - longueurRaquette_y - translationRaquette_Y) {
				y += translationRaquette_Y;
			} else {
				y = TABLE_Y_MIN + HAUTEUR_TABLE_Y - longueurRaquette_y;
			}
			raquetteMontée = false;
		}
	}
}