/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnjava1;

import java.text.DecimalFormat;

/*
 *
 * @author Costi
 */
public class CNJava1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        double machinePrecision = computeSummationPrecision();
        System.out.println("Summation Precision " + machinePrecision);
        System.out.println("Sum associativity for (1,u): " + isSummationAssociative(1.0, machinePrecision * 10));
        System.out.println("Mult associativty (1,u) " + isMultiplicationAssociative(1E-10, machinePrecision*10));
        
        System.out.println("Math.tan(0) " + Math.tan(1.8));
        System.out.println("modifiedLentzTan(0) " + modifiedLentzTan(1.8, 1e-3));
        
        RandomSource rs = new RandomSource(5, 0, 100);
        double[] vector = readVector( new KeyboardVectorSource());
        double[][] matrix = readMatrix( new KeyboardMatrixSource());
    }
    
    
    
    public static String printMatrix(double[][] matrix)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<matrix.length; i++)
        {
            for(int j=0; j<matrix[i].length; j++)
                sb.append(df.format(matrix[i][j]) + " ");
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    public static String printVector(double[] vector)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<vector.length; i++)
            sb.append(df.format(vector[i]) + " ");
        
        return sb.toString();
    }
    
    public static double[] readVector(VectorSource vs)
    {
        double[] vector = null;
        
        int numOfElements = vs.getNumOfElements();
        if(numOfElements <=0)
            return null;
        
        vector = new double[numOfElements];
        
        for(int i=0; i<numOfElements; i++)
        {
            double value = vs.getElementAtIndex(i);
            vector[i] = value;
        }
        
        return vector;
    }
    
    public static double[][] readMatrix(MatrixSource ms)
    {
        double[][] matrix = null;
        
        
        int numOfLines = ms.getNumOfLines();
        int numOfColumns = ms.getNumOfColumns();
        
        if(numOfColumns <=0 || numOfLines <= 0)
            return  null;
        
        matrix = new double[numOfLines][numOfColumns];
        
        for(int i=0; i< numOfLines; i++)
        {
            for(int jj=0; jj<numOfColumns; jj++)
            {
                double value = ms.getElementAtLineAndColumn(i, jj);
                matrix[i][jj] = value;
            }
        }
        
        return  matrix;
    }
    
    
    public static double computeSummationPrecision()
    {
        int precision = 0;
        
        while(1 + Math.pow(10, -precision) != 1)
            precision++;
        
        return Math.pow(10, -precision);
    }
    
    
    public static boolean isSummationAssociative(double a, double machinePrecision)
    {
        double b = machinePrecision / 10;
        double c = b;
        
        double firstVal = (a+b)+c;
        double secondVal = a+(b+c);
        
        System.out.println("b == " + b);
        System.out.println("firstVal " + firstVal + " secondVal " + secondVal);
        return firstVal == secondVal;
    }
    
    public static boolean isMultiplicationAssociative(double a, double machinePrecision)
    {
        double b = machinePrecision / 10;
        double c = b;
        
        double firstVal = (a*b)*c;
        double secondVal = a*(b*c);
        
        System.out.println("b == " + b);
        System.out.println("firstVal " + firstVal + " secondVal " + secondVal);
        return firstVal == secondVal;
    }
    
    
    public static double modifiedLentzTan(double x, double epsilon)
    {
        if(x <= -Math.PI/2)
        {
            
        }
        else if(x >= Math.PI /2 )
        {
            
        }
        
        double mic = 1E-20;
        
        double f0 = mic;
        double C0 = f0; double D0 = 0;
        
        double currentC, 
                currentD, 
                previousC, 
                previousD;
        
        previousC = C0;
        previousD = D0;
        
        double deltaJ, fJ, previousFJ = mic;
        
        int j=1;
        do {            
            
            currentD = tanBJ(j) + tanAJ(x, j)*previousD;
            if(currentD == 0) currentD = mic;
            
            currentC = tanBJ(j) + (tanAJ(x, j) / previousC);
            if(currentC == 0) currentC = mic;
            
            currentD = (double)1.0 / currentD;
            deltaJ = currentC * currentD;
            fJ = deltaJ * previousFJ;
            j++;
            
            previousC = currentC;
            previousD = currentD;
            
            previousFJ = fJ;
            
            
            
        } while (Math.abs(deltaJ - 1) >= epsilon);
        
        
        return fJ;
    }
    
    
    public static double tanBJ(int j)
    {
        return 2*j - 1;
    }
    
    public static double tanAJ(double x, int j)
    {
        if(j==1)
            return x;
        return -(x * x);
    }
}
