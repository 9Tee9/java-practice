package org.polina.practice;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue <T>{
    private final Queue<T> queue;
    private final int maxSize;

    public BlockingQueue(int maxSize) {
        this.queue = new LinkedList<>();
        this.maxSize = maxSize;
    }
    public synchronized void enqueue(T item) throws InterruptedException {
        while (size() == maxSize) {
            wait();
        }
        queue.add(item);
        System.out.println("Элемент добавлен в очередь: " + item);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.poll();
        System.out.println("Элемент извлечен из очереди.");
        notifyAll();
        return item;
    }

    public synchronized int size() {
        return queue.size();
    }
}
