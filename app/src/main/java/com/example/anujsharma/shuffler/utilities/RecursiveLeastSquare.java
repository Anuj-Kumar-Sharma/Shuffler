package com.example.anujsharma.shuffler.utilities;

public class RecursiveLeastSquare {

    private static final double FORGETTING_FACTOR = 0.8;
    /**
     * X is dataPoints and Y is Response
     * n rows and m columns
     */
    private Matrix X, Y;
    private int n, m;
    /**
     * V = (X'X)"           //  ' -> Transpose and " -> Inverse
     * B = VX'Y
     */
    private Matrix V, B;

    public RecursiveLeastSquare(Matrix X, Matrix Y) {
        this.X = X;
        this.Y = Y;
        n = X.getRowDimension();
        m = X.getColumnDimension();

        /*System.out.println("printing Xsub: ");
        X.print(1, 1);*/

        /*
          initialize V and B here
         */
        V = X.transpose().times(X).inverse();
        B = V.times(X.transpose()).times(Y);
    }

    public void updateWithData(Matrix x, Matrix y) {

//        x.print(1, 1);
        updateV(x);
        Matrix G = V.times(x.transpose());
        Matrix E = y.minus(x.times(B));
        B = B.plus(G.times(E));
    }

    private void updateV(Matrix x) {
        Matrix numerator = V.times(x.transpose()).times(x).times(V);
        Matrix denominator = x.times(V).times(x.transpose());
        double denominatorValue = denominator.get(0, 0) + 1;

        Matrix fraction = numerator.times(denominatorValue);
        V = V.minus(fraction).times(1 / FORGETTING_FACTOR);
    }

    public Matrix getB() {
        return this.B;
    }

}
