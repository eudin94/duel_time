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

                Character first;
                BufferedReader firstIn;
                BufferedWriter firstOut;

                Character last;
                BufferedReader lastIn;
                BufferedWriter lastOut;

                // Determina qual jogador será o primeiro
                if (player1.getSpeed() > player2.getSpeed()) {
                    first = player1;
                    firstIn = player1In;
                    firstOut = player1Out;

                    last = player2;
                    lastIn = player2In;
                    lastOut = player2Out;
                } else if (player1.getSpeed() < player2.getSpeed()) {
                    first = player2;
                    firstIn = player2In;
                    firstOut = player2Out;

                    last = player1;
                    lastIn = player1In;
                    lastOut = player1Out;
                } else {
                    var player1First = new Random().nextBoolean();
                    first = player1First ? player1 : player2;
                    firstIn = player1First ? player1In : player2In;
                    firstOut = player1First ? player1Out : player2Out;

                    last = !player1First ? player1 : player2;
                    lastIn = !player1First ? player1In : player2In;
                    lastOut = !player1First ? player1Out : player2Out;
                }

                // Os jogadores escolhem sua ações
                var firstAction = receiveAction(firstIn, firstOut, first);
                var lastAction = receiveAction(lastIn, lastOut, last);

                // O jogador mais rápido executa a ação escolhida
                if (firstAction == 1) {
                    executeAttack(first, last, firstOut, lastOut);
                } else {
                    executeBuff(first, firstOut, lastOut);
                }

                if (!last.getAlive()) break;

                // O jogador mais lento executa a ação escolhida
                if (lastAction == 1) {
                    executeAttack(last, first, lastOut, firstOut);
                } else {
                    executeBuff(last, lastOut, firstOut);
                }

                if (!first.getAlive()) break;

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
            ;
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
