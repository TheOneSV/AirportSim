package com.airsim.model;

import com.airsim.util.SimpleTime;
import com.airsim.view.RootLayoutController;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public abstract class Airplane {
	
	public static enum State{READY, SCHEDULED,  TAKEOF, FLIGHT, HOVER, LANDING, REFUEL};
	protected float refuelSpeed = 0.001f;
	final public static int MAX_FUEL = 1;

	private PathTransition airplanePathTransition = new PathTransition(), textPathTransition = new PathTransition();
	private StringProperty strPropName, strPropState, strPropSrcAirport, strPropDstAirport;
	private DoubleProperty x, y, speed, fuel, travelledDistance; 
	private SimpleTime scheduledTime = new SimpleTime();
	private ImageView imageView = new ImageView();
	private Airport srcAirport, dstAirport;
	private Text textName;
	private int maxRange;
	private State state;
	
	//curTime - time from the beginning of the flight, endTime - time in which the aircraft will reach the destination 
	private double curTime, endTime; 
	// Properties that help performance
	private double srcDestDistance, prevDistance; 
	
	public Airplane() {
		this("Unknown", null, 0, 0, null);
	}
	
	/**
     * Airplane constructor  
     * @param  name - name of the airplane
     * @param  airport - source airport where the airplane stands
     * @param  speed - speed of the airplane
     * @param  maxRangeKM - maximum range distance of the plane
     * @param  image - image of the airplane which is needed when is painted
     * @see Image
     * @see Airport
     */
	public Airplane(String name, Airport srsAirport, float speed, int maxRangeKM, Image image) {
		this.strPropState = new SimpleStringProperty();
		this.strPropName = new SimpleStringProperty(name);
		this.strPropSrcAirport = new SimpleStringProperty();
		this.strPropDstAirport = new SimpleStringProperty();

		this.srcAirport = srsAirport;
		updateSrcAiprortProperty();
		
		this.speed = new SimpleDoubleProperty(speed);
		this.travelledDistance = new SimpleDoubleProperty();
		this.maxRange = maxRangeKM;
		this.fuel = new SimpleDoubleProperty(1);
		this.imageView.setImage(image);
		
		this.x = new SimpleDoubleProperty();
		this.y = new SimpleDoubleProperty();
		this.x.bind(imageView.translateXProperty());
		this.y.bind(imageView.translateYProperty());
		
		changeState(State.READY);
		this.textName = new Text(name);
		textName.setFill(Color.RED);
		textName.setFont(Font.font(null, FontWeight.BOLD, 10));
	}

    /**
     * Update the aircraft according to his state  
     * @param  gr - destination Group object where the airplane is located
     * @see Group
     */
	public void update(Group gr) {
		switch(state) {
		case READY:
			break;
		case SCHEDULED:
			if(scheduledTime.compareTo(World.clock) > 0) {
				if (fuel.get() < MAX_FUEL) {
					if(fuel.get() >= MAX_FUEL - 0.01f) {
						fuel.set(MAX_FUEL);
					}
					fuel.set(fuel.get() + refuelSpeed);
				}
			} else {
				scheduledTime.resset();
				changeState(State.TAKEOF);
			}
			break;
		case TAKEOF:
			if(fly(gr)) {
				srcAirport.removePlane(this);
				prevDistance = 0;
				changeState(State.FLIGHT); 
			} else {
				changeState(State.READY);
			}
			break;
		case LANDING:
			clearShapes(gr);
			dstAirport.addAirplane(this);
			this.setDstAirport(null);
			this.changeState(State.REFUEL);
			break;
		case REFUEL:
			if(fuel.get() >= MAX_FUEL - 0.01f) {
				fuel.set(MAX_FUEL);
				changeState(State.READY);
			}
			fuel.set(fuel.get() + refuelSpeed);
			break;
		case FLIGHT:
			curTime += RootLayoutController.MILLIUPDATE;
			updateTravelledDistance();
			double deltaDist = getTravelledDistance() - prevDistance;
			prevDistance = getTravelledDistance();
			fuel.set(getFuel() - (deltaDist*srcDestDistance)/maxRange);
			if(getTravelledDistance() >= MAX_FUEL - 0.01f) {
				clearShapes(gr);
				this.setSrcAirport(dstAirport);
				if (dstAirport.emptyPlacesProperty().get() > 0) {
					changeState(State.LANDING);
					this.dstAirport.emptyPlacesProperty().set(this.dstAirport.getSize()-1);
				} else {
					changeState(State.HOVER);
					dstAirport.addHoverAirplane(this);
					if(!hover(gr)) {
						fuel.set(0.0f);
					}
				}
			}
			break;
		case HOVER:
			fuel.set(fuel.get() - refuelSpeed);
			if (fuel.get() < 0.01) {
				World.incrementCrashes();
				World.getAirplaneData().remove(this);
				dstAirport.removeHoverPlane(this);
				clearShapes(gr);
			}
			break;
		default:
			break;
		}
	}
    
    /**
     * Update percentage of travelled distance
     * <p>
     * curTime - time from the beginning of the flight
     * endTime - time in which the aircraft will reach the destination 
     */
    private void updateTravelledDistance() {
    	travelledDistance.set(curTime/endTime);
    }
    public Double getTravelledDistance() {
    	return this.travelledDistance.get();
    }
    public DoubleProperty travelledDistanceProperty() {
    	return this.travelledDistance;
    }
    
    /**
     * Checks whether the aircraft can fly to a destination with the available fuel at this time  
     * @param  dstAirport - destination aiport
     * @return  true - plane can reach the destination, false - plane can not reach the destination
     */
    public boolean canFlyNow(Airport dstAirport) {
    	if (isInRange(dstAirport)) {
    		if (srcDestDistance < maxRange*getFuel()) {
    			return true;
    		} 
    	}
    	return false;
    }
    
    /**
     * Verifies whether an airport is within range  
     * @param  dstAirport - destination aiport
     * @return  true - the airport is in range, false - the airport is not in range
     */
    public boolean isInRange(Airport dstAirport) {
    	srcDestDistance = calcDistance(dstAirport);
    	if (srcDestDistance > maxRange) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * Calculates distance from the current airport, which is located the aircraft to the airport, which should be reached  
     * @param  dstAirport - destination aiport
     * @return distance from the current airport, which is located the aircraft to the airport, which should be reached
     */
    public double calcDistance(Airport dstAirport) {
    	double x1 = (float) Math.abs(srcAirport.getX() - dstAirport.getX());
    	double y1 = (float) Math.abs(srcAirport.getY() - dstAirport.getY());
    	return Math.sqrt(x1*x1 + y1*y1);
    }
    
    /**
     * remove all objects from Group connected with this airplane
     * @param  gr - destination Group object where the airplane is located
     * @see Group
     */
    private void clearShapes(Group gr) {
		airplanePathTransition.stop();
		textPathTransition.stop();
		gr.getChildren().remove(imageView);
		gr.getChildren().remove(textName);
    }
    
    
    /**
     * launched aircraft to fly from this airport to destination airport
     * <p> Preconditions: airplane must have source and destination airport a.k.a(srcArport and dstAirport)
     * Must have an FlightPath from source airport to destination airport
     * @param  gr - destination Group object where the airplane is located
     * @see Group
     * @see FlightPath
     */
    private boolean fly(Group gr) {
    	if (canFlyNow(dstAirport)) {
    		if (World.pathManager.getFlightPath(srcAirport, dstAirport) != null) {
    			airplanePathTransition.setPath(World.pathManager.getFlightPath(srcAirport, dstAirport).getPath());
		    	endTime = (long) (calcDistance(dstAirport)/speed.get()*10000);
		    	curTime = 0;
		    	airplanePathTransition.setDuration(Duration.millis(endTime));
		    	airplanePathTransition.setNode(imageView);
		        airplanePathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		    	airplanePathTransition.setAutoReverse(false);
		        
		        textPathTransition.setPath(World.pathManager.getFlightPath(srcAirport, dstAirport).getPath());
		        textPathTransition.setDuration(Duration.millis(endTime));
		        textPathTransition.setNode(textName);
		        textPathTransition.setAutoReverse(false);

		        gr.getChildren().add(imageView);
		        gr.getChildren().add(textName);
		        airplanePathTransition.play();
		        textPathTransition.play();
		        return true;
    		} else {
        		System.out.println("Airplane: " + getName() + "\nsrcAirport: " + dstAirport.getName() + 
        							"\ndstAirport: " + dstAirport.getName() + "\nError: Flight path does not exist!");
        		return false;
    		}
    	}
    	return false;
    }
    
    /**
     * launched aircraft to hover over destination airport
     * <p> Preconditions: airplane must have destination airport a.k.a(dsAirport)
     * Must have an HoverPath from source airport to destination airport
     * @param  gr - destination Group object where the airplane is located
     * @see Group
     * @see HoverPath
     */
    private boolean hover(Group gr) {
    	if (World.pathManager.getHoverPath(dstAirport) != null) {
    		airplanePathTransition.setPath(World.pathManager.getHoverPath(dstAirport).getPath());
	    	airplanePathTransition.setDuration(Duration.millis(10000));
	    	airplanePathTransition.setNode(imageView);
	    	airplanePathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    	airplanePathTransition.setAutoReverse(false);
	        airplanePathTransition.setCycleCount(Animation.INDEFINITE);
	        gr.getChildren().add(imageView);
	        airplanePathTransition.play();
	        return true;
    	} else {
    		System.out.println("Airplane: " + getName() + "\nAirport: " + dstAirport.getName() + "\nError: Hover path does not exist on this airport");
    		return false;
    	}
    }
	
    
    abstract public String getInfo();
	
	// <================ Getters, Setters and update functions of the properties ================>
	
	public float getRefuelSpeed() {
		return refuelSpeed;
	}

	public String getName() {
        return strPropName.get();
    }
    public void setName(String name) {
        this.strPropName.set(name);
        this.textName.setText(name);
    }
    public StringProperty nameProperty() {
        return strPropName;
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
    
    public double getSpeed() {
        return speed.get();
    }
    public void setSpeed(float y) {
        this.speed.set(y);
    }
    public DoubleProperty speedProperty() {
        return speed;
    }
    
    public double getFuel() {
        return fuel.get();
    }
    public void setFuel(float y) {
        this.fuel.set(y);
    }
    public DoubleProperty fuelProperty() {
        return fuel;
    }
    
    public int getMaxRange() {
        return maxRange;
    }
    public void setMaxRange(int maxRange) {
    	this.maxRange = maxRange;
    }

    public State getState() {
    	return state;
    }
    public void changeState(State state) {
    	this.state = state;
    	strPropState.set(state.toString());
    }
    public StringProperty strStateProperty() {
        return strPropState;
    }
    
    public ImageView getImageView() {
		return imageView;
	}
	public void setImage(Image image) {
		this.imageView.setImage(image);
	}
	
	public SimpleTime getScheduledTime() {
		return scheduledTime;
	}
	
	public Airport getSrcAirport() {
		return srcAirport;
	}
	public void setSrcAirport(Airport srcAirport) {
		this.srcAirport = srcAirport;
		updateSrcAiprortProperty();
	}
	public StringProperty srcAirportProperty() {
		return strPropSrcAirport;
	}
	private void updateSrcAiprortProperty() {
		if(srcAirport == null) {
			strPropSrcAirport.setValue("--");
		} else {
			strPropSrcAirport.set(srcAirport.getName());
		}
	}
	
	public Airport getDstAirport() {
		return dstAirport;
	}
	public void setDstAirport(Airport dstAirport) {
		this.dstAirport = dstAirport;
		updateDstAirportProperty();
	}
	public StringProperty dstAirportProperty() {
		return strPropDstAirport;
	}
	private void updateDstAirportProperty() {
		if(dstAirport == null) {
			strPropDstAirport.set("--");
		} else {
			strPropDstAirport.set(dstAirport.getName());
		}
	}
    
    @Override 
    public String toString() {
    	return getName();
    }
}