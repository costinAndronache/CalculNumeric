package cn4;


import java.util.*;

public class RareMatrix 
{
	private static double epsilon = 0.0001;
	
	private Map<Integer, Map<Integer, Double>> matrixMap;
	int numOfLines, numOfColumns;
	
	RareMatrix(int lines, int columns)
	{
		this.commonInit();
		this.numOfLines = lines;
		this.numOfColumns = columns;
	}
	
	public void setValue(int i, int j, double value) 
	{
		this.checkAndThrow("setValue", i, j);
		Map<Integer, Double> map = this.getMapOrCreateForLine(i);
		map.put(j, value);
	}
	
	public double getValue(int i, int j)
	{
		double result = 0.0;
		this.checkAndThrow("getValue", i, j);
		Map<Integer, Double> map = this.matrixMap.get(i);
		 if(map == null)
			 return result;
		 
		 Double unboxed = map.get(j);
		 if(unboxed == null)
			 return result;
		 else
			 return unboxed;
	}
	
	public int getNumberOfColumns()
	{
		return this.numOfColumns;
	}
	
	public int getNumberOfLines()
	{
		return this.numOfLines;
	}
	

	public double[] multiplyWithVector(double[] vec)
        {
            double[] result = new double[vec.length];
            
            for(int i=0; i<this.numOfLines; i++)
            {
                double sum = 0.0;
                Map<Integer, Double> columnMap = this.matrixMap.get(i);
                if(columnMap != null)
                {
                    for(int j=0; j<this.numOfColumns; j++)
                    {
                        Double dRef = columnMap.get(j);
                        if(dRef != null)
                        {
                            sum+= dRef * vec[j];
                        }
                    }
                }
                
                result[i] = sum;
            }
            
            return result;
        }
	
	public RareMatrix multiplyWithRareMatrix(RareMatrix aMatrix)
	{
		RareMatrix result= null;
		
		if(this.numOfColumns != aMatrix.numOfLines)
		{
			return result;
		}
		
		result = new RareMatrix(this.numOfLines, aMatrix.numOfColumns);
		Map<Integer, Map<Integer, Double>> hisColumnIndexedMap = aMatrix.getColumnIndexedMap();
		
                Set<Integer> myLines = this.matrixMap.keySet();
                Set<Integer> hisColumns = hisColumnIndexedMap.keySet();

                
		for(int i : myLines)
		{
			Map<Integer, Double> myLineIMap = this.matrixMap.get(i);
			for(int j : hisColumns)
			{
                            Map<Integer, Double> hisColumnJMap = hisColumnIndexedMap.get(j);
                            
                            Set<Integer> commonIndexes_ColumnsForMe_LinesForHim = 
                                    hisColumnJMap.keySet();
                            
                            commonIndexes_ColumnsForMe_LinesForHim.retainAll(myLineIMap.keySet());
                            
                            double totalSum = 0.0;
                            
                            for(int commonIndex:  commonIndexes_ColumnsForMe_LinesForHim)
                            {
				double dRef = hisColumnJMap.get(commonIndex);
                                totalSum += dRef * myLineIMap.get(commonIndex);
                            }
							
                            if(totalSum != 0.0)
                            {
				result.setValue(i, j, totalSum);
                            }
						
			}
		}
		
		return result;
	}
	
