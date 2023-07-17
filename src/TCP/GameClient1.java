package TCP;

import java.io.*;
import java.net.Socket;

import static java.util.Objects.nonNull;

public class GameClient1 {

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Endereço IP do servidor
        int serverPort = 1234; // Porta do servidor

        try {
            // Conexão com o servidor
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Receber mensagem de boas-vindas do servidor
            String message = in.readLine();
            System.out.println(message);

            // Ler a escolha do personagem do jogador
            message = in.readLine();
            System.out.println(message);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String chosenCharacter = userInput.readLine();

            // Enviar a escolha do personagem para o servidor
            out.write(chosenCharacter + "\n");
            out.flush();

            // Receber mensagem de confirmação do servidor
            message = in.readLine();
            System.out.println(message);

            // Loop principal do jogo
            while (true) {

                // Receber mensagem de ação do servidor
                message = in.readLine();
                System.out.println(message);

                message = in.readLine();
                System.out.println(message);

                // Receber ação do jogador
                String action = userInput.readLine();

                // Enviar ação para o servidor
                out.write(action + "\n");
                out.flush();

                // Receber resposta do servidor
                message = in.readLine();
                System.out.println(message);

                // Verificar condição de saída do loop
                if (message.contains("Você foi derrotado") || message.contains("Você matou seu oponente")) {
                    break;
                }

                message = in.readLine();
                if (nonNull(message)) System.out.println(message);

            }

            // Fechar conexão
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}