import character.Character;
import character.enums.ClassEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DuelGame {

    public static void main(String[] args) {
        try {
            // Inicialização do servidor
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Servidor iniciado. Aguardando conexão...");

            // Aguardando conexão do jogador 1
            Socket player1Socket = serverSocket.accept();
            PrintWriter player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            player1Out.println("Conectado como Jogador 1. Aguardando oponente...");

            // Aguardando conexão do jogador 2
            Socket player2Socket = serverSocket.accept();
            PrintWriter player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player2Out.println("Conectado como Jogador 2. Iniciando jogo...");

            // Iniciando jogo
            Character player1Character = chooseCharacter(player1In, player1Out);
            Character player2Character = chooseCharacter(player2In, player2Out);
            player1Out.println("Jogo iniciado. Você escolheu: " + player1Character.getClazz());
            player2Out.println("Jogo iniciado. Você escolheu: " + player2Character.getClazz());

            // Loop principal do jogo
            while (true) {
                // Receber ações dos jogadores
                String player1Action = receiveAction(player1In, player1Out);
                String player2Action = receiveAction(player2In, player2Out);

                // Verificar ordem de ação
                Character firstCharacter;
                Character secondCharacter;
                if (player1Character.getSpeed() > player2Character.getSpeed()) {
                    firstCharacter = player1Character;
                    secondCharacter = player2Character;
                } else {
                    firstCharacter = player2Character;
                    secondCharacter = player1Character;
                }

                // Executar ação do primeiro personagem
                executeAction(firstCharacter, secondCharacter, player1Out, player2Out);

                // Verificar se o segundo personagem ainda está vivo
                if (secondCharacter.getHitPoints() <= 0) {
                    // O jogo termina se o segundo personagem morrer
                    player1Out.println("Você venceu!");
                    player2Out.println("Você perdeu!");
                    break;
                }

                // Executar ação do segundo personagem
                executeAction(secondCharacter, firstCharacter, player2Out, player1Out);

                // Verificar se o primeiro personagem ainda está vivo
                if (firstCharacter.getHitPoints() <= 0) {
                    // O jogo termina se o primeiro personagem morrer
                    player2Out.println("Você venceu!");
                    player1Out.println("Você perdeu!");
                    break;
                }
            }

            // Fechar conexões
            player1Socket.close();
            player2Socket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Character chooseCharacter(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Escolha seu personagem: Guerreiro, Mago, Gatuno ou Bardo");
        String chosenClass = in.readLine();
        return new Character(ClassEnum.valueOf(chosenClass.toUpperCase()));
    }

    private static String receiveAction(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Escolha sua ação: [Atacar, Defender, Curar, Especial]");
        return in.readLine();
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