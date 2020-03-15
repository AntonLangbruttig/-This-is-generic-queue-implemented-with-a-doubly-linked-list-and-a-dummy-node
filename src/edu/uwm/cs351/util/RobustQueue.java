// Anton Langbruttig
package edu.uwm.cs351.util;
import java.util.AbstractQueue;
import java.util.Iterator;
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

		Node<E> n = new Node (e,null,null);
		n.next = dummy;
		dummy.prev.next = n;
		n.prev = dummy.prev;
		dummy.prev = n;

		++manyNodes;
		assert wellFormed();

		return true;
	}

	@Override
	public Object poll() {
		// TODO Auto-generated method stub
		assert wellFormed();



		Node s = dummy.next;

		dummy.next.prev = dummy;
		dummy.next = dummy.next.next;
		if(manyNodes != 0) --manyNodes;
		assert wellFormed();

		return s.data;
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

		Node cursor = dummy;

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public E next() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
