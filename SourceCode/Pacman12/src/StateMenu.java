import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;


public class StateMenu extends State {
	
	// Private instance
	private int cursorX;
	private int cursorY;
	private byte currentOption;
	private byte currentMapOption; // Corresponds to the index in mapList
	private String[] mapList;

	public StateMenu(Game g) {
		super(g);
	}
	
	@Override
	public void reset() {
		// Set cursor & menu position
		cursorX = 525;
		cursorY = 310;
		currentOption = 0;
		currentMapOption = 0;
		
		// Load the map list
		File dir = new File(System.getProperty("user.dir")+ "\\resources\\");
             
		// It is also possible to filter the list of returned files.
		// This example does not return any files that start with `.'.
	        FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".map");
		    }
		};
		
		// Apply the filter
		mapList = dir.list(filter);
		
		if(mapList == null) {
			System.out.println("No maps exist!");
			game.requestChangeState(STATE_EXITING);
			return;
		}
	}

	@Override
	public void end() {
	}

	@Override
	public void logic() {
		Graphics2D g = game.getGraphicsContext();
		
		// Draw title
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 72));
		g.fillArc(156, 92, 100, 100, 35, 270); // First pacman
		g.drawString("PACMAN", 450, 180);
		g.fillArc(960, 92, 100, 100, 35, 270);
		
		// Draw menu options
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
		g.drawString("Play Game", 525, 300);
		g.drawString("Map Editor", 525, 340);
		g.drawString("Exit", 525, 380);
		if(mapList.length > 0)
			g.drawString("Current Map: " + mapList[currentMapOption], 525, 600);
		else
			g.drawString("No maps detected. Have you placed the maps file in the same directory as the program?", 100, 600);
		
		// Draw underline cursor
		g.setColor(Color.RED);
		g.fillRect(cursorX, cursorY, 150, 5);
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			if(currentMapOption >= 0 && currentMapOption < (mapList.length-1))
				currentMapOption++;
			break;
		case KeyEvent.VK_LEFT:
			if(currentMapOption > 0 && currentMapOption <= (mapList.length-1))
				currentMapOption--;
			break;
		case KeyEvent.VK_DOWN:
			if(currentOption >= 0 && currentOption < 2) {
				currentOption++;
				cursorY += 38;
			}
			break;
		case KeyEvent.VK_UP:
			if(currentOption > 0 && currentOption <= 2) {
				currentOption--;
				cursorY -= 38;
			}
			break;
		case KeyEvent.VK_ENTER:
			// Execute the appropriate state change
			switch(currentOption) {
				case 0:
					// Play game
					if(mapList.length > 0) {
						game.setStartMap(mapList[currentMapOption]);
						game.requestChangeState(STATE_GAME);
					}
					break;
				case 1:
					// Editor
					game.requestChangeState(STATE_EDITOR);
					break;
				case 2:
					// Exit
					game.requestChangeState(STATE_EXITING);
					break;
				default:
					break;
			}
			break;
		default:
			break;
		}
	}

}
