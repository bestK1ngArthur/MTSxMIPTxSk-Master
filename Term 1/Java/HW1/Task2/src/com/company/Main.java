package com.company;

import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();

            if (n == 0) {
                System.out.println("0");
            }

            Point[] points = inputPoints(scanner, n);

            double square = 0.0;

            for (int i = 0; i < n; i++) {
                int iPlus1;
                if ((i + 1) == n) {
                    iPlus1 = 0;
                } else {
                    iPlus1 = i + 1;
                }

                square += (points[i].x + points[iPlus1].x) * (points[i].y - points[iPlus1].y);
            }

            square = Math.abs(square) / 2;

            System.out.println(square);
        }
    }

    private static Point[] inputPoints(Scanner scanner, int n) {
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = inputPoint(scanner);
        }

        return points;
    }

    private static Point inputPoint(Scanner scanner) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        return new Point(x, y);
    }
}
