import java.util.Scanner;

public class Interface {

    Interface() {}

    public void run() {
        boolean quit = false;
        Scanner input = new Scanner(System.in);
        double lambda, atendimento;
        int servidores, filaTam, repeat;
        do {
            System.out.println("Digite o intervalo de tempo para a chegada de clientes na fila (Lambda): ");
            lambda = input.nextDouble();
            System.out.println("Digite o intervalo de tempo para o atendimento de um cliente na fila: ");
            atendimento = input.nextDouble();
            System.out.println("Digite o número de servidores: ");
            servidores = input.nextInt();
            System.out.println("Digite a capacidade da fila: ");
            filaTam = input.nextInt();
            System.out.println("Gostaria de simular outra fila(Digite 1 para sim ou qualquer outra tecla para fechar): ");
            repeat = input.nextInt();

            Simulation sim = new Simulation(lambda, atendimento, servidores, filaTam);

            if(repeat == 1) {
                quit = true;
            } else {
                quit = false;
            }
        } while(quit);
    }
}   
