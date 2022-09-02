import domain.Evento;
import domain.EventoRoteamento;
import domain.Fila;
import domain.FilaRoteamento;
import domain.FilaSimples;
import domain.FilaTandem;
import domain.Intervalo;
import domain.TipoEvento;
import domain.Transicao;
import domain.random.Generator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;
import static domain.TipoEvento.TRANSFERENCIA;

public class Interface {

    private final int MAX_NUMBERS = 100000;

    private final Scanner input;
    private final Generator generator;

    Interface() {
        this.input = new Scanner(System.in);
        this.generator = new Generator(MAX_NUMBERS);
    }

    public void run() {
        boolean shouldRun = true;
        do {
            System.out.println("Digite o tipo de fila que gostaria de instanciar:");
            System.out.println("1-Normal");
            System.out.println("2-Tandem");
            System.out.println("3-Probabilidade de Roteamento");
            int tipoFila = input.nextInt();

            Fila fila = createFila(tipoFila);

            Simulation sim = new Simulation(fila);
            sim.simulate(new EventoRoteamento(CHEGADA, BigDecimal.valueOf(1), -1, 0));

            System.out.println("Gostaria de simular outra fila(Digite 1 para sim ou qualquer outra tecla para fechar): ");
            shouldRun = input.nextInt() == 1;
            generator.reset();

//            simulateRouting();
//
//            System.out.println("Gostaria de simular outra fila(Digite 1 para sim ou qualquer outra tecla para fechar): ");
//            shouldRun = input.nextInt() == 1;
//            generator.reset();
        } while (shouldRun);
    }

    private void simulateRouting() {
        final List<FilaSimples> filas = Arrays.asList(
                new FilaSimples(new Intervalo(1, 4), new Intervalo(1, 1.5), 1, -1, generator),
                new FilaSimples(new Intervalo(0, 0), new Intervalo(5, 10), 3, 5, generator),
                new FilaSimples(new Intervalo(0, 0), new Intervalo(10, 20), 2, 8, generator)
        );


        final List<Transicao> transicoes = Arrays.asList(
                new Transicao(-1, 0, 1, CHEGADA),
                new Transicao(0, 1, 0.8, TRANSFERENCIA),
                new Transicao(0, 2, 0.2, TRANSFERENCIA),
                new Transicao(1, 2, 0.5, TRANSFERENCIA),
                new Transicao(1, -1, 0.2, SAIDA),
                new Transicao(1, 0, 0.3, TRANSFERENCIA),
                new Transicao(2, 1, 0.7, TRANSFERENCIA),
                new Transicao(2, -1, 0.3, SAIDA)
        );


        Fila fila = new FilaRoteamento(filas, generator, transicoes);
        new Simulation(fila).simulate(new EventoRoteamento(CHEGADA, BigDecimal.valueOf(1), -1, 0));
    }

    private Fila createFila(int tipoFila) {
        if (tipoFila == 1) {
            return createSimpleQueue();
        }
        if (tipoFila == 2) {
            return createTandem();
        }
        return createRoutingQueue();
    }

    private Fila createRoutingQueue() {
        boolean shouldRun;
        List<FilaSimples> filas = new ArrayList<>();
        List<Transicao> transicoes = new ArrayList<>();
        int indiceFilaAtual = 0;
        do {
            FilaSimples filaSimples = createSimpleQueue();
            checkIncoming(transicoes, indiceFilaAtual);

            checkOutcoming(transicoes, indiceFilaAtual);
            createTransicoes(indiceFilaAtual, transicoes);

            filas.add(filaSimples);
            indiceFilaAtual++;
            System.out.println("Deseja adicionar mais uma fila?");
            System.out.println("1 - Sim");
            System.out.println("2 -  Não");
            shouldRun = input.nextInt() == 1;
        } while (shouldRun);

        return new FilaRoteamento(filas, generator, transicoes);
    }

    private void checkIncoming(List<Transicao> transicoes, int indiceFilaAtual) {
        System.out.println("Essa fila tem chegada do Exterior? ");
        System.out.println("1 - Sim");
        System.out.println("2 -  Não");
        if (input.nextInt() == 1) {
            transicoes.add(new Transicao(-1, indiceFilaAtual, 1, TipoEvento.CHEGADA));
        }
    }

    private void checkOutcoming(List<Transicao> transicoes, int indiceFilaAtual) {
        System.out.println("Essa fila tem saída para o exterior? ");
        System.out.println("1 - Sim");
        System.out.println("2 -  Não");
        if (input.nextInt() == 1) {
            System.out.println("Digite a probabilidade da fila sair para o exterior: ");
            transicoes.add(new Transicao(indiceFilaAtual, -1, input.nextDouble(), TipoEvento.SAIDA));
        }
    }

    private void createTransicoes(int indiceFilaAtual, List<Transicao> transicoes) {
        System.out.println("Essa fila possui outras transições?");
        System.out.println("1 - Sim");
        System.out.println("2 -  Não");
        boolean shouldRun = input.nextInt() == 1;
        while (shouldRun) {
            System.out.println("Digite o índice (começando em 0) da fila para onde a fila atual terá uma transferencia:");
            int indiceDestino = input.nextInt();

            System.out.println("Digite a probabilidade da transição ocorrer:");
            double probabilidade = input.nextDouble();

            transicoes.add(new Transicao(indiceFilaAtual, indiceDestino, probabilidade, TipoEvento.TRANSFERENCIA));

            System.out.println("Deseja adicionar outras transições?");
            System.out.println("1 - Sim");
            System.out.println("2 -  Não");
            shouldRun = input.nextInt() == 1;
        }
    }

    private Fila createTandem() {
        boolean shouldRun;
        List<FilaSimples> filas = new ArrayList<>();
        do {
            FilaSimples filaSimples = createSimpleQueue();

            filas.add(filaSimples);

            System.out.println("Deseja adicionar mais uma fila ao tandem?");
            shouldRun = input.nextInt() == 1;
        } while (shouldRun);

        return new FilaTandem(filas, generator);
    }

    private FilaSimples createSimpleQueue() {
        System.out.println("Digite o intervalo inferior do tempo para a chegada de clientes na fila: ");
        double aux1 = input.nextDouble();
        System.out.println("Digite o intervalo superior do tempo para a chegada de clientes na fila: ");
        double aux2 = input.nextDouble();
        Intervalo taxaChegada = new Intervalo(aux1, aux2);

        System.out.println("Digite o intervalo inferior do tempo para o atendimento de um cliente na fila: ");
        aux1 = input.nextDouble();
        System.out.println("Digite o intervalo superior do tempo para o atendimento de um cliente na fila: ");
        aux2 = input.nextDouble();
        Intervalo taxaAtendimento = new Intervalo(aux1, aux2);

        System.out.println("Digite o número de servidores: ");
        int servidores = input.nextInt();

        System.out.println("Digite a capacidade da fila: ");
        System.out.println("(-1 para capacidade infinita)");
        int capacidade = input.nextInt();
        return new FilaSimples(taxaChegada, taxaAtendimento, servidores, capacidade, generator);
    }

    private static void simulateTandem(Generator generator) {
        FilaSimples fila1 = new FilaSimples(new Intervalo(2, 3), new Intervalo(2, 5), 2, 3, generator);
        FilaSimples fila2 = new FilaSimples(new Intervalo(0, 0), new Intervalo(3, 5), 1, 3, generator);

        Simulation sim = new Simulation(new FilaTandem(Arrays.asList(fila1, fila2), generator));
        sim.simulate(new Evento(CHEGADA, BigDecimal.valueOf(2.5)));
    }


}   
