import java.util.Scanner;

public class LUPDecomposition {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть розмірність системи СЛАР (n): ");
        int n = scanner.nextInt();

        double[][] A = new double[n][n];
        double[] b = new double[n];

        System.out.println("Введіть розширену матрицю (коефіцієнти A та вільний член b):");
        System.out.println("Формат для кожного рядка: a1 a2 ... an b");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = scanner.nextDouble();
            }
            b[i] = scanner.nextDouble();
        }

        System.out.println("\n--- ЗАДАНА СИСТЕМА (A * x = b) ---");
        printSystem(A, b);

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];
        double[][] P = new double[n][n];

        for (int i = 0; i < n; i++) {
            P[i][i] = 1.0;
            for (int j = 0; j < n; j++) {
                U[i][j] = A[i][j];
            }
        }

        for (int k = 0; k < n; k++)
        {
            double maxPivot = 0;
            int pivotRow = k;
            for (int i = k; i < n; i++) {
                if (Math.abs(U[i][k]) > maxPivot) {
                    maxPivot = Math.abs(U[i][k]);
                    pivotRow = i;
                }
            }

            if (maxPivot == 0) {
                System.out.println("Матриця вироджена або близька до виродженої. Розв'язок неможливий.");
                return;
            }

            swapRows(U, k, pivotRow);
            swapRows(P, k, pivotRow);
            swapRows(L, k, pivotRow);

            L[k][k] = 1.0;

            for (int i = k + 1; i < n; i++) {
                L[i][k] = U[i][k] / U[k][k];
                U[i][k] = 0.0;
                for (int j = k + 1; j < n; j++) {
                    U[i][j] -= L[i][k] * U[k][j];
                }
            }
        }

        System.out.println("\n--- МАТРИЦЯ ПЕРЕСТАНОВОК (P) ---");
        printMatrix(P);

        System.out.println("\n--- НИЖНЯ ТРИКУТНА МАТРИЦЯ (L) ---");
        printMatrix(L);

        System.out.println("\n--- ВЕРХНЯ ТРИКУТНА МАТРИЦЯ (U) ---");
        printMatrix(U);

        double[] b_star = multiplyMatrixVector(P, b);

        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            y[i] = b_star[i];
            for (int j = 0; j < i; j++) {
                y[i] -= L[i][j] * y[j];
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = y[i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= U[i][j] * x[j];
            }
            x[i] /= U[i][i];
        }

        System.out.println("\n--- РОЗВ'ЯЗОК СИСТЕМИ (Вектор x) ---");
        for (int i = 0; i < n; i++) {
            System.out.printf("x%d = %.4f\n", (i + 1), x[i]);
        }

        scanner.close();
    }

    private static void swapRows(double[][] matrix, int row1, int row2) {
        if (row1 == row2) return;
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    private static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        int n = vector.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = 0;
            for (int j = 0; j < n; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    private static void printMatrix(double[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%8.3f ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    private static void printSystem(double[][] A, double[] b) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%6.1f * x%d ", A[i][j], (j + 1));
                if (j < n - 1) System.out.print("+ ");
            }
            System.out.printf("= %6.1f\n", b[i]);
        }
    }
}
