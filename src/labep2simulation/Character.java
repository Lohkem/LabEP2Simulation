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
    String type;
    boolean dying;

    public boolean isDying() {
        return dying;
    }
    
  
    
    public Character(int birthDate){
        this.birthDate = birthDate;
        type = "Character";
        dying = false;
    }
    
    public int getBirthDate(){
        return this.birthDate;
    }
    public String getType() {
        return type;
    }
    public void willDie()
    {
        dying = true;
    }
}
