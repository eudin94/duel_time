package TCP;

import character.Character;
import character.clazz.Bard;
import character.clazz.Mage;
import character.clazz.Rogue;
import character.clazz.Warrior;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import static java.lang.Boolean.FALSE;

public class DuelGame {

    public static void main(String[] args) {
        Character player1;
        Character player2;
        var player1IsFirst = false;

        try {
            // Inicialização do servidor
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Servidor iniciado. Aguardando conexão...\n");

            // Aguardando conexão do jogador 1
            Socket player1Socket = serverSocket.accept();
            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(player1Socket.getOutputStream());
            BufferedWriter player1Out = new BufferedWriter(outputStreamWriter1);
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            player1Out.write("Conectado como Jogador 1. Aguardando oponente...\n");
            player1Out.flush();

            // Aguardando conexão do jogador 2
            Socket player2Socket = serverSocket.accept();
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(player2Socket.getOutputStream());
            BufferedWriter player2Out = new BufferedWriter(outputStreamWriter2);
            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player2Out.write("Conectado como Jogador 2. Iniciando jogo...\n");
            player2Out.flush();

            // Iniciando jogo
            player1 = chooseCharacter(player1In, player1Out);
            player2 = chooseCharacter(player2In, player2Out);
            player1Out.write("Jogo iniciado. Você escolheu: " + player1.getClass().getSimpleName() + "\n");
            player1Out.flush();
            player2Out.write("Jogo iniciado. Você escolheu: " + player2.getClass().getSimpleName() + "\n");
            player2Out.flush();


            // Loop principal do jogo
            while (player1.getAlive() && player2.getAlive()) {

                // Determina qual jogador será o primeiro
                if (player1.getSpeed() > player2.getSpeed()) {
                    player1IsFirst = true;
                } else if (player1.getSpeed() < player2.getSpeed()) {
                    player1IsFirst = false;
                } else {
                    player1IsFirst = new Random().nextBoolean();
                }

                // Os jogadores escolhem sua ações
                var firstAction = player1IsFirst
                        ? receiveAction(player1In, player1Out, player1)
                        : receiveAction(player2In, player2Out, player2);

                var lastAction = player1IsFirst
                        ? receiveAction(player2In, player2Out, player2)
                        : receiveAction(player1In, player1Out, player1);


                if (player1IsFirst) {
                    // O jogador mais rápido executa a ação escolhida
                    if (firstAction == 1) {
                        executeAttack(player1, player2, player1Out, player2Out);
                    } else {
                        executeBuff(player1, player1Out, player2Out);
                    }
                    if (!player2.getAlive()) break;

                    // O jogador mais lento executa a ação escolhida
                    if (lastAction == 1) {
                        executeAttack(player2, player1, player2Out, player1Out);
                    } else {
                        executeBuff(player2, player2Out, player1Out);
                    }
                    if (!player1.getAlive()) break;

                } else {
                    // O jogador mais rápido executa a ação escolhida
                    if (firstAction == 1) {
                        executeAttack(player2, player1, player2Out, player1Out);
                    } else {
                        executeBuff(player2, player2Out, player1Out);
                    }
                    if (!player1.getAlive()) break;

                    // O jogador mais lento executa a ação escolhida
                    if (lastAction == 1) {
                        executeAttack(player1, player2, player1Out, player2Out);
                    } else {
                        executeBuff(player1, player1Out, player2Out);
                    }
                    if (!player2.getAlive()) break;
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

    private static Character chooseCharacter(BufferedReader in, BufferedWriter out) throws IOException {
        out.write("Escolha seu personagem: Guerreiro, Mago, Gatuno ou Bardo\n");
        out.flush();
        String chosenClass = in.readLine().trim().toUpperCase();

        return switch (chosenClass) {
            case "GUERREIRO" -> new Warrior();
            case "MAGO" -> new Mage();
            case "GATUNO" -> new Rogue();
            case "BARDO" -> new Bard();
            default -> {
                out.write("Valor inválido!\n");
                out.flush();
                yield chooseCharacter(in, out);
            }
        };
    }

    private static Integer receiveAction(BufferedReader in, BufferedWriter out, Character player) throws IOException {
        out.write("[HP = " + player.getHitPoints() + "]Escolha sua ação (1 ou 2):\n [1.Atacar] [2." + player.retrieveActions() + "]\n");
        out.flush();
        var input = in.readLine();

        try {

            var opt = Integer.parseInt(input);
            if (opt == 1 || opt == 2) return opt;

        } catch (NumberFormatException ignore) {
        }

        out.write("Opção inválida!\n");
        out.flush();
        return receiveAction(in, out, player);
    }

    private static void executeAttack(Character attacker, Character target,
                                      BufferedWriter attackerOut, BufferedWriter targetOut) throws IOException {
        if (target instanceof Rogue && target.getBuffed() && new Random().nextBoolean()) {
            targetOut.write("Você desviou!\n");
            targetOut.flush();
            attackerOut.write("O gatuno desviou do seu ataque!\n");
            attackerOut.flush();
        } else {
            var damage = attacker.attack();
            target.setHitPoints(target.getHitPoints() - damage);
            targetOut.write("Você sofreu " + damage + " de dano.\n");
            targetOut.flush();
            attackerOut.write("Você atacou o oponente, causando " + damage + " de dano.\n");
            attackerOut.flush();
            if (target.getHitPoints() <= 0) {
                target.setAlive(FALSE);
                attackerOut.write("Você matou seu oponente\n");
                attackerOut.flush();
                targetOut.write("Você foi derrotado\n");
                targetOut.flush();
            }
        }
        attacker.debuff();
    }

    private static void executeBuff(Character attacker, BufferedWriter attackerOut, BufferedWriter targetOut) throws IOException {
        attacker.buff();
        attackerOut.write("Você usou seu turno para se fortalecer\n");
        attackerOut.flush();
        targetOut.write("O oponente usou o turno dele para se fortalecer\n");
        targetOut.flush();
    }
}
