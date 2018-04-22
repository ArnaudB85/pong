package pong;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import sprite.*;
import invariant.*;


public class PingPong implements ConstantesDuJeu, parametres, ActionListener, KeyListener, MouseListener {
	
	public static PingPong pong;
	public static spriteBalle balle;
	public static spriteScore sp_Score;
	public static Renderer rendu;
	
	public spriteRaquette joueur_1, joueur_2;
	
	public static spriteScore spriteScoreJoueur_1, spriteScoreJoueur_2;
		
	public boolean bot = false, choixDifficulté = false;
	public int botDifficulté, botDéplacement, botCooldown = 0;
	
	public boolean z, s, up, down, d, left, echap;
	public static spriteTouche touche;
	
	public int souris_x, souris_y;
	public boolean zoneCliquée = false;
	
	public static spriteBarre_Paramètre barre_Paramètre, barreTest;
	public spriteToken Token, TokenTest;
	public int X, Y, nombreDePosition, positionToken;

	public boolean balleServie = false, balleRenvoyée = false;
	
	public int statutJeu = 0;	// 0 = stop, 1 = pause, 2 = jeu en cours, 3 = victoire, -1 = paramètres, -2 = règles
	
	public PingPong() {
		Timer chrono = new Timer(20, this);
		
		JFrame monCadre = new JFrame("Table de ping-pong");
		rendu = new Renderer();
				
		monCadre.setSize(new Dimension(LONGUEUR_FENETRE_X + LARGEUR_BORD_FENETRE_X, HAUTEUR_FENETRE_Y + HAUTEUR_BANDEAU_FENETRE_Y + 9));
		monCadre.setLocationRelativeTo(null);	
		monCadre.setVisible(true);
		monCadre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		monCadre.add(rendu);
		monCadre.addKeyListener(this);
		monCadre.addMouseListener(this);

		chrono.start();
	}

	public void start() {
		statutJeu = 1;
		joueur_1 = new spriteRaquette( 1, 2);
		joueur_2 = new spriteRaquette( 2, 2);
		balle = new spriteBalle(this);
		balle.indiceServeur = 1;
		balle.longueurEchange = 0;
	}
	
	public void update() {
		if (z) {
			joueur_1.monterRaquette(true);
		}
		if(s) {
			joueur_1.monterRaquette(false);
		}
		if (d && balle.indiceServeur == 1 && balleServie == false) {
			balle.serviceEnCours = true;
			balle.service();
		}
		
		if(!bot) {
			if (up) {
				joueur_2.monterRaquette(true);
			}
			if(down) {
				joueur_2.monterRaquette(false);
			}
			if (left && balle.indiceServeur == 2 && balleServie == false) {
				balle.serviceEnCours = true;
				balle.service();
			}
		}
		
		else {
			if (botCooldown > 0) {
				botCooldown--;
				if (botCooldown == 0) {
					botDéplacement = 0;
				}
			}
			if (botDéplacement < 10) {
				if (balle.y <= joueur_2.y) {
					joueur_2.monterRaquette(true);
					botDéplacement ++;
				}
				if(balle.y + 2*RAYON_BALLE >= joueur_2.y + joueur_2.longueurRaquette_y) {
					joueur_2.monterRaquette(false);
					botDéplacement ++;
				}
				
				if (botDifficulté == 0) {
					botCooldown = BOT_COOLDOWN_EASY;
				}
				if (botDifficulté == 1) {
					botCooldown = BOT_COOLDOWN_NORMAL;
				}
				if (botDifficulté == 2) {
					botCooldown = BOT_COOLDOWN_HARD;
				}
			}
			
			if (balle.indiceServeur == 2 && balleServie == false) {
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				balle.serviceEnCours = true;
				balle.service();
			}
		}
		
		balle.update(joueur_1, joueur_2);
	}
	
