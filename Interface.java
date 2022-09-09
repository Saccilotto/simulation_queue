import domain.Intervalo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interface {

    Interface() {}

    public void run() {
        boolean quit = false;
        Scanner input = new Scanner(System.in);
        Intervalo chegada, atendimento;
        int aux1, aux2;
        int servidores, filaTam, repeat;
        List<FilaSimples> simple = new ArrayList<FilaSimples>(); 
        int count = 0;
        do {
            System.out.println("Digite o intervalo inferior do tempo para a chegada de clientes na fila: ");
            aux1 = input.nextInt();
            System.out.println("Digite o intervalo superior do tempo para a chegada de clientes na fila: ");
            aux2 = input.nextInt();
            chegada = new Intervalo(aux1, aux2);

            System.out.println("Digite o intervalo inferior do tempo para o atendimento de um cliente na fila: ");
            aux1 = input.nextInt();
            System.out.println("Digite o intervalo superior do tempo para o atendimento de um cliente na fila: ");
            aux2 = input.nextInt();
            atendimento = new Intervalo(aux1, aux2);

            System.out.println("Digite o número de servidores: ");
            servidores = input.nextInt();

            System.out.println("Digite a capacidade da fila: ");
            filaTam = input.nextInt();

            simple.add(new FilaSimples(chegada, atendimento, servidores, filaTam));

            System.out.println("Gostaria de adicionar outra fila à simulação(Digite 1 para sim ou qualquer outra tecla para fechar): ");
            repeat = input.nextInt();

            if(repeat == 1) {
                quit = true;
            } else {
                quit = false;
            }
            count++;
        } while(quit);

        FilaTandem filaFinal = new FilaTandem(simple);
        Simulation sim = new Simulation(chegada, atendimento, filaFinal);
        sim.simulate();
    }
}   
