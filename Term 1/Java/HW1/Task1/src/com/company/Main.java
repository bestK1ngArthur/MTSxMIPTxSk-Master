package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        if (n == 0) {
            System.out.println("0 0");
        }

        int arrayA[] = inputArray(scanner, n);
        int arrayB[] = inputArray(scanner, n);

        scanner.close();

        int maxI = 0;
        int maxJ = 0;

        int currentMaxI = 0;

        for (int i = 0; i < arrayA.length; i++) {
            if (arrayA[i] > arrayA[currentMaxI]) {
                currentMaxI = i;
            }

            if ((arrayA[currentMaxI] + arrayB[i]) > (arrayA[maxI] + arrayB[maxJ])) {
                maxI = currentMaxI;
                maxJ = i;
            }
        }

        System.out.println(String.format("%d %d", maxI, maxJ));
    }

    public static int[] inputArray(Scanner input, int n) {
        int array[] = new int[n];

        for (int i = 0; i < array.length; i++) {
            array[i] = input.nextInt();
        }

        return array;
    }
}
