package character;

import java.util.Random;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public abstract class Character {

    private Integer hitPoints;
    private Integer attack;
    private Integer speed;
    private Integer criticalChance;
    private Double criticalDamage;
    private Boolean buffed = FALSE;
    private Boolean alive = TRUE;

    public Integer getHitPoints() {
        return hitPoints;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Boolean getBuffed() {
        return buffed;
    }

    public Boolean getAlive() {
        return alive;
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

    public void setCriticalDamage(Double criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public void setBuffed(Boolean buffed) {
        this.buffed = buffed;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public abstract String retrieveActions();

    public Integer attack() {
        final var roll = new Random().nextInt(100) + 1;
        if (roll <= criticalChance) {
            System.out.println("CRITICAL HIT!!!");
            return (int) (attack * criticalDamage);
        }
        return attack;
    }

    public abstract void buff();

    public abstract void debuff();
}
