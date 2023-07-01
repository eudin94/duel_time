package fight;

import character.Character;
import character.enums.ClassEnum;

import java.util.Random;

public class Fight {

    public Fight(final Character player1, final Character player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    private Character player1;
    private Character player2;

    public static void main(String[] args) {
        Character player1 = new Character(ClassEnum.WARRIOR);
        Character player2 = new Character(ClassEnum.MAGE);

        System.out.println("Player 1: " + player1.getClazz());
        System.out.println("Player 2: " + player2.getClazz());

        Random random = new Random();
        int turn = random.nextInt(2) + 1;

        System.out.println("Iniciando a batalha!");

        while (player1.getHitPoints() > 0 && player2.getHitPoints() > 0) {
            Character attackingCharacter;
            Character defendingCharacter;

            if (turn == 1) {
                attackingCharacter = player1;
                defendingCharacter = player2;
            } else {
                attackingCharacter = player2;
                defendingCharacter = player1;
            }

            System.out.println("É a vez do " + attackingCharacter.getClazz() + " atacar!");

            double damage = calculateDamage(attackingCharacter, defendingCharacter);
            defendingCharacter.setHitPoints(defendingCharacter.getHitPoints() - (int) damage);

            System.out.println(attackingCharacter.getClazz() + " causou " + (int) damage + " de dano!");
            System.out.println(defendingCharacter.getClazz() + " tem " + defendingCharacter.getHitPoints() + " pontos de vida restantes.");

            turn = (turn % 2) + 1;
        }

        if (player1.getHitPoints() <= 0 && player2.getHitPoints() <= 0) {
            System.out.println("A batalha terminou em empate!");
        } else if (player1.getHitPoints() <= 0) {
            System.out.println(player2.getClazz() + " venceu a batalha!");
        } else {
            System.out.println(player1.getClazz() + " venceu a batalha!");
        }
    }

    private static double calculateDamage(Character attackingCharacter, Character defendingCharacter) {
        double damage = attackingCharacter.getAttack() * (1 + (attackingCharacter.getCriticalChance() / 100));

        if (Math.random() < (attackingCharacter.getCriticalChance() / 100)) {
            damage *= attackingCharacter.getCriticalDamage() / 100;
            System.out.println("Acerto crítico!");
        }

        return damage;
    }
}
