import character.enums.ClassEnum;

import java.io.*;
import java.net.*;
import java.util.Random;
import character.Character;

public class TCPServer {
    public static void main(String[] args) {
        try {
            // Criação do socket TCP e aguardo de conexões
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Servidor aguardando conexões...");

            // Aguarda a conexão de um cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado!");

            // Criação dos objetos de entrada e saída
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Lógica do jogo
            Character player1 = new Character(ClassEnum.WARRIOR);
            Character player2 = new Character(ClassEnum.MAGE);

            out.println("Player 1: " + player1.getClazz());
            out.println("Player 2: " + player2.getClazz());

            Random random = new Random();
            int turn = random.nextInt(2) + 1;

            out.println("Iniciando a batalha!");

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

                out.println("É a vez do " + attackingCharacter.getClazz() + " atacar!");

                double damage = calculateDamage(attackingCharacter, defendingCharacter);
                defendingCharacter.setHitPoints(defendingCharacter.getHitPoints() - (int) damage);

                out.println(attackingCharacter.getClazz() + " causou " + (int) damage + " de dano!");
                out.println(defendingCharacter.getClazz() + " tem " + defendingCharacter.getHitPoints() + " pontos de vida restantes.");

                turn = (turn % 2) + 1;
            }

            if (player1.getHitPoints() <= 0 && player2.getHitPoints() <= 0) {
                out.println("A batalha terminou em empate!");
            } else if (player1.getHitPoints() <= 0) {
                out.println(player2.getClazz() + " venceu a batalha!");
            } else {
                out.println(player1.getClazz() + " venceu a batalha!");
            }

            // Fechamento dos recursos
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double calculateDamage(Character attackingCharacter, Character defendingCharacter) {
        double damage = attackingCharacter.getAttack() * (1 + (attackingCharacter.getCriticalChance() / 100));

        if (Math.random() < (attackingCharacter.getCriticalChance() / 100)) {
            damage *= attackingCharacter.getCriticalDamage() / 100;
        }

        return damage;
    }
}
