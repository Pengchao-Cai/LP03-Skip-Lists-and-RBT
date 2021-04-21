/** Starter code for Red-Black Tree
 */
package lp3;

import java.util.Comparator;
import java.util.*;

/**
 * Extending BST to Red-Black Tree: Implementing add and remove operations
 * Author:
 * Jie Su jxs190058
 * Pengchao Cai pxc190029
 * Feng Mi fxm150930
 * Linqian Zhu lxz190009
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private static Entry NILL_NODE = new RedBlackTree.Entry<>(null,null,null);

    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        boolean isRed() {
	    return color == RED;
        }

        boolean isBlack() {
	    return color == BLACK;
        }
    }


    RedBlackTree() {
	super();
    }

    /**
     * add node to red-black tree. If succeed return true,otherwise return false
     *
     * @param x
     * @return true or false
     */
    public boolean add(T x){
        NILL_NODE.color = BLACK;
        Entry cur = new Entry<>(x,NILL_NODE,NILL_NODE);
        cur = (Entry) super.add(cur);
        if(cur == null || cur.element == null) return false;
        if(cur == root.right || cur == root.left){
            return true;
        }
        while(cur != root  && getParent(cur).isRed()){ //make sure when need to poll stack
            Entry parent = getParent(cur);
            Entry grandparent = getGrandParent(cur);
            if(compare(parent, grandparent) < 0){ // parent is the left child of grandparent
                Entry uncle = (Entry) grandparent.right;
                if(uncle.isRed()){ // case 1
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    cur = grandparent;
                    stack.pop();
                    stack.pop();
                    cur.color = RED;
                }else{
                    if(compare(cur, parent) > 0){ // case 2
                        cur = parent;
                        stack.pop();
                        Entry y = leftRotate(cur);
                        stack.push(y);
                    }
                    Entry par = getParent(cur); // case 3
                    Entry grPar = getGrandParent(cur);
                    par.color = BLACK;
                    grPar.color = RED;
                    stack.pop();
                    stack.pop();
                    Entry y = rightRotate(grPar);
                    stack.push(y);

                }

            }else{
                Entry uncle = (Entry) grandparent.left;
                if(uncle.isRed()){ // case 1
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    cur = grandparent;
                    stack.pop();
                    stack.pop();
                    cur.color = RED;
                }else{
                    if(compare(cur,parent) < 0){ // case 2
                        cur = parent;
                        stack.pop();
                        Entry y = rightRotate(cur);
                        stack.push(y);
                    }
                    Entry par = getParent(cur); // case 3
                    Entry grPar = getGrandParent(cur);
                    par.color = BLACK;
                    grPar.color = RED;
                    stack.pop();
                    stack.pop();
                    Entry y = leftRotate(grPar); //need to stack pop??? or push
                    stack.push(y);
                }
            }
        }
        Entry root = (Entry) this.root;
        root.color = BLACK;
        return true;
    }

    /**
     * remove node whose element is x from red-black tree. if succeed return x, otherwise return null;
     *
     * @param x
     * @return null or x
     */
    public T remove (T x){
        T res = super.remove(x);
        if(res == null) return null;
        Entry cur = (Entry) this.spliced;
        if(root == null || root.element == null){return res;}
        if(root.left == NILL_NODE && root.right == NILL_NODE){return res;}
        if(cur.isBlack()){
            fixup(cur);
        }
        return res;
    }
    private void fixup(Entry cur){
        while(cur != (Entry) root && cur.isBlack()){
            Entry parent = getParent(cur);
            if(compare(cur,parent) < 0){
                Entry sib = (Entry)parent.right;
                if(sib.isRed()){ //case 1
                    sib.color = BLACK;
                    parent.color = RED;
                    Entry curParent = (Entry) stack.pop();
                    Entry y = leftRotate(parent);
                    stack.push(y);
                    stack.push(curParent);
                }
                sib = getSib(cur);
                if((sib.isBlack() && ((Entry)sib.left).isBlack() && ((Entry)sib.right).isBlack())){ //case 2
                    sib.color = RED;
                    cur = getParent(cur);
                    if(cur.isRed()) {
                        cur.color = BLACK;
                        break;
                    }
                    stack.pop();
                }else { //case 3
                    sib = getSib(cur);
                    parent = getParent(cur);
                    if(((Entry) sib.right).isBlack()){
                        ((Entry)sib.left).color = BLACK;
                        sib.color = RED;
                        rightRotate(sib);
                    }
                    //case 4
                    sib = getSib(cur);
                    ((Entry)sib.right).color = BLACK;
                    sib.color = parent.color;
                    parent.color = BLACK;
                    stack.pop();
                    leftRotate(parent);
                    cur = (Entry) root;
                }
            }else{
                Entry sib = (Entry)parent.left;
                parent = getParent(cur);
                if(sib.isRed()){ //case 1
                    sib.color = BLACK;
                    parent.color = RED;
                    Entry curParent = (Entry) stack.pop();
                    Entry y = rightRotate(parent);
                    stack.push(y);
                    stack.push(curParent);
                }
                 sib = getSib(cur);
                if((sib.isBlack() && ((Entry)sib.left).isBlack() && ((Entry)sib.right).isBlack())){ //case 2
                    sib.color = RED;
                    cur = getParent(cur);
                    if(cur.isRed()) {
                        cur.color = BLACK;
                        break;
                    }
                    stack.pop();
                }else { //case 3
                    sib = getSib(cur);
                    parent = getParent(cur);
                    if(((Entry)sib.left).isBlack()){
                    ((Entry)sib.right).color = BLACK;
                    sib.color = RED;
                    leftRotate(sib);
                    }
                    //case 4
                    sib = getSib(cur);
                    ((Entry)sib.left).color = BLACK;
                    sib.color = parent.color;
                    parent.color = BLACK;
                    stack.pop();
                    rightRotate(parent);
                    cur = (Entry) root;
                }

            }

        }
        cur.color = BLACK;
    }

    /**
     * left rotate the tree based on Entry x. if succeed return new parent of x
     *
     * @param x
     * @return new parent of x
     */
    private Entry leftRotate(Entry x){
        Entry parent = getParent(x);
        Entry y = (Entry) x.right;
        x.right = y.left;
        if(parent == NILL_NODE){
            this.root = y;
        }
        else if(x == parent.left){
            parent.left = y;
        }else{
            parent.right = y;
        }
        y.left = x;
        return y;
    }

    /**
     * right rotate the tree based on Entry x. if succeed return new parent of x
     *
     * @param x
     * @return new parent of x
     */
    private Entry rightRotate(Entry x){
        Entry parent = getParent(x);
        Entry y = (Entry) x.left;
        x.left = y.right;
        if(parent == NILL_NODE){
            this.root = y;
        }
        else if(x == parent.left){
            parent.left = y;
        }else{
            parent.right = y;
        }
        y.right = x;
        return y;
    }

    /**
     * get the parent of Entry cur, if no parent, return NILL_NODE
     *
     * @param cur
     * @return parent or NILL_NODE
     */
    public Entry<T> getParent(Entry<T> cur){
        NILL_NODE.color = BLACK;
        if (!this.stack.isEmpty() && stack.peek() != null) {
            return (Entry) this.stack.peek();
        } else {
            return NILL_NODE;
        }
    }

    /**
     * get the grandparent of Entry cur
     *
     * @param cur
     * @return grandparent of cur
     */
    public Entry getGrandParent(Entry cur){
        Entry parent= (Entry) this.stack.pop();
        Entry grandParent= (Entry) this.stack.peek();
        this.stack.push(parent);
        return grandParent;
    }

    /**
     * get the sibling of Entry cur
     *
     * @param cur
     * @return sibling of cur
     */
    public Entry getSib(Entry cur){
        Entry parent = getParent(cur);
        return (Entry) (compare(cur,parent) < 0 ? parent.right:parent.left);
    }

    int numOfBlack = Integer.MIN_VALUE;

    /**
     * verify the Red-Black Tree, if it is, return true, otherwise return false
     *
     * @return true or false
     */
    public boolean verifyRBT(){
        Entry root = (Entry) this.root;
        if(root.isRed()){
            return false;
        }
        numOfBlack = Integer.MIN_VALUE;
        NILL_NODE.color = BLACK;
        return verifyHelper(root, BLACK, 1);
    }
    private boolean verifyHelper(Entry cur,boolean parentColor, int blackCount){
        if(cur == NILL_NODE){
            if(numOfBlack == Integer.MIN_VALUE){
                numOfBlack = blackCount;
                return true;
            }else if(blackCount != numOfBlack){
                System.out.println("black count");
                return false;
            }else return true;
        }
        if(parentColor == RED){
            if(cur.isRed()){
                System.out.println("adjacent red");
                return false;
            }
        }
        if(cur.isBlack()) blackCount++;
        if(!verifyHelper((Entry) cur.left, cur.color, blackCount)) return false;
        if(!verifyHelper((Entry) cur.right, cur.color, blackCount)) return false;
        return true;
    }

    /**
     * compare two Entries based on their elements
     *
     * @param a
     * @param b
     * @return -1 or 1 or 0
     */
    public int compare (Entry a, Entry b)
    {
        return ((T)a.element).compareTo((T)b.element);
    }

    public static void main(String[] args) {
        RedBlackTree<Integer> rb = new RedBlackTree<>();
        int[] arr = new int[]{10,11,12,13,14,15,1,2,3,4,5,6,7,8,9,79,23,37,34,66,77};
        for(int num : arr){
            System.out.println("add" + num);
            rb.add(num);
            boolean is = rb.verifyRBT();
            System.out.println("Is RBT "+rb.verifyRBT());
            if(!is) break;
        }

        rb.remove(23);
        System.out.println("is rbt" + rb.verifyRBT());
        System.out.println("remove" + 23);

    }

}

