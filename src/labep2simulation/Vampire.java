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
public class Vampire extends Character{
    
    int humansTransformed = 0;
    
    public Vampire(int birthDate) {
        super(birthDate);
    }
    
    public void transformHuman(){
        this.humansTransformed++;
    }
    
    public int getHumansTransform(){
        return this.humansTransformed;
    }
}
