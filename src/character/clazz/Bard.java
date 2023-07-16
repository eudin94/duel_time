package character.clazz;

import character.Character;

public class Bard extends Character {

    public Bard() {
        this.setHitPoints(150);
        this.setAttack(8);
        this.setSpeed(3);
        this.setCriticalChance(30);
        this.setCriticalDamage(1.75);
    }

    @Override
    public String retrieveActions() {
        return "Canto da Longitude - Aumenta levemente todos os atributos";
    }

    @Override
    public void buff() {
        setBuffed(true);
        setAttack(9);
        setSpeed(4);
        setCriticalChance(40);
        setCriticalDamage(2.0);
    }

    @Override
    public void debuff() {
        setBuffed(false);
        setAttack(8);
        setSpeed(3);
        setCriticalChance(30);
        setCriticalDamage(1.75);
    }
}
