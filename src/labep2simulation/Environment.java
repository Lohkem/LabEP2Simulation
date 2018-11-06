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
    private double zombieChanceToCatchHuman;
    private ArrayList<Character> players;
    
    public Environment()
    {
        //resetGame();
    }
    
    public void resetGame()
    {
        day = 1;
        temperature = 20;
        zombieChanceToCatchHuman = 0.1;
        players = new ArrayList<>();
        calculateInitialTotalPlayers();
        addPlayers("Human", humansLeft);
        addPlayers("VampireHunter", vampireHuntersLeft);
        addPlayers("Vampire", vampiresLeft);
        addPlayers("Zombie", zombiesLeft); 
            
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
                case "Human":
                    speed = rand.nextInt(41)+60;
                    Human h = new Human(day, speed);
                    players.add(h);
                    break;
                case "VampireHunter":
                    speed = rand.nextInt(41)+60;
                    VampireHunter vp = new VampireHunter(day, speed);
                    players.add(vp);
                    break;
                case "Vampire":
                    Vampire v = new Vampire(day);
                    players.add(v);
                    break;
                case "Zombie":
                    Zombie z = new Zombie(day);
                    players.add(z);
                    break;          
            }
            
        }
    }
    public void dayOfHumans()
    {   
        Random rand = new Random();
        ArrayList<Character> copy = new ArrayList<>(players);
        for (Character person: copy)
        {   //System.out.println("Day of Humans\n");
            //instanceof does not work, because VampireHunter is also Human
            if ((person.getType().equals("Human")) && !(person.isDying()))
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
            else if ((person.getType().equals("VampireHunter"))  && !(person.isDying()))
            {   // Hunting vampires
                
                if (vampiresLeft > 0 && (rand.nextDouble() < 0.33))
                {   
                    ((VampireHunter)person).killVampire();
                    randomCharacterDies("Vampire");
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
            if ((p.getType().equals("Human") || p.getType().equals("VampireHunter")) && p.isDying() == false)
            {
                if (rand.nextDouble() < (1/500+1/300))      p.willDie();
            }
        }            
    }
    public void dayOfVampires()
    {
        int addVampires = 0;
        Random rand = new Random();
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
            if ((p instanceof Vampire) && p.isDying() == false)
            {   
                if (getHumansLeft() == 0)   
                {   p.willDie();    }
                else {
                
                double dub = rand.nextDouble();
                if (dub < 0.5)     randomCharacterDies("Human");
                if (dub < 0.25) 
                {   addVampires++;
                    ((Vampire)p).transformHuman();
                }
                }
            }           
        } 
        for (int i = 0; i < addVampires; i++)
        {
            Vampire vp = new Vampire(getDay());
            players.add(vp);
            vampiresLeft++;
        }
    }
    public void randomCharacterDies(String type)
    {
        Random rand = new Random();
        int size = players.size();
        boolean someoneDies = false;
        for (int r = rand.nextInt(size); r < size; r++ )
        {
            Character p = players.get(r);
            if (p.getType().equals(type))        //Niet met instance werken, want kan je niet meegeven in methode
            {               
                p.willDie();
                someoneDies = true;
                break;
            }
        }
        if (!someoneDies)
        {
            for (Character c: players)
            {
                if (c.getType().equals(type))
                {
                    c.willDie();
                    break;
                }
            }
        }
    }
    public void dayOfZombie()
    {
        Random rand = new Random();
        int addZombies = 0;
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
            if (p instanceof Zombie)
            {   // Zombie dies or not?
                if ((getDay() - p.getBirthDate()) >= 8)     p.willDie();
               
                // Convert human
                else if (rand.nextDouble() < zombieChanceToCatchHuman)
                {   int speed = getSlowestVelocity();
                    boolean converted = false;
                    if (speed != 90000000)
                    {
                        for (Iterator<Character> pp = players.iterator(); pp.hasNext();) {
            
                            Character convertingHuman = pp.next();
                            //instantof does not work, because it classifies human and vampirehunters as both humans
                            if ((convertingHuman.getType().equals("Human")) && ((Human)convertingHuman).getVelocity() == speed 
                                    && convertingHuman.isDying() == false)
                            {   
                                convertingHuman.willDie();
                                converted = true;
                                break;
                            }
                            else if ((convertingHuman.getType().equals("VampireHunter")) && ((VampireHunter)convertingHuman).getVelocity() == speed
                                    && convertingHuman.isDying() == false)
                            {
                                convertingHuman.willDie();
                                converted = true;
                                break;
                            }
                        }
                    }
                    if (converted)
                    {   
                        addZombies++;
                        ((Zombie)p).transformHuman();   
                    }                    
                }                
            }
        }    
        System.out.println("New zombies = " + addZombies);
        for (int i = 0; i < addZombies; i++)
        {
            Zombie z = new Zombie(getDay());
            players.add(z);
            zombiesLeft++;
        }
    }
    public int getSlowestVelocity()
    {
        int slow = 90000000;
        for (Character ch: players)
        {
            if ((ch.getType().equals("Human") || ch.getType().equals("VampireHunter")) && ch.isDying() == false)
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
    
    public void charactersDying()
    {
        for (Iterator<Character> person = players.iterator(); person.hasNext();) {
            
            Character p = person.next();
           
            if (p.isDying())
            {   
                switch (p.getType()){
                    case "Human":
                        humansLeft--;
                        break;
                    case "VampireHunter":
                        System.out.println("vamphunter--\n");
                        vampireHuntersLeft--;
                        break;
                    case "Vampire":
                        vampiresLeft--;
                        break;
                    case "Zombie":
                        zombiesLeft--;
                        break;
                }       
                person.remove();
            }
        }
    }
    
    public void addDay()
    {
        day++;
    }
    
    public int getDay() {
        return day;
    }
    public void setZombieChanceToCatchHuman(double zombieChanceToCatchHuman) {
        this.zombieChanceToCatchHuman = zombieChanceToCatchHuman;
    }
    public int getTemperature() {
        return temperature;
    }
    public void changeTemperature(int value)
    {
        temperature += value;
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
