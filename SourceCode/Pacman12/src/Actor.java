import java.awt.Color;
import java.awt.Graphics2D;


public class Actor extends GameObject {
	
	// Direction constants
	public static final int MOVE_NONE = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int MOVE_DOWN = 4;
	public static final int MOVE_LEFT = 8;
	
	// Dead status
	protected boolean isDead;
	
	// Movement / Location
	protected int spawnX;
	protected int spawnY;
	protected int moveDir;
	protected int dirOrient;
	protected float deltaX;
	protected float deltaY;
	protected float speed;

	
	public Actor(int type, Color color, Map m, int x, int y) {
		super(type, color, m, x, y);
		
		isDead = false;
		
		// Movement
		spawnX = x;
		spawnY = y;
		moveDir = MOVE_NONE;
		dirOrient = 0;
		deltaX = 0;
		deltaY = 0;
		speed = 5;
	}
	
	// Getters and Setters
	
	public int getSpawnX() {
		return spawnX;
	}
	
	
	public int getSpawnY() {
		return spawnY;
	}
	
	
	public void setDead(boolean s) {
		isDead = s;
	}
	
	
	public boolean isDead() {
		return isDead;
	}
	
	public void setSpeed(float s) {
		speed = s;
	}
	
	
	public float getSpeed() {
		return speed;
	}
	
	
	public void setMoveDirection(int dir) {
		moveDir = dir;
	}
	
	// Public Methods
	
	
	public boolean move(int x, int y) {
		boolean res = map.canMove(this, x, y);
		if(res) {
			positionX = x;
			positionY = y;
		}
		return res;
	}
	
	
	@Override
	public void act() {
	}

	
	@Override
	public void paint(Graphics2D g) {
	}

}
