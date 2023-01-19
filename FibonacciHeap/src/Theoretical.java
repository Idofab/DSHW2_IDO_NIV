public class Theoretical {
	
	public static void q1(int j) {
		
		long startTime = System.currentTimeMillis();
		
		int m = (int) Math.pow(2, j);
		
		FibonacciHeap fibHeap = new FibonacciHeap();
		FibonacciHeap.HeapNode[] insertNodes = new FibonacciHeap.HeapNode[m+1];
		for(int k=m-1; k>=-1; k--) {
			insertNodes[k+1] = fibHeap.insert(k);
		}
		fibHeap.deleteMin();
		for(int i=j; i>=1; i--) {
			fibHeap.decreaseKey(insertNodes[(int) (m-Math.pow(2, i) + 2)], m+1);
			}

		fibHeap.decreaseKey(insertNodes[m-2+1], m+1);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		System.out.println("m=" + m +" Total time: " + totalTime);
		System.out.println("m=" + m +" Total links: " + FibonacciHeap.totalLinks());
		System.out.println("m=" + m +" Total cuts: " + FibonacciHeap.totalCuts());
		System.out.println("m=" + m +" Total potential: " + fibHeap.potential());
		}

	public static void q2(int j) {
		long startTime = System.currentTimeMillis();
		int m = (int) Math.pow(3, j) - 1;
		FibonacciHeap fibHeap = new FibonacciHeap();
		FibonacciHeap.HeapNode[] insertNodes = new FibonacciHeap.HeapNode[m+1];
		for(int k=0; k<=m; k++) {
			insertNodes[k] = fibHeap.insert(k);
		}
		
		for(int i=1; i<=(3*m/4); i++) {
			fibHeap.deleteMin();
			}
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		System.out.println("m=" + m +" Total time: " + totalTime);
		System.out.println("m=" + m +" Total links: " + FibonacciHeap.totalLinks());
		System.out.println("m=" + m +" Total cuts: " + FibonacciHeap.totalCuts());
		System.out.println("m=" + m +" Total potential: " + fibHeap.potential());
		System.out.println("m=" + m +" Total roots: " + fibHeap.numOfRoots);
	}
	
	public static void main(String[] args) {
//		q1(4);
//		q1(5);
//		q1(10);
//		q1(15);
//		q1(20);
		
		q2(6);
//		q2(8);
//		q2(10);
//		q2(12);
//		q2(14);
//		int m = (int) Math.pow(2, 5);
//		
//		FibonacciHeap fibHeap = new FibonacciHeap();
//		int[] arr = new int[] {0,1, 6,7,8, };
//		for(int k:arr) {
//			fibHeap.insert(k);
//		}
//		fibHeap.deleteMin();
//		int[] arr2 = new int[] {0,2,5,3,4 };
//		for(int k:arr2) {
//			fibHeap.insert(k);
//		}
//		fibHeap.deleteMin();
//		
//		FibonacciHeap.kMin(fibHeap, 4);
	}

}

