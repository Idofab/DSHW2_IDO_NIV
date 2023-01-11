import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {
	public int size = 0;
	public HeapNode minNode;
	public HashMap<HeapNode, Integer> RootsbyRank = new HashMap<HeapNode, Integer>();
	public HeapNode first;
	public static int marked = 0;
	public static int cutsCnt = 0;
	public static int linksCnt = 0;
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
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
    */
    public HeapNode insert(int key) {    
    	HeapNode insertNode = new HeapNode(key);
    	RootsbyRank.put(insertNode, 0);
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
    */
    public void deleteMin() {
    	
    	if(this.size() == 1) {
    		this.first = null;
    		this.minNode = null;
       		this.size--;
       		this.RootsbyRank = new HashMap<HeapNode, Integer>();
    		return;
    	}
    	
    	HeapNode deleteNode = this.minNode;
    	HeapNode deleteNodeChild = deleteNode.getChild();
		HeapNode deleteNodePrev = deleteNode.getPrev();
		HeapNode deleteNodeNext = deleteNode.getNext();

//    	If deleteNode has a child put all children in RootsbyRank and update roots conncetion's list
    	if(deleteNodeChild != null) {

    		deleteNodeChild.setParent(null);
    		RootsbyRank.put(deleteNodeChild, deleteNodeChild.getRank());
    		
        	HeapNode brotherNode = deleteNodeChild.getNext();
    		
        	while(brotherNode != deleteNodeChild) {
        		brotherNode.setParent(null);
        		RootsbyRank.put(brotherNode, brotherNode.getRank());
        		brotherNode = brotherNode.getNext();
        	}

        	HeapNode deleteNodeChildPrev = deleteNode.getChild().getPrev();
        	deleteNodePrev.setNext(deleteNodeChild);
        	deleteNodeChildPrev.setNext(deleteNodeNext);
    	}
    	
    	else {
    		deleteNodePrev.setNext(deleteNodeNext);
    		}
    	
    	if(deleteNode == this.getFirst()) {
    		this.first = deleteNode.getNext();
    	}
    	
    	RootsbyRank.remove(deleteNode);

    	this.consolidate();
    	
    	this.minNode = this.first;
    	for(Map.Entry<HeapNode, Integer> entRoot: RootsbyRank.entrySet()) {
    		if(entRoot.getKey().getKey() < this.minNode.getKey()) {
    			this.minNode = entRoot.getKey();
    		}
    	} 
    	
    	this.size --;
    }
    
    private void consolidate() {
    	HeapNode node = this.getFirst(); 
    	
    	List<HeapNode> origRoots = new ArrayList<HeapNode>();
    	for(int i=0; i<RootsbyRank.keySet().size(); i++) {
    		origRoots.add(i, node);
    		node = node.getNext();
    	}    	
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
        
   public void consolidateRec(HeapNode N1, HeapNode N2, HeapNode[] rankedRoots) {
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
		RootsbyRank.remove(bigNode);
		RootsbyRank.remove(smallNode);
		RootsbyRank.put(smallNode, smallNode.getRank());
		
		rankedRoots[ogRank]=null;
		if (rankedRoots[ogRank+1]==null) {
			rankedRoots[ogRank+1] = smallNode;
		}
		else {
			consolidateRec(smallNode, rankedRoots[ogRank+1], rankedRoots);
		}
   }
    
    public void consolidateRankZero(HeapNode bigNode, HeapNode smallNode) {
    	bigNode.parent = smallNode;
		smallNode.child = bigNode;
		bigNode.setNext(bigNode);
		
		smallNode.rank++;
    }
    
    public void consolidateRankNonZero(HeapNode bigNode, HeapNode smallNode) {
    	HeapNode smallNodeChild = smallNode.getChild();
    	HeapNode smallNodeChildPrev = smallNode.getChild().getPrev();
    	
    	bigNode.setNext(smallNodeChild);
    	smallNodeChildPrev.setNext(bigNode);
    	
		bigNode.parent = smallNode;
		smallNode.child = bigNode;	
		smallNode.rank++;
    }
    
   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
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
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size() {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    */
    public int[] countersRep() {
    	int[] arr = new int[(int)(Math.log(this.size()) / Math.log(2))+1];
    	if(this.size() == 0) {
    		return arr;
    	}
    	for(Map.Entry<HeapNode, Integer> entRoot: RootsbyRank.entrySet()) {
    		arr[entRoot.getValue()]++;
    	}
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) {
    	this.decreaseKey(x, x.getKey() + Math.abs(this.minNode.getKey()) + 1);
    	this.deleteMin();
    	return;
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) {
    	
    	x.setKey(x.getKey() - delta);
    	if(x.getKey() < this.findMin().getKey()) {
    		this.minNode = x;
    	}
    	
    	if((x.getParent() == null) || (x.getKey() > x.getParent().getKey())) {
    		return;
    	}
    	cascadingCut(x);
 
    	return;
    }
    
    public void cut(HeapNode x) {
    	cutsCnt++;
    	HeapNode y = x.parent;
    	x.setParent(null);
    	RootsbyRank.put(x, x.getRank());
    	x.setMark(false);
    	y.rank--;
    	
    	if(x.getNext() == x) {
    		y.setChild(null);
    	}
    	else {
    		y.setChild(x.getNext());
    		x.getPrev().setNext(x.getNext());
        	}
    	this.getFirst().prev.setNext(x);
		x.setNext(this.getFirst());
		this.first=x;
    }
    
    public void cascadingCut(HeapNode x) {
    	HeapNode y = x.parent;
    	cut(x);
    	
    	if( (y!= null) && (y.getParent() != null)) {
    		if (y.getMarked() == false) {
    			y.setMark(true);
    		}
    		else {
    			cascadingCut(y);
    		}
    	}
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
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
    */
    public int potential() {
        return RootsbyRank.size() + 2*marked;
    }

   /**
    * public static) int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
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
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
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
    		if(this.mark != bool) {
    			if(bool) {
    				marked++;
    			}
    			else {
    				marked--;
    			}
    		}
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

