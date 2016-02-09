import java.awt.Color;
import java.awt.Graphics2D;


public class EditorMarker extends GameObject {

    public EditorMarker(Color color, Map m, int x, int y) {
		super(GameObject.OBJECT_MARKER, color, m, x, y);
	}
	
	// Public Methods
	
	public void changeTile(int dx, int dy) {
		// Check bounds
		if(positionX+dx < 0 || positionY+dy < 0 || positionX+dx >= map.getWidth() || positionY+dy >= map.getHeight())
			return;
		
		positionX += dx;
		positionY += dy;
	}

	@Override
	public void act() {
	}

	@Override
	public void paint(Graphics2D g) {
		int screenX = (int)(map.CELL_SIZE * positionX);
		int screenY = (int)(map.CELL_SIZE * positionY);
			
		g.setColor(objColor);
		
		g.drawOval(screenX, screenY, map.CELL_SIZE, map.CELL_SIZE);
	}

}
