import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class Game extends Canvas {
	// Debug vars
	private boolean debugEnabled;
	
	// Threading
	private boolean runMainThread;
	
	// Graphics variables
	private Frame frame;
	public final int RES_X;
	public final int RES_Y;
	private BufferStrategy m_gBuffer;
	
	// State
	private int stateId;
	private State currentState;
	private boolean changeStateRequested;
	private int requestedState;
	private String startMap;
	
	public Game(int x, int y) {		
		// Set resolution settings
		RES_X = x;
		RES_Y = y;
		
		// Init game
		init();
	}
	
	private void init() {
		// Debug vars
		debugEnabled = false;
		
		startMap = "default.map";
		changeStateRequested = false;
		
		// Setup the game frame
		frame = new Frame("Pacman");
		frame.setLayout(null);
		setBounds(0, 0, RES_X, RES_Y);
		frame.add(this);
		frame.setSize(RES_X, RES_Y);
		frame.setResizable(false);
		frame.setVisible(true);
		
		// Set the exit handler with an anonymous class
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// Exit main thread
				runMainThread = false;
			}
		});
		
		// Setup double buffering
		setIgnoreRepaint(true); // We'll handle repainting
		createBufferStrategy(2);
		m_gBuffer = getBufferStrategy();
		
		runMainThread = true;
	}
	
	public Frame getFrame() {
		return frame;
	}

        public Graphics2D getGraphicsContext() {
		return (Graphics2D) m_gBuffer.getDrawGraphics();
	}
	
	
	public String getStartMap() {
		return startMap;
	}
	
	public void setStartMap(String m) {
		startMap = m;
	}
	
	
	public boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	
	public void toggleDebug() {
		debugEnabled = !debugEnabled;
	}
	
	
	public void requestChangeState(int state) {
		requestedState = state;
		changeStateRequested = true;
	}
	
	
	public void mainThreadLoop() {
		while(runMainThread) {
			// If a state change was requested, execute it now
			if(changeStateRequested) {
				changeStateRequested = false;
				changeState(requestedState);
				continue;
			}
			
			Graphics2D g = getGraphicsContext();
			
			// Wipe the screen
			g.setColor(Color.black);
			g.fillRect(0, 0, RES_X, RES_Y);
			
			// Run the logic of the current game state here
			currentState.logic();
			
			// Show the new buffer
			g.dispose();
			m_gBuffer.show();
			
			// Syncronize framerate
			try {
				Thread.sleep(10); // Rate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
        private void changeState(int state) {
		// Cleanup for the outgoing state
		if(currentState != null) {
			frame.removeKeyListener(currentState);
			removeKeyListener(currentState);
			currentState.end();
		}
		
		// Set the new state type
		stateId = state;
		
		// Instance the new state (reset() is called in the construtor)
		switch(stateId) {
			case State.STATE_GAME:
				currentState = new StateGame(this);
				break;
			case State.STATE_EDITOR:
				currentState = new StateEditor(this);
				break;
			case State.STATE_MENU:
				currentState = new StateMenu(this);
				break;
			case State.STATE_EXITING:
				currentState = null;
				runMainThread = false;
				break;
			default:
				break;
		}
		
		// Setup input handler and reset()
		if(currentState != null) {
			frame.addKeyListener(currentState);
			addKeyListener(currentState);
		}
	}
}
