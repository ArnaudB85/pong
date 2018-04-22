package invariant;

public interface ConstantesDuJeu {
	
	// Paramètres de la loi normale
	public final int AMPLITUDE_X = 1;
	public final int OFFSET_X = 6;
	public final int AMPLITUDE_X_MAX = AMPLITUDE_X + OFFSET_X;	// [-1;1] + 6 => [5;7]
		
	public final int AMPLITUDE_Y = 2;
	public final int OFFSET_Y = 0;
	public final int AMPLITUDE_Y_MAX = AMPLITUDE_Y + OFFSET_Y;	// [-1;1]*2 => [-2;2]
	
	// Taille de la fenêtre
	public final int LONGUEUR_FENETRE_X = 800;
	public final int HAUTEUR_FENETRE_Y = 770;
	public final int LARGEUR_BORD_FENETRE_X = 8;
	public final int HAUTEUR_BANDEAU_FENETRE_Y = 30;
	
	// Taille de la table
	public final int LONGUEUR_TABLE_X = 600;
	public final int HAUTEUR_TABLE_Y = 400;
	
	// Limite de la table
	public final int TABLE_X_MIN = 100 - LARGEUR_BORD_FENETRE_X;
	public final int TABLE_Y_MIN = 200 - HAUTEUR_BANDEAU_FENETRE_Y;
	
	// Dimension de la balle
	public final int RAYON_BALLE = 7;
	
	// Limite des positions de la balle
	public final int BALLE_X_MIN = TABLE_X_MIN - 5*RAYON_BALLE;
	public final int BALLE_X_MAX = TABLE_X_MIN + LONGUEUR_TABLE_X + 6*RAYON_BALLE;
	
	public final int BALLE_Y_MIN = TABLE_Y_MIN + 3;
	public final int BALLE_Y_MAX = TABLE_Y_MIN + HAUTEUR_TABLE_Y - 2*RAYON_BALLE - 4;
	
	// Dimension de la raquette
	public final int EPAISSEUR_RAQUETTE_X = 10;
	public final int LONGUEUR_RAQUETTE_Y = HAUTEUR_TABLE_Y / 6;
	
	// Déplacement des raquettes et positions initiales
	public final int INCREMENT_RAQUETTE_Y = 10;
	
	public final int RAQUETTE_1_X_DEPART = TABLE_X_MIN + EPAISSEUR_RAQUETTE_X;
	public final int RAQUETTE_2_X_DEPART = TABLE_X_MIN + LONGUEUR_TABLE_X - 2*EPAISSEUR_RAQUETTE_X;
	
	public final int RAQUETTE_Y_DEPART_HAUT = TABLE_Y_MIN + HAUTEUR_TABLE_Y/4;
	public final int RAQUETTE_Y_DEPART_BAS = TABLE_Y_MIN + HAUTEUR_TABLE_Y*3/4;
	
	public final int LONGUEUR_ECHANGE_MAX = 4;
	public final int FACTEUR_SMASH = 3;
	public final int FACTEUR_ACE = 3;
	public final int FACTEUR_EFFET_RAQUETTE_X = 4/3;
}
