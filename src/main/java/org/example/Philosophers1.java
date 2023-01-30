package org.example;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosophers1 {

    static class Fork {
        private final Integer id;

        public Fork(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Fork{" + "id='" + id + '\'' + '}';
        }
    }

    static class Philosopher {
        private final String name;
        private final Fork fork1;
        private final Fork fork2;
        private final Lock lock1;
        private final Lock lock2;


        private static final Random random = new Random();

        public Philosopher(String name, Fork fork1, Fork fork2, Lock lock1, Lock lock2) {
            this.name = name;
            this.fork1 = fork1;
            this.fork2 = fork2;
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        @Override
        public String toString() {
            return "Philosopher{" + "name='" + name + '\'' + '}';
        }

        public void atTheTable() {
            while (true) {
                snack();
                think();
            }
        }

        public void snack() {
            if (lock1.tryLock() && lock2.tryLock()) {
                lock1.lock();
                lock2.lock();
                eatSpaghetti(fork1);
                eatSpaghetti(fork2);
                System.out.println(this + " eating Spaghetti");
                System.out.println(this + " put " + fork2);
                System.out.println(this + " put " + fork1);
                lock1.unlock();
                lock2.unlock();
            }
        }

        public void think() {
            try {
                System.err.println(this + " thinks about important things... ");
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void eatSpaghetti(Fork fork) {
            try {
                System.out.println(this + " take " + fork);
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Fork fork1 = new Fork(1);
        Fork fork2 = new Fork(2);
        Fork fork3 = new Fork(3);
        Fork fork4 = new Fork(4);
        Fork fork5 = new Fork(5);

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Lock lock3 = new ReentrantLock();
        Lock lock4 = new ReentrantLock();
        Lock lock5 = new ReentrantLock();


        Philosopher philosopher1 = new Philosopher("Plato", fork1, fork2, lock1, lock2);
        Philosopher philosopher2 = new Philosopher("Socrates", fork2, fork3, lock2, lock3);
        Philosopher philosopher3 = new Philosopher("Aristotle", fork3, fork4, lock3, lock4);
        Philosopher philosopher4 = new Philosopher("Pythagoras", fork4, fork5, lock4, lock5);
        Philosopher philosopher5 = new Philosopher("Diogenes", fork5, fork1, lock5, lock1);

        new Thread(philosopher1::atTheTable).start();
        new Thread(philosopher2::atTheTable).start();
        new Thread(philosopher3::atTheTable).start();
        new Thread(philosopher4::atTheTable).start();
        new Thread(philosopher5::atTheTable).start();
    }
}
