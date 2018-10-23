/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labep2simulation;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Lokem
 */
public class Environment {

    private int day;

    public int getDay() {
        return day;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumansLeft() {
        return humansLeft;
    }

    public int getVampireHuntersLeft() {
        return vampireHuntersLeft;
    }

    public int getVampiresLeft() {
        return vampiresLeft;
    }

    public int getZombiesLeft() {
        return zombiesLeft;
    }

    public ArrayList<Character> getPlayers() {
        return players;
    }
    
    private int temperature;
    private int humansLeft;
    private int vampireHuntersLeft;
    private int vampiresLeft;
    private int zombiesLeft;
    private ArrayList<Character> players;
    
    public Environment()
    {
        //resetGame();
    }
    
    public void resetGame()
    {
        day = 1;
        temperature = 20;
        int lengthList = calculateInitialTotalPlayers();
        players = new ArrayList<>();
        addPlayers("human", humansLeft);
        addPlayers("hunter", vampireHuntersLeft);
        addPlayers("vampire", vampiresLeft);
        addPlayers("zombie", zombiesLeft); 
            
    }
    private int calculateInitialTotalPlayers()
    {
        Random rand = new Random(); 
        humansLeft = rand.nextInt(4001)+2000;   //Initial amount of humans between  4000-6000
        vampireHuntersLeft = rand.nextInt(11)+5;//                  hunters         10-15
        vampiresLeft = rand.nextInt(16)+5;      //                  vampires        15-20
        zombiesLeft = rand.nextInt(21)+10;      //                  zombies         20-30       
        
        return humansLeft + vampireHuntersLeft + vampiresLeft + zombiesLeft;
    }
    private void addPlayers(String type, int amount)
    {   
        Random rand = new Random(); 
        int speed;
        
        for (int i = 0; i < amount; i++)
        {   
            
            switch (type){
                case "human":
                    speed = rand.nextInt(41)+60;
                    Human h = new Human(day, speed);
                    players.add(h);
                    break;
                case "hunter":
                    speed = rand.nextInt(41)+60;
                    VampireHunter vp = new VampireHunter(day, speed);
                    players.add(vp);
                    break;
                case "vampire":
                    Vampire v = new Vampire(day);
                    players.add(v);
                    break;
                case "zombie":
                    Zombie z = new Zombie(day);
                    players.add(z);
                    break;          
            }
            
        }
    }
    
}
