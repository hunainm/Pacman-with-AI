import java.awt.Color;
import java.awt.Graphics2D;


public class Ghost extends Actor {
	// Movement
	private Path path;
	private int nextStepIdx;
	private boolean needNewPath;
	
	// State
	private boolean trapped;
	private boolean inFear;
	private boolean debugDrawPath;
	
	public Ghost(Color color, Map m, int x, int y, boolean trap) {
		super(GameObject.OBJECT_GHOST, color, m, x, y);
		needNewPath = true;
		inFear = false;
		trapped = trap;
		debugDrawPath = false;
	}
	
	
	public boolean isInFear() {
		return inFear;
	}
	
	
	public void setFear(boolean f) {
		inFear = f;
	}
	
	
	public boolean isTrapped() {
		return trapped;
	}
	
	
	public void setTrapped(boolean t) {
		trapped = t;
	}
	
	
	public boolean needsNewPath() {
		return needNewPath;
	}
	
	public void updatePath(Path p) {
		nextStepIdx = 1;
		path = p;
		needNewPath = false;
	}
	
	
	public void setDebugDrawPath(boolean d) {
		debugDrawPath = d;
	}
	
	
	@Override
	public void act() {
		// Move to the next step
		if(path != null && nextStepIdx < path.getLength()) {
			
			// Figure out the direction
			if((path.getY(nextStepIdx)-positionY) < 0)
				moveDir = MOVE_UP;
			else if((path.getY(nextStepIdx)-positionY) > 0)
				moveDir = MOVE_DOWN;
			else if((path.getX(nextStepIdx)-positionX) > 0)
				moveDir = MOVE_RIGHT;
			else
				moveDir = MOVE_LEFT;
			
			// Based on the direction, move the screen delta's and the X,Y coordinates if the # of pixels for the cell have been surpassed
			switch(moveDir) {
				case MOVE_UP:
					deltaX = 0;
					deltaY -= speed;
					// The delta has surpassed the # of pixels for the cell, meaning we can officially change the coordinate
					if(Math.abs(deltaY) >= map.CELL_SIZE) {
						deltaY = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = 90;
					break;
				case MOVE_RIGHT:
					deltaX += speed;
					deltaY = 0;
					if(Math.abs(deltaX) >= map.CELL_SIZE) {
						deltaX = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = 0;
					break;
				case MOVE_DOWN:
					deltaX = 0;
					deltaY += speed;
					if(Math.abs(deltaY) >= map.CELL_SIZE) {
						deltaY = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = -90;
					break; 
				case MOVE_LEFT:
					deltaX -= speed;
					deltaY = 0;
					if(Math.abs(deltaX) >= map.CELL_SIZE) {
						deltaX = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = 180;
					break;
				default:
					// MOVE_NONE (stand still)
					deltaX = 0;
					deltaY = 0;
					break;
			}
		} else {
			needNewPath = true;
		}
	}
	
	
	@Override
	public void paint(Graphics2D g) {
		// Change the position of pacman on screen by the offsets m_fDelta
		int screenX = (int)((map.CELL_SIZE * positionX) + deltaX);
		int screenY = (int)((map.CELL_SIZE * positionY) + deltaY);
			
		g.setColor(objColor);
		
		// Body
		if(inFear)
			g.setColor(Color.WHITE);
		g.fillArc(screenX, screenY, map.CELL_SIZE, map.CELL_SIZE, 0, 360);
		g.fillRect((int)((map.CELL_SIZE * positionX) + deltaX), (int)((map.CELL_SIZE * positionY)+(map.CELL_SIZE/2) + deltaY), map.CELL_SIZE, map.CELL_SIZE/2);
		
		// Eyes
		if(inFear)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.WHITE);
		g.fillOval((int)((map.CELL_SIZE * positionX)+6 + deltaX), (int)((map.CELL_SIZE * positionY)+4 + deltaY), 8, 10);
		g.fillOval((int)((map.CELL_SIZE * positionX)+16 + deltaX), (int)((map.CELL_SIZE * positionY)+4 + deltaY), 8, 10);
		
		// Eyeballs
		g.setColor(Color.BLUE);
		g.fillOval((int)((map.CELL_SIZE * positionX)+8 + deltaX), (int)((map.CELL_SIZE * positionY)+6 + deltaY), 4, 4);
		g.fillOval((int)((map.CELL_SIZE * positionX)+18 + deltaX), (int)((map.CELL_SIZE * positionY)+6 + deltaY), 4, 4);
		
		// Debug draw path
		if(debugDrawPath && path != null) {
			for(int i = 0; i < path.getLength(); i++) {
				Path.Step s = path.getStep(i);
				g.setColor(objColor);
				g.drawLine(map.CELL_SIZE * s.getX(), map.CELL_SIZE * s.getY(), (map.CELL_SIZE * s.getX())+map.CELL_SIZE, (map.CELL_SIZE * s.getY())+map.CELL_SIZE);
			}
		}
	}
}