	public void paint(Graphics2D contexteGraphique) {
		
		// Ajout anti-aliasing contre pixellisation
		contexteGraphique.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// BackgroundColor et Titre
		if (statutJeu == 0 || statutJeu == 1 || statutJeu == 2 || statutJeu == 3 || statutJeu == -1 || statutJeu == -2) {
			contexteGraphique.setColor(new Color(228, 228, 247));
			contexteGraphique.fillRect(0, 0, LONGUEUR_FENETRE_X, HAUTEUR_FENETRE_Y);
			contexteGraphique.setFont(new Font("Arial", 1, 36));
			contexteGraphique.setColor(new Color(0, 128, 128));
			contexteGraphique.drawString("Tennis de table", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 118, TABLE_Y_MIN/4 + 8);
			contexteGraphique.setFont(new Font("Arial", 1, 36));
			contexteGraphique.drawString("Tennis de table", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 119, TABLE_Y_MIN/4 + 9);
			contexteGraphique.setFont(new Font("Arial", 1, 36));
			contexteGraphique.setColor(new Color(255, 190, 45));
			contexteGraphique.drawString("Tennis de table", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 120, TABLE_Y_MIN/4 + 10);
		}
		
		// Peint la table en bleu, les lignes de marquages blanches et la barre de score 
		if (statutJeu == 1 || statutJeu == 2 || statutJeu == 3) {
			contexteGraphique.setColor(new Color(126, 216, 216));
			contexteGraphique.fillRect(TABLE_X_MIN, TABLE_Y_MIN, LONGUEUR_TABLE_X, HAUTEUR_TABLE_Y);
			contexteGraphique.setColor(Color.WHITE);
			contexteGraphique.drawRect(TABLE_X_MIN + 3, TABLE_Y_MIN + 3, LONGUEUR_TABLE_X - 7, HAUTEUR_TABLE_Y - 7);
			contexteGraphique.drawLine(TABLE_X_MIN + LONGUEUR_TABLE_X/2, TABLE_Y_MIN + 3, TABLE_X_MIN + LONGUEUR_TABLE_X/2, TABLE_Y_MIN + HAUTEUR_TABLE_Y - 4);
			
			// Revenir au menu principal
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 80, HAUTEUR_FENETRE_Y*9/10, "Esc", "Revenir au menu principal", 1);
			touche.paint(contexteGraphique);
			
			// Barre de score
			spriteScoreJoueur_1 = new spriteScore(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 40, TABLE_Y_MIN*2/3 - 10);
			spriteScoreJoueur_1.paint(contexteGraphique);
			contexteGraphique.setFont(new Font("Arial", 1, 20));
			contexteGraphique.setColor(Color.GRAY);
			if (joueur_1.score < 10) {
				contexteGraphique.drawString(String.valueOf(joueur_1.score), TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 22, TABLE_Y_MIN*2/3 + 22);
			} else {
				contexteGraphique.drawString(String.valueOf(joueur_1.score), TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 35, TABLE_Y_MIN*2/3 + 22);
			}
			
			spriteScoreJoueur_2 = new spriteScore(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 10, TABLE_Y_MIN*2/3 - 10);
			spriteScoreJoueur_2.paint(contexteGraphique);
			contexteGraphique.setFont(new Font("Arial", 1, 20));
			contexteGraphique.setColor(Color.GRAY);
			if (joueur_2.score < 10) {
				contexteGraphique.drawString(String.valueOf(joueur_2.score), TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 28, TABLE_Y_MIN*2/3 + 22);
			}else {
				contexteGraphique.drawString(String.valueOf(joueur_2.score), TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 15, TABLE_Y_MIN*2/3 + 22);
			}
			
			contexteGraphique.setFont(new Font("Arial", 1, 20));
			contexteGraphique.setColor(Color.GRAY);
			contexteGraphique.drawString("Joueur_1", TABLE_X_MIN + LONGUEUR_TABLE_X/2/2 - 15, TABLE_Y_MIN*2/3 + 22);
			contexteGraphique.drawString("  -  ", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 15, TABLE_Y_MIN*2/3 + 22);
			if (!bot) {
				contexteGraphique.drawString("Joueur_2", TABLE_X_MIN + LONGUEUR_TABLE_X/2*4/3 - 15, TABLE_Y_MIN*2/3 + 22);	
			} else {
				contexteGraphique.drawString("Ordinateur", TABLE_X_MIN + LONGUEUR_TABLE_X/2*4/3 - 15, TABLE_Y_MIN*2/3 + 22);	
			}			
		}
		