	public RareMatrix addWithRareMatrix(RareMatrix aMatrix)
	{
		RareMatrix result = null;
		
		if(this.numOfColumns != aMatrix.numOfColumns || this.numOfLines != aMatrix.numOfLines)
			{
			return result;
			}
		
		result = new RareMatrix(this.numOfLines, this.numOfColumns);
		
		for(int i=0; i<this.numOfLines; i++)
		{
			Map<Integer, Double> myColumnMap, hisColumnMap, resultColumnMap;
			myColumnMap = this.matrixMap.get(i);
			hisColumnMap = aMatrix.matrixMap.get(i);
			
			if(myColumnMap == null && hisColumnMap != null)
			{
				{
					resultColumnMap = new HashMap<Integer, Double>();
					//make a deep copy of hisMap
					RareMatrix.deepCopy(hisColumnMap, resultColumnMap);
					result.matrixMap.put(i, resultColumnMap);
				}
			}
			else if(myColumnMap != null && hisColumnMap == null)
			{
				if(hisColumnMap == null)
				{
					resultColumnMap = new HashMap<Integer, Double>();
					//make a deep copy of myMap
					RareMatrix.deepCopy(myColumnMap, resultColumnMap);
					result.matrixMap.put(i, resultColumnMap);
				}
			}
			else
				if(myColumnMap != null && hisColumnMap != null)
			{
					resultColumnMap = new HashMap<Integer, Double>();
					RareMatrix.deepCopy(myColumnMap, resultColumnMap);
					Set<Integer> myKeysSet = myColumnMap.keySet();
					
					for(Integer i1 : myKeysSet)
					{
						Double hisDouble = hisColumnMap.get(i1);
						if(hisDouble != null)
						{
							double dCopy = hisDouble.doubleValue();
							Double myDouble = resultColumnMap.get(i1);
							myDouble += dCopy;
							resultColumnMap.put(i1, myDouble);
						}
					}
					
					Set<Integer> hisKeysSet = hisColumnMap.keySet();
					for(Integer i2 : hisKeysSet)
					{
						Double d = resultColumnMap.get(i2);
						if(d == null)
						{
							Double hisD = hisColumnMap.get(i2);
							double dCopy = hisD.doubleValue();
							resultColumnMap.put(i2, dCopy);
						}
					}
					
					result.matrixMap.put(i, resultColumnMap);
			}
			
		}
		
		return result;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
                Set<Integer> linesSet = this.matrixMap.keySet();
                
                for(int i : linesSet)
                {
                    sb.append("For line " + i + " = {");
                    
                    Map<Integer, Double> columnMap = this.matrixMap.get(i);
                    Set<Integer> columnsSet = columnMap.keySet();
                    
                    for(int j: columnsSet)
                    {
                        double val = columnMap.get(j);
                        sb.append("(").append(j).append(",  ").append(val).append(")");
                    }
                    
                    sb.append("\n");
                }
                
                
		return sb.toString();
	}
	
        
        public boolean isEqualToMatrixWithEpsilon(RareMatrix other, double epsilon)
        {
            if(other.numOfColumns != this.numOfColumns ||
                    other.numOfLines != this.numOfLines)
            {
                return false;
            }
            
            Set<Integer> myLinesSet = this.matrixMap.keySet();
            Set<Integer> hisLinesSet = other.matrixMap.keySet();
            
            boolean hasMyLinesSetChanged = myLinesSet.retainAll(hisLinesSet);
            
            if(hasMyLinesSetChanged)
            {
                return false;
            }
            
            for(int i: myLinesSet)
            {
                Map<Integer, Double> myColumnMap = this.matrixMap.get(i);
                Map<Integer, Double> hisColumnMap = other.matrixMap.get(i);
                
                Set<Integer> myColumns = myColumnMap.keySet();
                Set<Integer> hisColumns = hisColumnMap.keySet();
                
                boolean hisColumnsAreDifferentThanMine = myColumns.retainAll(hisColumns);
                if(hisColumnsAreDifferentThanMine)
                {
                    return false;
                }
                
                for(int j: myColumns)
                {
                    double myVal = myColumnMap.get(j);
                    double hisVal = hisColumnMap.get(j);
                    
                    if(Math.abs(myVal - hisVal) > epsilon)
                    {
                        return false;
                    }
                }
            }
            
            
            return true;
        }
        
	
	private void checkAndThrow(String method, int i, int j)  throws IndexOutOfBoundsException
	{
		if(i<0 || i>numOfLines-1)
			throw new IndexOutOfBoundsException("In " + method + " i  is out of bounds, lines = " + numOfLines);
		
		if(j<0 || j>numOfColumns-1)
			throw new IndexOutOfBoundsException("In " + method + "j is out of bounds, columns = " + numOfColumns);
	}
	
	private Map<Integer, Map<Integer, Double>> getColumnIndexedMap()
	{
		Map<Integer, Map<Integer, Double> > resultMap = new HashMap<Integer, Map<Integer, Double>>();
		
		for(int j=0; j<this.numOfColumns; j++)
		{
			Map<Integer, Double> colMap = new HashMap<Integer, Double>();
			 for(int i=0; i<this.numOfLines; i++)
			 {
				 Map<Integer, Double> lineMap = this.matrixMap.get(i);
				 if(lineMap != null)
				 {
					 Double dRef = lineMap.get(j);
					 if(dRef != null)
					 {
						 colMap.put(i, dRef);
					 }
				 }
			 }
			 
			 if(colMap.size() > 0)
			 {
				 resultMap.put(j, colMap);
			 }
				 
		}
		
		return resultMap;
	}
	
	private void commonInit()
	{
		this.numOfColumns = this.numOfLines = 0;
		this.matrixMap = new HashMap<Integer, Map<Integer, Double>>();
	}

	
	private Map<Integer, Double> getMapOrCreateForLine(int line)
	{
		Map<Integer, Double> map = this.matrixMap.get(line);
		if(map == null)
		{
			map = new HashMap<Integer, Double>();
			this.matrixMap.put(line, map);
		}
		return map;
	}
	
	private static void deepCopy(Map<Integer, Double> source, Map<Integer, Double> dest)
	{
		for(Map.Entry<Integer, Double> entry : source.entrySet())
		{
			int copyInt = entry.getKey();
			double copyDouble = entry.getValue();
			dest.put(copyInt, copyDouble);
		}
	}
}