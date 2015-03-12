
package org.twisc.core.gameplay.essense;

import org.twisc.core.gameplay.Entity;

/**
 *
 * @author Whizzpered
 */
public class Weapon extends Entity {
    
    public WeaponClass type;
    public int moddmg,modstr,modagi,modint;
    
    public Weapon(WeaponClass clas) {
        type = clas;
    }
    
}
