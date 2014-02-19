package Temps;

import java.sql.Date;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Temps implements Runnable {

	enum Mois {
		Janvier, Fevrier, Mars, Avril, Mai, Juin, Juillet, Aout, Septembre, Octobre, Novembre, Decembre
	}

	private int annee;
	private Mois mois;
	private int jours;
	private int heures;
	private int minutes;
	private int secondes;
	
	public Temps() {
		Date date = new Date(System.currentTimeMillis());
		
		annee = 2014;
		mois = Mois.Decembre;
		jours = 31;
		heures = 23;
		minutes = 59;
		secondes = 55;
	}

	@Override
	public void run() {
		while (true) {
			waitOneSeconde();
			update();
			System.out.println(this);
		}
	}

	private void update() {
		secondes++;

		updateMinute();
		updateHeure();
		updateJour();
		updateMois();
	}

	private void updateMinute() {
		if (secondes >= 60) {
			minutes++;
			secondes = 0;
		}
	}

	private void updateHeure() {
		if (minutes >= 60) {
			heures++;
			minutes = 0;
		}
	}

	private void updateJour() {
		if (heures >= 24) {
			jours++;
			heures = 0;
		}
	}

	private void updateMois() {

		switch (mois) {

		case Novembre:
		case Septembre:
		case Avril:
		case Juin:
			if (jours >= 31) {
				int next = mois.ordinal() + 1;
				mois = Mois.values()[next];
				jours = 1;
			}
			break;

		case Octobre:
		case Decembre:
		case Aout:
		case Juillet:
		case Mai:
		case Mars:
		case Janvier:
			if (jours >= 32) {
				int next = (mois.ordinal() + 1) % 12;

				if (next == 0) {
					annee++;
				}

				mois = Mois.values()[next];
				jours = 1;
			}
			break;

		case Fevrier:
			if (annee % 4 == 0) {
				if (jours >= 30) {
					mois = Mois.Mars;
					jours = 1;
				}
			} else {
				if (jours >= 29) {
					mois = Mois.Mars;
					jours = 1;
				}
			}
			break;

		default:
			break;

		}
	}

	private void waitOneSeconde() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public String toString() {
		return "[ " + jours + " / " + mois + " / " + annee + " ] " + heures
				+ " / " + minutes + " / " + secondes;
	}

	public static void main(String[] args) {
		new Thread(new Temps()).start();
	}
}
