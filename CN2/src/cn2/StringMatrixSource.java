/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn2;

/**
 *
 * @author Costi
 */
public class StringMatrixSource implements cnjava1.MatrixSource
{
    int size;
    private final String[] elementsAsStrings;
    
    public StringMatrixSource(String matrixInString)
    {
        this.elementsAsStrings = matrixInString.split("\\s*,\\s*");
        this.size = (int)Math.sqrt(this.elementsAsStrings.length);
        System.out.println("String matrix source of length " + this.size);
    }
    
    @Override
    public int getNumOfLines() 
    {
        return size;
    }

    @Override
    public int getNumOfColumns() {
        return size;
    }

    @Override
    public double getElementAtLineAndColumn(int i, int j) 
    {
        String strElem = this.elementsAsStrings[i * this.size + j];
        return Double.parseDouble(strElem);
    }
    
}
