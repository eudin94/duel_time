package character;

import java.util.Random;

public abstract class Character {

    private Integer hitPoints;
    private Integer attack;
    private Integer speed;
    private Integer criticalChance;
    private Integer criticalDamage;

    public Integer getHitPoints() {
        return hitPoints;
    }

    public Integer getAttack() {
        return attack;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getCriticalChance() {
        return criticalChance;
    }

    public Integer getCriticalDamage() {
        return criticalDamage;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public void setCriticalChance(Integer criticalChance) {
        this.criticalChance = criticalChance;
    }

    public void setCriticalDamage(Integer criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public Integer attack(final Integer attack, final Integer criticalChance, final Integer criticalDamage) {
        final var random = new Random();
        final var roll = random.nextInt(100) + 1;
        if (roll <= criticalChance) {
            System.out.println("CRITICAL HIT!!!");
            return attack * criticalDamage / 100;
        }
        return attack;
    }
}
