package org.polina.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ComplexTaskExecutor {
    private final int numberOfTasks;
    private final AtomicInteger taskIdGenerator = new AtomicInteger(1);

    public ComplexTaskExecutor(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    public void executeTasks(int numberOfThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Integer> results = Collections.synchronizedList(new ArrayList<>());
        CyclicBarrier barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed. Combining results...");
            int totalResult = results.stream().mapToInt(Integer::intValue).sum();
            System.out.println("Total result: " + totalResult);
        });

        for (int i = 1; i <= numberOfTasks; i++) {
            executor.submit(() -> {
                int taskId = taskIdGenerator.getAndIncrement();
                ComplexTask task = new ComplexTask(taskId);
                int result = task.execute();
                System.out.println("Task " + taskId + " result: " + result);
                results.add(result);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
