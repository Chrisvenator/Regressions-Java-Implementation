package org.chrisvenator;


public abstract class Regression {
    abstract void fit(double[][] train, double[] labels);
    
    abstract double predict(double[] test);
    
    protected double[][] transposeMatrix(double[][] matrix) {
        if (matrix == null || matrix[0] == null) throw new IllegalArgumentException("Illegal Argument for input \"Matrix\"");
        
        double[][] B = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                B[j][i] = matrix[i][j]; // ← was matrix[j][i]
            }
        }
        
        return B;
    }
    
    protected double[][] matrixMultiply(double[][] A, double[][] B) {
        if (A == null || A[0] == null) throw new IllegalArgumentException("Illegal Argument for input \"Matrix\"");
        if (B == null || B[0] == null) throw new IllegalArgumentException("Illegal Argument for input \"Matrix\"");
        
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        
        if (B.length != colsA) throw new IllegalArgumentException("Incompatible matrix dimensions");
        
        double[][] C = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        
        return C;
    }
    
    protected double[] matrixMultiplyVector(double[][] A, double[] B) {
        if (A == null || A[0] == null) throw new IllegalArgumentException("Illegal Argument for input \"Matrix\"");
        if (B == null) throw new IllegalArgumentException("Illegal Argument for input \"Matrix\"");
        if (A[0].length != B.length) throw new IllegalArgumentException("Lengths of matrix and vector are not the same!");
        
        double[] C = new double[A.length];
        
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                C[i] += A[i][j] * B[j];
            }
        }
        
        return C;
    }
    
    
    
    // Only works for Square Matrices
    protected double[][] invertMatrix(double[][] matrix) {
        if (matrix == null || matrix[0] == null) throw new IllegalArgumentException("Illegal Argument for input \"Matrix\"");
        if (matrix.length != matrix[0].length) throw new IllegalArgumentException("Matrix must be square to be invertible");
        
        int N = matrix.length;
        
        // Build augmented matrix [A | I]
        double[][] augmented = new double[N][2 * N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(matrix[i], 0, augmented[i], 0, N);
            augmented[i][i + N] = 1; // identity on the right
        }
        
        // Forward elimination
        for (int i = 0; i < N; i++) {
            
            // Find pivot row and swap
            int maxRow = i;
            for (int k = i + 1; k < N; k++) {
                if (Math.abs(augmented[k][i]) > Math.abs(augmented[maxRow][i])) {
                    maxRow = k;
                }
            }
            double[] temp = augmented[i];
            augmented[i] = augmented[maxRow];
            augmented[maxRow] = temp;
            
            // Check if matrix is singular
            if (Math.abs(augmented[i][i]) < 1e-10) throw new IllegalArgumentException("Matrix is singular and cannot be inverted");
            
            // Scale pivot row so the diagonal becomes 1
            double pivot = augmented[i][i];
            for (int j = 0; j < 2 * N; j++) {
                augmented[i][j] /= pivot;
            }
            
            // Eliminate all other rows
            for (int k = 0; k < N; k++) {
                if (k == i) continue;
                double factor = augmented[k][i];
                for (int j = 0; j < 2 * N; j++) {
                    augmented[k][j] -= factor * augmented[i][j];
                }
            }
        }
        
        // Extract the right half as the inverted matrix
        double[][] inverted = new double[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(augmented[i], N, inverted[i], 0, N);
        }
        
        return inverted;
    }
}
