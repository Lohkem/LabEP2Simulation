/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labep2simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Lokem
 */
public class Environment {

    private int day;    
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
        players = new ArrayList<>();
        calculateInitialTotalPlayers();
        addPlayers("human", humansLeft);
        addPlayers("hunter", vampireHuntersLeft);
        addPlayers("vampire", vampiresLeft);
        addPlayers("zombie", zombiesLeft); 
            
    }
    private int calculateInitialTotalPlayers()
    {
        Random rand = new Random(); 
        humansLeft = rand.nextInt(2001)+4000;   //Initial amount of humans between  4000-6000
        vampireHuntersLeft = rand.nextInt(6)+10;//                  hunters         10-15
        vampiresLeft = rand.nextInt(6)+15;      //                  vampires        15-20
        zombiesLeft = rand.nextInt(11)+20;      //                  zombies         20-30       
        
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
    public void dayOfHumans()
    {   
        Random rand = new Random();
        ArrayList<Character> copy = players;
        for (Character person: copy)
        {
            if (person.getType().equals("Human"))
            {   // Baby boom
                if (((Human)person).reproduction(temperature) == true)
                {
                    for (int nr = 0; nr < (rand.nextInt(2)+1); nr++)            //every human can 1-3 a day
                    {   
                        Human child = new Human(day, ((Human)person).getVelocity());
                        players.add(child);
                        humansLeft++;
                    }
                }
            }
            else if (person.getType().equals("VampireHunter"))
            {   // Hunting vampires
                if (vampireHuntersLeft > 0 && (rand.nextDouble() < 1/3))
                {
                    ((VampireHunter)person).killVampire();
                    die("Vampire");
                }

                // Baby boom after the hunting
                if (((VampireHunter)person).reproduction(temperature) == true)
                {
                    for (int nr = 0; nr < (rand.nextInt(2)+1); nr++)            //every human can 1-3 a day
                    {   
                        VampireHunter child = new VampireHunter(day, ((VampireHunter)person).getVelocity());
                        players.add(child);
                        vampireHuntersLeft++;
                    }
                }
            }
        }
        deadHumans();
    }
    public void deadHumans()
    {
        Random rand = new Random();
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
            if (p.getType().equals("Human") || p.getType().equals("VampireHunter"))
            {
                if (rand.nextDouble() < (1/500+1/300))   
                {   
                    if (p.getType().equals("Human"))    humansLeft--;
                    else if (p.getType().equals("VampireHunter"))   vampireHuntersLeft--;
                    person.remove();
                }
                  
            }
        }
            
    }
    public void dayOfVampires()
    {
        Random rand = new Random();
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
            if (p.getType().equals("Vampire"))
            {   
                if (getHumansLeft() == 0)   
                {   person.remove();
                    vampiresLeft--;     }
                else {
                
                double dub = rand.nextDouble();
                if (dub < 0.5)     die("Human");
                if (dub < 0.25) 
                {   Vampire vp = new Vampire(getDay());
                    players.add(vp);
                    vampiresLeft++;
                    ((Vampire)p).transformHuman();
                }
                }
            }           
        } 
    }
    public void dayOfZombie()
    {
        Random rand = new Random();
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
            if (p.getType().equals("Zombie"))
            {   // Zombie dies or not?
                if ((getDay() - p.getBirthDate()) >= 8 )
                {
                    person.remove();
                    zombiesLeft--;
                }

                // Convert human
                else if (rand.nextDouble() < 0.1)
                {   int speed = getSlowestVelocity();
                    boolean converted = false;
                    if (speed != 90000000)
                    {
                        for (Iterator<Character> pp = players.iterator(); pp.hasNext();) {
            
                            Character convertingHuman = pp.next();
                            if (convertingHuman.getType().equals("Human") && ((Human)convertingHuman).getVelocity() == speed)
                            {
                                humansLeft--;
                                pp.remove();
                                converted = true;
                                break;
                            }
                            else if (convertingHuman.getType().equals("VampireHunter") && ((VampireHunter)convertingHuman).getVelocity() == speed)
                            {
                                vampireHuntersLeft--;
                                pp.remove();
                                converted = true;
                                break;
                            }
                        }
                    }
                    if (converted){
                        Zombie z = new Zombie(getDay());
                        players.add(z);
                        zombiesLeft++;
                        ((Zombie)p).transformHuman();
                    } 
                }                
            }
        }           
    }
    public int getSlowestVelocity()
    {
        int slow = 90000000;
        for (Character ch: players)
        {
            if (ch.getType().equals("Human") || ch.getType().equals("VampireHunter"))
                if (slow > ((Human)ch).getVelocity())
                    slow = ((Human)ch).getVelocity();
        }
        return slow;
    }
    public void changeClimate()
    {
        Random rand = new Random();
        int luckyNumber = rand.nextInt(101);  //0-100
        if (temperature >= 22)
        {
            if (luckyNumber <= 45)  temperature++;
            else                    temperature--;
        }
        else if (temperature <= 18)
        {
            if (luckyNumber <= 55)  temperature++;
            else                    temperature--;
        }
        else
        {
            if (luckyNumber <= 65)      temperature++;
            else if (luckyNumber <= 95) temperature--;
        }
    }
    
    public void die(String type)
    {
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
            if (p.getType().equals(type))    person.remove();       
        }
        switch (type){
            case "Humans":
                humansLeft--;
                break;
            case "VampireHunters":
                vampireHuntersLeft--;
                break;
            case "Vampire":
                vampiresLeft--;
                break;
        }
    }
    
    public void addDay()
    {
        day++;
    }
    
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
    
}
