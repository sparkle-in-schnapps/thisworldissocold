
package org.twisc.core.gameplay.essense;

import org.twisc.core.gameplay.Entity;
import org.twisc.core.gameplay.Sprite;
import org.twisc.core.gameplay.creatures.Player;

/**
 *
 * @author Whizzpered
 */
public class Weapon extends Entity {
    
    public WeaponClass type;
    Sprite sprite= new Sprite("weapons/bow/");
    public int moddmg,modstr,modagi,modint;
    
    public Weapon(WeaponClass clas) {
        type = clas;
    }
    
    public void render(Player player) {
        sprite.render(player.side);
    }
}
