package lp3;/* Starter code for LP3 */

// Change this to netid of any member of team

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {

    static final int maxLevel = 32;
    Entry<T> head, tail;
    int size;
    Entry<T>[] pred;



    static class Entry<E> {
	E element;
	Entry<E>[] next;
	int[] pass;
//	Entry prev;

	public Entry(E x, int lev) {
	    element = x;
	    next = new Entry[lev];
	    pass = new  int[lev];
	    // add more code if needed
	}
	public  int level() {return next.length;}

	public E getElement() {
	    return element;
	}
    }

    // Constructor
    public SkipList() {
        size =0;
        head = new Entry(null,maxLevel);
        tail = new Entry(null,maxLevel);
        for(int i =0; i<maxLevel;i++) head.next[i]=tail;
    }

    /**
     * helper function
     * @param  x is the target you plan to find.
     */
    public void findPred(T x){
        Entry<T> p = head;
        pred = new Entry[maxLevel-1];
        int pLevel = p.next.length;
        for(int i = pLevel-2;i>=0;i--){
            while(x!=null && p.next[i].element!=null && p.next[i].element.compareTo(x)<0) p = p.next[i];
            pred[i]=p;
        }
    }
    public int chooseLevel(){
        Random random = new Random();
        int level = 1+Integer.numberOfTrailingZeros(random.nextInt());
        return Math.min(level,maxLevel-1);

    }


    // Add x to list. If x already exists, reject it. Returns true if new node is added to list

    /**
     *  Add x to list. If x already exists, reject it. Returns true if new node is added to list
     * @param x the element you want insert to skip list
     * @return if add success return true
     */
    public boolean add(T x) {
        if (contains(x)){ return false;}
        else {
            int level = chooseLevel();
            Entry<T> entry = new Entry(x,level);
            for(int i=0; i<level;i++) {
                entry.next[i]= pred[i].next[i];
                pred[i].next[i]= entry;
                //entry.pass[i]= pred[i].pass[i]+1;
            }
            size= size+1;
//            valid_pass();
	        return true;
        }

    }
    public void valid_pass(){
        Entry<T> p =head;
        int pass_pre = -1;
        int pass_update=0;
        while (p.next[0].element!=null){
            System.out.println("enter");
            p = p.next[0];
            if(p.element==null) break;
            int level = p.level();
            for (int i=0; i<level;i++){
                pass_update = pass_pre+1;
                p.pass[i]= pass_update;
            }
            pass_pre = pass_update;

        }
        Entry<T> q = head.next[0];
        while (q.element!=null){
//            System.out.println("number "+q.element+ " pass "+Integer.toString(q.pass[0]));
            int level = q.level();
//            System.out.println(" level "+level);
            for(int i =0;i<level;i++) System.out.println(" pass "+Integer.toString(q.pass[i]));
            q = q.next[0];
        }
    }

    // Does list contain x?
    public boolean contains(T x) {
        findPred(x);
        if (pred[0].next[0].element==null|| pred[0].next[0].element.compareTo(x)!=0) return false;
        return true;
    }



    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
	    return getLinear(n);
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
      if(n>size-1) throw new NoSuchElementException();
      Entry<T> p = head;
      for (int i=0;i<n+1;i++){ p=p.next[0];}
        return  p.element;
	//return null;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n).
    public T getLog(int n) {
        if(n>size) return null;
        Entry<T> p = head;
        for (int i = p.level()-1; i >= 0; i--) {
            while (n > p.pass[i] && p.next[i].element != null) {
                p = p.next[i];
            }
            if(p.pass[i]==n) break;

    }
        return p.element;
    }

    // Is the list empty?
    public boolean isEmpty() {
	    return size==0;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        findPred(x);
        Entry<T> p =pred[0].next[0];
        if(pred[0].next[0]!=null) return p.element;
        return null;
    }
    // Return first element of list
    public T first() {
        Entry<T> first = head.next[0];
        return first.element;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        findPred(x);
        if (pred[0].next[0].element.compareTo(x) != 0)
            return pred[0].element;

        return x;
    }

    public Iterator<T> iterator() {
        return null;
    }

    // Return last element of list
    public T last() {
        int last = size-1;
        return get(last);
    }


    // Not a standard operation in skip lists.
    public void rebuild() {

    }


    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if(!contains(x))return null;
        size= size-1;
        Entry entry = pred[0].next[0];
        int level = entry.level();
        for(int i =0;i<level;i++){
            pred[i].next[i]=entry.next[i];
        }
//        valid_pass();
	    return x;
    }

    // Return the number of elements in the list
    public int size() {
	    return this.size;
    }
}
