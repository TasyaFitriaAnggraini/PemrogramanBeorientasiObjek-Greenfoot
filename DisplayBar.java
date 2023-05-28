import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Displays the health bar that all enemies possess
 * 
 * @author (Benny Wu) 
 * Last edited (Jan 20, 2021)
 */
public class DisplayBar extends Actor
{

    
    Game world;
    int[] pos = new int[2];
    GreenfootImage[] image = new GreenfootImage[9];
    
    public DisplayBar()
    {

        for(int i=0; i<9; i++)
        {
            image[i] = new GreenfootImage("healthBar" + i + ".png");
        }
        setImage(image[1]);

    }
    
    public void addedToWorld(World game)
    {
        Game world = (Game)getWorld();
    }

    public void act() 
    {
       Game world = (Game)getWorld();

    }   
    
    /**
     * Constantly sets the health bar to a new location
     */
    public void updatePosition(int x, int y)
    {
        setLocation(x,y);

    }
    
    /**
     * Sets bar to new image according to the percentage
     */
    public void updatePercentage(double percent)
    {
        world = (Game)getWorld();
        
        if(percent == 1)
        {
            setImage(image[0]);
        }
        else if(percent >= 0.875)
        {
            setImage(image[1]);
        }
        else if(percent >= 0.75)
        {
            setImage(image[2]);
        }
        else if(percent >= 0.625)
        {
            setImage(image[3]);
        }
        else if(percent >= 0.5)
        {
            setImage(image[4]);
        }
        else if(percent >= 0.375)
        {
            setImage(image[5]);
        }
        else if(percent >= 0.25)
        {
            setImage(image[6]);
        }
        else if(percent >= 0.125)
        {
            setImage(image[7]);
        }
        else if(percent > 0)
        {
            setImage(image[8]);
        }
        

    }
}
