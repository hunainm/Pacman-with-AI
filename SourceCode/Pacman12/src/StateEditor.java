import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JInternalFrame;

public class StateEditor extends State {
	
	// Logic object references
	private EditorFrame editorFrame;
	private EditorMarker marker;
	private boolean gameView;
	private Map map;
	
	// Placement variables
	private int markerObjectType;
	private byte markerWallType;
	private String markerGhostType;
	private boolean markerGhostTrapped;
	private int markerTeleportX;
	private int markerTeleportY;
	
	// Map vars. Store them as class member vars to eliminate function call overhead for getHeight/getWidth
	private int mapWidth;
	private int mapHeight;

	public StateEditor(Game g) {
		super(g);
		
		// If true, remove all editor helpers like grid lines
		gameView = false;
		
		// Create the editor toolpane
		game.getFrame().setSize(1024, game.RES_Y);
		editorFrame = new EditorFrame(this);
		editorFrame.setVisible(true);
		
		// Defaults
		markerObjectType = GameObject.OBJECT_WALL;
		markerWallType = GameObject.WALL_VERTICAL;
		markerGhostType = "Blinky";
		markerGhostTrapped = false;
		markerTeleportX = 13;
		markerTeleportY = 17;
	}
	
	// Getters and Setters
	
	public void setMarkerObjectType(int t) {
		markerObjectType = t;
	}

        public void setMarkerWallType(byte t) {
		markerWallType = t;
	}
	
	public void setMarkerGhostType(String t) {
		markerGhostType = t;
	}
	
	public void setMarkerGhostTrapped(boolean t) {
		markerGhostTrapped = t;
	}
	
        
	public boolean getMarkerGhostTrapped() {
		return markerGhostTrapped;
	}
	
	public void setMarkerTeleport(int x, int y) {
		markerTeleportX = x;
		markerTeleportY = y;
	}
	
	
	@Override
	public void reset() {
		// Force previous references out of scope
		marker = null;
		map = null;
		
		markerObjectType = GameObject.OBJECT_DOT;
	}
	
	
	public void newMap(int width, int height) {
		// Setup the game map
		game.getGraphicsContext().setBackground(Color.BLACK);
		mapWidth = width;
		mapHeight = height;
		map = new Map(28, 31, 32);
		
		// Create the marker (but don't put it "in" the map)
		marker = new EditorMarker(Color.GREEN, map, 0, 0);
	}
	
	
	public void saveMap(String filename) {
		map.write(System.getProperty("user.dir") + "\\resources\\" + filename);
	}
	
	
	public void loadMap(String filename) {
		// Setup the game map
		game.getGraphicsContext().setBackground(Color.BLACK);
		map = new Map(System.getProperty("user.dir") + "\\resources\\" + filename, 32,"");
		mapWidth = map.getWidth();
		mapHeight = map.getHeight();
		
		// Create the marker (but don't put it "in" the map)
		marker = new EditorMarker(Color.GREEN, map, 0, 0);
	}
	
