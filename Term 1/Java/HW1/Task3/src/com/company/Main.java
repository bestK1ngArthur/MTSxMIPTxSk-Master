package com.company;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int[] arrayA = inputArray(scanner, n);

            int m = scanner.nextInt();
            int[] arrayB = inputArray(scanner, m);

            int k = scanner.nextInt();

            int pairsCount = 0;

            int j = arrayB.length - 1;
            int i = 0;

            while ((j >= 0) && (i < arrayA.length)) {
                int sum = arrayB[j] + arrayA[i];

                if (sum > k) {
                    j--;
                } else if (sum < k) {
                    i++;
                } else {
                    pairsCount++;
                    j--;
                }
            }

            System.out.println(pairsCount);
        }
    }

    private static int[] inputArray(Scanner input, int n) {
        int[] array = new int[n];

        for (int i = 0; i < array.length; i++) {
            array[i] = input.nextInt();
        }

        return array;
    }
}
