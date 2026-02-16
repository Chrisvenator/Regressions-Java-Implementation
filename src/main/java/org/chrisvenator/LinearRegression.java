package org.chrisvenator;

public class LinearRegression implements Regression{
    private double[][] train;
    
    public LinearRegression(){}
    
    @Override
    public void fit(double[][] train) {
        this.train = train;
    }
}
