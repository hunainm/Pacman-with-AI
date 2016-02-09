import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.WindowConstants;



public class EditorFrame extends javax.swing.JFrame {
	
	private StateEditor editor;
	private JMenuItem jItemSaveAs;
	private JTextArea txtTeleportY;
	private JTextField txtTeleportX;
	private JLabel lblTeleportY;
	private JLabel lblTeleportX;
	private JLabel lblTeleportSettings;
	private JButton btnTeleport;
	private JButton btnNew;
	private JTextField txtFilename;
	private JButton btnLoad;
	private JButton btnSave;
	private JComboBox jWallTypeCombo;
	private JButton btnGhost;
	private JComboBox comboGhost;
	private JCheckBox chkGhostTrapped;
	private JLabel lblGhosts;
	private JButton btnPowerup;
	private JLabel jWallTypeLabel;
	private JMenuItem jItemExit;
	private JSeparator jSeperatorFile;
	private JMenuItem jItemSave;
	private JMenuItem jItemLoad;
	private JMenu jMenuFile;
	private JMenuBar jMenuBar1;
	private JLabel lblPlaceableObjs;
	private JSeparator jSeparator1;
	private JButton btnPacman;
	private JButton btnDot;
	private JButton btnWall;

	public EditorFrame(StateEditor e) {
		super();
		editor = e;
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Pacman Map Editor - HAX Developers");
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					editor.getGame().requestChangeState(State.STATE_EXITING);
				}
			});
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenuFile = new JMenu();
					jMenuBar1.add(jMenuFile);
					jMenuFile.setText("File");
					{
						jItemLoad = new JMenuItem();
						jMenuFile.add(jItemLoad);
						jItemLoad.setText("Load");
					}
					{
						jItemSave = new JMenuItem();
						jMenuFile.add(jItemSave);
						jItemSave.setText("Save");
					}
					{
						jItemSaveAs = new JMenuItem();
						jMenuFile.add(jItemSaveAs);
						jItemSaveAs.setText("Save As..");
					}
					{
						jSeperatorFile = new JSeparator();
						jMenuFile.add(jSeperatorFile);
					}
					{
						jItemExit = new JMenuItem();
						jMenuFile.add(jItemExit);
						jItemExit.setText("Exit");
					}
				}
			}
			{
				btnWall = new JButton();
				getContentPane().add(btnWall);
				btnWall.setText("Wall");
				btnWall.setBounds(12, 218, 59, 23);
				btnWall.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.setMarkerObjectType(GameObject.OBJECT_WALL);
					}
				});
			}
			{
				btnDot = new JButton();
				getContentPane().add(btnDot);
				btnDot.setText("Dot");
				btnDot.setBounds(12, 36, 59, 23);
				btnDot.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.setMarkerObjectType(GameObject.OBJECT_DOT);
					}
				});
			}
			{
				btnPacman = new JButton();
				getContentPane().add(btnPacman);
				btnPacman.setText("Pacman");
				btnPacman.setBounds(136, 36, 110, 23);
				btnPacman.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.setMarkerObjectType(GameObject.OBJECT_PLAYER);
					}
				});
			}
			{
				jSeparator1 = new JSeparator();
				getContentPane().add(jSeparator1);
				jSeparator1.setBounds(12, 301, 360, 10);
			}
			{
				lblPlaceableObjs = new JLabel();
				getContentPane().add(lblPlaceableObjs);
				lblPlaceableObjs.setText("Placeable Objects");
				lblPlaceableObjs.setBounds(12, 12, 129, 16);
			}
			{
				jWallTypeLabel = new JLabel();
				getContentPane().add(jWallTypeLabel);
				jWallTypeLabel.setText("Wall Type");
				jWallTypeLabel.setBounds(12, 196, 82, 16);
			}
			{
				ComboBoxModel jWallTypeComboModel = 
					new DefaultComboBoxModel(
							new String[] { "Vertical", "Horizontal", "Top Left", "Top Right", "Bottom Left", "Bottom Right", "Ghost Barrier" });
				jWallTypeCombo = new JComboBox();
				getContentPane().add(jWallTypeCombo);
				jWallTypeCombo.setModel(jWallTypeComboModel);
				jWallTypeCombo.setBounds(12, 246, 153, 23);
				jWallTypeCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						String sType = (String)jWallTypeCombo.getSelectedItem();
						if(sType.equals("Vertical"))
							editor.setMarkerWallType(GameObject.WALL_VERTICAL);
						else if(sType.equals("Horizontal"))
							editor.setMarkerWallType(GameObject.WALL_HORIZONTAL);
						else if(sType.equals("Top Left"))
							editor.setMarkerWallType(GameObject.WALL_TOPLEFT);
						else if(sType.equals("Top Right"))
							editor.setMarkerWallType(GameObject.WALL_TOPRIGHT);
						else if(sType.equals("Bottom Left"))
							editor.setMarkerWallType(GameObject.WALL_BOTTOMLEFT);
						else if(sType.equals("Bottom Right"))
							editor.setMarkerWallType(GameObject.WALL_BOTTOMRIGHT);
						else if(sType.equals("Ghost Barrier"))
							editor.setMarkerWallType(GameObject.WALL_GHOSTBARRIER);
						else
							editor.setMarkerWallType(GameObject.WALL_HORIZONTAL);
					}
				});
			}
			{
				btnSave = new JButton();
				getContentPane().add(btnSave);
				btnSave.setText("Save");
				btnSave.setBounds(12, 317, 70, 23);
				btnSave.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.saveMap(txtFilename.getText());
					}
				});
			}
			{
				btnLoad = new JButton();
				getContentPane().add(btnLoad);
				btnLoad.setText("Load");
				btnLoad.setBounds(87, 317, 68, 23);
				btnLoad.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.loadMap(txtFilename.getText());
					}
				});
			}
			{
				txtFilename = new JTextField();
				getContentPane().add(txtFilename);
				txtFilename.setBounds(12, 345, 225, 23);
				txtFilename.setText("test.map");
			}
			{
				btnNew = new JButton();
				getContentPane().add(btnNew);
				btnNew.setText("New");
				btnNew.setBounds(160, 317, 71, 23);
				btnNew.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.newMap(28, 31);
					}
				});
			}
			{
				btnTeleport = new JButton();
				getContentPane().add(btnTeleport);
				btnTeleport.setText("Teleport");
				btnTeleport.setBounds(237, 218, 110, 23);
				btnTeleport.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.setMarkerObjectType(GameObject.OBJECT_TELEPORT);
						editor.setMarkerTeleport(Integer.parseInt(txtTeleportX.getText()), Integer.parseInt(txtTeleportY.getText()));
					}
				});
			}
			{
				lblTeleportSettings = new JLabel();
				getContentPane().add(lblTeleportSettings);
				lblTeleportSettings.setText("Teleport Settings");
				lblTeleportSettings.setBounds(237, 196, 123, 16);
			}
			{
				lblTeleportX = new JLabel();
				getContentPane().add(lblTeleportX);
				lblTeleportX.setText("Dest X:");
				lblTeleportX.setBounds(237, 249, 60, 16);
			}
			{
				lblTeleportY = new JLabel();
				getContentPane().add(lblTeleportY);
				lblTeleportY.setText("Dest Y: ");
				lblTeleportY.setBounds(235, 279, 52, 16);
			}
			{
				txtTeleportX = new JTextField();
				getContentPane().add(txtTeleportX);
				txtTeleportX.setText("13");
				txtTeleportX.setBounds(280, 246, 85, 23);
			}
			{
				txtTeleportY = new JTextArea();
				getContentPane().add(txtTeleportY);
				txtTeleportY.setText("17");
				txtTeleportY.setBounds(280, 275, 82, 20);
			}
			{
				btnPowerup = new JButton();
				getContentPane().add(btnPowerup);
				btnPowerup.setText("Powerup");
				btnPowerup.setBounds(12, 65, 102, 23);
				btnPowerup.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.setMarkerObjectType(GameObject.OBJECT_POWERUP);
					}
				});
			}
			{
				lblGhosts = new JLabel();
				getContentPane().add(lblGhosts);
				lblGhosts.setText("Ghost Settings");
				lblGhosts.setBounds(272, 12, 76, 16);
			}
			{
				chkGhostTrapped = new JCheckBox();
				getContentPane().add(chkGhostTrapped);
				chkGhostTrapped.setText("Trapped");
				chkGhostTrapped.setBounds(360, 10, 100, 20);
				chkGhostTrapped.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						editor.setMarkerGhostTrapped(!editor.getMarkerGhostTrapped());
						System.out.println(editor.getMarkerGhostTrapped());
					}
				});
			}
			{
				ComboBoxModel comboGhostModel = 
					new DefaultComboBoxModel(
							new String[] { "Blinky", "Pinky", "Inky", "Clyde" });
				comboGhost = new JComboBox();
				getContentPane().add(comboGhost);
				comboGhost.setModel(comboGhostModel);
				comboGhost.setBounds(272, 65, 146, 23);
				comboGhost.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						String sType = (String)comboGhost.getSelectedItem();
						editor.setMarkerGhostType(sType);
					}
				});
			}
			{
				btnGhost = new JButton();
				getContentPane().add(btnGhost);
				btnGhost.setText("Add Ghost");
				btnGhost.setBounds(272, 36, 146, 23);
				btnGhost.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editor.setMarkerObjectType(GameObject.OBJECT_GHOST);
					}
				});
			}
			pack();
			this.setSize(451, 547);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void btnSaveMouseEntered(MouseEvent evt) {
		System.out.println("btnSave.mouseEntered, event="+evt);
		//TODO add your code for btnSave.mouseEntered
	}

}
