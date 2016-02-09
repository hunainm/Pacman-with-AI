import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public abstract class State implements KeyListener {
	// Game States
	public static final int STATE_MENU = 1;
	public static final int STATE_GAME = 4;
	public static final int STATE_DEAD = 8;
	//public static final int STATE_GAMEOVER = 16;
	public static final int STATE_EDITOR = 32;
	public static final int STATE_EXITING = 64;
	
	protected Game game;
	
	public State(Game g) {
		game = g;
		reset();
	}
	
	
	public Game getGame() {
		return game;
	}
	
	public abstract void reset();
	
	public abstract void logic();
	
	
	public abstract void end();
	
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// Esc
		switch(e.getKeyChar()) {
			case 27:
				game.requestChangeState(STATE_EXITING);
				break;
			default:
				break;
		}
	}
}