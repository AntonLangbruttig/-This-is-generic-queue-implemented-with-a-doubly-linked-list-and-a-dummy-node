// Anton Langbruttig
package edu.uwm.cs351.util;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;


/******************************************************************************
 * This is generic queue implemented with a doubly-linked list and a dummy node
 ******************************************************************************/


public class RobustQueue <E> extends AbstractQueue<E> {

	private Node <E> dummy;
	private int manyNodes = 0;

	private static class Node <T> {
		T data;
		Node<T> next, prev;
		Node(T d){
			data = d;
		}

	}

	private static boolean doReport = true;

	private boolean report(String error) {
		if (doReport) System.out.println("Invariant error: " + error);
		else System.out.println("Caught problem: " + error);
		return false;
	}

	private boolean wellFormed () {
		int count = 0;
		if (dummy.next != dummy) {
			Node<E> n = dummy.next;
			do {
				++count;
				Node<E> p = n;
				n = n.next;
				if (n == null) return report("found null after " + count + " nodes.");
				if (n.prev != p) return report("found bad link after " + count + " nodes.");
			} while (n != dummy);
		}
		if (manyNodes != count ) return report("manyNodes is " + manyNodes + ", not matching actual count=" + count);
		if(dummy == null) return report ("dummy can't be null");
		return true;

	}
	private Node<E> node(E d){
		return new Node<E>(d);
	}

	/**
	 * Instantiates a new RobustQueue.
	 *
	 * @postcondition
	 * 	This RobustQueue is empty except for the dummy node
	 */
	public RobustQueue() {
		dummy = node(null);
		dummy.next = dummy;
		dummy.prev = dummy;

		assert wellFormed(): "invariant broken at beginning";;
	}

	@Override
	public boolean offer(E e) {
		assert wellFormed(): "invariant broken at start of offer()";;

		if(e == null) throw new NullPointerException("data can't be null");
		Node<E> n = node(e);
		n.next = dummy;
		dummy.prev.next = n;
		n.prev = dummy.prev;
		dummy.prev = n;
		if(manyNodes == 0) dummy.next = n;
		++manyNodes;

		assert wellFormed(): "invariant broken at end of offer()";;

		return true;
	}

	@Override 
	public E poll() {
		assert wellFormed(): "invariant broken at start of poll()";;

		E s = dummy.next.data;
		dummy.next.data = null;
		dummy.next.prev = dummy;
		dummy.next = dummy.next.next;
		if(manyNodes != 0) --manyNodes;

		assert wellFormed(): "invariant broken at end of poll()";;

		return s;
	}

	@Override  
	public E peek() {
		assert wellFormed(): "invariant broken at start of peek()";;
		return dummy.next.data;
	}

	@Override  
	public Iterator<E> iterator() {
		assert wellFormed(): "invariant broken at start of iterator()";;
		return new MyIterator();
	}

	@Override  
	public int size() {
		assert wellFormed(): "invariant broken at start of size()";;
		return manyNodes;
	}

	private class MyIterator implements Iterator<E> {

		Node <E> cursor = dummy;

		private boolean wellFormed() {
			if(!RobustQueue.this.wellFormed()) return false;

			return true;
		}

		@Override  
		public boolean hasNext() {
			assert wellFormed() : "invariant broken in hasNext()";

			Node <E> n = cursor;
			while(n != dummy && n.data == null) {
				n = n.prev;
			}

			if(n.next == dummy || manyNodes == 0) return false;

			return true;
		}

		@Override 
		public E next() {
			if(!hasNext()) throw new NoSuchElementException ("no next");

			while(cursor != dummy && cursor.data == null) {
				cursor = cursor.prev;
			}
			cursor = cursor.next;

			return (E) cursor.data;
		}
		@Override 
		public void remove() {
			assert wellFormed() : "invariant broken in remove()";
			if(cursor.data == null || manyNodes == 0 ) throw new IllegalStateException ("cursor is not true");

			cursor.data = null;
			cursor.next.prev = cursor.prev;
			cursor.prev.next = cursor.next;
			--manyNodes;
			if( dummy.next == cursor) dummy.next = cursor.next;
		}

	}
}
