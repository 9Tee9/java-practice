package org.polina.practice;

public class Main {
    public static void main(String[] args) {
        CustomStringBuilder builder2 = new CustomStringBuilder();
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            String s = "lol";
            builder2.append(s).append("kat");
            builder2.undo();
        }
        System.out.println(builder2);
        System.out.println(System.currentTimeMillis() - start2);

        }
    }
