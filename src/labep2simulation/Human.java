/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labep2simulation;

import java.util.Random;


/**
 *
 * @author Lokem
 */
public class Human extends Character{
    
    int velocity;
    
    public Human(int birthDate, int speed) {
        super(birthDate);
        Random rand = new Random();
        velocity = speed;
        type = "Human";
    }
    
    public int getVelocity(){
        return this.velocity;
    }
    
    public boolean reproduction(int temp)
    {
        Random rand = new Random();
        double luckyNumber = rand.nextDouble();  //0.0-1.0
        if (temp >= 22)
        {
            if (luckyNumber <= (0.0666667))    return true;
            return false;
        }
        else if (temp <= 18)
        {
            if (luckyNumber <= (0.02))     return true;
            return false;
        }
        else
        {
            if (luckyNumber <= (0.03333))      return true;
            return false;
        }
    }
}
