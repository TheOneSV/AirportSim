package com.airsim.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Airport {
	private final StringProperty name;
	private DoubleProperty x, y;
	private ObservableList<Airplane> hoverAirplanes = FXCollections.observableArrayList();
	private ObservableList<Airplane> airplanes = FXCollections.observableArrayList();
	private IntegerProperty emptyPlaces;
	private IntegerProperty maxSize;
	private ImageView imageView;
	private Text textName;

	/**
     * Airport constructor. Default size of free places for planes is 2
     * @param  x - where the airport is sited on the x coordinate
     * @param  y - where the airport is sited on the y coordinate
     * @param  image - image of the airport which is needed when is painted
     * @see Image
     */
	public Airport(String name, int x, int y, Image image) {
		this(name, x, y, 2, image);
	}
	
	/**
     * Airport constructor.
     * @param  x - where the airport is sited on the x coordinate
     * @param  y - where the airport is sited on the y coordinate
     * @param  size - maximum number of free places for airplanes
     * @param  image - image of the airport which is needed when is painted
     * @see Image
     */
	public Airport(String name, float x, float y, int size, Image image) {
		this(name, x, y, size, image, Color.CYAN, 18);
	}
	
	/**
     * Airport constructor  
     * @param  x - where the airport is sited on the x coordinate
     * @param  y - where the airport is sited on the y coordinate
     * @param  size - maximum number of free places for airplanes
     * @param  image - image of the airport which is needed when is painted
     * @param  color - color of the text which the name is painted
     * @param  fontSize - size of the text which the name is painted
     * @see Image
     * @see Color
     */
	public Airport(String name, float x, float y, int size, Image image, Color color, int fontSize) {
		this.name = new SimpleStringProperty(name);
		this.x = new SimpleDoubleProperty(x);
		this.y = new SimpleDoubleProperty(y);
		this.maxSize = new SimpleIntegerProperty(size);
		this.emptyPlaces = new SimpleIntegerProperty(size);
		this.imageView = new ImageView();
		this.imageView.setImage(image);
		this.textName = new Text(x, y, name);
		textName.setCache(true);
		imageView.setX(x);
		imageView.setY(y);
        Tooltip.install(imageView, new Tooltip(name));
		textName.setFill(color);
		textName.setFont(Font.font(null, FontWeight.BOLD, fontSize));
		textName.setId("fancytext");
	}
	
	 /**
     * Àdding aircraft to the airport 
     * @param  airplane - the airplane who is need to be added
     * @return true if it can be added
     * @return false if it can not be added
     * @see Airplane
     */
    public boolean addAirplane(Airplane airplane) {
    	if(maxSize.get() == airplanes.size()) {
    		return false;
    	}
    	if(airplanes.add(airplane)) {
        	updateEmptyPlaces();
    		return true;
    	}
    	return false;
    }
	 /**
     * Removing aircraft from the airport 
     * @param  airplane - the airplane who is need to be removed
     * @return true if it can be removed
     * @return false if it can not be removed
     * @see Airplane
     */
    public boolean removePlane(Airplane airplane) {
    	if (airplanes.remove(airplane)) {
        	updateEmptyPlaces();
    		return true;
    	}
    	return false;
    }
    /**
     * Returns all airplanes which are on the airport
     * @return ObservableList of airplanes
     * @see Airplane
     * @see ObservableList
     */
    public ObservableList<Airplane> getAriplanes() {
    	return airplanes;
    }
    
    /**
     * Àdding aprilane to the list of hover airplanes 
     * @param  airplane - the airplane who is need to be added
     * @return true if it can be added
     * @return false if it can not be added
     * @see Airplane
     */
    public boolean addHoverAirplane(Airplane airplane) {
    	return hoverAirplanes.add(airplane);
    }
    /**
     * Remove airplane from the list of hovering airplanes
     * @param  airplane - the airplane who is need to be removed
     * @return true if it can be removed
     * @return false if it can not be removed
     * @see Airplane
     */
    public boolean removeHoverPlane(Airplane airplane) {
    	if (hoverAirplanes.remove(airplane)) {
        	updateEmptyPlaces();
    		return true;
    	}
    	return false;
    }
    /**
     * Returns the list with all hover airplanes
     * @return ObservableList of airplanes
     * @see Airplane
     * @see ObservableList
     */
    public ObservableList<Airplane> getHoverAriplanes() {
    	return hoverAirplanes;
    }
    
    /**
     * Change the size of the airport only if planes now sited on it is smaller than requested size 
     * @param  size - the new size of the airport
     * @return true if the size is successfully changed
     * @return false if the size is not successfully changed
     * @see Airplane
     */
    public boolean changeSize(int size) {
    	if(airplanes.size() > size) {
    		return false;
    	}
    	maxSize.set(size);
    	updateEmptyPlaces();
    	return true;
    }
    
    /**
     * Returns empty places on the airfield 
     * @return number of empty places
     */
    public int getEmptyPlaces() {
        return emptyPlaces.get();
    }
    /**
     * Update emptyPlacesProperty updated sites according to maximum places on the airfield and the occupied places
     */
    private void updateEmptyPlaces() {
		emptyPlaces.set(maxSize.get() - airplanes.size());
    }
    public IntegerProperty emptyPlacesProperty() {
    	return emptyPlaces;
    }
    
    /**
     * Get the ImageView of the airport
     * @return ImageView
     * @see ImageView
     */
    public ImageView getImageView() {
		return imageView;
	}
    /**
     * Set Image of the airport
     * @param image - image which need to be set
     * @see Image
     */
	public void setImage(Image image) {
		this.imageView.setImage(image);
	}
	
	/**
     * Returns Text object with name of the airport
     * @return Text object with airport name
     * @see Text
     */
	public Text getTextName() {
		return textName;
	}
	
	public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
        this.textName.textProperty().set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }
    
	public double getX() {
        return x.get();
    }
    public void setX(float x) {
        this.x.set(x);
    }
    public DoubleProperty xProperty() {
        return x;
    }
    
	public double getY() {
        return y.get();
    }
    public void setY(float y) {
        this.y.set(y);
    }
    public DoubleProperty yProperty() {
        return y;
    }
   
    /**
     * Maximum size of airplane places on the airport
     * @return the maximum size of airplane places
     */
    public int getSize() {
        return maxSize.get();
    }
    public IntegerProperty sizeProperty() {
        return maxSize;
    }
    
    @Override 
    public String toString() {
    	return getName();
    }
}
