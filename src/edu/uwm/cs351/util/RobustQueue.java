// Anton Langbruttig
package edu.uwm.cs351.util;
 
/******************************************************************************
 * This is generic queue implemented with a doubly-linked list and a dummy node
 ******************************************************************************/

public class RobustQueue <E> {

	private Node <E> head;
	private int manyNodes = 0;
	
	
	
	static class Node <T> {
		private T data;
		private Node<T> next;
		private Node<T> prev;
		Node(T t, Node<T> n, Node <T> p){
			data = t;
			next = n;
			prev = p;
		}
	}
}
