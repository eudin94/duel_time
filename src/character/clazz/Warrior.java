package character.clazz;

import character.Character;

public class Warrior extends Character {

    public Warrior() {
        this.setHitPoints(200);
        this.setAttack(10);
        this.setSpeed(1);
        this.setCriticalChance(10);
        this.setCriticalDamage(1.50);
    }

    @Override
    public String retrieveActions() {
        return "Grito de Guerra - Aumenta o ataque e chance de crítico";
    }

    @Override
    public void buff() {
        setBuffed(true);
        setAttack(15);
        setCriticalChance(30);
    }

    @Override
    public void debuff() {
        setBuffed(false);
        this.setAttack(10);
        this.setCriticalChance(10);
    }
}
