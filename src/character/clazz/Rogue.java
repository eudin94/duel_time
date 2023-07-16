package character.clazz;

import character.Character;

public class Rogue extends Character {

    public Rogue() {
        this.setHitPoints(125);
        this.setAttack(7);
        this.setSpeed(4);
        this.setCriticalChance(50);
        this.setCriticalDamage(200);
    }
}
