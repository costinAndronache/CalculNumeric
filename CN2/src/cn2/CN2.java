/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn2;

//import Jama.Matrix;
//import Jama.QRDecomposition;
import java.util.Arrays;
import la.matrix.DenseMatrix;
import org.apache.commons.math3.linear.*;
import org.ejml.alg.dense.linsol.qr.LinearSolverQrHouseCol_CD64;
import org.ejml.alg.dense.linsol.qr.LinearSolverQrHouseCol_D64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.interfaces.linsol.LinearSolver;
/**
 *
 * @author Costi
 */
public class CN2 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        double[][] matrix = new double[][]{
            {12, -51, 4},
            {6, 167, -68},
            {-4, 24, -41}
        };
        
        double[] s = new double[]{1,2,3};
        
        /*HHInput input = new HHInput(matrix, b, 1E-3);
        
        HHOutput out = factorize(input);
        
        String resultR = cnjava1.CNJava1.printMatrix(out.R);
        System.out.println(resultR);
        System.out.println(out.errorMessage);
        */
        
        CNLab2ExerciseInput input = new CNLab2ExerciseInput();
        input.A = matrix;
        input.s = s;
        input.epsilon = 1E-3;
        
        CNLab2ExerciseOutput out = solveExercise(input);
        
        String mySolString = cnjava1.CNJava1.printVector(out.xHouseholder);
        String labSolString = cnjava1.CNJava1.printVector(out.xQR);
        String bString = cnjava1.CNJava1.printVector(out.b);
        
        
        System.out.println(mySolString);
        System.out.println(labSolString);
        System.out.println(bString);
    }
    
    
    public static class CNLab2ExerciseInput
    {
        public double[][] A;
        public double[] s;
        public double epsilon;
    }
    
    
    public static class CNLab2ExerciseOutput
    {
        public HHOutput factorizationResult;
        public double [][] libR;
        public double[] xQR;
        public double[] xHouseholder;
        public double[] b;
        
        public double timeXQR;
        public double timeXHouseholder;
        
        public double NORMAinitXHausMinusBinit;
        public double NORMAinitXQRMinusBinit;
        public double NORMXHausMinusSDivNormS;
        public double NORMXQRMinusSDivNormS;
        
        public String errorMessage;
    }
    
    
    
    
    public static CNLab2ExerciseOutput solveExercise(CNLab2ExerciseInput in)
    {
        double timeMySolution = 0;
        double timeLibrarySolution = 0;
        
        CNLab2ExerciseOutput output = new CNLab2ExerciseOutput();
        
        double[] b = multiplyMatrixByVector(in.s, in.A);
        output.b = Arrays.copyOf(b, b.length);
        
        
        
        double[] copyOfS = Arrays.copyOf(in.s, in.s.length);
        double[][] copyOfA = copyOf(in.A);
        
        HHInput hhinput = new HHInput(copyOfA, copyOfS, in.epsilon);
        output.factorizationResult = factorize(hhinput);
        

        double[][] newCopyOfAForQR = copyOf(in.A);
        
        double[] copyOfBForQR = Arrays.copyOf(in.s, in.s.length);
        
        double[][] BData = new double[copyOfBForQR.length][1];
        for(int i=0; i<copyOfBForQR.length; i++){BData[i][0] = copyOfBForQR[i];}
        
        double start = System.currentTimeMillis();
        
        //using LAML
        /*la.matrix.DenseMatrix A = new DenseMatrix(copyOfA);
        la.matrix.DenseMatrix B = new DenseMatrix(BData);
        la.decomposition.QRDecomposition qRDecomposition = new la.decomposition.QRDecomposition(A);
        la.matrix.Matrix X = qRDecomposition.solve(B);
        la.matrix.Matrix R = qRDecomposition.getR();
        
        output.libR = R.getData();
        double[] x = new double[X.getRowDimension()];
        for(int i=0; i<x.length; i++){x[i] = X.getData()[i][0];}
        
        output.xQR = x;
        */
        // using JAMA
        
        Jama.Matrix A = new Jama.Matrix(newCopyOfAForQR);
        String newCopyOfAForQRAsString = cnjava1.CNJava1.printMatrix(newCopyOfAForQR);
        
        Jama.Matrix B = new Jama.Matrix(BData);
        String BAsString = cnjava1.CNJava1.printMatrix(BData);
        
        Jama.QRDecomposition decomposition = new Jama.QRDecomposition(A);
        Jama.Matrix X = decomposition.solve(B);
        

        
        Jama.Matrix residual = A.times(X);
        String residualAsString = cnjava1.CNJava1.printMatrix(residual.getArray());
        
        double[] x = new double[X.getRowDimension()];
        for(int i=0; i<x.length; i++){x[i] = residual.getArray()[i][0];}
        
        output.libR = decomposition.getR().getArrayCopy();
        output.xQR = x;
        
        
        //using EJML
        /*LinearSolver linearSolver = new LinearSolverQrHouseCol_D64();
        linearSolver.setA(new DenseMatrix64F(copyOfA));

        DenseMatrix64F B = new DenseMatrix64F(BData);
        DenseMatrix64F X = new DenseMatrix64F();
        X.setNumCols(1);
        X.setNumRows(copyOfBForQR.length);
        linearSolver.solve(B, X);
        output.xQR = X.getData();
        */
        
        //using Apache Commons Math
        /*RealVector bVector = MatrixUtils.createRealVector(copyOfBForQR);
        RealMatrix aMatrix  = MatrixUtils.createRealMatrix(newCopyOfAForQR);
        
        QRDecomposition dec = new QRDecomposition(aMatrix);
        RealMatrix R = dec.getR();
        
        System.out.println("Lib R: ");
        System.out.println(R);
        
        output.libR = R.getData();
        
        RealVector sol = dec.getSolver().solve(bVector);
        
        output.xQR = sol.toArray();
        */
        
        
        timeLibrarySolution = System.currentTimeMillis() - start;
        
        
        double[][] newCopyOfA = copyOf(in.A);
        double[] newCopyOfB = Arrays.copyOf(b, b.length);
        
        start = System.currentTimeMillis();
        
        HHInput hhInputSolve = new HHInput(newCopyOfA, newCopyOfB, in.epsilon);
        HHOutput hhoutputSolve = factorize(hhInputSolve);
        
        System.out.println("My R:");
        System.out.println(cnjava1.CNJava1.printMatrix(hhoutputSolve.R));
        
        LinearSystemInput lsi = new LinearSystemInput();
        lsi.upperTriangularMatrix = hhoutputSolve.R;
        lsi.b = hhoutputSolve.bModified;
        
        double[] mySolution = solveLinearSystem(lsi);
        timeMySolution = System.currentTimeMillis() - start;
        
        output.xHouseholder = mySolution;
        
        output.timeXHouseholder = timeMySolution;
        output.timeXQR = timeLibrarySolution;
        
        computeNorms(output, in);
        
        return output;
    }
    
    
    public static void computeNorms(CNLab2ExerciseOutput out,
            CNLab2ExerciseInput in)
    {
        double[] AinitXHaus = multiplyMatrixByVector(out.xHouseholder, in.A);
        double[] diffAinitXHausMinusBinit = substractSecondFromFirst(AinitXHaus, 
                out.b);
        out.NORMAinitXHausMinusBinit = computeVectorNorm(diffAinitXHausMinusBinit);
        
        
        double[] AinitXQR = multiplyMatrixByVector(out.xQR, in.A);
        double[] diffAInitXQRBInit = substractSecondFromFirst(AinitXQR, out.b);
        out.NORMAinitXQRMinusBinit = computeVectorNorm(diffAInitXQRBInit);
        
        
        double sNorm = computeVectorNorm(in.s);
        
        double[] diffXHausMinusS = substractSecondFromFirst(out.xHouseholder, in.s);
        out.NORMXHausMinusSDivNormS = computeVectorNorm(diffXHausMinusS) / sNorm;
        
        double[] diffXQRMinusS = substractSecondFromFirst(out.xQR, in.s);
        out.NORMXQRMinusSDivNormS = computeVectorNorm(diffXQRMinusS) / sNorm;
        
        
    }
    
    public static double[][] copyOf(double[][] a)
    {
        double[][] mat = new double[a.length][a.length];
        for(int i=0; i< mat.length; i++)
        {
            
            for(int j=0; j<mat[i].length; j++)
                mat[i][j] = a[i][j];
        }
        
        return mat;
    }
    
    
    public static class LinearSystemInput
    {
        public double[][] upperTriangularMatrix;
        public double[] b;
    }
    
    
    public static double[] solveLinearSystem(LinearSystemInput input)
    {
        double[] x = new double[input.b.length];
        
        for(int i = x.length-1; i>=0; i--)
        {
            double innerSum = 0;
            
            for(int j=i+1; j<x.length; j++)
            {
                innerSum+= x[j] * input.upperTriangularMatrix[i][j];
            }
            
            x[i] = (input.b[i] - innerSum) / input.upperTriangularMatrix[i][i];
        }
        
        return x;
    }
    
    
    public static class HHInput
    {
        public double[][] A;
        public double[] b;
        public double epsilon;
        
        public HHInput(double[][] A, double[] b, double epsilon)
        {
            this.A = A;
            this.b = b;
            this.epsilon = epsilon;
        }
    }
    
    public static class HHOutput
    {
        double[][] R = null;
        double[][] QTransposed = null;
        double[] bModified = null;
        
        public String errorMessage = null;
    }
    

    
    
    public static HHOutput factorize(HHInput input)
    {        
        CN2.HHOutput out = new CN2.HHOutput();
       
        if(isSingular(input.A))
        {
            out.errorMessage = "The matrix is singular!";
            return out;
        }
        
        if(!isSquare(input.A))
        {
            out.errorMessage = "The matrix is not square!";
            return out;
        }
        
        double[][] A = input.A;
        double[] b = input.b;
        double[][] q = identityMatrix(A.length);
        
        Householder(A, b, q, input.epsilon, out);
        
        out.R = A;
        out.bModified = b;
        out.QTransposed = q;
        
        return out;
    }
    
    public static boolean isSquare(double[][] a)
    {
        boolean oneRowHasMoreOrLessColumns = false;
        
        int rows = a.length;
        
        for(int i = 0; i< rows; i++)
        {
            if(a[i].length != rows)
            {
                oneRowHasMoreOrLessColumns = true;
                break;
            }
        }
        
        return !oneRowHasMoreOrLessColumns;
    }
    
    public static boolean isSingular(double[][] a)
    {
        boolean allAreZero = true;
        int n = a.length;
        
        for(int i=0; i<n; i++)
            if (a[i][i] != 0) {
                allAreZero = false;
                break;
            }
        
        return allAreZero;
    }
    
    public static double[] multiplyMatrixByVector(double[] vector, double[][] matrix)
    {
        double[] result = new double[vector.length];
        
        for(int i=0; i<result.length; i++)
        {
            double sum = 0;
            for(int jj = 0; jj<matrix[i].length; jj++)
            {
                sum += matrix[i][jj] * vector[jj];
            }
            
            result[i] = sum;
        }
        
        return result;
    }
    
    
    public static double computeVectorNorm(double[] vector)
    {
        double norm = 0;
        
        for(int i=0; i<vector.length; i++)
            norm+= vector[i] * vector[i];
        
        norm = Math.sqrt(norm);
        
        return norm;
    }
    
    
    public static double[] substractSecondFromFirst(double[] first, double[] second)
    {
        int n = second.length;
        double[] result = new double[n];
        
        for(int i=0; i<n; i++)
        {
            result[i] =  first[i] - second[i];
        }
        
        return result;
    }
    
    public static double computeBeta(double[][] a, int r)
    {
        double theta = computeTheta(a, r);
        return theta - computeK(a, r, theta)*a[r][r];
    }
    
    public static double computeTheta(double[][] a, int r) 
    {
        double theta = 0;
        int n = a.length;
        
        for(int j=r; j<=n-1; j++)
        {
            double elem = a[j][r];
            theta += elem * elem;
        }
        
        return theta;
        
    }
    
    public static double computeK(double[][] a, int r, double theta)
    {
        double k = -Math.signum(a[r][r]) * Math.sqrt(theta);
        return k;
    }
    
    public static double[] computeU(double[][] a, double k, int r)
    {
        double[] u = new double[a.length];
        for(int i=0; i<r; i++)
            u[i] = 0;
        
        u[r] = a[r][r] - k;
        
        for(int i=r+1; i<u.length; i++)
            u[i] = a[i][r];
        
        return u;
    }
    
    double[][] computeReflexionMatrix(int r, double[] u, double beta)
    {
        double f = 1.0/beta;
        double[][] P = new double[u.length][u.length];
        int n = P.length;
        
        for(int i=0; i<r; i++)
        {
            for(int j=0; j<n; j++)
            {
                if( i != j)
                {
                    P[i][j] = 0;
                }
            }
        }
        
        
        for(int i=0; i<r; i++)
            P[i][i] = 1;
        
        for(int i=r; i<n; i++)
        {
            for(int j=0; j<r; j++)
            {
                P[i][j] = 0;
            }
        }
        
        for(int i=r; i<n; i++)
        {
            for(int j=r; j<n; j++)
            {
                if(i != j)
                {
                    P[i][j] = -f * u[i] * u[j];
                }
            }
        }
        
        for(int i = r; i<n; i++)
            P[i][i] = 1 - f * u[i] * u[i];
        
        return P;
    }
    
    public static void Householder(double[][] A, double[] b, double[][] q, 
            double epsilon, HHOutput out)
    {
       
        int n = A.length; 
        
        for(int r=0; r<n-1; r++)
        {
            //constructia matricii Pr, constanta B, vectorul u
            double theta = computeTheta(A, r);
            if(theta <= epsilon) 
            {
                out.errorMessage = "theta is smaller than epsilon" + theta +", "
                        + epsilon;
                break;
            }
            
            double k = computeK(A, r, theta);
            double beta = computeBeta(A, r);
            
            if(beta<= epsilon) throw new RuntimeException("beta is almost 0");
            
            double[] u = computeU(A, k, r);
            
            for(int j=r+1; j<n; j++)
            {
                double gamma = computeGammaSumPartMatrix(u, A, r, j) / beta;
                
                for(int i=r; i<n; i++)
                {
                    A[i][j] = A[i][j] - gamma * u[i];
                }
                
            }
            
            //transformarea coloanei r a matricii A 
            
            A[r][r] = k;
            for(int i=r+1; i<n; i++)
                A[i][r] = 0;
            
            // b = Pr * b
            double gammaV = computeGammaSumPartVector(u, b, r) / beta;
            
            for(int i=r; i<n; i++)
            {
                b[i] = b[i] - gammaV * u[i];
            }
            
            //q = Pr * q
            
            for(int j=0; j<n; j++)
            {
                double gamma = computeGammaSumPartMatrix(u, q, r, j) / beta;
                for(int i=r; i<n; i++)
                {
                    q[i][j] = q[i][j] - gamma * u[i];
                }
            }
        }
        
    }
    
    
    public static double[][] identityMatrix(int n)
    {
        double[][] id = new double[n][n];
        
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                id[i][j] = 0;
            }
        }
        
        for(int i=0; i<n; i++)
            id[i][i] = 1;
        
        return id;
    }
    
    public static double computeGammaSumPartMatrix(double[] u, double[][] a, int r, int j)
    {
        double sum = 0;
        
        for(int i=r; i<u.length; i++)
        {
            sum = sum + u[i] * a[i][j];
        }
        
        return sum;
    }
    
    
    public static double computeGammaSumPartVector(double[] u, double[] b, int r)
    {
        double sum = 0;
        int n = u.length;
        for(int i=r; i<n; i++)
        {
            sum = sum + u[i] * b[i];
        }
        
        return sum;
    }
}
