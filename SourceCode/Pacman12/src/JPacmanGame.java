
public class JPacmanGame {
	public static void main(String[] arg) {
		Game g = new Game(1280, 1024);
		g.requestChangeState(State.STATE_MENU);
		g.mainThreadLoop();
		System.exit(0);
	}
}
