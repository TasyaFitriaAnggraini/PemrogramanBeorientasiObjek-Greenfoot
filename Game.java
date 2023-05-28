import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*; 
import java.io.*;

/**
* Benny's Tower Defense in a fun and engaging game to play with (Not Really lol)
* This is the main world where the different levels will be run on
* It consists of the statuses of all existing objects
* @Benny Wu
* 
*/

public class Game extends World
{
    Level current_level;
    
    /* These arraylists store the set position of the paths, 
    gathered from different text files that I create, 
    and the current status of the enemies */
    public ArrayList<int[]> pathOne = new ArrayList<int[]>(); 
    public ArrayList<int[]> pathTwo = new ArrayList<int[]>();
    public ArrayList<int[]> tiles = new ArrayList<int[]>();
    public ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    
    GreenfootImage background;
    
    SimpleTimer time = new SimpleTimer();    // This keeps track of the time gap between the spawn of enmies   
    SimpleTimer game_time = new SimpleTimer();
    SimpleTimer wave_alert = new SimpleTimer(); 
    SimpleTimer coin_delay = new SimpleTimer();
    
    
    // These labels represent the different messages displayed on the screen (time passed, new waves)
    Label time_counter;
    Label coin_display;
    Label wave_label;  
    Label new_wave;
    Label coins;
    
    int lives;
    int wave;
    int total_coins; 
    public int enemy_counter;
    static int level_num;
    
    // It stores the 5 lives 
    Heart[] hearts;
    
    
    
    // The explode button is used to spawn a large explosion in the game
    ExplodeButton explode_button; 
    
    /**
     * Constructor for objects of class Game
     * Initializes all instances within the Game, including enemies, tiles, and towers
     */
    public Game(Level level)
    {    
    
        super(1050, 700, 1); 
        level_num = level.getLevel();
        this.current_level = level;
        this.wave = 0;
        this.enemy_counter = 0;
        
        background = level.getBackground();
        background.scale(1050, 700);
        setBackground(background);
        
        
        // It reads the coordinates of the map and store it into certain arrays
        readMouseInfo(level.getPathOne(), pathOne);
        readMouseInfo(level.getPathTwo(), pathTwo);
        readMouseInfo(level.getTiles(), tiles);
    
        display_tiles(tiles);
        
        // Sets the labels and the amount of coins
        total_coins = 80;
        coins = new Label(total_coins, 50); 
        coins.setFillColor(Color.YELLOW);
        addObject(coins, 180, 70);
        GreenfootImage coin = new GreenfootImage("coin.png");
        coin.scale(60,60);
        background.drawImage(coin, 60, 40);
        
        // Updates the status of the gold mine
        updateMine(0.7, 1); 
    
        // Displays the hearts and lives onto the screen
        lives = 5;
        hearts = new Heart[lives];
        storeHeath();
        displayHealth(830, 70);
        
        time_counter = new Label("time: " + game_time.millisElapsed()/1000.0, 40);
        time_counter.setFillColor(Color.GRAY.brighter());
        addObject(time_counter, 900, 650);
        
        wave_label = new Label("Level " + this.level_num + " - " + this.wave, 35);
        displayWave();
        wave_alert.mark();
        
        explode_button = new ExplodeButton();
        addObject(explode_button, 110, 650);
        time.mark();
        
        
    }
    
