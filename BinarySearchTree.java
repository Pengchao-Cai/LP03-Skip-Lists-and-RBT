/** @author 
 *  Binary search tree (starter code)
 **/

package lp3;

import java.util.*;

/**
 * Implementing Binary Search Tree without recursion. Save space by using stack for tracking current node's parent.
 * Author: Jie Su jxs190058
 * Author: Pengchao Cai pxc190029
 *
 * @param <T>
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
	    this.left = left;
	    this.right = right;
        }
    }
    
    Entry<T> root;
    int size;
    Stack<Entry> stack;
    public BinarySearchTree() {
	root = null;
	size = 0;
	stack = new Stack<>();
    }

    /**
     * find target x in the BST. if find return Entry whose key is x
     * otherwise return its successor or predecessor
     *
     * @param x
     * @return Entry with x or successor or predecessor
     */
    public Entry find(T x){
        this.stack = new Stack<>();
        stack.push(null);
        Entry t = this.root;
        return findHelper(t, x);
    }
    private Entry findHelper(Entry t, T x){
        if(t == null || t.element == null || x.equals(t.element)){
            return t;
        }
        while (true){
            if(x.compareTo((T)t.element) < 0){
                if(t.left == null || t.left.element==null) break;
                this.stack.push(t);
                t = t.left;
            }
            else if(x.equals((T)t.element)) break;
            else if(t.right == null || t.right.element == null) break;
            else {
                this.stack.push(t); t = t.right;
            }
        }
        return t;
    }

    /**
     * TO DO: Is x contained in tree? if contained return true otherwise return false
     *
     * @param x
     * @return true or false
     */
    public boolean contains(T x) {
        Entry t = find(x);
        if(t == null || !x.equals((T)t.element) || t.element == null){
            return false;
        }
	return true;
    }

    /** TO DO: Is there an element that is equal to x in the tree?
     *  Element in tree that is equal to x is returned, null otherwise.
     *
     *  @param x
     *  @return x or null
     */
    public T get(T x) {
        Entry t = find(x);
        if(t == null || (T)t.element != x) return null;
	    return x;
    }

    /** TO DO: Add x to tree. 
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     *
     *  @param cur
     *  @return true or false
     */
    public Entry add(Entry cur) {
        T x = (T) cur.element;
        if(this.size == 0){
            this.root = cur;
            size++;
            return cur;
        }else{
            Entry t = find(x);
            if(x.equals((T)t.element)) return null;
            else if(x.compareTo((T)t.element) < 0){
                stack.push(t);
                t.left = cur;
            }else{
                stack.push(t);
                t.right = cur;
            }
            size++;
            return cur;
        }
    }

    /** TO DO: Remove x from tree. 
     *  Return x if found, otherwise return null
     *
     *   @param x
     *   @return x or null
     */
    Entry spliced;
    public T remove(T x) {
        if(this.size == 0) return null;
        Entry t = find(x);
        if(!x.equals(t.element)) return null;
        if(t.left == null || t.right == null || t.left.element == null || t.right.element == null){
            splice(t);
            size--;
            return x;
        }else{
            this.stack.push(t);
            Entry minRigth = findHelper(t.right, x);
            t.element = minRigth.element;
            splice(minRigth);
            size--;
            return x;
        }
    }
    public void splice(Entry t){
        Entry parent = this.stack.peek();
        Entry child = (t.left == null || t.left.element == null) ? t.right : t.left;//ATTENTION????
        if(parent == null){
            this.root = child;
        }else if(parent.left == t){
            parent.left = child;
        }else{
            parent.right = child;
        }
        spliced = t;
    }

    /**
     * find minimum element of entries in the BST
     *
     * @return T or null
     */
    public T min() {
        if(this.root == null) return null;
        Entry t = this.root;
        while(t.left != null){
            t = t.left;
        }
	    return (T)t.element;
    }

    /**
     * find maximum element of entries in the BST
     *
     * @return T or null
     */
    public T max() {
        if(this.root == null) return null;
        Entry t = this.root;
        while(t.right != null){
            t = t.right;
        }
        return (T)t.element;
    }

    // TODO: Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
	Comparable[] arr = new Comparable[size];
	/* write code to place elements in array here */
        List<Entry<T>> res = new LinkedList<>();
        inOrderTraverse(this.root, res);
        int i = 0;
        for (Entry<T> e :
                res) {
            arr[i++] = e.element;
        }
	    return arr;
    }
    private void inOrderTraverse(Entry<T> node, List<Entry<T>> res) {
        if(node != null) {
            inOrderTraverse(node.left, res);
            res.add(node);
            inOrderTraverse(node.right, res);
        }
    }

// Start of Optional problem 2

    /** Optional problem 2: Iterate elements in sorted order of keys
	Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
	return null;
    }

    // Optional problem 2.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem 2.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem 2.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem 2.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

// End of Optional problem 2

    public static void main(String[] args) {
	BinarySearchTree<Integer> t = new BinarySearchTree<>();
//        Scanner in = new Scanner(System.in);
        Scanner in = new Scanner("1 3 5 7 9 2 4 6 8 10 -1 -3 -6 -3 -10 0");
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
//                System.out.println("contains?" + t.contains(1));
//                System.out.println("get?" + t.get(1));
                System.out.print("Add " + x + " : ");
                t.add(new Entry(x, null,null));
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                System.out.println("min" + t.min());
                System.out.println("max" + t.max());
                return;
            }           
        }
    }


    public void printTree() {
	System.out.print("[" + size + "]");
	printTree(root);
	System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
	if(node != null) {
	    printTree(node.left);
	    System.out.print(" " + node.element);
	    printTree(node.right);
	}
    }

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
