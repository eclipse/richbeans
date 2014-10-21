package org.eclipse.dawnsci.analysis.examples.dataset;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.ConjugateGradient;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.dataset.impl.BooleanDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Comparisons;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetFactory;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetUtils;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.FFT;
import org.eclipse.dawnsci.analysis.dataset.impl.LinearAlgebra;
import org.eclipse.dawnsci.analysis.dataset.impl.Maths;
import org.eclipse.dawnsci.analysis.dataset.impl.Random;
import org.junit.Before;
import org.junit.Test;


/**
 * How to do the basics, @see http://wiki.scipy.org/NumPy_for_Matlab_Users
 * 
 * These examples are done as tests because it is easy to run the code.
 * However they are examples and do not actively test anything.
 *
 * @author Matthew Gerring
 *
 */
public class BasicExamples {
	
	private IDataset a, b;

	@Before
	public void create() {
		a = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);
		b = new DoubleDataset(new double[]{1.1,2.2,3.3,4.4,5.5,6.6}, 2, 3);
	}
	
	/**
	 * What follows in comments:
	 * 
	 * MATLAB CODE         NUMPY VECTOR  NUMPY MATRIX (may be unset)
	 */
	
	/**
	 * ndims(a)            ndim(a) or a.adim
	 */
	@Test
	public void ndims() {
		System.out.println("a has rank "+a.getRank());
	}
	
	/**
	 * numel(a)            size(a) or a.size
	 */
	@Test
	public void size() {
		System.out.println("a has size "+a.getSize());
	}
	
	/**
	 * size(a,n)           a.shape[n-1]
	 */
	@Test
	public void sizeN() {
		System.out.println("a has size "+a.getShape()[0]);
	}
	
	/**
	 * 2x3 matrix literal
	 * [ 1 2 3; 4 5 6 ]     array([[1.,2.,3.],[4.,5.,6.]])
	 */
	@Test
	public void createMatrix() {
		IDataset set = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2,3);
		System.out.println("Created a dataset : "+set);
	}
	
	/**
	 * construct a matrix from blocks a,b,c, and d
	 * 
	 * [ a b; c d ]              vstack([hstack([a,b]), hstack([c,d])])
	 */
	@Test
	public void concatenate() {
		
		IDataset c = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);
		IDataset d = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);

		IDataset h1 = DatasetUtils.concatenate(new IDataset[]{a,b},   1);
		IDataset h2 = DatasetUtils.concatenate(new IDataset[]{c,d},   1);
		IDataset m  = DatasetUtils.concatenate(new IDataset[]{h1,h2}, 0);
		
		System.out.println("Created matrix from blocks "+m);
	}
	
	/**
	 * access last element in the 1xn matrix a
	 * a(end)                     a[-1]   or    a[:,-1][0,0]
	 */
	@Test
	public void lastElement() {
		// a is 2x3 but the -1 principle for the end is still valid.
		double val = a.getDouble(-1,-1);
		System.out.println("The last value is "+val);
	}
	
	/**
	 * access element in second row, fifth column
	 * a(2,5)                     a[1,4]
	 */
	@Test
	public void element1x4() {
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{10, 10});
		
		// Get 1,4
		double val = a.getDouble(1,4);
		System.out.println("The last value at (1,4) "+val);
	}
	
	/** entire second row of a
	 * a(2,:)                     a[1] or a[1,:]
	 */
	@Test
	public void entireRow() {
		IDataset set = a.getSlice(new Slice(1,2), null);
		System.out.println("The row at index 1 is "+set);
	}
	
	/**the first five rows of a
	 * a(1:5,:)                   a[0:5] or a[:5] or a[0:5,:]
	 */
	@Test
	public void fiveRows() {
		
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{10, 10});
		
		IDataset set = a.getSlice(new Slice(0,5), null);
		System.out.println("The shape of five rows is "+set);

	}
	
	/**the last five rows of a
	 * a(end-4:end,:)             a[-5:]
	 */
	@Test
	public void lastFiveRows() {
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{10, 10});
		
		IDataset set = a.getSlice(new Slice(5,null), null); // FIXME Talk to Peter if this is right? It gives wrong answer...
		System.out.println("The shape of last five rows is "+set);
		
	}
	
	/**rows one to three and columns five to nine of a. This gives read-only access.
	 * a(1:3,5:9)                 a[0:3][:,4:9]

	 */
	@Test
	public void subslice() {
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{10, 10});
		
		IDataset set = a.getSlice(new Slice(0,3), new Slice(4,9));
		System.out.println("The shape of the slice is "+set);
		
	}
	
	/**every other row of a, starting with the third and going to the twenty-first
	 * (3:2:21,:)                 a[ 2:21:2,:]
	 */
	@Test
	public void everyOther() {
		
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{21, 10});

		IDataset set = a.getSlice(new Slice(2,21,2), null);
		System.out.println("The shape of the slice is "+set);

	}
	
	/**every other row of a, starting with the first
	 * a(1:2:end,:)                a[ ::2,:]
	 * */
	@Test
	public void everyOther2() {
		
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{21, 10});

		IDataset set = a.getSlice(new Slice(null,null,2), null);
		System.out.println("The shape of the everyother slice is "+set);
	}

	/**	a with rows in reverse order
	 * a(end:-1:1,:) or flipud(a)           a[ ::-1,:]
	 * **/
	@Test
	public void reverseOrder() {
		// a is 2x3 so we make a bigger one
		a = Random.rand(new int[]{21, 10});

		IDataset set = a.getSlice(new Slice(null,null,-1), null);
		System.out.println("The shape of the reversed slice is "+set);

	}
	
	/**a with copy of the first row appended to the end
	 * a([1:end 1],:)                       a[r_[:len(a),0]]
	 **/
    @Test
    public void appendLast() {
    	IDataset first = a.getSlice(new Slice(0,1), null);
    	IDataset aplus = DatasetUtils.append(a, first, 0);
    	
    	System.out.println("a with the first column added to the end is "+aplus);
    }
    
    /**transpose of a
     * a.'                                   a.transpose() or a.T
     */
    @Test
    public void transpose() {
    	IDataset trans = ((Dataset)a).transpose();
    	System.out.println("a transposed is "+trans);
    }
    
    /**conjugate transpose of a
     * a'                                   a.conj().transpose() or a.conj().T       a.H
     */
    @Test
    public void conjTranspose() {
    	IDataset ct = Maths.conjugate(a).transpose();
    	System.out.println("a conjugate transposed is "+ct);
    }
    
    /**element-wise multiply
     * a .* b                               a * b             multiply(a,b)
     */
    @Test
    public void multiply() {
    	
    	IDataset c = Maths.multiply(a, b);
       	System.out.println("Multiply element-wise to new array is "+c);

        ((Dataset)a).imultiply(b);
       	System.out.println("Multiply element-wise on a is "+a);
    }
    
    
    /** element-wise divide
     * a ./ b                               a / b
     */
    @Test
    public void divide() {
    	
    	IDataset c = Maths.divide(a, b);
       	System.out.println("Divide element-wise to new array is "+c);

        ((Dataset)a).idivide(b);
       	System.out.println("Divide element-wise on a is "+a);
    }

    /**element-wise exponentiation
     * a.^3                                 a**3    power(a,3)
     */   
    @Test
    public void exp() {
    	
    	IDataset c = Maths.power(a, 3);
       	System.out.println("Exponent element-wise to new array is "+c);

        ((Dataset)a).ipower(3);
       	System.out.println("Exponent element-wise on a is "+a);
    }
    
    /**matrix whose i,jth element is (a_ij > 0.5)
     * (a>0.5)                              (a>0.5)
     */
    @Test
    public void matrixTest1() {
    	IDataset g = (a.getDouble(1,2)>0.5) ? a : null;
    	// TODO Is this right? Perhaps they mean a sub-dataset of only the values which are >0.5?
    }
    
    /**extract the columms of a where vector v > 0.5
    a(:,find(v>0.5))                        a[:,nonzero(v>0.5)[0]]   a[:,nonzero(v.A>0.5)[0]]
    **/
    @Test
    public void matrixTest2() {
    	// TODO Peter help please, Something with Comparisions.nonZero(...)
    }
    
    
    /** extract the columms of a where column vector v > 0.5 
     * a(:,find(v>0.5))                      a[:,v.T>0.5]       a[:,v.T>0.5)]
     */
    @Test
    public void matrixTest3() {
    	// TODO Peter help please
    }
    
    /**a with elements less than 0.5 zeroed out
     * a(a<0.5)=0                            a[a<0.5]=0
     */
    @Test
    public void zeroedLt() {
    	IDataset lt = Comparisons.lessThan(a, 0.5);
    	System.out.println("Values less than 0.5 "+lt);
    }
    
    /**a with elements greater than 0.5 zeroed out
     * a .* (a>0.5)                          a * (a>0.5)      mat(a.A * (a>0.5).A)
     */
    @Test
    public void zeroedGt() {
    	IDataset gt = Comparisons.greaterThan(a, 0.5);
    	System.out.println("Values less than 0.5 "+gt);
    }
	
    /**set all values to the same scalar value
     * a(:) = 3                              a[:] = 3
     */
    @Test
    public void setScalar() {
    	((Dataset)a).fill(3);
    	System.out.println("Set all values to 3 "+a);
    }
    
    /**numpy assigns are by reference	
     * y=x                                    y = x.copy()
     */
    @Test
    public void assign() {
    	
    	IDataset y = a.clone();
    	System.out.println("Cloned y is a new array with the values of a "+y);
    }
    
    /**numpy slices are by reference, DAWN they are NOT.
     * 
     * y=x(2,:)                               y = x[1,:].copy()
     */
    @Test
    public void sliceClone() {
    	IDataset y = a.getSlice(new Slice(1), null);
    	System.out.println("The slice y does not reference the original array used by a "+y);
    }
    		
    /**turn array into vector (note that this forces a copy)	
     * y=x(:)                                 y = x.flatten(1)
     */
    @Test
    public void flatten() {
    	IDataset y = ((Dataset)a).flatten();
    	System.out.println("The flattened dataset is "+y);
    }
    	
    /**create an increasing vector 
     * 1:10                                  arange(1.,11.) 
     * 0:9                                   arange(10.)
     */
    @Test
    public void arange() {
    	IDataset aran = DatasetFactory.createRange(1, 11, 1, Dataset.FLOAT64);
    	System.out.println("arange type notation in dawnsci(1) "+aran);  	
    	
    	aran = DatasetFactory.createRange(10, Dataset.FLOAT64);
    	System.out.println("arange type notation in dawnsci(2) "+aran);
    }
    
    
    /**create a column vector
     * [1:10]'                               arange(1.,11.)[:, newaxis]
     */
    @Test
    public void columnVector() {
    	IDataset aran = DatasetFactory.createRange(1, 11, 1, Dataset.FLOAT64);
        // TODO Peter need help...    	
    }
    
    /** 
    3x4 rank-2 array full of 64-bit floating point zeros
    zeros(3,4)                               zeros((3,4))    
    
    3x4x5 rank-3 array full of 64-bit floating point zeros
    zeros(3,4,5)                             zeros((3,4,5))  
    
    3x4 rank-2 array full of 64-bit floating point ones
    ones(3,4)                                ones((3,4))    
    
    3x3 identity matrix
    eye(3)                                   eye(3)    
    
    vector of diagonal elements of a
    diag(a)                                  diag(a)   
    
    square diagonal matrix whose nonzero values are the elements of a
    diag(a,0)                                diag(a,0) 
    
    random 3x4 matrix
    rand(3,4)                                random.rand(3,4)    
    
    4 equally spaced samples between 1 and 3, inclusive
    linspace(1,3,4)                          linspace(1,3,4)     
    */
    @Test
    public void various() {
    	
    	IDataset zeros = DatasetFactory.zeros(new int[]{3, 4}, Dataset.FLOAT);
    	zeros = DatasetFactory.zeros(new int[]{3, 4, 5}, Dataset.FLOAT);
    	
    	IDataset ones = DatasetFactory.ones(new int[]{3,4}, Dataset.FLOAT);
    	
    	IDataset eye = DatasetUtils.eye(3, 3, 0, Dataset.FLOAT);
    	
    	IDataset rand = Random.rand(new int[]{3,4});
    	
    	IDataset line = DatasetUtils.linSpace(1, 3, 4, Dataset.FLOAT);
    }
     
    /**two 2D arrays: one of x values, the other of y values
     * [x,y]=meshgrid(0:8,0:5)      mgrid[0:9.,0:6.] or meshgrid(r_[0:9.],r_[0:6.]    
     */
    @Test
    public void meshGrid() {
    	List<Dataset> mg = DatasetUtils.meshGrid(DatasetFactory.createRange(0, 9, 1, Dataset.FLOAT64),
    			                                 DatasetFactory.createRange(0, 6, 1, Dataset.FLOAT64));
    	System.out.println("Created mesh grid of size "+mg.size());
    }
    
    /**the best way to eval functions on a grid
     *                              ogrid[0:9.,0:6.] or ix_(r_[0:9.],r_[0:6.]         
     */
    @Test
    public void oGrid() {
    	// TODO Peter how to we do ogrid?
    }
    
    /**create m by n copies of a
     * repmat(a, m, n)              tile(a, (m, n))         
     */
    @Test
    public void tile(){
    	// where m=10 and n=12
    	IDataset tiled = DatasetUtils.tile(a, 10, 12);
    	System.out.println("Tiled a is "+tiled);
    }
    
    
    /**concatenate columns of a and b
     * [a b]                         concatenate((a,b),1)
     */
    @Test
    public void concatenateColumns() {
		IDataset h1 = DatasetUtils.concatenate(new IDataset[]{a,b},   1);
		System.out.println("Concatenate (already done!) "+h1);
    }
    
    /**concatenate columns of a and b
     * [a b]                         concatenate((a,b),1)
     */
    @Test
    public void concatenateRows() {
		IDataset h1 = DatasetUtils.concatenate(new IDataset[]{a,b},   0);
		System.out.println("Concatenate (already done!) "+h1);
    }
 
    
    /**
     * 
    maximum element of a (with ndims(a)<=2 for matlab)
    max(max(a))                       a.max()
    
    maximum element of each column of matrix 
    amax(a)                           a.max(0)
    
    maximum element of each row of matrix a
    max(a,[],2)                       a.max(1)
    
    compares a and b element-wise, and returns the maximum value from each pair
    max(a,b)                          maximum(a, b)
    */
    @Test
    public void maxes() {
    	
    	double max = a.max().doubleValue();
    	System.out.println("Max value of a is "+max);
    	
    	IDataset m = ((Dataset)a).max(0);
    	
    	m = ((Dataset)a).max(1);
    	
    	Maths.maximum(a, b);
    }
    
    
    /** L2 norm of vector v
     * norm(v)                 sqrt(dot(v,v)) or linalg.norm(v)
     */
    @Test
    public void norm() {
    	
    	Dataset oneD = DatasetFactory.createRange(100, Dataset.FLOAT);
    	double vNorm = LinearAlgebra.norm(oneD);
    	System.out.println("Vector norm is "+vNorm);
    	
    	double mNorm = LinearAlgebra.norm((Dataset)a);
    	System.out.println("Matrix norm is "+mNorm);
    }
    
    /**element-by-element AND operator
     * a & b                   logical_and(a,b)
     */
    @Test
    public void logicalAnd() {
    	
    	// Equivalent tech
    	BooleanDataset bs = Comparisons.logicalAnd(a, b);
      	System.out.println("Result of a && b "+bs);
  	
    	// Or more useful (?)
    	final IDataset booleans = new BooleanDataset(2,3);
       	booleans.set(true, 0,0);
       	booleans.set(true, 1,1);
       	booleans.set(true, 1,2);
      	
      	final IDataset and = Maths.multiply(a, booleans);
      	System.out.println("Keeping some values using a boolean mask "+and);
    }
    
    /** element-by-element OR operator
     * a | b                    logical_or(a,b)
     */
    @Test
    public void logicalOr() {
    	BooleanDataset bs = Comparisons.logicalOr(a, b);
      	System.out.println("Result of a || b "+bs);
    }
    
    /**
    bitwise AND operator (Python native and Numpy ufunc)
    bitand(a,b)                        a & b
    */
    @Test
    public void bitAnd() {
    	// TODO Not possible without looping?
    }
    
    /** bitwise OR operator (Python native and Numpy ufunc)
    bitor(a,b)                         a | b
    */
    @Test
    public void bitOr() {
    	// TODO Not possible without looping?
    }
    
    /**inverse of square matrix a
     * inv(a)                        linalg.inv(a)
     */
    @Test
    public void invSquare() {
    	// TODO Not possible?
    }
    
    /**    pseudo-inverse of matrix a
     * pinv(a)                       linalg.pinv(a)
     */
    @Test
    public void pinv() {
    	// TODO Not possible?
    }
    
    
    /**
     * rank of a matrix a
     * rank(a)                       linalg.matrix_rank(a)
     */
    @Test
    public void matrixRank() {
    	// TODO What is the meaning of this?
    }
    
    /**
     * solution of a x = b for x
     * a\b                           linalg.solve(a,b) if a is square        linalg.lstsq(a,b) otherwise
     */
    @Test
    public void solve1() {
    	// TODO Not possible with DAWNSci code?
    }
    
    
    /**
     * solution of x a = b for x
     * b/a                           Solve a.T x.T = b.T instead
     */
    @Test
    public void solve2() {
    	// TODO Not possible with DAWNSci code?
    }

    
    /**singular value decomposition of a
     * [U,S,V]=svd(a)               U, S, Vh = linalg.svd(a), V = Vh.T
     */
    @Test
    public void decomposition() {
    	double[] da = LinearAlgebra.calcSingularValues((Dataset)a); // TODO Peter is this right?
    	System.out.println("Linear decomposition of a "+Arrays.asList(da));
    }

    /**    cholesky factorization of a matrix (chol(a) in matlab returns an upper triangular matrix, but linalg.cholesky(a) returns a lower triangular matrix)

    chol(a)                          linalg.cholesky(a).T
    */
    @Test
    public void cholesky() {
    	
// TODO Is something like this right Pete?
//    	Dataset    s = DatasetFactory.ones(new int[]{100,100}, Dataset.FLOAT);
//    	RealMatrix m = LinearAlgebra.apacheMatrix(s);
//		double det = new CholeskyDecomposition(m).getDeterminant();
//        System.out.println("The determinant is "+det);
    }
    
    /**eigenvalues and eigenvectors of a
     * [V,D]=eig(a)                   D,V = linalg.eig(a)
     */
    @Test
    public void eigen() {
    	
    	Dataset    a = Random.rand(new int[]{100, 100});
       	RealMatrix m = LinearAlgebra.apacheMatrix(a);
        EigenDecomposition e = new EigenDecomposition(m);
        RealVector rv = e.getEigenvector(0);
        System.out.println("Real vector is "+rv);
    }
    

    /**QR decomposition
     * [Q,R,P]=qr(a,0)                Q,R = Sci.linalg.qr(a)
     */
    @Test
    public void qr() {
    	
    	Dataset    a = Random.rand(new int[]{100, 100});
       	RealMatrix m = LinearAlgebra.apacheMatrix(a);
       	QRDecomposition qr = new QRDecomposition(m);
       	System.out.println("q is "+qr.getQ());
    }
    
    /**LU decomposition (note: P(Matlab) == transpose(P(numpy)) 
    [L,U,P]=lu(a)                     L,U = Sci.linalg.lu(a)
    */
    @Test
    public void lu() {
    	Dataset    a = Random.rand(new int[]{100, 100});
       	RealMatrix m = LinearAlgebra.apacheMatrix(a);
       	LUDecomposition qr = new LUDecomposition(m);
       	System.out.println("l is "+qr.getL());
    }
    
    /**Conjugate gradients solver
     * conjgrad                      Sci.linalg.cg
     */
    @Test
    public void conjGrad() {
    	
    	Dataset    a  = Random.rand(100);
    	RealVector rv = LinearAlgebra.apacheVector(a);
    	
    	Dataset s = Random.rand(new int[]{100, 100});
       	RealMatrix m = LinearAlgebra.apacheMatrix(s);
   	
    	ConjugateGradient grad = new ConjugateGradient(100, 1, false);
    	grad.solve((RealLinearOperator)m, rv);
    }
    
    /**Fourier transform of a
     * fft(a)                       fft(a)
     */
    @Test
    public void fft() {
    	
    	Dataset    a = Random.rand(new int[]{100, 100});
    	Dataset  fft = FFT.fft(a);
    	System.out.println("The fft is "+fft);
    }
    
    
    /**
     * inverse Fourier transform of a
     * ifft(a)                       ifft(a)
     * 
     */
    @Test
    public void ifft() {
    	Dataset    a = Random.rand(new int[]{100, 100});
    	Dataset ifft = FFT.ifft(a);
    	System.out.println("The ifft is "+ifft);
    }
    
    /**sort the matrix
     * sort(a)                       sort(a) or a.sort()
     */
    @Test
    public void sort() {
    	
       	Dataset    a = Random.rand(new int[]{100, 100});
       	Dataset    s = DatasetUtils.sort(a, 0);
    	System.out.println("The sorted data "+s);
    }
    
    /**sort the rows of the matrix
     * [b,I] = sortrows(a,i)         I = argsort(a[:,i]), b=a[I,:]
     */
    @Test
    public void sortRows() {
       	Dataset    a = Random.rand(new int[]{100, 100});
       	Dataset    s = DatasetUtils.sort(a, 0);
    	System.out.println("The sorted data "+s);
    }

    /**multilinear regression
     * regress(y,X)                  linalg.lstsq(X,y)
     * */
    @Test
    public void multilinear() {
    	
    	// You have to use:
    	// uk.ac.diamond.scisoft.analysis.fitting.Fitter;
    	// uk.ac.diamond.scisoft.analysis.fitting.functions.Quadratic;

//		DoubleDataset x = new DoubleDataset(new double[] {102,134,156});
//		DoubleDataset y = new DoubleDataset(new double[] {102.1,134.2,156.3});
//
//		Quadratic q = new Quadratic(new double[] {0, 1, 0});
//		try {
//			Fitter.llsqFit(new Dataset[] {x}, y, q);
//
//			DoubleDataset z = q.calculateValues(x);
//			Assert.assertEquals(y.getDouble(0), z.getDouble(0), 0.1);
//			Assert.assertEquals(y.getDouble(1), z.getDouble(1), 0.1);
//			Assert.assertEquals(y.getDouble(2), z.getDouble(2), 0.2);
//		} catch (Exception e) {
//			Assert.fail("" + e);
//		}

    }
    
    /**downsample with low-pass filtering
     * decimate(x, q)                           Sci.signal.resample(x, len(x)/q)
     * */
    @Test
    public void downsample() {
    	
    	// Use
    	// uk.ac.diamond.scisoft.analysis.dataset.function.Downsample
    	//Downsample ds      = new Downsample(DownsampleMode.MAXIMUM, 4);
    	//IDataset   smaller = ds.value(a).get(0);
    }
    
    /**
    unique(a)
    unique(a)
    **/
    @Test
    public void unique() {
    	// TODO needs a sort and loop?
    }
    
    /**Squeeze
     * squeeze(a)                      a.squeeze()
     */
    @Test
    public void squeeze() {
    	IDataset s = a.squeeze();
    	System.out.println("Squeezed is "+s);
    }
    
}
