import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.*;
/**
 * Contains the properties of every level, and it is being passed into the Game class
 * 
 * @author (Benny Wu) 
 * Last Edited (Jan 20, 2021)
 */
public class Level extends Actor
{

    int length;
    int width;
    int level;
    int wave;
    GreenfootImage background;
    String pathOne;
    String pathTwo;
    String tiles;
    ArrayList<Integer> enemy_list = new ArrayList<Integer>();
    
    public Level(int length, int width, int level, GreenfootImage background, String pathOne, String pathTwo, String tiles)
    {
        this.length = length;
        this.width = width;
        this.background = background;
        this.pathOne = pathOne;
        this.pathTwo = pathTwo;
        this.tiles = tiles;
        this.level = level;
        this.wave = 0;
        
    }
    
    public Level(File file)
    {
        ArrayList<String> list = new ArrayList<String>();
        try
        {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine())
            {
                list.add(sc.nextLine());
            }
            sc.close();
            this.length = Integer.parseInt(list.get(0));
            this.width = Integer.parseInt(list.get(1));
            this.level = Integer.parseInt(list.get(2));
            this.background = new GreenfootImage(list.get(3));
            this.pathOne = list.get(4);
            this.pathTwo = list.get(5);
            this.tiles = list.get(6);
            for(int i=7; i<list.size(); i++)
            {
                enemy_list.add(Integer.parseInt(list.get(i)));
            }
        }
        catch(IOException e)
        {
            System.out.println("didn't get added");
        }
        
        
    }
    
    /**
     * Changes the wave within a game to an integer
     */
    public void changeWave(int wave)
    {
        this.wave = wave;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public GreenfootImage getBackground()
    {
        return background;
    }
    
    public String getPathOne()
    {
        return pathOne;
    }
    
    public String getPathTwo()
    {
        return pathTwo;
    }
    
    public String getTiles()
    {
        return tiles;
    }
    
    public ArrayList<Integer> getEnemy()
    {
        return enemy_list;
    }
    
}
