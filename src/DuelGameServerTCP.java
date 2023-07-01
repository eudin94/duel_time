import character.Character;
import character.enums.ClassEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DuelGameServerTCP {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Servidor iniciado. Aguardando conexão...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão estabelecida com o cliente: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Lógica do jogo de duelos
                Character player1Character = chooseCharacter(in, out, "Jogador 1");
                Character player2Character = chooseCharacter(in, out, "Jogador 2");

                boolean gameOver = false;
                while (!gameOver) {
                    String player1Action = receiveAction(in, out, player1Character);
                    String player2Action = receiveAction(in, out, player2Character);

                    executeAction(player1Character, player2Character, out, out);
                    executeAction(player2Character, player1Character, out, out);

                    if (player1Character.getHitPoints() <= 0 || player2Character.getHitPoints() <= 0) {
                        gameOver = true;
                    }
                }

                // Fechar a conexão com o cliente
                clientSocket.close();
                System.out.println("Conexão encerrada com o cliente: " + clientSocket.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Character chooseCharacter(BufferedReader in, PrintWriter out, String playerName) throws IOException {
        out.println(playerName + ", escolha seu personagem: Guerreiro, Mago, Gatuno ou Bardo");
        String chosenClass = in.readLine().trim().toUpperCase();
        while (!isValidClass(chosenClass)) {
            out.println("Personagem inválido. Escolha novamente: Guerreiro, Mago, Gatuno ou Bardo");
            chosenClass = in.readLine().trim().toUpperCase();
        }
        return new Character(ClassEnum.valueOf(chosenClass));
    }

    private static boolean isValidClass(String className) {
        try {
            ClassEnum.valueOf(className);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static String receiveAction(BufferedReader in, PrintWriter out, Character character) throws IOException {
        out.println(character.getClazz() + ", escolha sua ação: [Atacar, Defender, Curar, Especial]");
        String chosenAction = in.readLine().trim();
        while (!isValidAction(chosenAction)) {
            out.println("Ação inválida. Escolha novamente: [Atacar, Defender, Curar, Especial]");
            chosenAction = in.readLine().trim();
        }
        return chosenAction;
    }

    private static boolean isValidAction(String action) {
        return action.equals("Atacar") || action.equals("Defender") || action.equals("Curar") || action.equals("Especial");
    }

    private static void executeAction(Character attacker, Character target, PrintWriter attackerOut, PrintWriter targetOut) {
        // Lógica de execução das ações do jogo
        // Implemente a lógica de acordo com as regras do jogo
        // Exemplo simples: Ação de ataque
        target.setHitPoints(target.getHitPoints() - attacker.getAttack());
        targetOut.println("Você sofreu " + attacker.getAttack() + " de dano.");
        attackerOut.println("Você atacou o oponente, causando " + attacker.getAttack() + " de dano.");
    }
}