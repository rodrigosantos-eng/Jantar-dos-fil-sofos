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
     * O tempo em que cada filósofo passa pensando
     * A thread em execução é bloqueada durante 5 segundos para que o filósofo possa pensar
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

        int first = Math.min(left, right);
        int second = Math.max(left, right);

        System.out.println("Filósofo " + philosopher_id + " tentando adquirir o garfo " + first);
        /**
         * Quando falamos em "pegar garfo" estamos nos referindo ao momento em que a thread está tendo acesso ao recurso disponível
         * 
         * Pega o primeiro garfo(recurso) disponpivel e garante bloqueando 
         * para que nemhum outro filósofo possa pegar o garfo ou seja o filósofo garante o primeiro garfo 
         * sem deixar que outros filósofos tenham acesso a esse garfo
         */
        table.forks[first].lock();

        /**
         *  **Tentativa de pegar o os garfos **
         * Ao tentar acessar um recuros (garfo) disponível por exemplo o garfo 1 está disponível mas a thread (filósofo) não conseguir acessar o recurso (garfo) 2 que não está disponível acontece o comportamento de posse e espera
         * como o garfo 1 está dispnível e a thread (filósofo) conseguiu pegar-lo o garfo 1 entra em lock - está bloqueado e garantido pelo filósofo que o pegou sendo assim espera a disponibilidade do garfo 2.
         * Esse comportamento ajuda a evitar impasse (deadlock).
         */
        try {
            System.out.println("Filósofo " + philosopher_id + " adquiriu o garfo " + first + " e tentará adquirir o " + second);
            //Pega o segundo garfo na mesa disponível e garante bloqueando para que outros filósofos não tenha acesso ao garfo
            table.forks[second].lock();
            try {
                System.out.println("Filósofo " + philosopher_id + " adquiriu os dois garfos e está comendo");
                //Com os dois garfos em mãos o filósofo pode comer ou seja acessar seu recurso
                //depois de ter comido o filósofo para de comer e espera 10 segundos
                Thread.sleep(ThreadLocalRandom.current().nextInt(10000));

                System.out.println("O Filósofo " + philosopher_id + " está largando os garfos...");
            } finally {

                /**
                 * Após terminar de comer o filósofo larga um garfo por vez por meio do método unlock que faz o desbloqueio do recurso, 
                 * deixando-o livre para que quando a thread atual (filósofo) terminar de fazer seu trabalho (comer) largar o garfo(recurso) deixando-o esse recurso disponível 
                 * para que outras threads possam ter acesso ao recruso 
                 */

                //destranca e deixa disponível o recurso(garfo) - segundo garfo - disponível para que outra (thread) filósofo possa ter acesso
                table.forks[second].unlock();
            }
        } finally {
            //destranca e deixa disponível o recurso(garfo) - primeiro garfo - para que outra (thread) filósofo possa ter acesso
            table.forks[first].unlock();
        }
    }
}
