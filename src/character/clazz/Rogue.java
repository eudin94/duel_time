package character.clazz;

import character.Character;

public class Rogue extends Character {

    public Rogue() {
        this.setHitPoints(125);
        this.setAttack(7);
        this.setSpeed(4);
        this.setCriticalChance(50);
        this.setCriticalDamage(3.00);
    }

    @Override
    public String retrieveActions() {
        return "Evasão - Chance de desviar do próximo ataque e contra atacar";
    }

    @Override
    public void buff() {
        setBuffed(true);
        setAttack(10);
        setCriticalChance(100);
    }

    @Override
    public void debuff() {
        setBuffed(false);
        setAttack(7);
        setCriticalChance(50);
    }
}
