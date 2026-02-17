package org.chrisvenator;

import java.util.Arrays;

public class RegressionMetrics {
    public final double rSquared;
    public final double adjustedRSquared;
    public final double mse;
    public final double rmse;
    public final double mae;
    public final double rse;
    public final double mean;
    
    public RegressionMetrics(double[] yTrue, double[] yPred, int numFeatures) {
        if (yTrue == null || yPred == null || numFeatures < 0) throw new IllegalArgumentException("Something is wrong with the arguments!");
        if (yTrue.length != yPred.length) throw new IllegalArgumentException("yTrue and yPred must have the same length!");
        
        this.mean = mean(yTrue);
        this.mse = mse(yTrue, yPred);
        this.rmse = rmse(yTrue, yPred);
        this.mae = mae(yTrue, yPred);
        this.rse = rse(yTrue, yPred, numFeatures);
        this.rSquared = rSquared(yTrue, yPred);
        this.adjustedRSquared = adjustedRSquared(yTrue, yPred, numFeatures);
    }
    
    // R²: how much variance is explained by the model (higher is better, max 1.0)
    private double rSquared(double[] yTrue, double[] yPred) {
        double yMean = mean(yTrue);
        double ssTot = 0, ssRes = 0;
        for (int i = 0; i < yTrue.length; i++) {
            ssTot += Math.pow(yTrue[i] - yMean, 2);
            ssRes += Math.pow(yTrue[i] - yPred[i], 2);
        }
        return 1 - (ssRes / ssTot);
    }
    
    // Adjusted R²: penalizes for number of features (higher is better, max 1.0)
    private double adjustedRSquared(double[] yTrue, double[] yPred, int numFeatures) {
        return 1 - (1 - rSquared(yTrue, yPred)) * (yTrue.length - 1) / (yTrue.length - numFeatures - 1);
    }
    
    // MSE: average squared error (lower is better)
    // MSE = (1/n) * Σᵢ (yᵢ - ŷᵢ)²
    private double mse(double[] yTrue, double[] yPred) {
        double sum = 0.0;
        for (int i = 0; i < yTrue.length; i++) {
            double e = yTrue[i] - yPred[i];
            sum += e * e;
        }
        return sum / yTrue.length;
    }
    
    // RMSE: square root of MSE, same unit as y (lower is better)
    private double rmse(double[] yTrue, double[] yPred) {
        return Math.sqrt(mse(yTrue, yPred));
    }
    
    // MAE: mean absolute error, robust to outliers (lower is better)
    private double mae(double[] yTrue, double[] yPred) {
        double sum = 0;
        for (int i = 0; i < yTrue.length; i++) {
            sum += Math.abs(yTrue[i] - yPred[i]);
        }
        
        return sum / yTrue.length;
    }
    
    // RSE: standard deviation of residuals (lower is better)
    private double rse(double[] yTrue, double[] yPred, int numFeatures) {
        double sum = 0.0;
        for (int i = 0; i < yTrue.length; i++) {
            double e = yTrue[i] - yPred[i];
            sum += e * e;
        }
        
        return Math.sqrt(sum / yTrue.length - numFeatures - 1);
    }
    
    // ȳ = (1/n) * Σᵢ yᵢ
    private double mean(double[] values) {
        return Arrays.stream(values).sum() / values.length;
    }
    
    // Print all metrics at once
    public void printReport() {
        System.out.println("=== Regression Metrics ===");
        System.out.printf("R²:            %.4f%n", rSquared);
        System.out.printf("Adjusted R²:   %.4f%n", adjustedRSquared);
        System.out.printf("MSE:           %.4f%n", mse);
        System.out.printf("RMSE:          %.4f%n", rmse);
        System.out.printf("MAE:           %.4f%n", mae);
        System.out.printf("RSE:           %.4f%n", rse);
    }
}
