public class Tester {
	
	public static void Test1(boolean pr) {

		int[] arr1 = new int[] {0,1,2};
		int del1=1;
		int[] arr2 = new int[] {4,5,6,7,8,9,10};
		int del2=2;
		int[] arr3 = new int[] {13,14,15};
		int del3=1;
		int[] arr4 = new int[] {17};
		int del4=1;
		int[] arr5 = new int[] {19,20,21,22};
		int del5=2;
		int[] arr6= new int[] {25};
		int del6=1;
		int[] arr7= new int[] {27,28,29,30,31,32,33};
		int del7=2;
		
		FibonacciHeap Test1 = new FibonacciHeap();
		for(int val: arr1) {
			Test1.insert(val);
		}
		for(int i=0; i<del1; i++) {
			System.out.println("B" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());
		}
		for(int val: arr2) {
			Test1.insert(val);
		}
		for(int i=0; i<del2; i++) {
			System.out.println("B" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());		}
		for(int val: arr3) {
			Test1.insert(val);
		}
		for(int i=0; i<del3; i++) {
			System.out.println("\nB" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());		}
		for(int val: arr4) {
			Test1.insert(val);
		}
		for(int i=0; i<del4; i++) {
			System.out.println("\nB" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());		}
		for(int val: arr5) {
			Test1.insert(val);
		}
		for(int i=0; i<del5; i++) {
			System.out.println("\n1B" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());		}
		for(int val: arr6) {
			Test1.insert(val);
		}
		for(int i=0; i<del6; i++) {
			System.out.println("B" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());		}
		for(int val: arr7) {
			Test1.insert(val);
		}
		for(int i=0; i<del7; i++) {
			System.out.println("B" + Test1.findMin().getKey());
			Test1.deleteMin();
			System.out.println("F" + Test1.findMin().getKey());		}
	}
	public static void Test2(int n) {	
		FibonacciHeap T2 = new FibonacciHeap();

		for(int i=0; i<n; i++) {
			if(T2.size() == 0) {
				System.out.println("insert " + i);
				T2.insert(i);
			}
			else {
		        double random = Math.random();
		        if(random < 0.7) {
		        	System.out.println("insert " + i);
		        	T2.insert(i);
		        }
		        else {
					System.out.println("delete min " + i);
					T2.deleteMin();
				}
			}
		}
	}

	public static void Test3(boolean pr) {
		FibonacciHeap t = new FibonacciHeap();
		t.insert(0);
		t.insert(1);
		t.deleteMin();
		t.insert(3);
		t.deleteMin();
		t.insert(5);
		t.deleteMin();
		t.insert(7);
		t.insert(8);
		t.insert(9);
		t.insert(10);
		System.out.println();
		t.deleteMin();
		
		
		
	}
	public static void main(String[] args) {
//		Test1(false);
		Test2(1000);
		System.out.println("DONE");

	}

}
