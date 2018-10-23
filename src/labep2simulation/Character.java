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
public abstract class Character {
    int birthDate;
    
    public Character(int birthDate){
        this.birthDate = birthDate;
    }
    
    public int getBirthDate(){
        return this.birthDate;
    }
}
