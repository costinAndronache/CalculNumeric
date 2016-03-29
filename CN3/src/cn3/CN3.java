/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn3;

import cn2.CN2;

/**
 *
 * @author Costi
 */
public class CN3 {

    /**
     * @param args the command line arguments
     */
    
    static class InverseResult
    {
        public double[][] matrix;
        public double[][] inverse;
        public String errorMessage;
    }
    
    public static class CN3LabExerciseInput
    {
        public double[][] A;
        public double epsilon;
    }
    
    public static class CN3LabExerciseOutput
    {
        public String errorMessageString;
        public double[][] inverse;
        public double determinant;
        public double ATimesInversMinusIdentityNorm;
    }
    
    public static CN3LabExerciseOutput solveExercise(CN3LabExerciseInput input)
    {
        CN3LabExerciseOutput out = new CN3LabExerciseOutput();
        double[][] A = input.A;
        
        double[][] augmented = augmentMatrixWithIdentity(A);        
        String gaussEliminateErrorMessage = partialGaussEliminate(augmented, input.epsilon);
        if (gaussEliminateErrorMessage != null) 
        {
            out.errorMessageString = gaussEliminateErrorMessage;
            return out;
        }
        
        double det = 1.0;
        for(int i=0; i<A.length; i++)
        {
            det = det * augmented[i][i];
        }
        out.determinant = det;
        
        InverseResult inverseResult = findInverse(augmented, input.epsilon);
        
        if(inverseResult.errorMessage != null)
        {
            out.errorMessageString = inverseResult.errorMessage;
            return out;
        }
        
        double[][] multiplied = multiplyMatrixes(A, inverseResult.inverse);
        
        double[][] identity = cn2.CN2.identityMatrix(multiplied.length);
        double[][] substracted = substractSecondFromFirst(multiplied, identity);
        
        out.inverse = inverseResult.inverse;
        out.ATimesInversMinusIdentityNorm = computeL1Norm(substracted);
        
        return out;
    }
    
    public static void main(String[] args) 
    {
        
        double[][] A = new double[][]{{1,0,2}, {0,1,0}, {1,1,1}};
        
        double[][] augmented = augmentMatrixWithIdentity(A);
        System.out.println(cnjava1.CNJava1.printMatrix(augmented));
        
        partialGaussEliminate(augmented, 1e-5);
        System.out.println(cnjava1.CNJava1.printMatrix(augmented));
        
        InverseResult inverseResult = findInverse(augmented, 1e-3);
        System.out.println(cnjava1.CNJava1.printMatrix(inverseResult.inverse));
        
        double[][] multiplied = multiplyMatrixes(A, inverseResult.inverse);
        System.out.println(cnjava1.CNJava1.printMatrix(multiplied));
        
        double[][] identity = cn2.CN2.identityMatrix(multiplied.length);
        double[][] substracted = substractSecondFromFirst(multiplied, identity);
        
        System.out.println(cnjava1.CNJava1.printMatrix(substracted));
        
        System.out.println(computeL1Norm(substracted));
    }
    
    
    
    public static void exchangeLines(int i0, int l, double[][] A)
    {
        if(i0 == l){return;}
        
        for(int j=0; j<A[i0].length; j++)
        {
            long firstLongBits = Double.doubleToRawLongBits(A[i0][j]);
            long secondLongBits = Double.doubleToLongBits(A[l][j]);
            
            
            firstLongBits ^= secondLongBits;
            secondLongBits ^= firstLongBits;
            firstLongBits ^= secondLongBits;
            
            
            A[i0][j] = Double.longBitsToDouble(firstLongBits);
            A[l][j] = Double.longBitsToDouble(secondLongBits);

        }
    }
    
    public static void exchangeColumns(int j0, int l, double[][] A)
    {
        if(j0 == l){return;}
        
        for(int i=0; i<A.length; i++)
        {
            long firstLongBits = Double.doubleToRawLongBits(A[i][j0]);
            long secondLongBits = Double.doubleToLongBits(A[i][l]);
            
            firstLongBits ^= secondLongBits;
            secondLongBits ^= firstLongBits;
            firstLongBits ^= secondLongBits;
            
            A[i][j0] = Double.longBitsToDouble(firstLongBits);
            A[i][l] = Double.longBitsToDouble(secondLongBits);
        }
    }
    
    public static void applyPartialPivoting(int L, double[][] A)
    {
        double max = A[L][L];
        int iZero = L;
        
        for(int i=L; i<A.length; i++)
        {
            double elem = A[i][L];
            
            if(Math.abs(max) < Math.abs(elem))
            {
                max = elem;
                iZero = i;
            }
        }
        
        exchangeLines(iZero, L, A);
    }
    
