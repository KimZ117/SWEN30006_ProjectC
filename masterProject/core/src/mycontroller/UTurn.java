 package mycontroller;

import utilities.Coordinate;
import world.*;

public class UTurn extends Driver {
		
	// change made
	// an attribute to store the previous Direction
	private WorldSpatial.Direction previousDirection;
	public static final float F_SPEED = 3;
	public static final float SIDE_SPACE_REQ = 2;
	
	// Initialize the distance to make it only calculate this once
	private int distToLeft = 10;
	private int distToRight = 10;

	@Override
	public void behave(MyAIController controller, float delta) {
		if(previousDirection == null){
			previousDirection = controller.getOrientation();
		}
		WorldSpatial.Direction direction = controller.getOrientation();
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		// only calculate this once
		if(distToLeft == 10 && distToRight == 10){
			switch(direction){
			case NORTH:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(currentPosition.x - i, currentPosition.y).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(currentPosition.x + i, currentPosition.y).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			case SOUTH:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(currentPosition.x + i, currentPosition.y).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(currentPosition.x - i, currentPosition.y).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			case WEST:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(currentPosition.x, currentPosition.y - i).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(currentPosition.x, currentPosition.y + i).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			case EAST:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(currentPosition.x, currentPosition.y + i).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(currentPosition.x, currentPosition.y - i).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			}
		}
		
		// control speed
		if(controller.getVelocity() > F_SPEED){
			controller.applyBrake();
		}
		if(controller.getVelocity() < F_SPEED){
			controller.applyForwardAcceleration();
		}
		
		if(distToLeft > distToRight){
			// keep turn left until the car is heading opposite direction
			controller.turnLeft(delta);
		}else{
			// keep turn right until the car is heading opposite direction
			controller.turnRight(delta);
		}
	}

	@Override
	/**
	 * It's considered done when the car faces the opposite direction
	 */
	public boolean isDone(MyAIController controller) {
		WorldSpatial.Direction direction = controller.getOrientation();
		switch(previousDirection){
		case NORTH:
			if(direction.equals(WorldSpatial.Direction.SOUTH)){
				return true;
			}	
			break;
		case SOUTH:
			if(direction.equals(WorldSpatial.Direction.NORTH)){
				return true;
			}
			break;
		case WEST:
			if(direction.equals(WorldSpatial.Direction.EAST)){
				return true;
			}
			break;
		case EAST:
			if(direction.equals(WorldSpatial.Direction.WEST)){
				return true;
			}
			break;
		}
		return false;
	}

}
