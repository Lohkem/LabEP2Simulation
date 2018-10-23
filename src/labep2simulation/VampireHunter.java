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
public class VampireHunter extends Human{
    
    int vampiresKilled = 0;
    
    public VampireHunter(int birthDate) {
        super(birthDate);
    }
    
    public void killVampire(){
        this.vampiresKilled++;
    }
    
    public int getVampiresKilled(){
        return this.vampiresKilled;
    }
}
