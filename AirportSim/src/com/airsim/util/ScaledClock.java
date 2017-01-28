package com.airsim.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScaledClock extends SimpleTime{
	
	private StringProperty strDays, strHours, strMinutes, strSeconds, strScale;
	private int scaledMillis;
	
	public ScaledClock() {
		this(1);
	}
	
	public ScaledClock(float scale) {
		super();
		strDays = new SimpleStringProperty("1");
		strHours = new SimpleStringProperty("0");
		strMinutes = new SimpleStringProperty("0");
		strSeconds = new SimpleStringProperty("0");
		strScale = new SimpleStringProperty("x1");
		setScale(scale);
	}
	
	@Override
	public void addMilliseconds(int milliseconds) {
		setMilliseconds(getMilliseconds() + milliseconds);
		if(getMilliseconds() >= scaledMillis) {
			addSeconds(getMilliseconds()/scaledMillis);
			setMilliseconds(getMilliseconds()%scaledMillis);
		}
		updateTimeProperty();
	}	
	@Override
	protected void addSeconds(int seconds) {
		super.addSeconds(seconds);
		strSeconds.set(Long.toString(getSeconds()));
	}
	protected void addMinutes(int minutes) {
		super.addMinutes(minutes);
		strMinutes.set(Long.toString(getMinutes()));
	}
	protected void addHours(int hours) {
		super.addHours(hours);
		strHours.set(Long.toString(getHours()));
	}
	protected void addDays(int days) {
		super.addDays(days);
		strDays.set(Long.toString(getDays()));
	}
	
	@Override
	protected void updateTimeProperty() {
		timeProperty().set(this.toString());
	}

	public StringProperty daysProperty() {
		return this.strDays;
	}
	public StringProperty hoursProperty() {
		return this.strHours;
	}
	public StringProperty minutesProperty() {
		return this.strMinutes;
	}
	public StringProperty secondsProperty() {
		return this.strSeconds;
	}
	public StringProperty scaleProperty() {
		return this.strScale;
	}
	
	public void setScale(float scale) {
		if (scale != 0) {
			strScale.set(Float.toString(scale));
			scaledMillis = (int) (1000 / scale);
		}
	}
	public float getScale() {
		return Float.parseFloat(strScale.get());
	}
	public int getScaledMillis() {
		return scaledMillis;
	}
	
	public void resset() {
		super.resset();
		strDays.set("1");
		strHours.set("0");
		strMinutes.set("0");
		strSeconds.set("0");
		setScale(1);
	}

	@Override
	public String toString() {
		String strTime = super.toString() + " | Time scale: " + strScale.get();
		return strTime;
	}
	
}
