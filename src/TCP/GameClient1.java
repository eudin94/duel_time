package TCP;

import java.io.*;
import java.net.Socket;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class GameClient1 {

    private static Boolean GAME_OVER = FALSE;

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Endereço IP do servidor
        int serverPort = 1234; // Porta do servidor

        try {
            // Conexão com o servidor
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Thread para ler as mensagens do servidor
            Thread messageReaderThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.contains("matou") || message.contains("derrotado")) GAME_OVER = TRUE;
                        System.out.println(message);
                    }
                } catch (IOException ignore) {
                }
            });

            // Iniciar a thread para ler as mensagens do servidor
            messageReaderThread.start();

            // Loop principal do jogo
            do {
                // Verificar se há entrada disponível
                if (System.in.available() > 0) {
                    // Receber ação do jogador
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                    String action = userInput.readLine();

                    // Enviar ação para o servidor
                    out.write(action + "\n");
                    out.flush();
                }
            } while (!GAME_OVER);

            // Fechar conexão
            socket.close();

            // Aguardar a conclusão da thread de leitura das mensagens do servidor
            messageReaderThread.join();

        } catch (IOException | InterruptedException ignore) {
        }

        System.exit(0);
    }
}

//            ────────▓▓▓▓▓▓▓────────────▒▒▒▒▒▒
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