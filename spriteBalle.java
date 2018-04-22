package sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import pong.*;
import invariant.*;

public class spriteBalle implements ConstantesDuJeu{

	public static PingPong pong;
	public static spriteBalle balle;
	
	public int x, y;
	public int deplacement_X, deplacement_Y;
	
	public Random randomDeplacement_X, randomDeplacement_Y;
	
	public int longueurEchange;
	
	public Random randomServicePuissant;
	public int probaServicePuissant, probaMax_ServicePuissant, facteurAce = FACTEUR_ACE;
	public boolean ace, testServicePuissant, aceChecked;
	
	public Random randomTirPuissant;
	public int probaTirPuissant, probaMax_TirPuissant, facteurSmash = FACTEUR_SMASH;
	public boolean smash, testTirPuissant, smashChecked;
	
	public Random randomEffetRaquette;
	public int effetRaquette, facteurEffetRaquette_X = FACTEUR_EFFET_RAQUETTE_X;
	public boolean testEffetRaquette;
	
	public Random randomFilet;
	public int evenementFilet;
	public boolean filet = false, filetChecked = false, testFilet;
	
	public int indiceServeur, indiceJoueurSmash;
	public boolean serviceEnCours = false, renvoiEnCours = false;
	
	public String vainqueur = " ";
	
	public spriteBalle(PingPong pong) {
		this.randomDeplacement_X = new Random();
		this.randomDeplacement_Y = new Random();
		
		this.x = RAQUETTE_1_X_DEPART + EPAISSEUR_RAQUETTE_X;
		this.y = RAQUETTE_Y_DEPART_HAUT - RAYON_BALLE;
	}
	
