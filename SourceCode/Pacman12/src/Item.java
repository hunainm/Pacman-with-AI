import java.awt.Color;
import java.awt.Graphics2D;

public class Item extends GameObject {
	// Teleportation vars
	private int teleportDestX;
	private int teleportDestY;

        public Item(int type, Color color, Map m, int x, int y) {
		super(type, color,  m, x, y);
		
		teleportDestX = 13;
		teleportDestY = 17;
	}
	
	public void setTeleport(int x, int y) {
		teleportDestX = x;
		teleportDestY = y;
	}
	
	public int getTeleportX() {
		return teleportDestX;
	}
	
	public int getTeleportY() {
		return teleportDestY;
	}

	public boolean use(Player pl) {
		boolean destroy = false;
		
		// Perform action based on type
		switch(objType) {
			case OBJECT_DOT:
				pl.incrementScore(10);
				destroy = true;
				break;
			case OBJECT_POWERUP:
				pl.incrementScore(50);
				pl.setPowerUp(true);
				destroy = true;
				break;
			case OBJECT_TELEPORT:
				pl.move(teleportDestX, teleportDestY);
				break;
			default:
				break;
		}
		
		return destroy;
	}

	@Override
	public void act() {
	}

        @Override
	public void paint(Graphics2D g) {
		g.setColor(objColor);
		
		// Item's are placed in the center of a cell
		int center_x = (positionX*map.CELL_SIZE)+map.CELL_SIZE/2;
		int center_y = (positionY*map.CELL_SIZE)+map.CELL_SIZE/2;
		
		// Render item based on type
		switch(objType) {
			case OBJECT_DOT:
				g.fillArc(center_x-4, center_y-4, 8, 8, 0, 360);
				break;
			case OBJECT_POWERUP:
				g.fillArc(center_x-8, center_y-8, 16, 16, 0, 360);
				break;
			case OBJECT_TELEPORT:
				g.fillOval(center_x-6, center_y-8, 12, 16);
				break;
			default:
				break;
		}
	}

}
