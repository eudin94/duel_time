package character;

import character.enums.ClassEnum;

import java.util.Random;

public class Character {

    private String clazz;
    private Integer hitPoints;
    private Integer attack;
    private Integer speed;
    private Integer criticalChance;
    private Integer criticalDamage;

    public Character(ClassEnum clazz) {

        switch (clazz) {

            case WARRIOR -> {
                this.clazz = "Guerreiro";
                this.hitPoints = 200;
                this.attack = 10;
                this.speed = 1;
                this.criticalChance = 10;
                this.criticalDamage = 150;
            }

            case MAGE -> {
                this.clazz = "Mago";
                this.hitPoints = 100;
                this.attack = 20;
                this.speed = 2;
                this.criticalChance = 20;
                this.criticalDamage = 120;
            }

            case ROGUE -> {
                this.clazz = "Gatuno";
                this.hitPoints = 125;
                this.attack = 7;
                this.speed = 4;
                this.criticalChance = 50;
                this.criticalDamage = 200;
            }

            case BARD -> {
                this.clazz = "Bardo";
                this.hitPoints = 150;
                this.attack = 8;
                this.speed = 3;
                this.criticalChance = 30;
                this.criticalDamage = 175;
            }
        }

    }

    public String getClazz() {
        return clazz;
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

    public Integer getSpeed() {
        return speed;
    }

    public Integer getCriticalChance() {
        return criticalChance;
    }

    public Integer getCriticalDamage() {
        return criticalDamage;
    }

    public Integer calculateDamage(final Integer attack, final Integer criticalChance, final Integer criticalDamage) {
        final var random = new Random();
        final var roll = random.nextInt(100) + 1;
        if (roll <= criticalChance) {
            System.out.println("CRITICAL HIT!!!");
            return attack * criticalDamage / 100;
        }
        return attack;
    }
}
