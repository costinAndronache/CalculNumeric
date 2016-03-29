/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn4;
import java.io.*;

/**
 *
 * @author Costi
 */
public class CN4 
{
    
    public static class FileReadOutput
    {
        public double[] x;
        public RareMatrix A;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        FileReadOutput outFirst = readFromFile("a.txt");
        FileReadOutput outSecond = readFromFile("b.txt");
        FileReadOutput outPutSum = readFromFile("aplusb.txt");
        FileReadOutput outputProduct = readFromFile("aorib.txt");
        
        System.out.println("did read both matrixes");
        RareMatrix sum = outFirst.A.addWithRareMatrix(outSecond.A);
        RareMatrix product = outFirst.A.multiplyWithRareMatrix(outSecond.A);
        ///sum.setValue(0, 200, 158.014);
        System.out.println("is sum equal to fileSum? " + sum.isEqualToMatrixWithEpsilon(outPutSum.A, 1e-3));
        System.out.println("is product equal to fileProduct? " + product.isEqualToMatrixWithEpsilon(outputProduct.A
                , 1e-3));
        
        double[] oneToN = vectorOfElementsFrom1To(outFirst.x.length);
        double[] aMultiplied = outFirst.A.multiplyWithVector(oneToN);
        
        System.out.println("are vectors equal? " + areVectorsEqualWithEpsilon(aMultiplied, outFirst.x, 1e-3));
        
        //System.out.println(cnjava1.CNJava1.printVector(aMultiplied));
        //System.out.println(cnjava1.CNJava1.printVector(outFirst.x));
        
    }
    
    
    public static double[] vectorOfElementsFrom1To(int maxN)
    {
        double[] vec = new double[maxN];
        
        for(int i=0; i<maxN; i++)
        {
            vec[i] = i + 1;
        }
        
        return vec;
    }
    
    public static boolean areVectorsEqualWithEpsilon(double[] a, double[] b, double epsilon)
    {
        if(a.length != b.length)
        {
            return false;
        }
        
        for(int i=0; i<a.length; i++)
        {
            if(Math.abs(a[i] - b[i]) > epsilon)
            {
                return false;
            }
        }
        
        
        return true;
    }
    
    public static FileReadOutput readFromFile(String filename) throws Exception
    {
        FileReadOutput out = new FileReadOutput();
        
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            String numOfElementsAsString = br.readLine();
            int numOfElems = Integer.parseInt(numOfElementsAsString);
            
            out.x = new double[numOfElems];
            out.A = new RareMatrix(numOfElems, numOfElems);
            
            br.readLine();
            for(int i=0; i<numOfElems; i++)
            {
                String elemAsString = br.readLine();
                double elem = Double.parseDouble(elemAsString);
                out.x[i] = elem;
            }
            
            
            br.readLine();
            
            String infoLine = br.readLine();
            while(infoLine != null)
            {
                String[] elems = infoLine.split("\\s*(=>|,|\\s)\\s*");
                double elem = Double.parseDouble(elems[0]);
                int line = Integer.parseInt(elems[1]);
                int column = Integer.parseInt(elems[2]);
                
                double val = out.A.getValue(line, column);
                out.A.setValue(line, column, val + elem);
                //System.out.println("Did read line " + infoLine);
                infoLine = br.readLine();
            }
        }
        return out;
    }
    
}
