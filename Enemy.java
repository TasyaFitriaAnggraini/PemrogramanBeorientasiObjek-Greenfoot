
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Moves and controls all enemies
 * 
 * @author (Benny Wu) 
 * Last edited (Jan 20, 2021)
 */

public class Enemy extends Entity
{

    SimpleTimer time = new SimpleTimer();

    protected double max_health;
    protected double health;
    protected int coins;
    protected int[] destination;
    protected DisplayBar healthBar;
    
    private ArrayList<int[]> path;
    private int currentIndex = 0;
    private int xDiff;
    private int yDiff;
    
    public Enemy(double speed, double max_health, int coins, int x, int y)
    {
       this.speed = speed;
       this.max_health = max_health;
       this.health = max_health;
       this.coins = coins;
       gif = true;
       pos[0] = x;
       pos[1] = y;
       healthBar = new DisplayBar();
       this.existing = true;
       time.mark();
       
    }
    
    public void addedToWorld(World game)
    {
       if(game.getClass() == Game.class)
       {
           world = (Game)game;
           int num = Greenfoot.getRandomNumber(2);
           world.enemy_counter ++;
           // Randomly chooses between the two paths
           if(num==0)
           {
               path = world.pathOne;
           }
           else
           {
               path = world.pathTwo;
           }
           
           destination = new int[]{path.get(0)[0], path.get(0)[1]};
           setLocation(path.get(0)[0], path.get(0)[1]);
           game.addObject(healthBar, getX()+5, getY()-20);
       }
       else
       {
           destination = new int[2];
       }
    }     
    
    public void act() 
    {
        if(getWorld().getClass() == Game.class)
        {
            if (currentIndex == path.size()) 
            {
                Game world = (Game)getWorld();
                world.lives --;
                world.displayHealth(800, 50);
                existing = false;
                return;
                
            }
            if (distanceFrom(destination[0], destination[1])<5)
            {
            int x = path.get(currentIndex)[0];
            int y = path.get(currentIndex)[1];
            currentIndex++;
            relocate(x,y);
            }

            if (this.health <= -1)
            {
            world = (Game)getWorld();
            getWorld().removeObject(healthBar);
            this.existing = false;
            return;
            }

        }
        
        
        move(destination[0], destination[1]);
        healthBar.updatePercentage(this.health/this.max_health);
        healthBar.updatePosition(getX()+5,getY()-20);

    }   
    
    /**
     * Relocates the desination of the enemy to the coordinate (x,y)
     */
    public void relocate(int x, int y)
    {
        destination[0] = x;
        destination[1] = y;
        xDiff = destination[0] - getX();
        yDiff = destination[1] - getY();
    }
    
    /**
     * Gradually moves the enemy toward its updated destination
     */
    public void move()
    {

        double d = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        double xBlock = xDiff/(d/speed);
        double yBlock = yDiff/(d/speed);
        setLocation(getX()+(int)(xBlock+0.5), getY()+(int)(yBlock+0.5));
        
    }
    
    public GreenfootImage getImage()
    {
        
        GreenfootImage image = images.get((int)imageIndex);
        imageIndex += 0.25;
        if (imageIndex>=images.size())
        {
            imageIndex = 0;
        }
        time.mark();  
        return image;
    }
    
    /**
     * Takes away a double from its total health
     */
    public void takeDamage(double ouch) //LMAO THIS IS A JOKE DON'T TAKE ANY MARKS OFF
    {
        if(this.health-ouch<0)
        {
            this.health = -1;
        }
        else
        {
            this.health -= ouch;
        }
    }

    public int getCoins()
    {
        return this.coins;
    }
    
    public double getHealth()
    {
        return this.health;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }
    
    public int[] getDestination()
    {
        return this.destination;
    } 
    
    
    public void remove()
    {
        if (currentIndex == path.size())
        {
            getWorld().removeObject(healthBar);
            getWorld().removeObject(this);
            return;
        }
        else
        {
            DropCoin drop = new DropCoin();
            getWorld().addObject(drop, this.getX(), this.getY());
            ((Game)getWorld()).updateCoins(getCoins());
            getWorld().removeObject(healthBar);
            getWorld().removeObject(this);
            return;
        }
    }
}

    
    
    
    
    

