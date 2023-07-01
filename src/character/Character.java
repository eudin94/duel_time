package character;

import character.enums.ClassEnum;

import java.util.Set;

public class Character {

    private String clazz;
    private Integer hitPoints;
    private Integer attack;
    private Integer speed;
    private Double criticalChance;
    private Double criticalDamage;
    private Set<String> actions;

    public Character(ClassEnum clazz) {

        switch (clazz) {

            case WARRIOR -> {
                this.clazz = "Guerreiro";
                this.hitPoints = 200;
                this.attack = 10;
                this.speed = 1;
                this.criticalChance = 10D;
                this.criticalDamage = 150D;
            }

            case MAGE -> {
                this.clazz = "Mago";
                this.hitPoints = 100;
                this.attack = 20;
                this.speed = 2;
                this.criticalChance = 20D;
                this.criticalDamage = 120D;
            }

            case ROGUE -> {
                this.clazz = "Gatuno";
                this.hitPoints = 125;
                this.attack = 7;
                this.speed = 4;
                this.criticalChance = 50D;
                this.criticalDamage = 200D;
            }

            case BARD -> {
                this.clazz = "Bardo";
                this.hitPoints = 150;
                this.attack = 8;
                this.speed = 3;
                this.criticalChance = 30D;
                this.criticalDamage = 175D;
            }
        }

    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Double getCriticalChance() {
        return criticalChance;
    }

    public void setCriticalChance(Double criticalChance) {
        this.criticalChance = criticalChance;
    }

    public Double getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(Double criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public Set<String> getActions() {
        return actions;
    }

    public void setActions(Set<String> actions) {
        this.actions = actions;
    }
}