	public void update(spriteRaquette raquetteJoueur_1, spriteRaquette raquetteJoueur_2) {
		
		if (PingPong.pong.balleServie == true) {
			this.x += deplacement_X;
			this.y += deplacement_Y;
			
			// Rebond sur les bords du terrain
			if ((this.y < BALLE_Y_MIN && deplacement_Y < 0) || (this.y > BALLE_Y_MAX && deplacement_Y > 0)) {
				deplacement_Y = -deplacement_Y;
			}
			
			// 1 = rebond sur la surface d'une raquette
			if (testRebond(raquetteJoueur_1) == 1) {
				renvoiEnCours = true;
				renvoiBalle(raquetteJoueur_1);
				if (smash) {
					indiceJoueurSmash = 1;
				}
				else {
					indiceJoueurSmash = -1;
				}
			}
			
			if (testRebond(raquetteJoueur_2) == 1) {
				renvoiEnCours = true;
				renvoiBalle(raquetteJoueur_2);
				if (smash) {
					indiceJoueurSmash = 2;
				}
				else {
					indiceJoueurSmash = -1;
				}
			}
			
			// 2 = franchissement de la ligne de fond
			if (testRebond(raquetteJoueur_1) == 2 || testRebond(raquetteJoueur_2) == 2) {
				
				if (testRebond(raquetteJoueur_1) == 2) {
					raquetteJoueur_2.score++;
					if (conditionVictoire(raquetteJoueur_1, raquetteJoueur_2) == 2) {
						PingPong.pong.statutJeu = 3;
						if (PingPong.pong.bot == false) {
							vainqueur = "Joueur_2";
						}
						else {
							vainqueur = "Ordinateur";
						}
					}
				}
				
				if (testRebond(raquetteJoueur_2) == 2) {
					raquetteJoueur_1.score++;
					if (conditionVictoire(raquetteJoueur_1, raquetteJoueur_2) == 1) {
						PingPong.pong.statutJeu = 3;
						vainqueur = "Joueur_1";
					}
				}
				
				filetChecked = false;
				indiceJoueurSmash = -1;
				PingPong.pong.balleServie = false;
				raquetteJoueur_1.positionRaquettesService++;
				if (raquetteJoueur_1.positionRaquettesService > 4) {
					raquetteJoueur_1.positionRaquettesService = 1;
				}
				raquetteJoueur_1.positionRaquette(1, raquetteJoueur_1.positionRaquettesService);
				raquetteJoueur_2.positionRaquette(2, raquetteJoueur_1.positionRaquettesService);
				indiceJoueurServeur(raquetteJoueur_1, raquetteJoueur_2, raquetteJoueur_1.positionRaquettesService);
				positionBalleService(raquetteJoueur_1, raquetteJoueur_2, indiceServeur);
				service();
			}
			
			// 3 = rebond sur la tranche de la raquette
			if (testRebond(raquetteJoueur_1) == 3 || testRebond(raquetteJoueur_2) == 3) {
				this.deplacement_Y = - deplacement_Y;
			}
			
			// Franchissement du filet pour le joueur_1 ou le joueur_2
			if ((this.x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 25 && this.x + 2*RAYON_BALLE < TABLE_X_MIN + LONGUEUR_TABLE_X/2
						&& deplacement_X > 0 && filetChecked == false) 
				|| (this.x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 && this.x + 2*RAYON_BALLE < TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 25
						&& deplacement_X < 0 && filetChecked == false)) {
				
				this.evenementFilet = evenementFilet();
				filetChecked = true;
				
				// Balle ralentie par le filet
				if (evenementFilet == 1) {
					if (this.deplacement_X > 0) {
						this.deplacement_X = this.deplacement_X*2/3;
						if (this.deplacement_X == 0) {
							this.deplacement_X = 2;
						}
					}
					if (this.deplacement_X < 0) {
						this.deplacement_X = this.deplacement_X*2/3;
						if (this.deplacement_X == 0) {
							this.deplacement_X = -2;
						}
					}
					this.deplacement_Y = this.deplacement_Y/2;
				}
				
				/*
				 *  Balle arrêtée par le filet:
				 *   - si balle de service, balle à rejouer
				 *   - sinon, point pour l'adversaire
				 */
				
				if (evenementFilet == 2) {
					if (deplacement_X > 0) {
						this.x = TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 2*RAYON_BALLE;
						positionnerBalle(x, y);
					}
					if (deplacement_X < 0) {
						this.x = TABLE_X_MIN + LONGUEUR_TABLE_X/2;
						positionnerBalle(x, y);
					}
					
					try {
						TimeUnit.MILLISECONDS.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(longueurEchange != 0 && deplacement_X > 0) {
						// Point pour le joueur_2
						raquetteJoueur_2.score++;
						if (conditionVictoire(raquetteJoueur_1, raquetteJoueur_2) == 2) {
							pong.statutJeu = 3;
							if (pong.bot == false) {
								vainqueur = "Joueur_2";
							}
							else {
								vainqueur = "Ordinateur";
							}
						}
						raquetteJoueur_1.positionRaquettesService++;
						if (raquetteJoueur_1.positionRaquettesService > 4) {
							raquetteJoueur_1.positionRaquettesService = 1;
						}
					}			
					
					if (longueurEchange != 0 && deplacement_X < 0) {
						// Point pour le joueur_1
						raquetteJoueur_1.score++;
						if (conditionVictoire(raquetteJoueur_1, raquetteJoueur_2) == 1) {
							pong.statutJeu = 3;
							vainqueur = "Joueur_1";
						}
						raquetteJoueur_1.positionRaquettesService++;
						if (raquetteJoueur_1.positionRaquettesService > 4) {
							raquetteJoueur_1.positionRaquettesService = 1;
						}
					}
					
					filetChecked = false;
					indiceJoueurSmash = -1;
					// Service à refaire
					PingPong.pong.balleServie = false;
					raquetteJoueur_1.positionRaquette(1, raquetteJoueur_1.positionRaquettesService);
					raquetteJoueur_2.positionRaquette(2, raquetteJoueur_1.positionRaquettesService);
					indiceJoueurServeur(raquetteJoueur_1, raquetteJoueur_2, raquetteJoueur_1.positionRaquettesService);
					positionBalleService(raquetteJoueur_1, raquetteJoueur_2, indiceServeur);
					this.service();
				}
			}
		}
	}

	public void positionnerBalle(int x2, int y2) {
		this.x = x2;
		this.y = y2;
	}
	
	
	/*
	 * Défini le joueur devant servir en fonction du score de la partie:
	 *  - Si score inférieur à 10-10, chacun sert 2 fois,
	 *  - sinon, chacun sert 1 fois
	 */
	public int indiceJoueurServeur(spriteRaquette raquetteJoueur_1, spriteRaquette raquetteJoueur_2, int positionRaquettesService) {
		if (raquetteJoueur_1.score < 10 && raquetteJoueur_2.score < 10) {
			if (raquetteJoueur_1.positionRaquettesService == 1 || raquetteJoueur_1.positionRaquettesService == 2) {
				return indiceServeur = 1;
			}
			else if (raquetteJoueur_1.positionRaquettesService == 3 || raquetteJoueur_1.positionRaquettesService == 4) {
				return indiceServeur = 2;
			}
		}
		if (raquetteJoueur_1.score >= 10 && raquetteJoueur_2.score >= 10) {
			if (raquetteJoueur_1.positionRaquettesService == 1 || raquetteJoueur_1.positionRaquettesService == 3) {
				return indiceServeur = 1;
			}
			else if (raquetteJoueur_1.positionRaquettesService == 2 || raquetteJoueur_1.positionRaquettesService == 4) {
				return indiceServeur = 2;
			}
		}
		return indiceServeur = 2;
	}
	
	
	/*
	 * Affecte la balle sur la raquette du joueur devant servir
	 */
	public void positionBalleService(spriteRaquette raquetteJoueur_1, spriteRaquette raquetteJoueur_2, int indiceServeur) {
		if (indiceServeur == 1) {
			this.x = raquetteJoueur_1.x + EPAISSEUR_RAQUETTE_X + 1;
			this.y = raquetteJoueur_1.y + raquetteJoueur_1.longueurRaquette_y/2 - RAYON_BALLE;
		}
		if (indiceServeur == 2) {
			this.x = raquetteJoueur_2.x - 2*RAYON_BALLE - 1;
			this.y = raquetteJoueur_2.y + raquetteJoueur_2.longueurRaquette_y/2 - RAYON_BALLE;
		}
	}
	
	
	/*
	 * Test les rebonds possibles ou non pour la balle:
	 *  - 1, la balle peut rebondir sur la surface d'une raquette,
	 *  - 2, la balle franchie la ligne de fond,
	 *  - 3, la balle rebondie sur la tranche de la raquette,
	 *  - 0, la balle est quelque part sur la table,
	 */
	public int testRebond (spriteRaquette raquette) {
		
		// Test de rebond du coté gauche
		if (raquette.indiceJoueurRaquette == 1 && deplacement_X <= 0) {
			
			if (smash) {
				if (this.x >= raquette.x && this.x <= raquette.x + EPAISSEUR_RAQUETTE_X + facteurSmash*OFFSET_X
						&&  this.y + RAYON_BALLE >= raquette.y && this.y + RAYON_BALLE <= raquette.y + raquette.longueurRaquette_y) {
						return 1;	// Rebond possible
					}
	
				if (this.x < BALLE_X_MIN) {
						return 2;	// Point perdu
				} 
			}
			
			else {
				if (this.x >= raquette.x && this.x <= raquette.x + EPAISSEUR_RAQUETTE_X + OFFSET_X
						&&  this.y + RAYON_BALLE >= raquette.y && this.y + RAYON_BALLE <= raquette.y + raquette.longueurRaquette_y) {
					return 1;
				}
				if (this.x < BALLE_X_MIN) {
					return 2;
				}
			}
		}
		
		// Test de rebond du côté gauche
		if (raquette.indiceJoueurRaquette == 2 && deplacement_X >= 0) {
			if (smash) {
				if (this.x <= raquette.x + EPAISSEUR_RAQUETTE_X && this.x + 2*RAYON_BALLE + facteurSmash*OFFSET_X >= raquette.x
						&& this.y + RAYON_BALLE >= raquette.y && this.y + RAYON_BALLE < raquette.y + raquette.longueurRaquette_y) {
						return 1;
					}
					if (this.x > BALLE_X_MAX) {
						return 2;	// point perdu 
					}
			}
			else {
				if (this.x <= raquette.x + EPAISSEUR_RAQUETTE_X && this.x + 2*RAYON_BALLE + OFFSET_X >= raquette.x 
						&& this.y + RAYON_BALLE >= raquette.y && this.y + RAYON_BALLE < raquette.y + raquette.longueurRaquette_y) {
					return 1;
				}
				if (this.x > BALLE_X_MAX) {
					return 2;	// point perdu
				}
			}
		}
		
		// Rebond sur la tranche de la raquette
		if ((this.x + RAYON_BALLE > raquette.x && this.x + RAYON_BALLE < raquette.x + EPAISSEUR_RAQUETTE_X)
			&& ((deplacement_Y > 0 &&this.y + 2*RAYON_BALLE < raquette.y && this.y > raquette.y - AMPLITUDE_Y_MAX)
					|| (deplacement_Y < 0 && this.y > raquette.y + raquette.longueurRaquette_y 
							&& this.y + 2* RAYON_BALLE < raquette.y + raquette.longueurRaquette_y + AMPLITUDE_Y_MAX))) {
			return 3;	
		}

		return 0;	// pas de contact
	}
	
	public int evenementFilet() {
		
		aceChecked = false;
		indiceJoueurSmash = -1;
		PingPong.pong.balleRenvoyée = false;
		
		// Test si la balle reste dans le filet
		this.randomFilet = new Random();
		int probaFilet = (int) Math.ceil(this.randomFilet.nextFloat()*12);
		
		// Le filet ralentie la vitesse longitudinale
		if (probaFilet == 11) {
			filet = true;
			return 1;
		}
		
		// Le filet arrête la balle
		if (probaFilet == 12) {
			filet = true;
			return 2;
		}
		filet = false;
		return 0;
	}
	
	public int translationBalle_X(int nombre) {
		if (nombre == 1) {
			deplacement_X = (int) Math.round(randomDeplacement_X.nextGaussian()*AMPLITUDE_X + facteurAce*OFFSET_X);
		}
		else if(nombre == 2) {
			deplacement_X = (int) Math.round(randomDeplacement_X.nextGaussian()*AMPLITUDE_X + facteurSmash*OFFSET_X);
		}
		else if (nombre == 0){
			deplacement_X = (int) Math.round(randomDeplacement_X.nextGaussian()*AMPLITUDE_X + OFFSET_X);
		}
		return deplacement_X;
	}
	
	public int translationBalle_Y(int nombre) {
		if (nombre == 1) {
			deplacement_Y = (int) Math.round(randomDeplacement_Y.nextGaussian()*AMPLITUDE_Y + facteurAce*OFFSET_Y);
		}
		else if(nombre == 2) {
			deplacement_Y = (int) Math.round(randomDeplacement_Y.nextGaussian()*AMPLITUDE_Y + facteurSmash*OFFSET_Y);
		}
		else if (nombre == 0) {
			deplacement_Y = (int) Math.round(randomDeplacement_Y.nextGaussian()*AMPLITUDE_Y + OFFSET_Y);
		}
		return deplacement_Y;
	}
	
	/*
	 *  Evenement ace au cours du service
	 *  - probabilité d'apparition de l'événement
	 *  - effet sur les composantes du vecteur vitesse de la balle
	 */
	
	// Probabilité de renvoyer un tir smashé
	public boolean servicePuissant() {
		this.randomServicePuissant = new Random();
		this.probaServicePuissant = (int) Math.ceil(this.randomServicePuissant.nextFloat()*6);
		if (probaServicePuissant == 6) {
			return ace = true;
		}
		return ace = false;
	}
	
	public boolean service() {
		
		this.longueurEchange = 0;
		filetChecked = false;
		
		ace = servicePuissant();
		if (serviceEnCours) {
			if (this.x < TABLE_X_MIN + LONGUEUR_TABLE_X/2) {
				if (ace) {
					this.deplacement_X = Math.abs(translationBalle_X(1));
				}
				else {
					this.deplacement_X = Math.abs(translationBalle_X(0));
				}
			}
			if (this.x > TABLE_X_MIN + LONGUEUR_TABLE_X/2) {
				if (ace) {
					this.deplacement_X = - Math.abs(translationBalle_X(1));
				}
				else {
					this.deplacement_X = - Math.abs(translationBalle_X(0));
				}
			}
			if (this.y < TABLE_Y_MIN + HAUTEUR_TABLE_Y/2) {
				this.deplacement_Y = Math.abs(translationBalle_Y(0));
				if (this.deplacement_Y == 0) {
					this.deplacement_Y = AMPLITUDE_Y;
				}
			}
			if (this.y > TABLE_Y_MIN + HAUTEUR_TABLE_Y/2) {
				this.deplacement_Y = - Math.abs(translationBalle_Y(0));
				if (this.deplacement_Y == 0) {
					this.deplacement_Y = - AMPLITUDE_Y;
				}
			}
		
			serviceEnCours = false;
			return PingPong.pong.balleServie = true;
		}
		
		return PingPong.pong.balleServie = false;
	}
	
	/*
	 * Evenement smash au cours du renvoi de la balle
	 *  - probabilité d'apparition de l'événement
	 *  - effet sur les composantes du vecteur vitesse de la balle
	 */

	// Probabilité de renvoi
	public boolean tirPuissant() {
		this.randomTirPuissant = new Random();
		this.probaTirPuissant = (int) Math.ceil(this.randomTirPuissant.nextFloat()*6);
		if (probaTirPuissant == 6) {
			return smash = true;
		}
		return smash = false;
	}
	
	// Renvoi smashé
	public boolean renvoiBalle (spriteRaquette raquette) {
		effetRaquette = effetBalle_Y();
		
		smash = tirPuissant();
		
		if (renvoiEnCours) {
			/*
			 * Indépendamment d'un renvoi smashé ou non, modification des composantes du vecteur vitesse selon l'effet de la raquette
			 *  - vitesse de la balle et mouvement de la raquette de même signe, ajout de la composante Effet,
			 *  - vitesse de la balle et mouvement de la raquette de signe opposé, soustraction de la composante Effet,
			 *  - si raquette immobile, renvoi aléatoire en amplitude selon X, renvoi miroir selon Y
			 */
			if (smash) {
				if ((deplacement_Y >= 0 && raquette.raquetteMontée) || (deplacement_Y < 0 && !raquette.raquetteMontée)) {
		
					deplacement_X = translationBalle_X(1) + longueurEchange/LONGUEUR_ECHANGE_MAX - facteurEffetRaquette_X*effetRaquette;
					if (deplacement_X <= 0) {
						deplacement_X = translationBalle_X(2) + longueurEchange/LONGUEUR_ECHANGE_MAX;
					}
					if (raquette.indiceJoueurRaquette == 2) {
						deplacement_X = - deplacement_X;
					}
						
					deplacement_Y = deplacement_Y - effetRaquette;	
				} 
				else if ((deplacement_Y >= 0 && !raquette.raquetteMontée) || (deplacement_Y < 0 && raquette.raquetteMontée)) {
					
					deplacement_X = translationBalle_X(2) + longueurEchange/LONGUEUR_ECHANGE_MAX + facteurEffetRaquette_X*effetRaquette;
					if (raquette.indiceJoueurRaquette == 2) {
						deplacement_X = - deplacement_X;
					}
					
					deplacement_Y = effetRaquette;	
				}
				else {
					deplacement_X = translationBalle_X(2) + longueurEchange/LONGUEUR_ECHANGE_MAX;
					if (raquette.indiceJoueurRaquette == 2) {
						deplacement_X = - deplacement_X;
					}
					
					deplacement_Y = + deplacement_Y;
				}
			}
			
			else {
				if ((deplacement_Y >= 0 && raquette.raquetteMontée) || (deplacement_Y < 0 && !raquette.raquetteMontée)) {
					
					deplacement_X = translationBalle_X(0) + longueurEchange/LONGUEUR_ECHANGE_MAX - facteurEffetRaquette_X*effetRaquette;
					if (deplacement_X <= 0) {
						deplacement_X = translationBalle_X(0) + longueurEchange/LONGUEUR_ECHANGE_MAX;
					}
					if (raquette.indiceJoueurRaquette == 2) {
						deplacement_X = - deplacement_X;
					}
					
					deplacement_Y = deplacement_Y - effetRaquette;
				}
				else if ((deplacement_Y >= 0 && !raquette.raquetteMontée) || (deplacement_Y < 0 && raquette.raquetteMontée)) {
					
					deplacement_X = translationBalle_X(0) + longueurEchange/LONGUEUR_ECHANGE_MAX + facteurEffetRaquette_X*effetRaquette;
					if (raquette.indiceJoueurRaquette == 2) {
						deplacement_X = - deplacement_X;
					}
					
					deplacement_Y = effetRaquette;
				}
				else {
					
					deplacement_X = translationBalle_X(0) + longueurEchange/LONGUEUR_ECHANGE_MAX;
					if (raquette.indiceJoueurRaquette == 2) {
						deplacement_X = - deplacement_X;
					}
					deplacement_Y = + deplacement_Y;
				}
			}
			
			smashChecked = true;
			longueurEchange++;
			renvoiEnCours = false;
			return PingPong.pong.balleRenvoyée = true;
		}
		return PingPong.pong.balleRenvoyée = false;
	}
	
	/*
	 * Défini selon l'axe Y l'amplitude de l'effet de brossage de la raquette sur la balle
	 */
	public int effetBalle_Y() {
		randomEffetRaquette = new Random();
		effetRaquette = 2*((int) Math.abs(Math.round(randomEffetRaquette.nextGaussian()*AMPLITUDE_Y + OFFSET_Y)));
		return effetRaquette;
	}
	
	public int conditionVictoire(spriteRaquette raquette1, spriteRaquette raquette2) {
		
		if ((raquette1.score >= 11 && raquette1.score >= raquette2.score +2) || raquette1.score == 21) {
			return 1;
		}
		else if ((raquette2.score >= 11 && raquette2.score >= raquette1.score +2) || raquette2.score == 21) {
			return 2;
		}
		else {
			return 0;
		}
	}
	
	public void paint(Graphics designBalle) {
		designBalle.setColor(new Color(255, 215, 0));
		designBalle.fillOval(this.x, this.y, 2*RAYON_BALLE, 2*RAYON_BALLE);
	}

}