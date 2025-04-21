package org.polina.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomStringBuilder {
    private char[] value;
    private int count;
    private final List<Snapshot> history = new ArrayList<>();
    private final int maxHistorySize = 100;

    public CustomStringBuilder(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Емкость не может быть отрицательной");
        }
        this.value = new char[capacity];
        this.count = 0;
    }

    public CustomStringBuilder() {
        this(16);
    }

    public CustomStringBuilder append(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        saveSnapshot();
        ensureCapacity(count + str.length());
        str.getChars(0, str.length(), value, count);
        count += str.length();
        return this;
    }

    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > value.length) {
            int newCapacity = value.length * 2 + 2;
            if (newCapacity < minimumCapacity) {
                newCapacity = minimumCapacity;
            }
            value = Arrays.copyOf(value, newCapacity);
        }
    }

    public void undo() {
        if (history.isEmpty()) {
            throw new IllegalStateException("Нет состояний для отмены");
        }
        Snapshot snapshot = history.removeLast();
        this.value = snapshot.value();
        this.count = snapshot.count();
    }

    private void saveSnapshot() {
        if (history.size() >= maxHistorySize) {
            history.remove(0);
        }
        history.add(new Snapshot(Arrays.copyOf(value, count), count));
    }

    @Override
    public String toString() {
        return new String(value, 0, count);
    }

    private record Snapshot(char[] value, int count) {
    }
}

