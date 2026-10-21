public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("O jantar dos filósofos");

        //Cria a única mesa que será compartilhada por todos
        Table table = new Table();
        
        //Guarda as referências ás cinco threads
        Thread[] threads = new Thread[5];

        
        for (int i = 0; i < 5; i++) {
            Philosopher philosopher = new Philosopher(i, table);
            threads[i] = philosopher.spawn();
        }

        
        for (int i = 0; i < 5; i++) {
            threads[i].join();
        }
    }
}
