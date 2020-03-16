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

	private boolean wellFormed () {
		if(manyNodes < 0 )return false;
		if(dummy == null) return false;
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

		assert wellFormed();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean offer(Object e) {

		assert wellFormed();
		if(e == null) throw new NullPointerException("data can't be null");
		Node<E> n = new Node (e,null,null);
		n.next = dummy;
		dummy.prev.next = n;
		n.prev = dummy.prev;
		dummy.prev = n;
		if(manyNodes == 0) dummy.next = n;
		++manyNodes;
		assert wellFormed();

		return true;
	}

	@Override
	public Object poll() {
		// TODO Auto-generated method stub
		assert wellFormed();
		Object s = dummy.next.data;
		dummy.next.data = null;
		dummy.next.prev = dummy;
		dummy.next = dummy.next.next;
		if(manyNodes != 0) --manyNodes;
		assert wellFormed();

		return s;
	}

	@Override
	public Object peek() {
		// TODO Auto-generated method stub

		return dummy.next.data;
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		assert wellFormed();
		return new MyIterator();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub

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
			// TODO Auto-generated method stub
			assert wellFormed() : "invariant broken in hasNext()";
			Node n = cursor;
			while(n != dummy && n.data == null) {
				n = n.prev;
			}

			if(n.next == dummy || manyNodes ==0) return false;

			return true;
		}

		@Override
		public E next() {
			// TODO Auto-generated method stub
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
