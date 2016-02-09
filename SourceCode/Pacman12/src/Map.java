import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Map {	
	// Map parameters (width & height represent # of cells)
	private int mapWidth;
	private int mapHeight;
	public final int CELL_SIZE;
	private int InitialFitness;
	// Instance vars
	private byte collideMap[][];
	private Item itemMap[][];
	private ArrayList<Actor> actorList;
	private int dotsRemaining;
        ArrayList<String> MapBits;
        
	public Map(int w, int h, int cs) {
		// Set map parameters
		mapWidth = w;
		mapHeight = h;
		CELL_SIZE = cs;
		dotsRemaining = 0;
		
                
		// Initialize collideMap, a 2D array that contains all static collidable GameObjects
		// We use this for fast lookup during collision detection and AI movement paths
		collideMap = new byte[mapWidth][mapHeight];
		
		// Initialize itemMap, a 2D array that contains items (dots, powerups, cherry) on the map
		itemMap = new Item[mapWidth][mapHeight];
		
		// Create m_objects, an arraylist with all actorList
		actorList = new ArrayList<Actor>();
	}
	
        //for stateEditor class
	public Map(String filename, int cs,String s) {
		// Set the cell size
		CELL_SIZE = 22;
		
		// Read contents of the map file
		readEditor(filename);
	}
        
	public Map(String filename, int cs){
		// Set the cell size
		CELL_SIZE = 22;
		MapBits = new ArrayList<String>();
                MapBits.add("000");
                MapBits.add("001");
                MapBits.add("010");
                MapBits.add("011");
                MapBits.add("100");
                MapBits.add("101");
                MapBits.add("110");
                MapBits.add("111");
                
                InitialFitness = 100;
                Random r = new Random();
                int Low = 0;
                int High = 8;
                String chromosome = "";
                while(true)
                {
                    collideMap = new byte[28][31];
                    itemMap = new Item[28][31];
                    actorList = new ArrayList<Actor>();
                
                    int R = r.nextInt(High-Low) + Low;
                    String s = MapBits.get(R);
                    String file = getFilename(s);
                    read(file);
                    chromosome += s;
                    
                    int R1 = r.nextInt(High-Low) + Low;
                    String s1 = MapBits.get(R1);
                    file = getFilename(s1);
                    read2(file);
                    chromosome += s1;
                    
                    int R2 = r.nextInt(High-Low) + Low;
                    String s2 = MapBits.get(R2);
                    file = getFilename(s2);
                    read3(file);
                    chromosome += s2;
                    
                    FitnessFunction(chromosome);
                    if(InitialFitness >= 50)
                        break;
                    InitialFitness = 100;
                    chromosome = "";
                }
	}
        
        public void FitnessFunction(String Chromosome){
            
            // Tokenizing String into three parts 
            String start = Chromosome.substring(0, 2);
            String mid = Chromosome.substring(3, 5);
            String end = Chromosome.substring(6, 8);
            
            // Checking if Blocks Overlap or not
            for(int i = 0; i<28; i++)
            {
                for(int j = 0; j<30;j++)
                {
                    if(collideMap[i][j] == GameObject.WALL_VERTICAL && collideMap[i][j+1] == GameObject.WALL_HORIZONTAL)
                    {
                        InitialFitness -= 50;
                    }
                }
            }
            
            // Checking if components are from the same map
            if(start.compareToIgnoreCase(mid) == 0 &&  mid.compareToIgnoreCase(end) == 0)
                InitialFitness -= 50;
            
            // Checking if Ghosts are present or not
            if (actorList.size() < 4)
                InitialFitness -= 100;
            else if(actorList.size() == 4)
                InitialFitness -= 50;
            else if (actorList.size()>4)
                InitialFitness += 10;
        }
	
        // Getting File Name Based on Bits of Chromosome
        
        public String getFilename(String bit)
        {
            String basepath = System.getProperty("user.dir") + "\\resources\\";
            switch (bit) {
            case "000":  
                return basepath + "h1.map";
            case "001":
                return basepath + "a1.map";
            case "010":
                return basepath + "z2.map";
            case "011":
                return basepath + "h2.map";
            case "100":
                return basepath + "a4.map";
            case "101":
                return basepath + "a3.map";
            case "110":
                return basepath + "z1.map";
            case "111":
                return basepath + "a2.map";
             default:
                return "default.map";
            }       
        }
	
	public int getWidth() {
		return mapWidth;
	}
	
	public int getHeight() {
		return mapHeight;
	}
	
        public int getNumActors() {
		return actorList.size();
	}
	
        public byte[][] getCollidableMap() {
		return collideMap;
	}
	
        public Item[][] getItemMap() {
		return itemMap;
	}
	
	public int getDotsRemaining() {
		return dotsRemaining;
	}
	
        public boolean addCollidable(int x, int y, byte t) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Check if theres already something there
		if(collideMap[x][y] > 0)
			return false;
		
		// Add to the collideMap
		collideMap[x][y] = t;
		return true;
	}
	
	
        public boolean addItem(Item item) {
		if(item == null) 
			return false;
		
		// Check bounds
		int x = item.getX();
		int y = item.getY();
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Add to the itemMap
		if(item.getType() == GameObject.OBJECT_DOT)
			dotsRemaining++;
		itemMap[x][y] = item;
		return true;
	}
	
	public boolean addActor(Actor act) {
		if(act == null) 
			return false;
		
		// Check bounds
		int x = act.getX();
		int y = act.getY();
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Add to the array list
		actorList.add(act);
		return true;
	}
	
	public byte getCollidable(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return -1;
		
		return collideMap[x][y];
	}
	
	public Item getItem(int x, int y) {
		// Check  bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return null;
		
		return itemMap[x][y];
	}
	
	
	public Actor getActor(int idx) {
		Actor act = null;
		try {
			act = actorList.get(idx);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return act;
	}
	
	
	public Player getPlayer() {
		// Get from the object map
		for(Actor g : actorList) {
			if(g.getType() == GameObject.OBJECT_PLAYER)
				return (Player)g;
		}
		
		return null;
	}
	
	
	public Actor getActor(int x, int y, boolean notPlayer) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return null;
		
		// Get from the object map
		for(Actor g : actorList) {
			if(notPlayer && g.getType() == GameObject.OBJECT_PLAYER)
				continue;
			
			if(g.getX() == x && g.getY() == y) {
				return g;
			}
		}
		
		return null;
	}
	
	public void removeActor(int idx) {		
		actorList.remove(idx);
	}
	
        public void removeItem(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return;
		
		if(itemMap[x][y].getType() == GameObject.OBJECT_DOT)
			dotsRemaining--;
		
		itemMap[x][y] = null;
	}
	
	public boolean removeAnyAt(int x, int y) {
		boolean rm = false;
		 
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Remove any collidable
		if(collideMap[x][y] != 0) {
			collideMap[x][y] = 0;
			rm = true;
		}
		
		 // Remove any item
		if(itemMap[x][y] != null) {
			itemMap[x][y] = null;
			rm = true;
		}
		
		// Remove any actor
		for(int i = 0; i < actorList.size(); i++) {
			Actor a = actorList.get(i);
			if(a.getX() == x && a.getY() == y) {
				actorList.remove(i);
				a = null;
				i--;
				rm = true;
			}
		}
		
		return rm;
	}
	
	public int findDistance(GameObject start, GameObject end) {
		return (int)Math.sqrt(Math.pow(Math.abs(start.getX()-end.getX()), 2) + Math.pow(Math.abs(start.getY()-end.getY()), 2));
	}
	
	public boolean isEmpty(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Check if the Object is hitting something on the collideMap
		if(getCollidable(x, y) != 0)
			return false;
		
		// Check if object is hitting something on the itemMap
		if(getItem(x, y) != null)
			return false;
		
		// Actor collission
		if(getActor(x, y, false) != null)
			return false;
		
		return true;
	}
	
	public boolean canMove(Actor act, int x, int y) {
		if(act == null)
			return false;
		
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Check if the Object is hitting something on the collideMap
		if(getCollidable(x, y) != 0)
			return false;
		
		// Allow the Actor to move
		return true;
	}
	
	public float getCost(Actor mover, int sx, int sy, int tx, int ty) {
		return 1;
	}

	public void write(String filename) {
		FileOutputStream fout;
		DataOutputStream data;
		
		try {
			fout = new FileOutputStream(filename);
                        int size = filename.length();
                        
			data = new DataOutputStream(fout);
			System.out.println(filename);
			// Write the map file magic
			data.writeUTF("PACMAP");
			
			// Write map width & height
			data.writeInt(mapWidth);
                        System.out.print(mapWidth);
			data.writeInt(mapHeight);
			// Write the collision map
                       
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					data.write(collideMap[x][y]);
                   	    
				}
			}
			
			// Write the item map
			Item item = null;
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					item = itemMap[x][y];
					
					// If an item doesnt exist at (x,y), write 'false' for nonexistant and continue
					if(item == null) {
						data.writeBoolean(false);
                                                System.out.print(" " + false);
						continue;
					}
					data.writeBoolean(true);
					System.out.println(" " + true);
			
					// Write properties of the item
					data.writeInt(item.getType());
                                        System.out.println(" " + item.getType());
			
					data.writeInt(item.getX());
                                        System.out.println(" " + item.getX());
					data.writeInt(item.getY());
                                        System.out.println(" " + item.getY());
					data.writeInt(item.getColor().getRGB());
                                        System.out.println(" " + item.getColor().getRGB());
					if(item.getType() == GameObject.OBJECT_TELEPORT) {
						data.writeInt(item.getTeleportX());
                                                System.out.println(" " + item.getTeleportX());

						data.writeInt(item.getTeleportY());
                                                System.out.println(" " + item.getTeleportY());

					}
				}
			}
			
			// Write the number of actorList, then all actor data
			data.writeInt(actorList.size());
                        System.out.print(" " + actorList.size());
			for(Actor a : actorList) {
				data.writeInt(a.getType());
				data.writeInt(a.getX());
				data.writeInt(a.getY());
				data.writeInt(a.getColor().getRGB());
				if(a.getType() == GameObject.OBJECT_GHOST) {
					data.writeBoolean(((Ghost) a).isTrapped());
				}
			}
			
			data.close();
			fout.close();
		} catch(IOException e) {
			System.out.println("Failed to write map file: " + e.getMessage());
		}
	}
	
	private void read(String filename){
		
		FileInputStream fin;
		DataInputStream data;
		
		try {
			fin = new FileInputStream(filename);
			data = new DataInputStream(fin);
			
			data.readUTF();
			
			// Read map width & height
			mapWidth = data.readInt();
			mapHeight = data.readInt();
			dotsRemaining = 0;
			
			// Create m_objects, an arraylist with all actorList
                        actorList = new ArrayList<Actor>();
			
      
              		// Read the collision map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
                                        byte s = data.readByte();
                                        if(y<=9)
                                            addCollidable(x, y, s);
                                        
                                        
				}
                        }
                        
			
			// Read the item map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					// If an item doesnt exist at (x,y), continue
					if(!data.readBoolean())
						continue;
					
					// Read and set properties of the item
					int t = data.readInt();
					int ix = data.readInt();
					int iy = data.readInt();
					Color c = new Color(data.readInt());
                                        if(iy <=9)
                                            addItem(new Item(t, c, this, ix, iy));
					if(t == GameObject.OBJECT_TELEPORT) {
						int teleX = data.readInt();
						int teleY = data.readInt();
                                                if(iy<=9)
                                                    itemMap[ix][iy].setTeleport(teleX, teleY);
					}  
				}
			}
			
			// Read the number of actorList, then all actor data
			int nActorsSize = data.readInt();
			for(int i = 0; i < nActorsSize; i++) {
				int t = data.readInt();
				int ix = data.readInt();
				int iy = data.readInt();
                                Color c = new Color(data.readInt());
				if(t == GameObject.OBJECT_GHOST) {
					boolean trap = data.readBoolean();
                                        if(iy<=9)
                                        	addActor(new Ghost(c, this, ix, iy, trap));
				} else {
                                    if(iy<=9)
                                	addActor(new Actor(t, c, this, ix, iy));
				}  
			}  
			
			data.close();
			fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read map file: " + e.getMessage());
		}
	}
	
	