    /**
     * Constantly runs to spawn enemy and detect any occuring events
     */
    public void act()
    {
    
    
        if(new_wave != null && wave_alert.millisElapsed() > 2000)
        {
            removeObject(new_wave);
        }
        
        if(enemy_counter % 10 == 0 && this.wave < 5)
        {
            this.wave ++;
            displayWave();
            wave_alert.mark();
            this.enemy_counter ++;
            
            if(this.wave == 1)
            {
                if(this.wave == 1 && this.level_num == 1)
                {
                    newWave("GOOD LUCK!");
                }
                else
                {
                    newWave("LEVEL " + this.level_num + "!");
                }
            }
            else if(this.wave == 5)
            {
                newWave("LAST WAVE!");
            }
            else
            {
                newWave("NEW WAVE APPROACHING");
            }
            
        }
        
        if(game_time.millisElapsed() > 400 && Greenfoot.isKeyDown("ENTER"))
        {
            
            if(level_num>=3)
            {
                level_num = 0;
            }
            level_num++;
            changeLevel(level_num);
        }
        
        /*
         * Adds an enemy onto the screen after a certain amount of time
         * Changes level if it passes the last wave
         */
        
        if(wave == 5)
        {
            if(enemyList.size() == 0)
            {
                if(this.level_num == 4)
                {
                    changeLevel(-1);
                }
                else 
                {
                    changeLevel(this.level_num + 1);
                }
            }
        }
        else if(game_time.millisElapsed()>10000 && time.millisElapsed()>Greenfoot.getRandomNumber(1500)+(4-this.wave)*1500)
        {
            if(this.wave == 4)
            {
                int n = Greenfoot.getRandomNumber(2);
                switch(n)
                {
                    case 0:
                        addEnemy();
                        return;
                    case 1:
                        addEnemy();
                        enemy_counter --;
                        return;
                }
            }
            time.mark();
            addEnemy();
        }
        
        // Automatically wins if player lasted for 500 seconds in a single game
        if(game_time.millisElapsed()>500000)
        {
            detectCondition(true);
        }
        
        if(game_time.millisElapsed()>10)
        {
            updateTimer();
        }
        
        detectCondition(false); // Constantly check if health is under 0
        checkEnemyStatus(); // Constantly checks if the enemy is in the world
        
        if (coin_display != null && 2000<coin_delay.millisElapsed() && coin_delay.millisElapsed()<3000)
        {
            removeObject(coin_display);
        }
    
    }
    
    /**
     * Displays the images of tiles onto the screen
     */
    public void display_tiles(ArrayList<int[]> t) 
    {
        
        for (int[] pos: t)
        {
            TowerTile tile = new TowerTile(pos[0], pos[1]);
            addObject(tile, pos[0], pos[1]);
        }
        
    }
    
    /**
    * Creates different enmies randomly based on numbers stored in a text file
    */    
    public void addEnemy()
    {
        int n = current_level.getEnemy().get(Greenfoot.getRandomNumber(current_level.getEnemy().size()));
        switch (n) {
            case 1:
                DudeEnemy d = new DudeEnemy(2.3+wave-1,30+wave*4,10+wave*2,pathOne.get(0)[0], pathOne.get(0)[1]);
                createEnemy(d);
                break;
            case 2:
                Yoshi y = new Yoshi(2.7+wave-1,23+wave,7+wave*2,pathOne.get(0)[0], pathOne.get(0)[1]);
                createEnemy(y);
                break;
            case 3:
                Bat b = new Bat(4.3+wave-1,3,4+wave*3,pathOne.get(0)[0], pathOne.get(0)[1]);
                createEnemy(b);
                break;
            case 4:
                WalkingSoldier w = new WalkingSoldier(2+wave-1,45+wave, 9+wave*2,pathOne.get(0)[0], pathOne.get(0)[1]);
                
                createEnemy(w);
                break;
            case 5:
                Snail s = new Snail(1.6+wave*0.7,45+wave*3,12+wave*2,pathOne.get(0)[0], pathOne.get(0)[1]);
                createEnemy(s);
                break;
            case 6:
                Golem g = new Golem(1.5+wave*0.7,600+wave*10,34+wave*5,pathOne.get(0)[0], pathOne.get(0)[1]);
                createEnemy(g);
                break;
            default:
                Bat bat = new Bat(4.2,3,15,pathOne.get(0)[0], pathOne.get(0)[1]);
                createEnemy(bat);
                break;
      
        }
        
    }
    
    /**
     * Creates a new enemy and adds it into the world
     */  
    public void createEnemy(Enemy e)
    {
        time.mark();
        enemyList.add(e); 
        addObject(e, pathOne.get(0)[0], pathOne.get(0)[1]);
    }
    
    /**
     * Checks if every enemy is still in world, else removes it
     */  
    public void checkEnemyStatus()
    {
        for(int i=0; i<enemyList.size(); i++)
        {
            
           if (!enemyList.get(i).existing)
           {
              enemyList.get(i).remove();
              enemyList.remove(i);
              
           }
        }
    }
    
    /**
     * Updates the status of the mine when updated
     */  
    public void updateMine(double speed, int level)
    {
        GoldMine mine = new GoldMine(speed, level);
        addObject(mine, 550, 440);
    }
        
    
    /**
     * Updates the amount of coins when the amount is changed
     */  
    public void updateCoins(int coin)
    {
        total_coins += coin;
        coins.setValue(total_coins);
    }
    
    
    public void updateTimer()
    {
        if (game_time.millisElapsed()<10000)
        {
            time_counter.setValue("TIME: " + Double.toString(game_time.millisElapsed()/1000.0).substring(0,3));
        }
        else if(game_time.millisElapsed()<100000)
        {
            time_counter.setValue("TIME: " + Double.toString(game_time.millisElapsed()/1000.0).substring(0,4));
        }
        else
        {
            time_counter.setValue("TIME: " + Double.toString(game_time.millisElapsed()/1000.0).substring(0,5));
        }
    }
    
