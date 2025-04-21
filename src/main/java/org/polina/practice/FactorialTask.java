package org.polina.practice;

import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<Long> {
    private final int start;
    private final int end;

    private static final int THRESHOLD = 2;

    public FactorialTask(int n) {
        this.start = 1;
        this.end = n;
    }

    private FactorialTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            return calculateFactorial();
        } else {
            int mid = (start + end) / 2;
            FactorialTask leftTask = new FactorialTask(start, mid);
            FactorialTask rightTask = new FactorialTask(mid + 1, end);
            leftTask.fork();
            long rightResult = rightTask.compute();
            long leftResult = leftTask.join();

            return leftResult * rightResult;
        }
    }

    private long calculateFactorial() {
        long result = 1;
        for (int i = start; i <= end; i++) {
            result *= i;
        }
        return result;
    }
}
