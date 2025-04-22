package org.polina.practice;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 20; i++) {
                    queue.enqueue(i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 20; i++) {
                    Integer item = queue.dequeue();
                    System.out.println("Извлеченный элемент: " + item);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread monitor = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (queue.size() > 0) {
                try {
                    Thread.sleep(5000);
                    System.out.println("Текущий размер очереди: " + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        producer.start();
        consumer.start();
        monitor.start();
    }
}