private void read3(String filename){
		
		FileInputStream fin;
		DataInputStream data;
		
		try {
			fin = new FileInputStream(filename);
			data = new DataInputStream(fin);

                       data.readUTF();
			
			// Read map width & height
			mapWidth = data.readInt();
			mapHeight = data.readInt();
      
              		// Read the collision map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
                                        byte s = data.readByte();
                                        if(y>=20 && y<=30)
                                            addCollidable(x, y, s);
                                        
                                        
				}
                        }
                        
			
			// Read the item map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					// If an item doesnt exist at (x,y), continue
					if(!data.readBoolean())
						continue;
					
					// Read and set properties of the item
					int t = data.readInt();
					int ix = data.readInt();
					int iy = data.readInt();
					Color c = new Color(data.readInt());
                                        if(iy>=20 && iy<=30)
                                            addItem(new Item(t, c, this, ix, iy));
					if(t == GameObject.OBJECT_TELEPORT) {
						int teleX = data.readInt();
						int teleY = data.readInt();
                                                if(iy>=20 && iy<=30)
                                                    itemMap[ix][iy].setTeleport(teleX, teleY);
					}  
				}
			}
			
			// Read the number of actorList, then all actor data
			int nActorsSize = data.readInt();
			for(int i = 0; i < nActorsSize; i++) {
				int t = data.readInt();
				int ix = data.readInt();
				int iy = data.readInt();
				Color c = new Color(data.readInt());
				if(t == GameObject.OBJECT_GHOST) {
					boolean trap = data.readBoolean();
                                        if(iy>=20 && iy<=30)
                                            addActor(new Ghost(c, this, ix, iy, trap));
				} else {
                                    if(iy>=20 && iy<=30)
					addActor(new Actor(t, c, this, ix, iy));
				}
			}
			
			
			data.close();
			fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read map file: " + e.getMessage());
		}
	}
		
