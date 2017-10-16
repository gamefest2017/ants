package com.ibm.sk.ant.implementation;

import java.awt.Point;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;

public class StripedAnt extends AbstractAnt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Zone antZone;	
	private Direction mainDirection;

	enum Zone{
		/*
		ZONE_1(new int[] {0,1}),
		ZONE_2(new int[] {2,5}), 
		ZONE_3(new int[] {6,10}),
		ZONE_4(new int[] {11,16}), 
		ZONE_5(new int[] {17,23}), 
		ZONE_6(new int[] {24,31}), 
		ZONE_7(new int[] {32,40}), 
		ZONE_8(new int[] {41,50}), 
		ZONE_9(new int[] {50,59}), 
		ZONE_10(new int[] {60,68}), 
		ZONE_11(new int[] {69,76}), 
		ZONE_12(new int[] {77,83}), 
		ZONE_13(new int[] {84,89}),
		ZONE_14(new int[] {90,94}),
		ZONE_15(new int[] {95,98}),
		ZONE_16(new int[] {99,100});
		*/
		
		ZONE_1(new int[] {2,5}),
		ZONE_2(new int[] {2,4}), 
		ZONE_3(new int[] {4,6}),
		ZONE_4(new int[] {6,8}), 
		ZONE_5(new int[] {8,10});
		
		private int[] minMaxY;
		
		Zone(int[] minMaxY){
			this.minMaxY = minMaxY;
		}

		public int[] getMinMaxY() {
			return minMaxY;
		}

	}
	
	public StripedAnt(long id, Point position, Hill myHill) {
		super(id, position, myHill);
		//this.antZone = Zone.values()[new Random().nextInt(Zone.values().length - 1)];		
		this.antZone = Zone.ZONE_1;
	}

	@Override
	public Direction move(Vision vision) {
		Direction returnValue = Direction.NO_MOVE;
		if (!this.hasFood()) {			
			for (final Direction direction : Direction.values()) {
				final ObjectType objectType = vision.look(direction);
				if (ObjectType.FOOD.equals(objectType)) {
					System.out.println("I see food!");
					returnValue = direction;
				}
			}

			if (Direction.NO_MOVE.equals(returnValue)) {
				System.out.println("Where to go?");
				if (antZone.getMinMaxY()[0] <= this.position.y) { 
					if ( this.position.y < antZone.getMinMaxY()[1]) {
						returnValue = Direction.random();
					}
					else {
						returnValue = Direction.SOUTH;
					}				
				} else { 
					returnValue = Direction.NORTH;
				} 
			}
		} else {
			System.out.println("Going home!");
			returnValue = findWayHome();
		}
		// Add your implementation here

		return returnValue;
	}
	
	/**
	 * Finds the Direction towards home based on current coordinates and
	 * coordinates of the Anthill.
	 *
	 * @return Direction home
	 */
	private Direction findWayHome() {
		return Direction.get(this.position, this.getMyHill().getPosition());
	}

}
