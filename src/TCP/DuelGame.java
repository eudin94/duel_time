package TCP;

import character.Character;
import character.clazz.Bard;
import character.clazz.Mage;
import character.clazz.Rogue;
import character.clazz.Warrior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            player1 = chooseCharacter(player1In, player1Out);
            player2 = chooseCharacter(player2In, player2Out);
            player1Out.println("Jogo iniciado. Você escolheu: " + player1.getClass().getSimpleName());
            player2Out.println("Jogo iniciado. Você escolheu: " + player2.getClass().getSimpleName());


            // Loop principal do jogo
            while (player1.getAlive() && player2.getAlive()) {

                Character first;
                BufferedReader firstIn;
                PrintWriter firstOut;

                Character last;
                BufferedReader lastIn;
                PrintWriter lastOut;

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

    private static Character chooseCharacter(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Escolha seu personagem: Guerreiro, Mago, Gatuno ou Bardo");
        String chosenClass = in.readLine().trim().toUpperCase();

        return switch (chosenClass) {
            case "GUERREIRO" -> new Warrior();
            case "MAGO" -> new Mage();
            case "GATUNO" -> new Rogue();
            case "BARDO" -> new Bard();
            default -> {
                out.println("Valor inválido!");
                yield chooseCharacter(in, out);
            }
        };
    }

    private static Integer receiveAction(BufferedReader in, PrintWriter out, Character player) throws IOException {
        out.println("[HP = " + player.getHitPoints() + "]Escolha sua ação (1 ou 2):\n [1.Atacar] [2." + player.retrieveActions() + "]");
        var input = in.readLine();

        try {

            var opt = Integer.parseInt(input);
            if (opt == 1 || opt == 2) return opt;

        } catch (NumberFormatException e) {
            out.println("Opção inválida!");
            return receiveAction(in, out, player);
        }

        out.println("Opção inválida!");
        return receiveAction(in, out, player);
    }

    private static void executeAttack(Character attacker, Character target, PrintWriter attackerOut, PrintWriter targetOut) {
        if (target instanceof Rogue && target.getBuffed() && new Random().nextBoolean()) {
            targetOut.println("Você desviou!");
            attackerOut.println("O gatuno desviou do seu ataque!");
        } else {
            var damage = attacker.attack();
            target.setHitPoints(target.getHitPoints() - damage);
            targetOut.println("Você sofreu " + damage + " de dano.");
            attackerOut.println("Você atacou o oponente, causando " + damage + " de dano.");
            if (target.getHitPoints() <= 0) {
                target.setAlive(FALSE);
                attackerOut.println("Você matou seu oponente");
                targetOut.println("Você foi derrotado");
            }
        }
        attacker.debuff();
    }

    private static void executeBuff(Character attacker, PrintWriter attackerOut, PrintWriter targetOut) {
        attacker.buff();//easter egg se vc me encontrou, vc ganhou 3 premios
        attackerOut.println("Você usou seu turno para se fortalecer");
        targetOut.println("O oponente usou o turno dele para se fortalecer");
    }
}
//⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀         ⠀⠀⢀⣠⣤⣶⣶⣶⠶⣤⣤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⣾⣿⣿⣿⣟⣛⣿⣶⣬⣿⣿⣿⣿⢶⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⣿⠿⠽⣿⣿⣿⡿⠛⠉⠉⠉⠉⠙⠻⣿⡿⠿⣿⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣤⡤⠒⠒⣻⠏⣿⣶⠤⠴⣿⣿⡿⠋⠀⠀⠀⠀⠀⠀⣸⣿⣠⠠⡘⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⠶⣟⣛⣿⣿⣿⣿⡀⢠⣧⣄⣿⣿⣿⣽⣿⡏⠀⠀⠀⠀⠉⠓⠒⠶⣿⣿⣿⡆⣷⢸⡼⣿⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⣾⣿⣿⢿⣿⣿⠿⣿⡿⣿⣿⠿⠉⢸⣿⣻⡟⠙⠆⠹⠃⠐⢶⣶⣤⣤⣄⠲⣤⡟⣆⢻⣿⣿⠂⡇⢿⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⣠⣴⣿⣿⠟⣋⠵⣺⡿⣫⣴⡿⠋⣴⡿⢁⣠⣤⣌⠟⠻⠧⣆⡈⠀⠀⠀⢸⣿⣿⣿⣿⣷⠀⠙⠈⢸⡏⣿⣷⣿⢸⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⣿⣿⣟⣵⣚⣡⣾⠿⠿⠿⠿⠒⠚⠋⠁⠈⠀⠀⠙⣷⣄⠀⠀⠙⡄⠀⠀⠈⠉⠛⠿⠋⠃⠀⠀⠀⢚⣃⣿⣿⣿⣾⡇⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⣀⣀⡀⠀⠛⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣦⠀⠀⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣻⣿⣿⣿⣇⠀⢀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠈⠉⠓⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⡟⣷⡄⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠻⡿⣹⠿⣟⣿⣏⠹⣏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠉⠳⣄⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠀⠀⠀⢀⣀⡤⠿⢬⣿⣍⢳⣄⠀⠀⠀⠀⠠⣤⠋⠀⠀⣠⡇⠀⣿⣿⣿⣷⣌⣳⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠲⢤⣤⣤⣶⣶⣾⣏⣿⡿⠟⠛⠛⠛⠤⠤⡄⠀⠙⣿⣿⣛⣧⣀⠀⠠⡘⠁⠀⢠⣼⠟⠀⠀⢹⠉⢻⣿⣿⣿⣿⣦⠀⠀⠀⠀⠀⠀⠀⠀
//        ⣀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠻⣿⡟⣻⡁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⣿⣿⣿⣿⣿⣦⣴⣖⡋⠉⠀⠀⠀⢀⢿⡄⠈⣿⡝⢿⡿⣿⣧⠀⠀⠀⠀⠀⠀⠀
//        ⠈⠑⠢⢤⣀⡀⠀⠀⠀⠠⠀⠀⡄⠀⠀⠀⠀⠈⠙⣿⣿⣶⣤⣤⣤⣤⣤⡤⠶⣶⣾⣿⣿⡇⠈⠙⠻⠿⠛⠉⠉⠳⢦⣄⠀⠀⠀⣷⡀⢸⣿⡌⣷⠸⣿⣧⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⢠⡿⢹⣶⡶⢤⣦⠀⠃⠀⠀⠀⠀⠀⠀⠈⢿⣿⣮⣉⣉⣉⣉⣛⣻⣋⣉⠉⠹⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠙⢿⣆⠀⢻⣽⣾⣿⡿⠋⢀⡇⢿⡆⠀⠀⠀⠀⠀
//        ⠀⠀⠀⢠⡿⢳⣾⠸⣧⢸⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠈⢿⡟⠉⠉⠉⠉⣉⣉⣉⣹⣷⣶⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣷⣾⣿⣿⢋⣠⣶⢟⣧⣼⠇⠀⠀⠀⠀⠀
//        ⠀⠀⠀⣾⠁⠘⣿⣦⣹⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠈⢷⣶⠿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠛⠛⠛⠛⠛⠒⠲⠦⢤⣀⣙⣿⣿⣿⣿⣿⣿⣡⣾⣿⠏⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠹⡄⠀⠈⠻⣿⡏⠛⢿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⣫⣿⣯⡿⣿⡿⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⢿⣿⣿⣿⣿⣏⣼⠃⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⢘⡇⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣿⣿⣧⡾⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⢿⣿⣿⢻⣦⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⢀⠞⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⣷⣙⣷⡄⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡄⣧⠀⡇⠈⢦⠀⠀⠀⠀⠀⠀⡌⠹⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢾⣛⣿⠇⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢘⣆⢳⡀⠸⡷⠀⠀⡟⠂⠚⣷⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⣥⡄⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠋⠘⢦⣧⠀⢣⠀⠀⡿⠀⢠⠟⠀⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢿⣆⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣿⢋⡠⠤⠤⢼⣄⣸⡄⠀⢧⠀⢸⠀⠀⠘⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢿⣆⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⡴⡿⠁⠀⠀⠀⠀⠀⠈⠻⣇⢀⣸⡷⣯⡀⠀⠀⠉⣳⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⡆⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⣼⠟⠁⠀⠀⠀⠀⠀⢀⡴⢾⣿⣿⣦⡃⠘⠁⠉⠉⠹⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⡀⠀
//        ⠀⠀⠀⠀⠀⠀⣸⠃⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠙⢻⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣇⠀
//        ⠀⠀⠀⠀⠀⢠⠇⠀⠀⠀⠀⠀⠀⠀⠀⠹⣿⣿⣿⣿⣿⡏⣧⠀⠀⠀⠀⠀⠈⡟⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⠀
//        ⠀⠀⠀⠀⠀⣸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣹⣿⡉⠹⣿⠇⠀⠀⠀⠀⠀⠀⡇⠸⡍⠳⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇
//        ⠀⠀⠀⠀⠀⡏⢀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡞⢹⠁⠛⣦⠀⠀⠀⠀⠀⠀⠀⠀⡇⢠⠷⢄⡈⢻⣶⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⡇
//        ⠀⠀⠀⠀⠀⠇⠘⠀⠀⠀⠀⠀⠀⠀⠀⠼⠀⣸⣁⠀⢉⡇⠀⠀⠀⠀⠀⠀⠐⡿⠸⡄⠀⢱⣈⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣷
//        ⠀⠀⠀⠀⠀⢠⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠛⣿⣿⣷⡌⠀⠀⠀⠀⠀⠀⠀⠀⢹⡆⠹⡄⠀⢿⡅⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹
//        ⠀⠀⠀⠀⠀⢸⡀⡆⠀⠀⠀⠀⠀⠀⣿⣿⣷⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⢧⠀⠹⣅⢸⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
//        ⠀⠀⠀⠀⠀⠀⣇⢇⠀⠀⠀⠀⠀⠀⠸⣿⣿⣿⣿⣿⠟⠀⠀⠀⠀⠀⠀⠀⠀⠀⢨⣿⣄⢘⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
//        ⠀⠀⠀⠀⠀⠀⠸⣼⠀⠀⠀⠀⠀⠀⠀⠈⠙⣿⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠈⠻⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡏
//        ⠀⠀⠀⠀⠀⠀⠀⢿⡆⠀⠀⠀⠀⠀⠀⡤⠞⢻⡧⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇
//        ⠀⠀⠀⠀⠀⠀⠀⠸⡖⠀⠀⠀⠀⠀⢼⠁⠀⠸⡇⢹⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠁
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠹⡄⠀⠀⠀⠀⠘⣆⣀⣀⣧⡞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡸⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⡄⠀⠀⠀⠀⣰⡟⢻⣿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡼⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡇⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠱⡄⠀⠀⠰⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⣄⠀⠀⠻⣿⣿⣿⣿⡿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡇⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢦⠀⠀⠀⠁⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡼⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠳⣄⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠃⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⣄⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠏⠀⠀⠀⠀
//    ────────        ▓▓▓▓▓▓▓────────────▒▒▒▒▒▒
//            ──────▓▓▒▒▒▒▒▒▒▓▓────────▒▒░░░░░░▒▒
//            ────▓▓▒▒▒▒▒▒▒▒▒▒▒▓▓────▒▒░░░░░░░░░▒▒▒
//            ───▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▒▒░░░░░░░░░░░░░░▒
//            ──▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░▒
//            ──▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░▒
//            ─▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░░▒
//            ▓▓▒▒▒▒▒▒░░░░░░░░░░░▒▒░░▒▒▒▒▒▒▒▒▒▒▒░░░░░░▒
//            ▓▓▒▒▒▒▒▒▀▀▀▀▀███▄▄▒▒▒░░░▄▄▄██▀▀▀▀▀░░░░░░▒
//            ▓▓▒▒▒▒▒▒▒▄▀████▀███▄▒░▄████▀████▄░░░░░░░▒
//            ▓▓▒▒▒▒▒▒█──▀█████▀─▌▒░▐──▀█████▀─█░░░░░░▒
//            ▓▓▒▒▒▒▒▒▒▀▄▄▄▄▄▄▄▄▀▒▒░░▀▄▄▄▄▄▄▄▄▀░░░░░░░▒
//            ─▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░▒
//            ──▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░▒
//            ───▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▀▀▀░░░░░░░░░░░░░░▒
//            ────▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░▒▒
//            ─────▓▓▒▒▒▒▒▒▒▒▒▒▄▄▄▄▄▄▄▄▄░░░░░░░░▒▒
//            ──────▓▓▒▒▒▒▒▒▒▄▀▀▀▀▀▀▀▀▀▀▀▄░░░░░▒▒
//            ───────▓▓▒▒▒▒▒▀▒▒▒▒▒▒░░░░░░░▀░░░▒▒
//            ────────▓▓▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░▒▒
//            ──────────▓▓▒▒▒▒▒▒▒▒▒░░░░░░░░▒▒
//            ───────────▓▓▒▒▒▒▒▒▒▒░░░░░░░▒▒
//            ─────────────▓▓▒▒▒▒▒▒░░░░░▒▒
//            ───────────────▓▓▒▒▒▒░░░░▒▒
//            ────────────────▓▓▒▒▒░░░▒▒
//            ──────────────────▓▓▒░▒▒
//            ───────────────────▓▒░▒
//            ────────────────────▓▒
//    ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀         ⣠⣤⣤⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣶⣿⠟⠉⠉⠻⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⣾⠿⠉⠀⠀⠀⠀⠀⠹⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⣾
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⣾⡿⠛⠉
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣾⡿⠟⠁⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣴⣿⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⣿⣷⣶⣶⣦⣤⣤⣄⣤⡀⣀⣩⣾⣿⠿⠋⠀⠀⠀⠀⠀⣠
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⡿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣿⣿⣿⣿⠟⠋⠉⠉⠙⣿⣿⣿⣿⣿⣿⠟⠁⠀⠀⠀⠀⠀⢀⡾⠁
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⠁⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⣿⣿⣿⡟⠁⠀⠀⠀⢀⣴⣿⣿⣿⣿⡏⠋⠀⠀⠀⠀⠀⠀⠀⡞⠋⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣼⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⡏⠀⠀⠀⠀⣶⣿⣿⣿⣿⡿⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸⡯⠤⣤
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⣿⣿⣿⡏⠀⠀⠀⠀⣼⣿⣿⣿⣿⡟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡾⠁
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣄⣄⣼⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⡿⠋⠀⠀⠀⠀⣼⣿⣿⣿⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢷⣄
//            ⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠀⠀⠀⠀⣾⣿⣿⣿⡿⠁⠀⠀⠀⠀⠀⠀⣠⣶⣶⣶⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠿⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻
//            ⠀⠀⠀⠀⠀⠀⢀⣾⠿⠛⢿⣿⣷⣄⡀⣿⠋⠀⠈⠀⠀⠀⠀⠀⠀⢀⣾⡏⠀⢹⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⣠⣤⣦⣼⣿⠀⠀⠀⣿⣿⣿⣿⣿⣦⣀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⡿⠀⠀⠀⢀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣤⣾⣿⣿⣿⢷⣄⠀⠀⠀⠀⠀⠀⠀
//            ⠀⣠⣾⡿⠋⠉⠉⠁⠀⠀⠀⠀⠉⢯⡙⠻⣿⣿⣷⣤⡀⠀⠀⠀⠀⢿⣿⣿⣿⣿⡿⠃⢀⡤⠖⠋⣉⣉⣉⣉⠉⠉⠒⠦⣄⠀⠀⠀⢔⡟⡿⠟⠉⣟⣻⣮⣿⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣾⣿⠋⠀⠀⠀⠀⣀⣀⡀⠀⠀⠀⠀⠙⢦⣄⠉⠻⢿⣿⣷⣦⡀⠀⠈⠙⠛⠛⠋⠀⢰⠟⡇⠀⠀⠈⠻⡿⠛⠁⠀⠀⠀⠈⠳⣄⠀⠸⣧⣿⣿⣿⣿⣿⣿⣿⡏⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⠀⣴⠙⣩⣿⣿⣄⠀⠀⠀⠀⡶⢌⡙⠶⣤⡈⠛⠿⣿⣷⣦⣀⠀⠀⠀⠀⡇⠀⢻⣄⠀⠀⣠⢷⠀⠀⠀⠀⠀⡶⠀⠘⡆⠀⠀⠻⣿⣿⣿⣿⣿⣿⡿⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⢸⣇⢸⣿⣿⣿⣿⠀⠀⠀⠀⡇⠀⠈⠛⠦⣝⡳⢤⣈⠛⠻⣿⣷⣦⣀⠀⠀⠀⠀⠈⠙⠋⠁⠀⠛⠦⢤⡤⠞⠃⠀⠀⢳⠀⠀⠀⠈⠋⠙⠛⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⠈⢿⣄⣿⣿⣿⠏⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠙⠳⢬⣛⠦⠀⠙⢻⣿⣷⣦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⠀⠀⠉⠛⠋⠁⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠈⣿⠉⢻⣿⣷⣦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡼⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⠀⠀⠀⣠⣄⠀⠀⢰⠶⠒⠒⢧⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⠀⢸⡇⢸⡟⢿⣷⣦⣴⣶⣶⣶⣶⣤⣔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⣤⠀⠀⠿⠿⠁⢀⡿⠀⠀⠀⡄⠈⠙⡷⢦⣄⡀⠀⠀⠀⠀⠀⠀⠀⣿⠀⢸⡇⢸⡇⠀⣿⠙⣿⣿⣉⠉⠙⠿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣿⡇⠀⠀⠙⠷⢤⣀⣠⠴⠛⠁⠀⠀⠀⠇⠀⠀⡇⢸⡏⢹⡷⢦⣄⡀⠀⠀⠀⣿⡀⢸⡇⢸⡇⠀⡟⠀⢸⠀⢹⡷⢦⣄⣘⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⣿⣿⠢⣤⡀⠀⠀⠀⠀⠀⠀⣠⠾⣿⣿⡷⣤⣀⡇⠸⡇⢸⡇⢸⠉⠙⠳⢦⣄⡻⢿⣾⣧⣸⣧⠀⡇⠀⢹⡀⢸⡇⢤⣈⠙⠻⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⢹⣿⣷⣌⡉⠛⠲⢶⣶⠖⠛⠛⢶⣄⡉⠛⠿⣽⣿⣶⣧⣸⡇⢸⠀⠀⠀⠀⠈⠙⠲⢮⣝⠻⣿⣷⣷⣄⣼⠀⢸⡇⠀⠈⠁⠀⢸⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠈⠙⠻⢿⣷⣶⣤⣉⡻⢶⣄⣀⠈⠙⠳⢦⣈⡉⠻⢿⣿⣷⣾⣦⡀⠀⠀⠀⠀⠀⠀⠈⠙⠲⢭⣛⠿⣿⣷⣼⡇⠀⠀⠀⠀⠈⣿⡇⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠀⠀⠀⠀⠀⣠
//            ⠀⠀⠀⠀⠀⠈⠙⠻⢿⣿⣷⣶⣽⣻⡦⠀⠀⠈⠙⠷⣦⣌⡙⠻⢿⣟⣷⣤⣀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠳⢯⣻⡇⠀⠀⠀⠀⠀⢸⣿⠀⣀⠀⠀⠀⠀⠈⠳⣄⡀⠀⠀⢀⣏⡟⠛
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⠻⢿⣿⣿⣿⣶⣤⣤⣤⣀⣈⠛⠷⣤⣈⡛⠷⢽⡻⢶⣄⣀⠀⠀⠀⠀⠀⠀⠀⠈⠛⠳⢤⣀⠀⠀⢸⣿⡀⠈⠻⢭⣲⣴⣴⠗⠋⠛⠶⠿⠿⠃⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⣿⡿⠛⠉⠙⠛⠛⠻⢷⣦⣄⣩⣿⠶⠖⠛⠛⠛⠛⠛⠛⠿⢷⣶⣦⣄⠀⠀⠀⠀⠉⢻⣶⣿⣿⠇⠀⠀⠀⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⠁⠀⠀⠀⠀⠀⠀⠀⣿⣿⠋⠀⠀⠀⠀⢀⣠⡶⠂⠀⠀⠀⠈⠙⠿⣿⣦⡄⠀⠀⣸⣿⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⣴⠏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⢿⣶⣄⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⢸⣧⠀⠀⢀⡾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠙⢿⣿⣇⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡿⠿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⡿⠦⠠⠋⠀⠀⠀⠀⠀⢀⡶⠂⠀⠀⠀⠀⠀⠀⠧⠤⠄⠙⡿⠿⠦⠤⠤⠤⠤⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