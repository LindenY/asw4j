package ca.uwaterloo.asw4j.internal;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingLinkedQueue<E>{

	private AtomicReference<Node> head;
	private AtomicReference<Node> tail;
	private AtomicInteger size;
	
	public NonBlockingLinkedQueue() {
		head = new AtomicReference<NonBlockingLinkedQueue.Node>();
		tail = new AtomicReference<NonBlockingLinkedQueue.Node>();
		size = new AtomicInteger(0);
	}

	public void clear() {
		head.set(null);
		tail.set(null);
		size.set(0);
	}

	public boolean isEmpty() {
		return (size.get() <= 0);
	}

	public int size() {
		return size.get();
	}

	public void push(E e) {
		Node newNode = new Node(e);
		size.incrementAndGet();
		
		if (head.get() == tail.get() 
				&& head.get() == null) {
			head.set(newNode);
			tail.set(newNode);	
			return;
		}
		
		Node oldTail = tail.getAndSet(newNode);
		oldTail.next = newNode;
	}

	@SuppressWarnings("unchecked")
	public E peek() {
		return (E) (isEmpty() ? null : head.get().value); 
	}

	@SuppressWarnings("unchecked")
	public E poll() {
		if (isEmpty()) {
			return null;
		}
		
		size.decrementAndGet();
		if (head.get() == tail.get()) {
			head.set(null);
			return (E) tail.getAndSet(null).value;
		}
		return (E) head.getAndSet(head.get().next).value;
	}
	
	public Object[] toArray() {
		Node pointer = head.get();
		Object[] objs = new Object[size()];
		
		for (int i=0; i<size(); i++) {
			objs[i] = pointer.value;
			pointer = pointer.next;
		}
		
		return objs;
	}
	
	private static class Node {
		
		private Node next;
		private Object value;
		
		public Node(Object obj) {
			value = obj;
		}
	}
}