    /**
     * Constantly displays the levels and waves as a label on the screen
     */  
    public void displayWave()
    {
        wave_label.setValue("Level " + this.level_num + " - " + this.wave);
        wave_label.setFillColor(Color.ORANGE);
        addObject(wave_label, 900, 100);
    }
    
    public void storeHeath()
    {
        for(int i=0; i<lives; i++)
        {
            hearts[i] = new Heart();
        }
    }
    
    public void displayHealth(int x, int y)
    {
        
        for (int i=0; i<lives; i++)
        {
            addObject(hearts[i], x+i*30, y);
        }
        
        for (int i=4; i>lives-1; i--)
        {
            removeObject(hearts[i]);
        }
        
    }
    
    public boolean takeCoins(int coin)
    {
        if(coin_display != null && coin_display.getWorld() != null)
        {
            removeObject(coin_display);
        }
        
        if(this.total_coins - coin >= 0)
        {
            this.total_coins -= coin;
            return true;
        }
        else
        {
            coin_display = new Label("Tidak Cukup Koin, Untuk Membeli!", 50);
            coin_display.setFillColor(Color.RED);
            addObject(coin_display, 500, 250);
            coin_delay.mark(); 
            return false;
        }
        
    
    }
    
    /**
     * Constantly detects if the game is won or lost
     */
    public void detectCondition(boolean win)
    {
        if (lives <= 0)
        {
            GameOver oo = new GameOver(this);
            Greenfoot.setWorld(oo);
        }
        else if(win)
        {
            GameWin yah = new GameWin(this);
            Greenfoot.setWorld(yah);
        }
    }
    
    public void newWave(String t)
    {
        if(new_wave != null && new_wave.getWorld() != null)
        {
            removeObject(new_wave);
        }
        new_wave = new Label(t, 80);
        new_wave.setFillColor(Color.YELLOW.brighter());
        addObject(new_wave, 525, 350);
    }
        
    public int getLevel()
    {
        return this.level_num;
    }
   
    public void changeLevel(int l)
    {
        if(l==1)
        {
            File file = new File("Level_01.txt");
            Level level = new Level(file);
            Game game = new Game(level);
            Greenfoot.setWorld(game);

        }
        else if (l==2)
        {

            File file = new File("Level_02.txt");
            Level level = new Level(file);
            Game game = new Game(level);
            Greenfoot.setWorld(game);
            
        }
        else if (l==3)
        {

            File file = new File("Level_03.txt");
            Level level = new Level(file);
            Game game = new Game(level);
            Greenfoot.setWorld(game);
            
        }
        else
        {
            GameWin win = new GameWin(this);
            Greenfoot.setWorld(win);
        }
    }
    
    /**
     * The following three methods: Benny calls this the "mouse-clicking method", 
     * a way to accurately write the position of tiles and routes into text files.
     */
    public void mouseCoords(String f)
    {
        if(Greenfoot.mouseClicked(null))
        {
            int x = Greenfoot.getMouseInfo().getX();
            int y = Greenfoot.getMouseInfo().getY();
            int[] arr = {x,y};
            writeMouseInfo(x,y,f);
        }
    }
    
    public void writeMouseInfo(int x, int y, String f)
    {
        try
        {
            String file = f;
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter myWriter = new BufferedWriter(writer);
            myWriter.write("(" + x + ", " + y + ")");
            myWriter.newLine();
            myWriter.close();
            System.out.println("written!");
        }
        
        catch(IOException e)
        {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
        
    }
    
    public void readMouseInfo(String f, ArrayList<int[]> path)
    {
        try
        {
            File file = new File(f);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine())
            {
                String data = sc.nextLine();
                String store_x = "";
                String store_y = "";
                int index = 1;
                while (data.charAt(index) != ',')
                {
                    store_x += data.charAt(index);
                    index ++;
                }
                index += 2;
                while (data.charAt(index) != ')')
                {
                    store_y += data.charAt(index);
                    index ++;
                }
                
                int x = Integer.parseInt(store_x);
                int y = Integer.parseInt(store_y);
                int[] arr = {x, y};
                path.add(arr);
            }
            
        }
        
        catch(IOException e)
        {
            System.out.print("Cannot read file");
        }
    }
    // OMG MR CHAN I SPENT SO MUCH TIME ON THIS
}
