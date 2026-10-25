import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe que implementa a existência de um filósofo que deve pensar e comer
 * 
 */
public class Philosopher implements Runnable{

    private Integer philosopher_id;
    private final Table table;

    Philosopher(int id, Table table){
        this.philosopher_id = id;
        this.table = table;
    }

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

    private void think() throws InterruptedException {
        System.out.println("O Filósofo " + philosopher_id + " está pensando...");
        Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
    }

    private void eat() throws InterruptedException {
        int left = philosopher_id;
        int right = (philosopher_id + 1) % table.forks.length;

        int first = Math.min(left, right);
        int second = Math.max(left, right);

        System.out.println("Filósofo " + philosopher_id + " tentando adquirir o garfo " + first);
        table.forks[first].lock();

        try {
            System.out.println("Filósofo " + philosopher_id + " adquiriu o garfo " + first + " e tentará adquirir o " + second);
            
            table.forks[second].lock();
            try {
                System.out.println("Filósofo " + philosopher_id + " adquiriu os dois garfos e está comendo");
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
