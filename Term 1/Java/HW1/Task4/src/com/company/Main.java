package com.company;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int k = scanner.nextInt();

            int last = 0;
            for (int i = 1; i <= n; i++) {
                last = (last + k) % i;
            }
            last++;

            System.out.println(last);
        }
    }
}
