
package org.twisc.core.gameplay.essense;

import org.twisc.core.gameplay.Entity;
import org.twisc.core.gameplay.Side;
import org.twisc.core.gameplay.Sprite;
import org.twisc.core.gameplay.creatures.Player;

/**
 *
 * @author Whizzpered
 */
public class Armor extends Entity{
    
    public Sprite sprite;
    public int modhp, modarm, modstr,modagi,modint;
    public ArmMaterial material;
    public int weight;
    public boolean equip;
    public Side side;
    
    public Armor(ArmMaterial material, String type){
        this.material = material;
        sprite = new Sprite("essenses/"+type+"/");
    }
    
    public void render(Player player) {
        side = player.side;
        sprite.render(side);
    }
}
