package character.clazz;

import character.Character;

public class Warrior extends Character {

    public Warrior() {
        this.setHitPoints(200);
        this.setAttack(10);
        this.setSpeed(1);
        this.setCriticalChance(10);
        this.setCriticalDamage(150);
    }
}
