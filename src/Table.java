import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * Classe que implementa a mesa de jantar com todos os garfos
 */
public class Table {

    final ReentrantLock[] forks = new ReentrantLock[5];

    Table(){
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }
    }
}
