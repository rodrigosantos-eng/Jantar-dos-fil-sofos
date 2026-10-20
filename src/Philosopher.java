import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe que implementa a existência de um filósofo que deve pensar e comer
 * 
 */
public class Philosopher implements Runnable{

    /** ID do filósofo */
    private Integer philosopher_id;
    private final Table table;

    Philosopher(int id, Table table){
        this.philosopher_id = id;
        this.table = table;
    }

    /**
     * Inicia a execução paralela de um filósofo que compartilha a mesma mesa com outros filósofos
     * 
     */
    Thread spawn(){
        Thread t = new Thread(this, "Philosopher-" + philosopher_id);
        t.start();
        return t;
    }
    
    @Override 
    public void run(){
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * O tempo aleatório em que cada filósofo passa pensando
     *  
     */
    private void think() throws InterruptedException {
        System.out.println("O Filósofo " + philosopher_id + " está pensando...");
        Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
    }

    /**
     * O processo de alimentação de cada instância de filósofo
     * 
     * 1° Um filósofo tenta obter o bloqueio de ambos os garfos que precisa para comer
     * Se um garfo tiver sido adquirido por outro filósofo a thread aguardará até que o recurso/garfo seja liberado
     * Quando ambos os garfos são adquiridos pela thread em execução, chega
     * o momento de o Filósofo comer. Ao final desse processo, ambos os
     * garfos são liberados.
     * @throws InterruptedException
     */
    private void eat() throws InterruptedException {
        int left = philosopher_id;
        int right = (philosopher_id + 1) % table.forks.length;

        System.out.println("O Filósofo " + philosopher_id + " está pegando os garfos...");

        int first = Math.min(left, right);
        int second = Math.max(left, right);

        table.forks[first].lock();
        try {
            table.forks[second].lock();
            try {
                System.out.println("O Filósofo " + philosopher_id + " está comendo...");
                Thread.sleep(ThreadLocalRandom.current().nextInt(10000));

                System.out.println("O Filósofo " + philosopher_id + " está largando os garfos...");
            } finally {
                table.forks[second].unlock();
            }
        } finally {
            table.forks[first].unlock();
        }
    }
}
