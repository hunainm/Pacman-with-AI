import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class StateGame extends State {
	
	// Logic object references
	private Player player;
	private Map map;
	private AIManager ai;
	
	// Game vars
	private String mapName;
	private int currentLevel;
	private int sessionScore; // Overall score for the game session. The player object score is only the score for that life / level
	private int livesRemaining;
	private boolean gamePaused;
	private long pauseTime;
	
	// Map vars. Store them as class member vars to eliminate function call overhead for getHeight/getWidth
	private int mapWidth;
	private int mapHeight;

	public StateGame(Game g) {
		super(g);
	}
	
	public int getSessionScore() {
		return sessionScore;
	}
	
	@Override
	public void reset() {
		// Set game vars
		mapName = game.getStartMap();
		currentLevel = 0;
		sessionScore = 0;
		livesRemaining = 5;
		pauseTime = 0;
		
		// Respawn (start level 1)
		respawn(true);
	}
	
	public void respawn(boolean nextLevel) {
		gamePaused = true;
		pauseTime = System.currentTimeMillis() + 3000;
		
		// If we're jumping to the next level, reset everything
		if(nextLevel) {
			currentLevel++;
			
			// Force previous references out of scope
			player = null;
			map = null;
			ai = null;
			
			// Setup the game map
			game.getGraphicsContext().setBackground(Color.BLACK);
			map = new Map(mapName, 32);
			mapWidth = map.getWidth();
			mapHeight = map.getHeight();
			
			// Spawn the player
			player = map.getPlayer();
			
			// Setup AI
			ai = new AIManager(map, player, game.isDebugEnabled());
			
			// Slighly increase the game speed 
			
		} else { // Player died, reset the map
			// Move all actors back to their spawn positions
			int nActors = map.getNumActors();
			for(int i = 0; i < nActors; i++) {
				Actor a = map.getActor(i);
				if(a != null) {
					a.move(a.getSpawnX(), a.getSpawnY());
					a.setDead(false);
					if(a.getType() == GameObject.OBJECT_GHOST)
						((Ghost)a).updatePath(null);
				}
			}
		}
	}
	
	@Override
	public void logic() {
		if(map == null)
			return;
		
		Graphics2D g = game.getGraphicsContext();
		
		// Offset the buffer so object's arent clipped by the window borders
		g.translate(10, 30);
		
		// Paint right UI with lives remaining, score, highscore etc
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
		g.drawString("PACMAN by HAX Developers", 925, 50);
		g.drawString("Score: " + player.getScore(), 1000, 100);
		g.drawString("Total: " + sessionScore, 1000, 150);
		g.drawString("Lives: " + livesRemaining, 1000, 200);
		g.drawString("Level: " + currentLevel, 1000, 250);
                g.drawString("Dots: " + map.getDotsRemaining(), 1000, 300);
                
		
		if(!gamePaused) {
			ai.process();
			player.act();
		}
		
		if(player.isDead()) {
			lose();
			return;
		}
		
		if(map.getDotsRemaining() <= 0) {
			win();
			return;
		}
		
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
		
		// Debug
		if(game.isDebugEnabled()) {
			g.setColor(Color.RED);
			g.drawString("DEBUG ON", 1000, 650);
			
			
			// Player X,Y coordinates bottom right
			g.drawString("X: " + player.getX() + ", Y: " + player.getY(), 1000, 700);
		}
		
		// Check for game pause and print pause status
		if(gamePaused) {
			g.setColor(Color.RED);
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
			g.drawString("PAUSED", 1000, 500);
			if(pauseTime > System.currentTimeMillis())
				g.drawString("Pause ends in..." + ((pauseTime-System.currentTimeMillis())/1000), 1000, 550);
			if(pauseTime != 0 && System.currentTimeMillis() > pauseTime) {
				pauseTime = 0;
				gamePaused = false;
			}
			return;
		}
	}
	
	@Override
	public void end() {
		// Cleanup
		player = null;
		map = null;
	}
	
	public void win() {
		sessionScore += player.getScore();
		
		respawn(true);
	}
	
	public void lose() {
		livesRemaining--;
		
		if(livesRemaining > 0) {
			respawn(false);
		} else {
			if(currentLevel == 1)
				sessionScore = player.getScore(); // win() never called, so score is the 1st level score
			game.requestChangeState(State.STATE_MENU);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(player == null)
			return;
		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player.setMoveDirection(Actor.MOVE_UP);
				break;
			case KeyEvent.VK_RIGHT:
				player.setMoveDirection(Actor.MOVE_RIGHT);
				break;
			case KeyEvent.VK_DOWN:
				player.setMoveDirection(Actor.MOVE_DOWN);
				break;
			case KeyEvent.VK_LEFT:
				player.setMoveDirection(Actor.MOVE_LEFT);
				break;
			case KeyEvent.VK_SPACE:
				player.setMoveDirection(Actor.MOVE_NONE);
				break;
			case KeyEvent.VK_P:
				// Don't interupt system pauses
				if(pauseTime < System.currentTimeMillis())
					gamePaused = !gamePaused;
				break;
			case KeyEvent.VK_V:
				game.toggleDebug();
				// AI debug
				ai.setDebugEnabled(game.isDebugEnabled());
				break;
			case KeyEvent.VK_0:
				//game.changeState(STATE_MENU);
				break;
			default:
				break;
		}
	}
}