    public static double[][] augmentMatrixWithIdentity(double[][] A)
    {
        double[][] result = new double[A.length][2*A.length];
        
        for(int i=0; i<A.length; i++)
        {
            for(int j=0; j<A.length; j++)
            {
                result[i][j] = A[i][j];
                result[i][j + A.length] = 0;
            }
            
            result[i][i+A.length] = 1;
        }
        
        return result;
    }
    
    
    public static String partialGaussEliminate(double[][] augmentedA, double epsilon)
    {
        int L = 0;
        applyPartialPivoting(L, augmentedA);
        int n = augmentedA.length;
        
        while (L <= n-2 && Math.abs(augmentedA[L][L]) > epsilon) 
        {            
          for(int i=L+1; i<n; i++)
          {
              double f = - (augmentedA[i][L] / augmentedA[L][L]);
              for(int j=L+1; j<2 * n; j++)
              {
                  augmentedA[i][j] += f * augmentedA[L][j];
              }
              
              augmentedA[i][L] = 0;
          }
          
          L++;
            applyPartialPivoting(L, augmentedA);
        }
        
        if(Math.abs(augmentedA[L][L]) <= epsilon)
        {
            return "At step " + L + " the matrix became singular";
        }
        
        return null;
    }
    
    public static InverseResult findInverse(double[][] augmentedMatrix, double epsilon)
    {
        InverseResult inverseResult = new InverseResult();
        
        CN2.LinearSystemInput lsi = new CN2.LinearSystemInput();
        double[][] modifiedA = new double[augmentedMatrix.length][augmentedMatrix.length];
        double[][] vectorList = new double[augmentedMatrix.length][augmentedMatrix.length];
        
        for(int i=0; i<modifiedA.length; i++)
        {
            System.arraycopy(augmentedMatrix[i], 0, modifiedA[i], 0, modifiedA.length);
        }
        
        for(int j=0; j<modifiedA.length; j++)
        {
            for(int i=0; i<modifiedA.length; i++)
            {
                vectorList[j][i] = augmentedMatrix[i][j+modifiedA.length];
            }
        }
        
        
        double[][] inverse = new double[vectorList.length][vectorList.length];
        
        lsi.upperTriangularMatrix = modifiedA;
        for(int i=0; i<vectorList.length; i++)
        {
            lsi.b = vectorList[i];
            double[] columnSolution = cn2.CN2.solveLinearSystem(lsi);
            
            if (columnSolution == null) 
            {
                inverseResult.errorMessage = "A solution to a vector " + cnjava1.CNJava1.printVector(lsi.b) +
                        "could not be found";
            }
            
            for(int j=0; j<vectorList.length; j++)
            {
                inverse[j][i] = columnSolution[j];
            }
        }
        
        
        inverseResult.inverse = inverse;
        inverseResult.matrix = modifiedA;
        return inverseResult;
    }
    
    public static double computeL1Norm(double[][] A)
    {
        double norm = 0;
        
        for(int i=0; i<A.length; i++)
        {
            double currentLineSum  = 0;
            for(int j=0; j<A[i].length; j++)
            {
                currentLineSum += Math.abs(A[i][j]);
            }
            
            if(currentLineSum > norm)
                norm = currentLineSum;
        }
        
        
        return norm;
    }
    
    
    public static double[][] multiplyMatrixes(double[][] first, double[][] second)
    {
                
        int m = first.length;
        int n = first[0].length;
        
        int p = second.length;
        int q = second[0].length;
        
        if(n != p)
        {
            return null;
        }
        
        double[][] multiply = new double[m][q];
        
        for (int c = 0 ; c < m ; c++ )
         {
            for (int d = 0 ; d < q ; d++ )
            {   
               if(c==2)
               {
                   System.out.println("what the f ");
               }
               double sum = 0;
               for (int k = 0 ; k < p ; k++ )
               {
                   double firstElem = first[c][k];
                   double secondElem = second[k][d];
                  sum += firstElem * secondElem;
               }
 
               multiply[c][d] = sum;
               

               
                System.out.print(sum + ", ");
            }
            
             System.out.println("");
         }
        
      
        return multiply;
    }
    
    
    public static double[][] substractSecondFromFirst(double[][] first, double[][] second)
    {
        double[][] result = new double[first.length][first[0].length];
        
        for(int i = 0; i<result.length; i++)
        {
            for(int j =0 ; j<result[i].length; j++)
            {
                result[i][j] = first[i][j] - second[i][j];
            }
        }
        
        
        return result;
    }
    
}
