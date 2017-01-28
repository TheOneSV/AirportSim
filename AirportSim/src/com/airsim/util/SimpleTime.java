package com.airsim.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleTime implements Comparable<SimpleTime> {
	
	private StringProperty timeProperty = new SimpleStringProperty();
	private int days, hours, minutes, seconds, milliseconds;

	public SimpleTime() {
		days = 1;
		hours = seconds = milliseconds = 0;
		timeProperty.set("-- : -- : -- : --");
	}

	public SimpleTime(int days, int hours, int minutes, int seconds, int milliseconds) {
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		updateTimeProperty();
	}

	public void addMilliseconds(int milliseconds) {
		this.milliseconds += milliseconds;
		if(this.milliseconds >= 1000) {
			addSeconds(this.milliseconds/1000);
			this.milliseconds %= 1000;
		}
		updateTimeProperty();
	}
	protected void addSeconds(int seconds) {
		this.seconds += seconds;
		if(this.seconds >= 60) {
			addMinutes(this.seconds/60);
			this.seconds %= 60;	
		}
		updateTimeProperty();
	}
	protected void addMinutes(int minutes) {
		this.minutes += minutes;
		if(this.minutes >= 60) {
			addHours(this.minutes/60);	
			this.minutes %= 60;
		}
		updateTimeProperty();
	}
	protected void addHours(int hours) {
		this.hours += hours;
		if(this.hours >= 24) {
			addDays(this.hours/24);	
			this.hours %= 24;
		}
		updateTimeProperty();
	}
	protected void addDays(int days) {
		this.days += days;
		updateTimeProperty();
	}
	
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
		updateTimeProperty();
	}

	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
		updateTimeProperty();
	}

	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
		updateTimeProperty();
	}

	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
		updateTimeProperty();
	}

	public int getMilliseconds() {
		return milliseconds;
	}
	public void setMilliseconds(int milliseconds) {
		this.milliseconds = milliseconds;
		updateTimeProperty();
	}
	
	public StringProperty timeProperty() {
		return timeProperty;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (days ^ (days >>> 32));
		result = prime * result + (hours ^ (hours >>> 32));
		result = prime * result + (minutes ^ (minutes >>> 32));
		result = prime * result + (seconds ^ (seconds >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleTime other = (SimpleTime) obj;
		if (days != other.days)
			return false;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		if (seconds != other.seconds)
			return false;
		return true;
	}

	@Override
	public int compareTo(SimpleTime obj) {
		if (days >  obj.days) {
			return 1;
		} else if (days == obj.days) {
			if (hours > obj.hours) {
				return 1;
			} else if (hours == obj.hours) {
				if (minutes > obj.minutes) {
					return 1;
				} else if (minutes == obj.minutes) {
					if (seconds > obj.seconds) {
						return 1;
					} else if (seconds == obj.seconds) {
						return 0;
					}
				}
			}
		}
		return -1;
	}
	
	public double getTimeInSeconds() {
		double time = days*86400;
		time += hours*3600;
		time += minutes*60;
		return time;
	}
	public void resset() {
		days = 1;
		hours = seconds = milliseconds = 0;
		timeProperty.set("-- : -- : -- : --");
	}

	@Override
	public String toString() {
		StringBuilder strTime = new StringBuilder("Day: " + days + " | Time: ");
		
		if(hours < 10) {
			strTime.append('0');
		}
		strTime.append(hours + " : ");
		
		if(minutes < 10) {
			strTime.append('0');
		}
		strTime.append(minutes + " : ");
		

		if(seconds < 10) {
			strTime.append('0');
		}
		strTime.append(seconds);
		
		return strTime.toString();
	}
	
	protected void updateTimeProperty() {
		timeProperty.set(this.toString());
	}


}
