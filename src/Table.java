import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * Classe que implementa a mesa de jantar com todos os garfos
 */
public class Table {

    //Cada filósofo terá um garfo que permitirá o filósofo ter acesso ao recurso quando for sua vez de comer
    //Permite que uma thread adquira acesso exclusivo a um recurso
    final ReentrantLock[] forks = new ReentrantLock[5];

    /**
     * O recurso compartilhado que cada filósofo disputará
     */
    Table(){
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }
    }
}