	@Override
	public void logic() {
		if(map == null)
			return;
		
		Graphics2D g = game.getGraphicsContext();
		
		// Offset the buffer so object's arent clipped by the window borders
		g.translate(10, 30);
		
		// Now run render logic
		// Paint boundaries and items
		Item item = null;
		for(int x = 0; x < mapWidth; x++) {
			for(int y = 0; y < mapHeight; y++) {
				// Get and paint the static object here
				byte sType = map.getCollidable(x, y);
				
				// Switch based on wall type and paint
				g.setColor(Color.BLUE);
				switch(sType) {
					case 0:
						// Nothing
						break;
					case GameObject.WALL_VERTICAL:
						// Vertical wall, no edges
						g.fillRoundRect(x*map.CELL_SIZE+10, y*map.CELL_SIZE, 12, map.CELL_SIZE, 0, 0); // 2x+12 = map.CELL_SIZE. x = 10
						break;
					case GameObject.WALL_HORIZONTAL:
						// Horizontal wall, no edges
						g.fillRoundRect(x*map.CELL_SIZE, y*map.CELL_SIZE+10, map.CELL_SIZE, 12, 0, 0);
						break;
					case GameObject.WALL_TOPLEFT:
						//g.fillArc(x*map.CELL_SIZE+10, y*map.CELL_SIZE, map.CELL_SIZE, map.CELL_SIZE, 90, 90);
						g.fillRoundRect(x*map.CELL_SIZE+(map.CELL_SIZE/2), y*map.CELL_SIZE+10, map.CELL_SIZE/2, 12, 0, 0);
						g.fillRoundRect(x*map.CELL_SIZE+10, y*map.CELL_SIZE+(map.CELL_SIZE/2), 12, map.CELL_SIZE/2, 0, 0);
						break;
					case GameObject.WALL_TOPRIGHT:
						g.fillRoundRect(x*map.CELL_SIZE, y*map.CELL_SIZE+10, map.CELL_SIZE/2, 12, 0, 0);
						g.fillRoundRect(x*map.CELL_SIZE+10, y*map.CELL_SIZE+(map.CELL_SIZE/2), 12, map.CELL_SIZE/2, 0, 0);
						break;
					case GameObject.WALL_BOTTOMLEFT:
						g.fillRoundRect(x*map.CELL_SIZE+(map.CELL_SIZE/2), y*map.CELL_SIZE+10, map.CELL_SIZE/2, 12, 0, 0); // hori
						g.fillRoundRect(x*map.CELL_SIZE+10, y*map.CELL_SIZE, 12, map.CELL_SIZE/2, 0, 0); //vert
						break;
					case GameObject.WALL_BOTTOMRIGHT:
						g.fillRoundRect(x*map.CELL_SIZE, y*map.CELL_SIZE+10, map.CELL_SIZE/2, 12, 0, 0); // hori
						g.fillRoundRect(x*map.CELL_SIZE+10, y*map.CELL_SIZE, 12, map.CELL_SIZE/2, 0, 0); //vert
						break;
					case GameObject.WALL_GHOSTBARRIER:
						g.setColor(Color.PINK);
						g.fillRoundRect(x*map.CELL_SIZE, y*map.CELL_SIZE+10, map.CELL_SIZE, 6, 0, 0);
						break;
					default:
						break;
				}
				
				// Paint any of the items here
				item = map.getItem(x, y);
				if(item != null)
					item.paint(g);
			}
		}
		
		// Paint actors ontop
		int nActors = map.getNumActors();
		for(int i = 0; i < nActors; i++) {
			Actor a = map.getActor(i);
			if(a != null)
				a.paint(g);
		}
		
		// Paint the marker
		marker.paint(g);
		
		// Paint gridline overlay if in editor view
		if(!gameView) {
			g.setColor(Color.RED);
			for(int i = 0; i < mapWidth; i++)
				g.drawLine(i*map.CELL_SIZE, 0, i*map.CELL_SIZE, mapHeight*map.CELL_SIZE);
			for(int i = 0; i < mapHeight; i++)
				g.drawLine(0, i*map.CELL_SIZE, mapWidth*map.CELL_SIZE, i*map.CELL_SIZE);
		
			// Player X,Y coordinates bottom right
			g.drawString("X: " + marker.getX() + ", Y: " + marker.getY(), 900, 700);
		}
	}

	
	@Override
	public void end() {
		// Cleanup
		marker = null;
		map = null;
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				marker.changeTile(0, -1);
				break;
			case KeyEvent.VK_RIGHT:
				marker.changeTile(1, 0);
				break;
			case KeyEvent.VK_DOWN:
				marker.changeTile(0, +1);
				break;
			case KeyEvent.VK_LEFT:
				marker.changeTile(-1, 0);
				break;
			case KeyEvent.VK_ENTER:
				if(marker == null)
					return;
				
				// If not empty, bail
				if(!map.isEmpty(marker.getX(), marker.getY()))
					return;
				
				switch(markerObjectType) {
					case GameObject.OBJECT_WALL:
						map.addCollidable(marker.getX(), marker.getY(), markerWallType);
						break;
					case GameObject.OBJECT_DOT:
						map.addItem(new Item(GameObject.OBJECT_DOT, Color.WHITE, map, marker.getX(), marker.getY()));
						break;
					case GameObject.OBJECT_POWERUP:
						map.addItem(new Item(GameObject.OBJECT_POWERUP, Color.WHITE, map, marker.getX(), marker.getY()));
						break;
					case GameObject.OBJECT_GHOST:
						if(markerGhostType.equals("Blinky"))
							map.addActor(new Ghost(Color.RED, map, marker.getX(), marker.getY(), markerGhostTrapped));
						else if(markerGhostType.equals("Pinky"))
							map.addActor(new Ghost(Color.PINK, map, marker.getX(), marker.getY(), markerGhostTrapped));
						else if(markerGhostType.equals("Inky"))
							map.addActor(new Ghost(Color.CYAN, map, marker.getX(), marker.getY(), markerGhostTrapped));
						else
							map.addActor(new Ghost(Color.ORANGE, map, marker.getX(), marker.getY(), markerGhostTrapped));
						break;
					case GameObject.OBJECT_PLAYER:
						// If there is already a player in the actors list, remove it
						for(int i = 0; i < map.getNumActors(); i++) {
							if(map.getActor(i).getType() == GameObject.OBJECT_PLAYER) {
								map.removeActor(i);
								i--;
							}
						}
						
						// Add the new player
						map.addActor(new Player(map, marker.getX(), marker.getY()));
						break;
					case GameObject.OBJECT_TELEPORT:
						Item tel = new Item(GameObject.OBJECT_TELEPORT, Color.LIGHT_GRAY, map, marker.getX(), marker.getY());
						tel.setTeleport(markerTeleportX, markerTeleportY);
						map.addItem(tel);
						break;
						
					default:
						break;
				}
				break;
			case KeyEvent.VK_DELETE:
				// Delete a placed object. Will reduce excessive memory consumption if the user cant just replace a tile with a new object
				
				// If empty, bail
				if(map.isEmpty(marker.getX(), marker.getY()))
					return;
				
				// Remove anything (collidable, actor, or item) at (x,y)
				map.removeAnyAt(marker.getX(), marker.getY());
				break;
			case KeyEvent.VK_V:
				gameView = !gameView;
				break;
			case KeyEvent.VK_0:
				//editorFrame.setEnabled(false);
				//game.changeState(STATE_MENU);
				break;
			default:
				break;
		}
	}

}
