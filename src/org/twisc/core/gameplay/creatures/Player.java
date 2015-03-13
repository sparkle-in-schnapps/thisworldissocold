package org.twisc.core.gameplay.creatures;

import java.io.Serializable;
import static org.twisc.core.gameplay.Keyboard.*;
import org.newdawn.slick.Graphics;
import org.twisc.core.gameplay.*;
import static org.twisc.core.gameplay.Side.*;
import org.twisc.core.gameplay.essense.ArmMaterial;
import org.twisc.core.gameplay.essense.Armor;
import org.twisc.core.gameplay.essense.Weapon;
import org.twisc.core.gameplay.essense.WeaponClass;

/**
 *
 * @author yew_mentzaki && Whizzpered
 */
public class Player extends Entity implements Serializable{
    
    
    static Sprite sprite = new Sprite("creatures/player/");
    public Side side = RIGHT;
    public double vx,vy;               // spatial variables
    public int thishp=10,thisarm,thisdmg,currhp;     // Base characteristics
    public int str,agi,in;                 // Power
    public Armor [] armor = new Armor [3]; // worn armor
    public Weapon lhand, rhand;            // Equiped weapon
    public Mannequin aim;                  // **************************Change after!
    
    public void init(Object... args) {
        x = (Double)args[0];
        y = (Double)args[1];
        currhp=4;
        armor[0]=new Armor(new ArmMaterial(0),"boots");
        armor[1]=new Armor(new ArmMaterial(0),"body");
        armor[2]=new Armor(new ArmMaterial(0),"shoulders");
        rhand = new Weapon(new WeaponClass(4));
        world.guis.add(new Gui());
        world.guis.get(0).init(0, 50, "frame", "scale",this);
    }
    
    public int maxhp() {
        int hp = thishp;
        for(int i = 0; i<armor.length;i++){
            if(armor[i]!=null)
                hp+=armor[i].modhp;
        }
        return hp;
    }         // Returns overall HP
    
    public int arm() {
        int arm = thisarm;
        for(int i = 0; i<armor.length;i++){
            if(armor[i]!=null)
                arm+=armor[i].modarm;
        }
        return arm;
    }        // Return overall Armor
    
    public boolean poss(int weight) {
        int we=0;
        for(int i = 0; i<armor.length;i++){
            if(armor[i]!=null)
                we+=armor[i].modarm;
        }
        if(str-we>weight)return true;
        else return false;
    } //Check is wear possible 
    
    
    public void equipArm(Armor arm) {
        if(poss(arm.weight)){
            for(int i = 0; i<armor.length;i++){
            if(armor[i]==null){
                armor[i] = arm;
                armor[i].equip = true;
            }
        }}
    }
    
    public void equipWeap(Weapon weapon){
        if(rhand==null){
            
        }
    }

    @Override
    public void tick() {
        if(isKeyDown(KEY_RIGHT)||isKeyDown(KEY_D)){
            x += 1;
            side = RIGHT;
        }
        if(isKeyDown(KEY_LEFT)||isKeyDown(KEY_A)){
            x -= 1;
            side = LEFT;
        }
        if(isKeyDown(KEY_DOWN)||isKeyDown(KEY_S)){
            y += 1;
            side = FRONT;
        }
        if(isKeyDown(KEY_UP)||isKeyDown(KEY_W)){
            y -= 1;
            side = BACK;
        }
        
        world.targetCamera.x = (int) x;
        world.targetCamera.y = (int) y;
        
    }
    
    
    
    @Override
    public void render(Graphics g) {
        
        sprite.render(side);
        
        for(Armor ar:armor){
            if(ar!=null)ar.render(this);
        }
        
        if(rhand!=null){
            rhand.render(this);
        }
    }
    
}
