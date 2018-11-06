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
public class Zombie extends Character{
    
    int humansTransformed = 0;
    
    public Zombie(int birthDate) {
        super(birthDate);
        type = "Zombie";
    }
    
    public void transformHuman(){
        this.humansTransformed++;
    }
    
    public int getHumansTransformed(){
        return this.humansTransformed;
    }
}
