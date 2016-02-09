import java.awt.Color;
import java.awt.Graphics2D;


public abstract class GameObject {
	// Static type vars
	public static final int OBJECT_DOT = 1;
	public static final int OBJECT_POWERUP = 2;
	public static final int OBJECT_CHERRY = 4;
	public static final int OBJECT_PLAYER = 8;
	public static final int OBJECT_GHOST = 16;
	public static final int OBJECT_MARKER = 32; // Virtual
	public static final int OBJECT_WALL = 64; // Virtual
	public static final int OBJECT_TELEPORT = 128;
	
	// Wall types (Walls aren't instanced GameObject's)
	public static final byte WALL_VERTICAL = 1;
	public static final byte WALL_HORIZONTAL = 2;
	public static final byte WALL_TOPLEFT = 3;
	public static final byte WALL_TOPRIGHT = 4;
	public static final byte WALL_BOTTOMLEFT = 5;
	public static final byte WALL_BOTTOMRIGHT = 6;
	public static final byte WALL_GHOSTBARRIER = 7;
	
	// Generic object attributes
	protected int objType;
	protected Color objColor;
	protected int positionX;
	protected int positionY;
	
	// Outside refereneces
	protected final Map map; // Can only be set once. Object only exists within the map. If the map changes, new objects are created
	
	// Getters and Setters
	
	
	public int getType() {
		return objType;
	}
	
	
	public Color getColor() {
		return objColor;
	}
	
	
	public void setColor(Color c) {
		objColor = c;
	}
	
	
	public int getX() {
		return positionX;
	}
	
	
	public int getY() {
		return positionY;
	}
	
	// Public & Protected Abstract methods
	
	
	public GameObject(int type, Color color, Map m, int x, int y) {
		objType = type;
		objColor = color;
		map = m;
		positionX = x;
		positionY = y;
	}
	
	
	public abstract void act();

	
	public abstract void paint(Graphics2D g);
}
