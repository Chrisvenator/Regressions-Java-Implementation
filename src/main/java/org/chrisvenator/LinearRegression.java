package org.chrisvenator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class LinearRegression extends Regression {
    private double[] betaHat; //Estimated coefficient vector β^
    
    public LinearRegression() {}
    
    @Override
    public void fit(double[][] X, double[] Y) {
        // β̂^ = (X⊤X)⁻¹ X⊤y
        double[][] Xt = super.transposeMatrix(X);         // X⊤
        double[][] XtX = super.matrixMultiply(Xt, X);      // X⊤X
        double[][] XtX_inv = super.invertMatrix(XtX);          // (X⊤X)⁻¹
        double[][] XtX_inv_Xt = super.matrixMultiply(XtX_inv, Xt);// (X⊤X)⁻¹X⊤
        this.betaHat = super.matrixMultiplyVector(XtX_inv_Xt, Y); // (X⊤X)⁻¹X⊤y
    }
    
    // Predict expects a feature vector without a leading 1
    @Override
    public double predict(double[] x) {
        // ŷ_i = f̂(x_{i1}, x_{i2}, …, x_{ip}) = β̂_0 + Σ_{j=1}^{p} x_{ij} β̂_j
        double prediction = betaHat[0];
        for (int i = 1; i < x.length; i++) {
            prediction += x[i - 1] * betaHat[i];
        }
        
        return prediction;
    }
}
