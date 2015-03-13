
package org.twisc.core.gameplay.essense;

/**
 *
 * @author Whizzpered
 */
public class WeaponClass {
    
    public int type, reqchar;     // Type and Requed Characteristic
    public int range;             // Range in blocks.
    
    public WeaponClass(int type) {
        this.type = type;
        switch (type){
            case(1):              // Sword or Axe. Reques STR
                range = 1;
                reqchar = 1;
            break;
            case (2):             //Hammer. Reques STR
                range = 2;
                reqchar = 1;
            break;
            case(3):
                range = 2;
                reqchar = 2;     // Spear. Reques AGI
            break;
            case(4):             //Bow or Musket. Reques AGI
                range = 5;
                reqchar = 2;  
            break;
            case (5):            // Staff. Reques INT
                range = 5;
                reqchar = 3; 
            break;
            case (6):
                range = 1;
                reqchar = 3;
            break;
        }
    }
    
}
