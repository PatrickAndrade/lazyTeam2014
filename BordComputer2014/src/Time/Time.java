package Time;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Time {

	private enum Mois {
		Janvier, Fevrier, Mars, Avril, Mai, Juin, Juillet, Aout, Septembre, Octobre, Novembre, Decembre
	}

	private int year = 1970;
	private Mois month = Mois.Janvier;
	private int day = 1;
	private int hour = 0;
	private int minutes = 0;
	private int seconds = 0;

	public Time(int year, Mois month, int day, int hour, int minutes,
			int seconds) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public Time() {
	}

	public Time(int seconds) {
		convertSecondsToTime(seconds);
	}

	public synchronized void now() {
		long secondsUntilNow = System.currentTimeMillis() / 1000;
		convertSecondsToTime(secondsUntilNow);
		hour++; // On est a GMT + 1
	}
	
	public synchronized void reset() {
		year = 0;
		month = Mois.Janvier;
		day = 1;
		hour = 0;
		minutes = 0;
		seconds = 0;
	}

	public synchronized void update() {
		seconds++;

		updateMinute();
		updateHour();
		updateDay();
		updateMonth();
	}
	
	public synchronized Time clone() {
		return new Time(year, month, day, hour, minutes, seconds);
	}

	public synchronized String getHourMinuteSecond() {
		return hour + " / " + minutes + " / " + seconds;
	}

	public synchronized String toString() {
		return "[ " + day + " / " + month + " / " + year + " ] " + hour
				+ " / " + minutes + " / " + seconds;
	}
	
	public synchronized int totalSeconds(){
	    return hour * 3600 + minutes * 60 + seconds;
	}
	
	private void convertSecondsToTime(long seconds) {
		int numberSecondsDay = 60 * 60 * 24;
		int numberSecondsYear = numberSecondsDay * 365;

		boolean end = false;
		while (seconds >= numberSecondsYear && !end) {
			if (isLeapYear(year)) {
				if (seconds >= numberSecondsYear + numberSecondsDay) {
					seconds -= (numberSecondsYear + numberSecondsDay);
					year += 1;
				} else {
					end = true;
				}
			} else {
				seconds -= numberSecondsYear;
				year += 1;
			}
		}

		while (seconds >= (daysForMonth(year, month) * numberSecondsDay)) {
			seconds -= (daysForMonth(year, month) * numberSecondsDay);
			month = Mois.values()[month.ordinal() + 1];
		}

		while (seconds >= numberSecondsDay) {
			day += 1;
			seconds -= numberSecondsDay;
		}

		while (seconds >= 3600) {
			hour += 1;
			seconds -= 3600;
		}

		while (seconds >= 60) {
			minutes += 1;
			seconds -= 60;
		}

		this.seconds = (int) seconds;
	}
	
	private int daysForMonth(int year, Mois month) {
		int days = 30;
		switch (month) {
		case Fevrier:
			if (isLeapYear(year)) {
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

	private boolean isLeapYear(int year) {
		return (year % 4) == 0;
	}
	
	private void updateMinute() {
		if (seconds >= 60) {
			minutes++;
			seconds = 0;
		}
	}

	private void updateHour() {
		if (minutes >= 60) {
			hour++;
			minutes = 0;
		}
	}

	private void updateDay() {
		if (hour >= 24) {
			day++;
			hour = 0;
		}
	}

	private void updateMonth() {

		switch (month) {

		case Novembre:
		case Septembre:
		case Avril:
		case Juin:
			if (day >= 31) {
				int next = month.ordinal() + 1;
				month = Mois.values()[next];
				day = 1;
			}
			break;

		case Octobre:
		case Decembre:
		case Aout:
		case Juillet:
		case Mai:
		case Mars:
		case Janvier:
			if (day >= 32) {
				int next = (month.ordinal() + 1) % 12;

				if (next == 0) {
					year++;
				}

				month = Mois.values()[next];
				day = 1;
			}
			break;

		case Fevrier:
			if (year % 4 == 0) {
				if (day >= 30) {
					month = Mois.Mars;
					day = 1;
				}
			} else {
				if (day >= 29) {
					month = Mois.Mars;
					day = 1;
				}
			}
			break;

		default:
			break;

		}
	}
}