		// Menu principal
		if (statutJeu == 0) {
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100, TABLE_Y_MIN + 40, "U", "Partie contre IA", 3);	
			touche.paint(contexteGraphique);
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100, TABLE_Y_MIN + 105, "M", "Partie 2 joueurs", 3);
			touche.paint(contexteGraphique);
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100, TABLE_Y_MIN + 170, "P", "Paramètres", 3);
			touche.paint(contexteGraphique);
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100, TABLE_Y_MIN + 235, "R", "Règles du jeu", 3);
			touche.paint(contexteGraphique);
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100, TABLE_Y_MIN + 300, "Q", "Quitter le jeu", 3);
			touche.paint(contexteGraphique);
		}

		// Jeu en fonctionnement ou en pause
		if (statutJeu == 2 || statutJeu == 1) {
			contexteGraphique.setFont(new Font("Arial", 1, 10));
			contexteGraphique.setColor(Color.GRAY);
			contexteGraphique.drawString("Presse 'Espace' pour enlever/mettre le jeu en pause", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 130, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 25);

			// Aide touches joueur 1
			touche = new spriteTouche(TABLE_X_MIN + 20, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 40, "Z", "Monter", 2);
			touche.paint(contexteGraphique);
			touche = new spriteTouche(TABLE_X_MIN + 20, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 70, "S", "Descendre", 2);
			touche.paint(contexteGraphique);
			touche = new spriteTouche(TABLE_X_MIN + 20, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 100, "D", "Servir", 2);
			touche.paint(contexteGraphique);
			
			if (!bot) {
				// Aide touches joueur 2
				touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X - 100, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 40, "\u2191", "Monter", 2);
				touche.paint(contexteGraphique);
				touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X - 100, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 70, "\u2193", "Descendre", 2);
				touche.paint(contexteGraphique);
				touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X - 100, TABLE_Y_MIN + HAUTEUR_TABLE_Y + 100, "\u2190", "Servir", 2);
				touche.paint(contexteGraphique);
			} 
			
			if (balle.ace && balle.aceChecked) {
				contexteGraphique.setFont(new Font("Arial", 1, 12));
				contexteGraphique.setColor(Color.GRAY);
					
				if (balle.indiceServeur == 1) {
					contexteGraphique.drawString("Ace", (BALLE_X_MIN), (joueur_1.y + joueur_1.longueurRaquette_y/2));
				}
				if (balle.indiceServeur == 2) {
					contexteGraphique.drawString("Ace", (TABLE_X_MIN + LONGUEUR_TABLE_X), (joueur_2.y + joueur_2.longueurRaquette_y/2));
				}
			}
			
			if (balle.indiceJoueurSmash == 1) {
				contexteGraphique.setFont(new Font("Arial", 1, 12));
				contexteGraphique.setColor(Color.GRAY);
				contexteGraphique.drawString("Smash", (BALLE_X_MIN), (joueur_1.y + joueur_1.longueurRaquette_y/2));
			}
			
			if (balle.indiceJoueurSmash == 2) {
				contexteGraphique.setFont(new Font("Arial", 1, 12));
				contexteGraphique.setColor(Color.GRAY);
				contexteGraphique.drawString("Smash", (TABLE_X_MIN + LONGUEUR_TABLE_X), (joueur_2.y + joueur_2.longueurRaquette_y/2));
			}
			
			if (balle.filet && balle.filetChecked) {
				contexteGraphique.setFont(new Font("Arial", 1, 12));
				contexteGraphique.setColor(Color.GRAY);
				contexteGraphique.drawString("Filet", (TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 10), TABLE_Y_MIN + HAUTEUR_TABLE_Y/2);
			}
			
			joueur_1.paint(contexteGraphique);
			joueur_2.paint(contexteGraphique);
			balle.paint(contexteGraphique);
		}
		
		// Jeu en pause
		if (statutJeu == 1) {
			contexteGraphique.setFont(new Font("Arial", 1, 30));
			contexteGraphique.setColor(Color.WHITE);
			contexteGraphique.drawString("Jeu en pause", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 98, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 6);
			contexteGraphique.setColor(Color.GRAY);
			contexteGraphique.drawString("Jeu en pause", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 99, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 7);
		}
		
		// Ecran victoire
		if (statutJeu == 3) {
			// Affichage vainqueur de la manche
			contexteGraphique.setFont(new Font("Arial", 1, 30));
			contexteGraphique.setColor(Color.WHITE);
			contexteGraphique.drawString(balle.vainqueur + " a gagné !!", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 134, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 6);
			contexteGraphique.setColor(new Color(255, 215, 0));
			contexteGraphique.drawString(balle.vainqueur + " a gagné !!", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 135, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 7);
		}
		
		// Ecran règles du jeu
		if (statutJeu == -1) {
			contexteGraphique.setFont(new Font("Arial", 1, 20));
			contexteGraphique.setColor(Color.GRAY);
			contexteGraphique.drawString("Paramètres", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 70, TABLE_Y_MIN*3/4);
			
			barreTest = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 12, 3);
			barreTest.paint(contexteGraphique);
			TokenTest = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 12, 0);
			TokenTest.paintComponent(contexteGraphique);
			
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 350, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 3);
			barre_Paramètre.paint(contexteGraphique);
			
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 150, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 3);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 150, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 0);
			Token.paintComponent(contexteGraphique);
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 3);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 5);
			Token.paintComponent(contexteGraphique);
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 250, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 3);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 250, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 100, 9);
			Token.paintComponent(contexteGraphique);
			
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 350, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 4);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 350, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 0);
			Token.paintComponent(contexteGraphique);
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 150, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 4);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 150, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 3);
			Token.paintComponent(contexteGraphique);
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 4);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 6);
			Token.paintComponent(contexteGraphique);
			barre_Paramètre = new spriteBarre_Paramètre(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 250, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 4);
			barre_Paramètre.paint(contexteGraphique);
			Token = new spriteToken(TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 250, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 100, 9);
			Token.paintComponent(contexteGraphique);
			
			// Revenir au menu principal
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X - 100, HAUTEUR_FENETRE_Y*9/10, "Esc", "Revenir au menu principal", 1);
			touche.paint(contexteGraphique);
		}
		
		// Ecran règles du jeu
		if (statutJeu == -2) {
			contexteGraphique.setFont(new Font("Arial", 1, 20));
			contexteGraphique.setColor(Color.GRAY);
			contexteGraphique.drawString("Règles du jeu", TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 70, TABLE_Y_MIN*3/4);
			
			contexteGraphique.setFont(new Font("Arial", 1, 15));
			int newline = contexteGraphique.getFont().getSize() + 5;
			String[] règlesDuJeu = {"Conditions de victoires: ",
				" - le premier joueur ayant au moins 11 points et possèdant 2 points d'avance,",
				" - ou le premier ayant atteint 21 points.",
				" ",
				"Service:",
				" - chaque joueur sert 2 fois d'affilé jusqu'à 10-10, puis chacun son tour,",
				" - le serveur doit envoyer la balle dans la direction de la diagonale.",
				" ",
				"Filet:",
				" - si la balle de service reste dans le filet, le service est à refaire,",
				" - si la balle en jeu reste dans le filet, le point est perdu.",
				" ",
				" Gameplay :",
				" - à chaque service, probabilité de faire un service à forte vitesse (ace),",
				" - à chaque rebond sur la raquette:",
				"        - la vitesse longitudinale: ",
				"        amplitude redéfinie aléatoirement et s'incrémente en fonction de la longueur de",
				"        l'échange auquel s'ajoute/se retire un effet du au mouvement de la raquette,",
				"        - la vitesse latérale:",
				"        amplitude redéfinie aléatoirement,",
				"        direction haut/bas dépend du mouvement haut/bas de la raquette,",
				"        - probabilité de faire un renvoi à forte vitesse (smash).",
			};
			int hauteurLigne = TABLE_Y_MIN - 10;
			for (int i = 0; i < règlesDuJeu.length; i++) {
				contexteGraphique.drawString(règlesDuJeu[i], TABLE_X_MIN, hauteurLigne += newline);
			}
			
			// Revenir au menu principal
			touche = new spriteTouche(TABLE_X_MIN + LONGUEUR_TABLE_X - 100, HAUTEUR_FENETRE_Y*9/10, "Esc", "Revenir au menu principal", 1);
			touche.paint(contexteGraphique);
		}
	}
	
	public void evenement_Echap(boolean echap) {	
		if (echap) {
			if (statutJeu == 0) {
				Object[] options = {"OK", "Annuler"};
				int n = JOptionPane.showOptionDialog(null,
					"Etes-vous sûr de vouloir quitter le jeu ?", "Choississez une option...",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
					options, options[1]);
				
				if (n == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
			
			if (statutJeu == 2) {
				statutJeu = 1;
			}
			
			if (statutJeu == 1) {
				Object[] options = {"OK", "Annuler"};
				int n = JOptionPane.showOptionDialog(null,
					"Etes-vous sûr de vouloir quitter la partie en cours ?", "Choississez une option...",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
					options, options[1]);
				
				if (n == JOptionPane.OK_OPTION) {
					statutJeu = 0;
					joueur_1.score = 0;
					joueur_2.score = 0;
					balle.serviceEnCours = false;
					balleServie = false;
				}
			}
			
			if (statutJeu == 3) {
				statutJeu = 0;
				joueur_1.score = 0;
				joueur_2.score = 0;
				balle.serviceEnCours = false;
				balleServie = false;
			}
			
			if (statutJeu == -1 || statutJeu == -2) {
				statutJeu = 0;
			}
		}
	}
	
	public int choixDifficulté (boolean choixEnCours) {
		this.choixDifficulté = choixEnCours;
		
		if(choixDifficulté && statutJeu == 0) {
			Object[] optionDifficulté = {"Facile", "Normal", "Difficile"};
			int n = JOptionPane.showOptionDialog(null,
				"Niveau de l'IA ?", "Choississez une option...",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				optionDifficulté, optionDifficulté[1]);
				
			if (n == JOptionPane.YES_OPTION) {
				botDifficulté = 0;
			}
			if (n == JOptionPane.NO_OPTION) {
				botDifficulté = 1;
			}
			if (n == JOptionPane.CANCEL_OPTION) {
				botDifficulté = 2;
			}
			return botDifficulté;
		}
		
		return 1;
	}
	
	public int zoneCliquableToken (int souris_x, int souris_y, int X, int Y, int nombreDePosition) {
		this.X = X;
		this.Y = Y;
		this.souris_x = souris_x;
		this.souris_y = souris_y;
		this.nombreDePosition = nombreDePosition;
		
		if (souris_x >= X - 8 && souris_x <= X + 20 && souris_y >= Y - 7 && souris_y <= Y + 21) {
			return 0;
		}
		
		else if (nombreDePosition == 4 && souris_x >= X + 25 && souris_x <= X + 53 && souris_y >= Y - 7 && souris_y <= Y + 21) {
			return 3;
		}
		
		else if (nombreDePosition == 3 && souris_x >= X + 42 && souris_x <= X + 60 && souris_y >= Y - 7 && souris_y <= Y + 21) {
			return 5;
		}
		
		else if(nombreDePosition == 4 && souris_x >= X + 58 && souris_x <= X + 86 && souris_y >= Y - 7 && souris_y <= Y + 21) {
			return 6;
		}

		else if (souris_x >= X + 92 && souris_x <= X + 120 && souris_y >= Y - 7 && souris_y <= Y + 21) {
			return 9;
		}
		
		else {
			return -1;
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (statutJeu == 2) {
			update();
		}
		rendu.repaint();
	}

	public static void main(String[] args) {
		pong = new PingPong();
	}

	@Override
	public void keyPressed(KeyEvent evenementClavier) {
		int ID_touche = evenementClavier.getKeyCode();
		
		if(ID_touche == KeyEvent.VK_Z) {
			z = true;
		}
		if(ID_touche == KeyEvent.VK_S) {
			s = true;
		}
		if(ID_touche == KeyEvent.VK_UP) {
			up = true;
		}
		if(ID_touche == KeyEvent.VK_DOWN) {
			down = true;
		}
		if(ID_touche == KeyEvent.VK_D) {
			d = true;
		}
		if (ID_touche == KeyEvent.VK_LEFT) {
			left = true;
		}
		if (ID_touche == KeyEvent.VK_ESCAPE) {
			echap = true;
			evenement_Echap(echap);
		}
		if (ID_touche == KeyEvent.VK_SPACE) {
			if (statutJeu == 1) {
				statutJeu = 2;
			}
			else if (statutJeu == 2) {
				statutJeu = 1;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent evenementClavier) {
		int ID_touche = evenementClavier.getKeyCode();
		
		if(ID_touche == KeyEvent.VK_Z) {
			z = false;
		}
		if(ID_touche == KeyEvent.VK_S) {
			s = false;
		}
		if(ID_touche == KeyEvent.VK_UP) {
			up = false;
		}
		if(ID_touche == KeyEvent.VK_DOWN) {
			down = false;
		}
		if(ID_touche == KeyEvent.VK_D) {
			d = false;
		}
		if (ID_touche == KeyEvent.VK_LEFT) {
			left = false;
		}
		if (ID_touche == KeyEvent.VK_ESCAPE) {
			echap = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent evenementClavier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent evenementSouris) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent evenementSouris) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent evenementSouris) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent evenementSouris) {
		souris_x = evenementSouris.getX();
		souris_y = evenementSouris.getY();
		
		System.out.println(souris_x + " " + souris_y);
		
		// Menu principal
		if(statutJeu == 0) {
			// Bouton partie 1 joueur
			if(this.souris_x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100 && this.souris_x < TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 112
					&& this.souris_y > TABLE_Y_MIN + 40 && this.souris_y < TABLE_Y_MIN + 82) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				bot = true;
				zoneCliquée = true;
				choixDifficulté(zoneCliquée);
				
				start();
			}
			
			// Bouton partie 2 joueurs
			if(this.souris_x >= TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100 && this.souris_x <= TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 112
					&& this.souris_y >= TABLE_Y_MIN + 105 && this.souris_y <= TABLE_Y_MIN + 146) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				zoneCliquée = true;
				bot = false;
				start();
			}
						
			// Bouton Paramètres
			if(this.souris_x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100 && this.souris_x < TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 112
					&& this.souris_y > TABLE_Y_MIN + 170 && this.souris_y < TABLE_Y_MIN + 212) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				zoneCliquée = true;
				statutJeu = -1;
			}
								
			// Bouton Règles du jeu
			if(this.souris_x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100 && this.souris_x < TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 112
					&& this.souris_y > TABLE_Y_MIN + 235 && this.souris_y < TABLE_Y_MIN + 277) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				zoneCliquée = true;
				statutJeu = -2;
			}
						
			// Bouton Quitter le jeu
			if(this.souris_x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 100 && this.souris_x < TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 112
					&& this.souris_y > TABLE_Y_MIN + 300 && this.souris_y < TABLE_Y_MIN + 342) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				echap = true;
				evenement_Echap(echap);
			}
		}
		
		// Menu Paramètres
		if(statutJeu == -1) {
			
			if(this.souris_x > TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 50 && this.souris_x < TABLE_X_MIN + LONGUEUR_TABLE_X/2 + 50
					&& this.souris_y > TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 14 && this.souris_y < TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 + 14 ) {
				positionToken = zoneCliquableToken(souris_x, souris_y, TABLE_X_MIN + LONGUEUR_TABLE_X/2 - 50, TABLE_Y_MIN + HAUTEUR_TABLE_Y/2 - 12, 3);		
				
				System.out.println(souris_x + " " + souris_y);
				System.out.println(positionToken);
				
				if (positionToken == 0) {
					
				}
				
				if(positionToken == 5) {
					
				}
				
				if(positionToken == 9) {
					
				}
			}
			
			zoneCliquée = true;
			
			System.out.println(positionToken);
		}
	}

	@Override
	public void mouseReleased(MouseEvent evenementSouris) {
		zoneCliquée = false;
		echap = false;
	}
}
