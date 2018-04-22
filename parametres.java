package invariant;

public interface parametres extends ConstantesDuJeu {
	
	// Longueur de la raquette
	public final int LONGUEUR_RAQUETTE_Y_6 = HAUTEUR_TABLE_Y / 6;
	public final int LONGUEUR_RAQUETTE_Y_7 = HAUTEUR_TABLE_Y / 7;
	public final int LONGUEUR_RAQUETTE_Y_8 = HAUTEUR_TABLE_Y / 8;
	
	// Paramètres ordinateur pour jeu solo
	public final int BOT_COOLDOWN_EASY = 20;
	public final int BOT_COOLDOWN_NORMAL = 15;
	public final int BOT_COOLDOWN_HARD = 10;
	
	// Paramètres incrément vitesse selon la longueur de l'échange
	public final int LONGUEUR_ECHANGE_0 = 0;
	public final int LONGUEUR_ECHANGE_1 = 1;
	public final int LONGUEUR_ECHANGE_2 = 2;
	public final int LONGUEUR_ECHANGE_4 = 4;
	
	// Paramètres Ace
	public final int PROBA_MAX_SERVICE_PUISSANT_0 = 0;
	public final int PROBA_MAX_SERVICE_PUISSANT_3 = 3;
	public final int PROBA_MAX_SERVICE_PUISSANT_6 = 6;
	public final int PROBA_MAX_SERVICE_PUISSANT_12 = 12;
	
	// Paramètres Smash
	public final int PROBA_MAX_TIR_PUISSANT_0 = 0;
	public final int PROBA_MAX_TIR_PUISSANT_3 = 3;
	public final int PROBA_MAX_TIR_PUISSANT_6 = 6;
	public final int PROBA_MAX_TIR_PUISSANT_12 = 12;
	
	// Paramètres évenement Filet
	public final int PROBA_MAX_FILET_0 = 0;
	public final int PROBA_MAX_FILET_6 = 6;
	public final int PROBA_MAX_FILET_12 = 12;
	
}
