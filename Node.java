/**
* Generic singly-linked node in SortedList, to enable reordering.
*/
public class Node<E> {
	private final E element;
	private Node<E> next;

	public Node(E newElement){
		element = newElement;
		next = null;
	}

	public E getElement(){
		return element;
	}

	public Node<E> getNext(){
		return next;
	}

	//order from within Node or SortedList class?
	public void setNext(Node<E> newNext){
		next = newNext;
	}
}