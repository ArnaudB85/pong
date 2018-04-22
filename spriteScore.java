package sprite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import invariant.*;

public class spriteScore implements ConstantesDuJeu {
	
	public int x, y;
	
	public spriteScore(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void paint(Graphics2D designFiche) {
		
		// Fond blanc et perspective foncé
		designFiche.setColor(Color.darkGray);
		designFiche.setStroke(new BasicStroke(2f));
		designFiche.drawRect(x + 1, y + 8, 30, 28);
		designFiche.setColor(Color.WHITE);
		designFiche.fillRect(x, y + 7, 30, 28);
		
		// Anneau rouge
		designFiche.setColor(Color.gray);
		designFiche.setStroke(new BasicStroke(1f));
		designFiche.fillOval(x + 6, y + 9 , 4, 4);
		designFiche.setColor(Color.red);
		designFiche.setStroke(new BasicStroke(3f));
		designFiche.drawArc(x + 2, y, 8, 10, 0, 270);
		designFiche.setColor(Color.darkGray);
		designFiche.setStroke(new BasicStroke(1f));
		designFiche.drawArc(x + 2, y + 1, 8, 10, 0, 90);
		
		// Anneau rouge
		designFiche.setColor(Color.gray);
		designFiche.setStroke(new BasicStroke(1f));
		designFiche.fillOval(x + 21, y + 9, 4, 4);
		designFiche.setColor(Color.red);
		designFiche.setStroke(new BasicStroke(3f));
		designFiche.drawArc(x + 17, y, 8, 10, 0, 270);
		designFiche.setColor(Color.darkGray);
		designFiche.setStroke(new BasicStroke(1f));
		designFiche.drawArc(x + 17, y + 1, 8, 10, 0, 90);
		
		// Ligne de séparation unité/dizaine
		designFiche.setColor(Color.lightGray);
		designFiche.setStroke(new BasicStroke(1f));
		designFiche.drawLine(x + 15, y + 7, x + 15 , y + 35);
	}
	
}
