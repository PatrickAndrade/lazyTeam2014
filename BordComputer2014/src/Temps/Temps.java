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

	private int annee = 1970;
	private Mois mois = Mois.Janvier;
	private int jours = 1;
	private int heures = 0;
	private int minutes = 0;
	private int secondes = 0;
	
	public Temps() {
		long secondsUntilNow = System.currentTimeMillis() / 1000;
		int nombre_sec_jour = 60 * 60 * 24;
		int nombre_sec_annee = nombre_sec_jour * 365;
		
		boolean end = false;
	    while (secondsUntilNow >= nombre_sec_annee && !end) {
	        if (isLeapYear(annee)) {
	            if (secondsUntilNow >= nombre_sec_annee + nombre_sec_jour) {
	                secondsUntilNow -= (nombre_sec_annee + nombre_sec_jour);
	                annee += 1;
	            } else {
					end = true;
	            }
	        } else {
	            secondsUntilNow -= nombre_sec_annee;
	            annee += 1;
	        }
	    }
	    
	    while (secondsUntilNow >= (daysForMonth(annee, mois) * nombre_sec_jour)) {
			secondsUntilNow -= (daysForMonth(annee, mois) * nombre_sec_jour);
			mois = Mois.values()[mois.ordinal() + 1];
		}
	    		
		while (secondsUntilNow >= nombre_sec_jour) {
			jours += 1;
			secondsUntilNow -= nombre_sec_jour;
		}
		
		while (secondsUntilNow >= 3600) {
			heures += 1;
			secondsUntilNow -= 3600;
		}
		
		while (secondsUntilNow >= 60) {
			minutes += 1;
			secondsUntilNow -= 60;
		}
		
		secondes = (int) secondsUntilNow;
		heures++; //On est a GMT + 1
	}

	private int daysForMonth(int annee, Mois mois) {
		int days = 30;
		switch (mois) {
			case Fevrier:
				if (isLeapYear(annee)) {
					days = 29;
				} else {
					days = 28;
				}
				break;
			case Octobre:
			case Decembre:
			case Aout:
			case Juillet:
			case Mai:
			case Mars:
			case Janvier:
				days = 31;
				break;
			default:
				break;
		}
		return days;
	}

	private boolean isLeapYear(int annee) {
		return (annee % 4) == 0;
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
