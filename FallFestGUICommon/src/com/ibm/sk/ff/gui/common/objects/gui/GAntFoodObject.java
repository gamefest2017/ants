package com.ibm.sk.ff.gui.common.objects.gui;

public class GAntFoodObject extends GUIObject {
	
	private long id;
	
	private GUIObjectTypes type = GUIObjectTypes.ANT_FOOD;
	
	private Location location;
	
	private GAntObject ant;
	private GFoodObject food;
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() { 
		return id;
	}
	
	public void setLocation(Location position) {
		this.location = position;
	}
	
	public void setLocation(int x, int y) {
		this.location = new Location(x, y);
	}
	
	public Location getLocation(){ 
		return location;
	}
	
	public void setAnt(GAntObject ant) {
		this.ant = ant;
	}
	
	public GAntObject getAnt() {
		return ant;
	}
	
	public void setFood(GFoodObject food) {
		this.food = food;
	}
	
	public GFoodObject getFood() {
		return food;
	}
	
	@Override
	public String toString() {
		return id + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		GAntFoodObject other = (GAntFoodObject) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public GUIObjectTypes getType() {
		return type;
	}
	
	public void setType(GUIObjectTypes type) {
		this.type = type;
	}

}
