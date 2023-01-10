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
		int del5=1;
		
		
		FibonacciHeap Test1 = new FibonacciHeap();
		for(int val: arr1) {
			Test1.insert(val);
		}
		for(int i=0; i<del1; i++) {
			Test1.deleteMin();
		}
		for(int val: arr2) {
			Test1.insert(val);
		}
		for(int i=0; i<del2; i++) {

			Test1.deleteMin();
			}
		for(int val: arr3) {
			Test1.insert(val);
		}
		for(int i=0; i<del3; i++) {

			Test1.deleteMin();
}
		for(int val: arr4) {
			Test1.insert(val);
		}
		for(int i=0; i<del4; i++) {

			Test1.deleteMin();
}
		for(int val: arr5) {
			Test1.insert(val);
		}
		for(int i=0; i<del5; i++) {

			Test1.deleteMin();
		}
		
		FibonacciHeap.HeapNode dec1 = Test1.getFirst().getNext().getChild().getChild();
		FibonacciHeap.HeapNode dec2 = dec1.getNext();
		System.out.println("dec: "+dec2.getKey());
		Test1.decreaseKey(dec1, 100);
		System.out.println("non: "+ Test1.nonMarked() +" "+ Test1.size());
		Test1.decreaseKey(dec2, 100);
		System.out.println("dec: "+dec2.getKey());
		
		
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
		System.out.println(T2.countersRep());
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
		t.insert(0);
		t.insert(10);
		System.out.println(t.countersRep());
	}
	public static void main(String[] args) {
		Test1(false);
//		Test2(1000);
//		Test3(false);
		System.out.println("DONE");

	}

}
