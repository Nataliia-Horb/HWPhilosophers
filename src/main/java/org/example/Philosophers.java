package org.example;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Задача: см. класс Philosophers в репозитории
 * <p>
 * Трое безмолвных философов сидят вокруг круглого стола, перед каждым философом стоит тарелка спагетти.
 * Вилки лежат на столе между каждой парой ближайших философов.
 * Каждый философ может либо есть, либо размышлять. Приём пищи не ограничен количеством оставшихся спагетти — подразумевается бесконечный запас.
 * Тем не менее, философ может есть только тогда, когда держит две вилки — взятую справа и слева.
 * Каждый философ может взять ближайшую вилку (если она доступна) или положить — если он уже держит её.
 * Взятие каждой вилки и возвращение её на стол являются раздельными действиями, которые должны выполняться одно за другим.
 * Вопрос задачи заключается в том, чтобы разработать модель поведения (параллельный алгоритм),
 * при котором ни один из философов не будет голодать, то есть будет вечно чередовать приём пищи и размышления.
 * <p>
 * Реализация вышеприведенной задачи подвержена следующим проблемам:
 * Deadlock — состояние системы, в котором каждый философ взял вилку слева и ждёт, когда вилка справа освободится
 * Starvation (e.g. livelock) — состояние системы меняется, но она не совершает никакой полезной работы
 * Unfairness — состояние системы, в котором некоторые философы получают доступ к своим вилкам существенно чаще,
 * чем остальные философы (такая проблема наблюдается в асимметричных решениях)
 * <p>
 * В классе Philosophers в репозитории предварительный вариант решения данной задачи.
 * Через какое-то время при таком решении возникает Deadlock.
 * <p>
 * разработать модель поведения, при которой Deadlock будет невозможен
 * <p>
 * обобщить задачу до пяти философов
 */

public class Philosophers {

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

        Semaphore semaphore;

        private static final Random random = new Random();

        public Philosopher(String name, Fork fork1, Fork fork2, Semaphore semaphore) {
            this.name = name;
            this.fork1 = fork1;
            this.fork2 = fork2;
            this.semaphore = semaphore;
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
            try {
                semaphore.acquire();
                synchronized (fork1) {
                    synchronized (fork2) {
                        eatSpaghetti(fork1);
                        eatSpaghetti(fork2);
                        System.out.println(this + " eating Spaghetti");
                        System.out.println(this + " put " + fork2);
                    }
                    System.out.println(this + " put " + fork1);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            semaphore.release();
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

        Semaphore semaphore = new Semaphore(4, true);

        Philosopher philosopher1 = new Philosopher("Plato", fork1, fork2, semaphore);
        Philosopher philosopher2 = new Philosopher("Socrates", fork2, fork3, semaphore);
        Philosopher philosopher3 = new Philosopher("Aristotle", fork3, fork4, semaphore);
        Philosopher philosopher4 = new Philosopher("Pythagoras", fork4, fork5, semaphore);
        Philosopher philosopher5 = new Philosopher("Diogenes", fork5, fork1, semaphore);

        new Thread(philosopher1::atTheTable).start();
        new Thread(philosopher2::atTheTable).start();
        new Thread(philosopher3::atTheTable).start();
        new Thread(philosopher4::atTheTable).start();
        new Thread(philosopher5::atTheTable).start();
    }
}
