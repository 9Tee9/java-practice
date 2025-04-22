package org.polina.practice;

import java.util.Random;

public class ComplexTask {
    private final int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public int execute() {
        System.out.println("Task " + taskId + " is starting...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Random random = new Random();
        return random.nextInt(100);
    }
}
