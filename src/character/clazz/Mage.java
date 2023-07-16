package character.clazz;

import character.Character;

public class Mage extends Character {

    public Mage() {
        this.setHitPoints(100);
        this.setAttack(20);
        this.setSpeed(2);
        this.setCriticalChance(20);
        this.setCriticalDamage(1.20);
    }

    @Override
    public String retrieveActions() {
        return "Meditação - Sacrifica velocidade e chance de crítico para um ataque devastador";
    }

    @Override
    public void buff() {
        setBuffed(true);
        setAttack(30);
        setSpeed(0);
        setCriticalChance(10);
        setCriticalDamage(2.75);
    }

    @Override
    public void debuff() {
        setBuffed(false);
        this.setAttack(20);
        this.setSpeed(2);
        this.setCriticalChance(20);
        this.setCriticalDamage(1.20);
    }
}
