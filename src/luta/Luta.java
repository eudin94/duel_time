package luta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Luta {
    private TesteChar campeao;
    private Random r = new Random();
    Scanner in = new Scanner(System.in);
    List<TesteChar> campeoes = new ArrayList<>();

    public void organizarLuta() {
        while (campeoes.size() != 2) {
            System.out.println("""
                    1-Guerreiro
                    2-Mago
                    3-Gatuno
                    4-Bardo
                    """);
            System.out.println("Escolha o campeao");
            if (in.nextInt() == 1) {
                System.out.println("Vc escolheu guerreiro");

                campeao = new TesteChar();
                campeao.setNome("guerreiro");
                campeao.setDano(15);
                campeao.setVida(200);

                campeoes.add(campeao);
            } else if (in.nextInt() == 2) {
                System.out.println("Vc escolheu mago");

                campeao = new TesteChar();
                campeao.setNome("mago");
                campeao.setDano(20);
                campeao.setVida(100);

                campeoes.add(campeao);
            }
        }
        System.out.println("Luta organizada");
    }

    public void lutar() {
        System.out.println(campeoes.get(0).getNome());
        r.nextInt(campeoes.get(0).getDano());

        System.out.printf("%s deu %d de dano",campeoes.get(0).getNome(), campeoes.get(0).getDano());


    }

}
