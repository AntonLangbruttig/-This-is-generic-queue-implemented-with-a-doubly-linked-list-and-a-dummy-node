// Anton Langbruttig
package edu.uwm.cs351.util;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;



/******************************************************************************
 * This is generic queue implemented with a doubly-linked list and a dummy node
 ******************************************************************************/

@SuppressWarnings("rawtypes")
public class RobustQueue <E> extends AbstractQueue {

	private Node <E> dummy;
	private int manyNodes = 0;

	static class Node <T> {
		T data;
		Node<T> next, prev;
		Node(T t, Node<T> n, Node <T> p){
			data = t;
			next = n;
			prev = p;
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

	/**
	 * Instantiates a new RobustQueue.
	 *
	 * @postcondition
	 * 	This RobustQueue is empty except for the dummy node
	 */
	public RobustQueue() {
		dummy = new Node <E>(null,null,null);
		dummy.next = dummy;
		dummy.prev = dummy;

		assert wellFormed(): "invariant broken at beginning";;
	}

	@SuppressWarnings("unchecked")
	@Override  // required
	public boolean offer(Object e) {
		assert wellFormed(): "invariant broken at start of offer()";;

		if(e == null) throw new NullPointerException("data can't be null");
		Node<E> n = new Node  (e,null,null);
		n.next = dummy;
		dummy.prev.next = n;
		n.prev = dummy.prev;
		dummy.prev = n;
		if(manyNodes == 0) dummy.next = n;
		++manyNodes;

		assert wellFormed(): "invariant broken at end of offer()";;

		return true;
	}

	@Override  // required
	public Object poll() {
		assert wellFormed(): "invariant broken at start of poll()";;

		Object s = dummy.next.data;
		dummy.next.data = null;
		dummy.next.prev = dummy;
		dummy.next = dummy.next.next;
		if(manyNodes != 0) --manyNodes;

		assert wellFormed(): "invariant broken at end of poll()";;

		return s;
	}

	@Override  // required
	public Object peek() {
		assert wellFormed(): "invariant broken at start of peek()";;
		return dummy.next.data;
	}

	@Override  // required
	public Iterator iterator() {
		assert wellFormed(): "invariant broken at start of iterator()";;
		return new MyIterator();
	}

	@Override  // required
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

		@Override  // required
		public boolean hasNext() {
			assert wellFormed() : "invariant broken in hasNext()";

			Node n = cursor;
			while(n != dummy && n.data == null) {
				n = n.prev;
			}

			if(n.next == dummy || manyNodes == 0) return false;

			return true;
		}

		@Override  // required
		public E next() {
			if(!hasNext()) throw new NoSuchElementException ("no next");

			while(cursor != dummy && cursor.data == null) {
				cursor = cursor.prev;
			}
			cursor = cursor.next;

			return (E) cursor.data;
		}
		@Override  // required
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
