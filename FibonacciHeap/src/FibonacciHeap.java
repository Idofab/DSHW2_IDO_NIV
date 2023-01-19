/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {
	public static int cutsCnt = 0;
	public static int linksCnt = 0;
	
	public int size = 0;
	public HeapNode minNode;
	public HeapNode first;
	public int numOfRoots = 0;
	public int marked = 0;

	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    * Complexity = O(1)
    */
    public boolean isEmpty() {
    	return this.size() == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    * Complexity = O(1)
    */
    public HeapNode insert(int key) {
    	HeapNode insertNode = new HeapNode(key);
    	numOfRoots++;
    	HeapNode curFirst = this.getFirst();
    	if(this.isEmpty()) {
    		insertNode.setNext(insertNode);
    		this.minNode = insertNode;
    	}
    	else {
    		curFirst.getPrev().setNext(insertNode);
    		insertNode.setNext(curFirst);
        	if(insertNode.getKey() < this.minNode.getKey()) {
        		this.minNode = insertNode;
        	}
    	}

    	this.first = insertNode;    	
    	this.size++;
    	return insertNode;
    }
    
    /**
     * public HeapNode insert( HeapNode linkNode)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * Also create a link from the insert node to linkNode.
     * The added key is assumed not to already belong to the heap.  
     * 
     * Returns the newly created node.
     * Complexity = O(1)
     */
    private HeapNode insert(HeapNode linkNode) {
    	int key = linkNode.getKey();
    	HeapNode insertNode = new HeapNode(key);
    	insertNode.linkNode=linkNode;
    	numOfRoots++;
    	HeapNode curFirst = this.getFirst();
    	if(curFirst == null) {
    		insertNode.setNext(insertNode);
    		this.minNode = insertNode;
    	}
    	else {
    		curFirst.getPrev().setNext(insertNode);
    		insertNode.setNext(curFirst);
    	}

    	this.first = insertNode;
    	
    	if(insertNode.getKey() < this.minNode.getKey()) {
    		this.minNode = insertNode;
    	}
    	
    	this.size++;
    	return insertNode;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    *Complexity = O(n)
    */
    public void deleteMin() {

    	if(this.size() == 1) {
    		this.first = null;
    		this.minNode = null;
       		this.size--;
       		this.numOfRoots--;
       		marked = 0;
    		return;
    	}
    	
    	HeapNode deleteNode = this.minNode;
    	HeapNode deleteNodeChild = deleteNode.getChild();
		HeapNode deleteNodePrev = deleteNode.getPrev();
		HeapNode deleteNodeNext = deleteNode.getNext();

//    	If deleteNode has a child update number of roots and roots connections
    	if(deleteNodeChild != null) {
    		if(deleteNodeChild.getMarked()) {
    			this.marked--;
    		}
    		deleteNodeChild.setMark(false);
    		deleteNodeChild.setParent(null);
    		numOfRoots++;
    		
        	HeapNode brotherNode = deleteNodeChild.getNext();
    		
        	while(brotherNode != deleteNodeChild) {
        		if(brotherNode.getMarked()) {
        			this.marked--;
        		}
        		brotherNode.setMark(false);
        		brotherNode.setParent(null);
        		numOfRoots++;
        		brotherNode = brotherNode.getNext();
        	}

        	HeapNode deleteNodeChildPrev = deleteNode.getChild().getPrev();
        	deleteNodePrev.setNext(deleteNodeChild);
        	deleteNodeChildPrev.setNext(deleteNodeNext);
    		if(deleteNode == this.getFirst()) {
        		this.first = deleteNode.getChild();
        		}
    	}
    	
    	else {
    		deleteNodePrev.setNext(deleteNodeNext);
    		if(deleteNode == this.getFirst()) {
        		this.first = deleteNode.getNext();
        		}
    		}
    	
    	numOfRoots--;

    	this.consolidate();
    	
    	this.minNode = this.first;
    	HeapNode[] rootsArr = this.getRoots();
    	
    	for(HeapNode root :rootsArr) {
    		if(root.getKey() < this.minNode.getKey()) {
    			this.minNode = root;
    		}
    	} 
    	
    	this.size --;
    }
    
    /*
     * Consolidate the tree roots after deleteMin()
    * Complexity = O(n)
    */

    private void consolidate() {    	
    	HeapNode[] origRoots = this.getRoots();    	
    	HeapNode[] rankedRoots = new HeapNode[(int)(Math.log(this.size()) / Math.log(2)) + 2];

    	for(HeapNode root: origRoots) {
    		if (root.getParent() == null) {
	    		int ogRank = root.getRank();
	    		HeapNode brother = rankedRoots[ogRank];
	    		if (brother != null) {
	    			consolidateRec(root,brother,rankedRoots);
	    		}
	    		else {
	    			rankedRoots[ogRank]=root;
	    		}	    		
    		}
    	}
    	
    	this.first=null;
    	for (HeapNode n: rankedRoots) {
    		if (n != null) {
    			this.first = n;
    			break;
    		}
    	}
    	
    	HeapNode newOrder=this.first;
    	for (HeapNode n: rankedRoots) {
    		if (n != null) {
    			newOrder.setNext(n);
    			newOrder=newOrder.getNext();
    		}
    	}
    	newOrder.setNext(this.first);
    }
  
    /*
     *private void consolidateRec(HeapNode N1, HeapNode N2, HeapNode[] rankedRoots)
    * Consolidate recursive function the tree roots after deleteMin() by rank
    * Complexity = O(log(n))
    */
    
    private void consolidateRec(HeapNode N1, HeapNode N2, HeapNode[] rankedRoots) {
    	linksCnt++;
	   	HeapNode smallNode;
		HeapNode bigNode;
		int ogRank = N1.rank;

		if(N1.getKey() < N2.getKey()) {    				
			smallNode = N1;
			bigNode = N2;
		}
		else {
			smallNode = N2;
			bigNode = N1;
		}
		
		if(N1.rank == 0) {
			consolidateRankZero(bigNode, smallNode);
		}
		else {
			consolidateRankNonZero(bigNode,smallNode);
		}
		
		numOfRoots--;
		
		rankedRoots[ogRank]=null;
		if (rankedRoots[ogRank+1]==null) {
			rankedRoots[ogRank+1] = smallNode;
		}
		else {
			consolidateRec(smallNode, rankedRoots[ogRank+1], rankedRoots);
		}
   }
    
    /*
     *private void consolidateRankZero(HeapNode bigNode, HeapNode smallNode)
    * Consolidating two roots with rank zero
    * Complexity = O(1)
    */
    private void consolidateRankZero(HeapNode bigNode, HeapNode smallNode) {
    	bigNode.parent = smallNode;
		smallNode.child = bigNode;
		bigNode.setNext(bigNode);
		
		smallNode.rank++;
    }
    
    /*
     *private void consolidateRankNonZero(HeapNode bigNode, HeapNode smallNode)
    * Consolidating two roots with rank higher than zero
    * Complexity = O(1)
    */
    private void consolidateRankNonZero(HeapNode bigNode, HeapNode smallNode) {
    	HeapNode smallNodeChild = smallNode.getChild();
    	HeapNode smallNodeChildPrev = smallNode.getChild().getPrev();
    	
    	bigNode.setNext(smallNodeChild);
    	smallNodeChildPrev.setNext(bigNode);
    	
		bigNode.parent = smallNode;
		smallNode.child = bigNode;	
		smallNode.rank++;
    }
    
    /*
     * private HeapNode[]  getRoots()
    * Returns an array of HeapNodes of the roots of the heap
    * Complexity = O(n)
    */
    
    private HeapNode[]  getRoots() {
    	HeapNode[] arr = new HeapNode[numOfRoots];
    	
    	HeapNode node= this.getFirst();
    	for(int i=0; i < numOfRoots; i++) {
    		arr[i] = node;
    		node = node.getNext();
    	}
    	
    	return arr;
    }
   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    * Complexity = O(1)
    */
    public HeapNode findMin() {
    	if(this.size() == 0) {
    		return null;
    	}
    	return this.minNode;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    * Complexity = O(1)
    */
    public void meld (FibonacciHeap heap2) {
    	if(heap2.size() == 0) {
    		return;
    	}
    	
    	if(this.size() == 0) {
    		this.size = heap2.size();
    		this.first = heap2.getFirst();
    		this.minNode = heap2.findMin();
    		this.marked = heap2.marked;
    		this.numOfRoots = heap2.numOfRoots;
    		return;
    	}
    	
    	HeapNode heap2lastNode = heap2.getFirst().getPrev();
    	this.getFirst().getPrev().setNext(heap2.getFirst());
    	heap2lastNode.setNext(this.getFirst());
    	
    	if(heap2.findMin().getKey() < this.findMin().getKey()) {
    		this.minNode = heap2.findMin();
    	}
    	
    	this.size += heap2.size();    	
    	this.numOfRoots += heap2.numOfRoots;
    	this.marked += heap2.marked;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    * Complexity = O(1)
    */
    public int size() {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * Complexity = O(n)
    */
    public int[] countersRep() {
    	if(this.size() == 0) {
    		return new int[] {};
    	}
    	int maxRank = 0;
    	for(HeapNode root:  this.getRoots()) {
    		if(root.getRank() > maxRank) {
    			maxRank = root.getRank();
    		}
    	}
    	int[] arr = new int[maxRank+1];
    	for(HeapNode root:  this.getRoots()) {
    		arr[root.getRank()]++;
    	}
    	return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    * Complexity = O(n)
    */
    public void delete(HeapNode x) {
    	this.decreaseKey(x, x.getKey() - (Integer.MIN_VALUE));
    	this.deleteMin();
    	}

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * Complexity = O(n)
    */
    public void decreaseKey(HeapNode x, int delta) {

    	x.setKey(x.getKey() - delta);
    	
    	if(x.getKey() < this.findMin().getKey()) {
    		this.minNode = x;
    		}
    	
    	if((x.getParent() != null) && (x.getKey() < x.getParent().getKey())) {
        	HeapNode y = x.getParent();
    		cut(x);
        	cascadingCut(y);
    		}
    	}
    
    /**
     * public void cascadingCut(HeapNode x)
     * Cascading up in the tree and cutting relevant nodes
     * Complexity = O(n)
     */
    public void cascadingCut(HeapNode x) {
    	if(x.getParent() != null) {
    		if (x.getMarked()) {
            	HeapNode y = x.getParent();
    	    	cut(x);
    			cascadingCut(y);
    		}
    		else {
    			x.setMark(true);
    			this.marked++;
    		}
    	}
    }
    
    /**
     * public void cut(HeapNode x)
     * Cuts the insert HeapNode from it's parent and adding it as a root
     * Complexity = O(1)
     */
    public void cut(HeapNode x) {
    	cutsCnt++;
    	
    	HeapNode y = x.parent;
    	if(x.getMarked()) {
    		this.marked--;
    	}
    	x.setMark(false);
    	x.setParent(null);
    	numOfRoots++;
    	y.rank--;
    	
    	
    	if(x.getNext() == x) {
    		y.setChild(null);
    		}
    	else {
    		if(y.getChild() == x) {
    			y.setChild(x.getNext());
    			}
    		x.getPrev().setNext(x.getNext());
    		}
    	
    	this.getFirst().getPrev().setNext(x);
		x.setNext(this.getFirst());
		
		this.first=x;
    }
   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    * Complexity = O(1)
    */
    public int nonMarked() {    
        return this.size() - marked;
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap.
    * Complexity = O(1)
    */
    public int potential() {
    	if(this.size() == 0) {
    		return 0;
    	}
        return numOfRoots + 2*marked;
    }

   /**
    * public static) int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    * Complexity = O(1)
    */
    public static int totalLinks() {
    	return linksCnt;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods).
    * Complexity = O(1)
    */
    public static int totalCuts() {
    	return cutsCnt;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H.
    * Complexity = O(k*deg(H))
    */
    public static int[] kMin(FibonacciHeap H, int k) {
    	int insertCnt = 0;
    	if(k == 0) {
    		return new int[] {};
    	}
    	
    	if(k > H.size()) {
    		k=H.size();
    	}
    	
        int[] arr = new int[k];
        
        FibonacciHeap selectMin = new FibonacciHeap();
        HeapNode minNode;
        
        selectMin.insert(H.getFirst());
        insertCnt++;
        
        for(int i=0; i<k; i++) {
        	minNode = selectMin.findMin();
        	arr[i] = minNode.getKey();
        	System.out.println("DELETE: " + selectMin.numOfRoots);
        	selectMin.deleteMin();

        	HeapNode minNodeChild = minNode.linkNode.getChild();
        	if(minNodeChild != null) {
        		selectMin.insert(minNodeChild);
        		insertCnt++;
        		HeapNode minNodeChildBrother = minNodeChild.getNext();
	        	while(minNodeChild != minNodeChildBrother) {
	        		selectMin.insert(minNodeChildBrother);
	        		insertCnt++;
	        		minNodeChildBrother = minNodeChildBrother.getNext();
	        	}
        	}
        }
        System.out.println("INSERT: " + insertCnt);
        return arr;
    }
    /**
     * public HeapNode getFirst()
     * Returns the Fibonacci Heap first node 
     *  Complexity = O(1)
     */
    public HeapNode getFirst() {
    	if(this.size() == 0) {
    		return null;
    	}
    	return this.first;
    } 

   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
    	public int rank = 0;
    	public boolean mark = false;
    	public HeapNode child = null;
    	public HeapNode next = null;
    	public HeapNode prev = null;
    	public HeapNode parent = null;
    	public HeapNode linkNode = null;

    	public HeapNode(int key) {
    		this.key = key;    		
    	}

    	public int getKey() {
    		return this.key;
    	}
    	
    	public void setKey(int val) {
    		this.key = val;
    	}
    	
    	public int getRank() {
    		return this.rank;
    	}
    	
    	public boolean getMarked() {
    		return this.mark;
    	}
    	
    	public void setMark(boolean bool) {
    		this.mark = bool;
    	}
    	
    	public HeapNode getParent() {
    		return this.parent;
    	}
    	
    	public void setParent(HeapNode parent) {
    		this.parent = parent;
    	}
    	
    	public HeapNode getNext() {
    		return this.next;
    	}
    	
    	public HeapNode getPrev() {
    		return this.prev;
    	}
    	
    	public HeapNode getChild() {
    		return this.child;
    	}
    	
    	public void setChild(HeapNode child) {
    		this.child = child;
    	}
    	
    	private void setNext(HeapNode N2) {
			this.next = N2;
			N2.prev = this;
		}
    }
}

