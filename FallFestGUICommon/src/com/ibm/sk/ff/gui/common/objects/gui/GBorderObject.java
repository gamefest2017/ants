
package com.ibm.sk.ff.gui.common.objects.gui;

public class GBorderObject extends GUIObject {

	private long id = System.currentTimeMillis();

	private Location location;

	private GUIObjectTypes type = GUIObjectTypes.BORDER;

	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return this.id;
	}

	public void setLocation(final Location position) {
		this.location = position;
	}

	public void setLocation(final int x, final int y) {
		this.location = new Location(x, y);
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public String toString() {
		return this.id + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ this.id >>> 32);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GBorderObject other = (GBorderObject) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public GUIObjectTypes getType() {
		return this.type;
	}

	public void setType(final GUIObjectTypes type) {
		this.type = type;
	}

}
