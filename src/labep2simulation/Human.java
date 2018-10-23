/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labep2simulation;


/**
 *
 * @author Lokem
 */
public class Human extends Character{
    
    int velocity;
    
    public Human(int birthDate) {
        super(birthDate);
    }
    
    public int getVelocity(){
        return this.velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
