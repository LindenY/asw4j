package ca.uwaterloo.asw4j.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An implementation of {@link Queue} which is thread-safe.
 * 
 * <p>
 * {@link NonBlockingLinkedQueue} uses lock free algorithm to avoid the usage of
 * lock.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.1
 * 
 * @param <E>
 */
public class NonBlockingLinkedQueue<E> implements Queue<E> {

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

	public Iterator<E> iterator() {
		return new Iterator<E>() {

			private Node pointer;

			public boolean hasNext() {
				return (pointer != null);
			}

			@SuppressWarnings("unchecked")
			public E next() {
				if (pointer == null) {
					throw new NoSuchElementException();
				}
				E e = (E) pointer.value;
				pointer = pointer.next;
				return e;
			}

			private Iterator<E> init(Node head) {
				this.pointer = head;
				return this;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		}.init(head.get());
	}

	public boolean addAll(Collection<? extends E> c) {
		Iterator<? extends E> iterator = c.iterator();
		while (iterator.hasNext()) {
			add(iterator.next());
		}
		return true;
	}

	public boolean add(E e) {
		assertNull(e);

		Node newNode = new Node(e);
		size.incrementAndGet();

		if (head.get() == tail.get() && head.get() == null) {
			head.set(newNode);
			tail.set(newNode);
			return true;
		}

		Node oldTail = tail.getAndSet(newNode);
		oldTail.next = newNode;
		return true;
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

	public E element() {
		E e = peek();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
	}

	public boolean offer(E e) {
		return add(e);
	}

	public E remove() {
		E e = poll();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
	}

	public Object[] toArray() {
		Node pointer = head.get();
		Object[] objs = new Object[size()];

		for (int i = 0; i < size(); i++) {
			objs[i] = pointer.value;
			pointer = pointer.next;
		}

		return objs;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size()) {
			return (T[]) toArray();
		}

		Node pointer = head.get();
		if (pointer == null) {
			a[0] = null;
			return a;
		}

		if (!a.getClass().getComponentType()
				.isAssignableFrom(pointer.value.getClass())) {
			throw new ArrayStoreException();
		}

		for (int i = 0; i < size(); i++) {
			a[i] = (T) pointer.value;
			pointer = pointer.next;
		}
		if (a.length > size()) {
			a[size()] = null;
		}
		return a;
	}

	public boolean contains(Object o) {
		assertNull(o);

		Iterator<E> iterator = iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(o)) {
				return true;
			}
		}

		return false;
	}

	public boolean containsAll(Collection<?> c) {
		Iterator<?> iterator = c.iterator();
		while (iterator.hasNext()) {
			if (!contains(iterator.next())) {
				return false;
			}
		}
		return true;
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException(
				"Removing a specific object is not supported.");
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException(
				"Removing specific objects is not supported.");
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException(
				"RetainAll is currently not supported.");
	}

	private void assertNull(Object obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
	}

	private static class Node {

		private Node next;
		private Object value;

		public Node(Object obj) {
			value = obj;
		}
	}
}