private void read2(String filename){
		
		FileInputStream fin;
		DataInputStream data;
		
		try {
			fin = new FileInputStream(filename);
			data = new DataInputStream(fin);
			
			data.readUTF();
			
			// Read map width & height
			mapWidth = data.readInt();
			mapHeight = data.readInt();
			
              		// Read the collision map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
                                        byte s = data.readByte();
                                        if(y>=10 && y<=19)
                                            addCollidable(x, y, s);
                                        
                                        
				}
                        }
                        
			
			// Read the item map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					// If an item doesnt exist at (x,y), continue
					if(!data.readBoolean())
						continue;
					
					// Read and set properties of the item
					int t = data.readInt();
					int ix = data.readInt();
					int iy = data.readInt();
					Color c = new Color(data.readInt());
                                        if(iy>=10 && iy<=19)
                                            addItem(new Item(t, c, this, ix, iy));
					if(t == GameObject.OBJECT_TELEPORT) {
						int teleX = data.readInt();
						int teleY = data.readInt();
                                                if(iy>=10 && iy<=19)
                                                    itemMap[ix][iy].setTeleport(teleX, teleY);
					}  
				}
			}
			
			// Read the number of actorList, then all actor data
			int nActorsSize = data.readInt();
			for(int i = 0; i < nActorsSize; i++) {
				int t = data.readInt();
				int ix = data.readInt();
				int iy = data.readInt();
                                Color c = null;
                                if(iy>=10 && iy<=19)
                                    c = new Color(data.readInt());
				if(t == GameObject.OBJECT_PLAYER) {
                                    if(iy>=10 && iy<=19)
                                            addActor(new Player(this, ix, iy));
                                    
				} else if(t == GameObject.OBJECT_GHOST) {
					boolean trap = data.readBoolean();
                                        if(iy>=10 && iy<=19)
                                        	addActor(new Ghost(c, this, ix, iy, trap));
				} else {
                                    if(iy>=10 && iy<=19)
                                	addActor(new Actor(t, c, this, ix, iy));
				}  
			}  
			
			data.close();
			fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read map file: " + e.getMessage());
		}
	}

        private void readEditor(String filename) {
		
		FileInputStream fin;
		DataInputStream data;
		
		try {
			fin = new FileInputStream(filename);
			data = new DataInputStream(fin);
			
			data.readUTF();
			
			// Read map width & height
			mapWidth = data.readInt();
			mapHeight = data.readInt();
			dotsRemaining = 0;
			
			// Initialize collideMap, a 2D array that contains all static collidable GameObjects
			// We use this for fast lookup during collision detection and AI movement paths
			collideMap = new byte[mapWidth][mapHeight];
			
			// Initialize itemMap, a 2D array that contains items (dots, powerups, cherry) on the map
			itemMap = new Item[mapWidth][mapHeight];
			
			// Create m_objects, an arraylist with all actorList
			actorList = new ArrayList<Actor>();
			
			// Read the collision map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					addCollidable(x, y, data.readByte());
				}
			}
			
			// Read the item map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					// If an item doesnt exist at (x,y), continue
					if(!data.readBoolean())
						continue;
					
					// Read and set properties of the item
					int t = data.readInt();
					int ix = data.readInt();
					int iy = data.readInt();
					Color c = new Color(data.readInt());
					addItem(new Item(t, c, this, ix, iy));
					if(t == GameObject.OBJECT_TELEPORT) {
						int teleX = data.readInt();
						int teleY = data.readInt();
						itemMap[ix][iy].setTeleport(teleX, teleY);
					}
				}
			}
			
			// Read the number of actorList, then all actor data
			int nActorsSize = data.readInt();
			for(int i = 0; i < nActorsSize; i++) {
				int t = data.readInt();
				int ix = data.readInt();
				int iy = data.readInt();
				Color c = new Color(data.readInt());
				if(t == GameObject.OBJECT_PLAYER) {
					addActor(new Player(this, ix, iy));
				} else if(t == GameObject.OBJECT_GHOST) {
					boolean trap = data.readBoolean();
					addActor(new Ghost(c, this, ix, iy, trap));
				} else {
					addActor(new Actor(t, c, this, ix, iy));
				}
			}
			
			data.close();
			fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read map file: " + e.getMessage());
		}
	}
	
}


