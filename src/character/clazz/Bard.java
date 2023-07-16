package character.clazz;

import character.Character;

public class Bard extends Character {

    public Bard() {
        this.setHitPoints(150);
        this.setAttack(8);
        this.setSpeed(3);
        this.setCriticalChance(30);
        this.setCriticalDamage(175);
    }
}
