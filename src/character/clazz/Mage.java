package character.clazz;

import character.Character;

public class Mage extends Character {

    public Mage() {
        this.setHitPoints(100);
        this.setAttack(20);
        this.setSpeed(2);
        this.setCriticalChance(20);
        this.setCriticalDamage(120);
    }
}
