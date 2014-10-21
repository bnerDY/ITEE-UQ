package problem;

import java.util.LinkedList;
import java.util.List;

/**
 * Generic tree node
 * @author Joshua Song
 *
 * @param <T> Data type
 */
public class Node<T> {
	
	/** The node's data */
	private T data;
	/** Reference to the parent node, null if there is no parent */
	private Node<T> parent;
	/** The children of this node */
	private List<Node<T>> children;
	
	double nVisits, totalValue;
	
	Action action;
	
	public Node() {
		this.data = null;
		this.nVisits = 0;
		this.totalValue = 0;
		this.action = null;
		this.parent = null;
		this.children = new LinkedList<Node<T>>();
	}

	public Node(T data) {
		this.data = data;
		this.nVisits = 0;
		this.totalValue = 0;
		this.action = null;
		this.parent = null;
		this.children = new LinkedList<Node<T>>();
	}
	
	public Node(T data, Node<T> parent) {
		this.data = data;
		this.nVisits = 0;
		this.totalValue = 0;
		this.action = null;
		this.parent = parent;
		parent.addChild(this);
		this.children = new LinkedList<Node<T>>();
	}
	
	public Node(T data, Action a, Node<T> parent) {
		this.data = data;
		this.nVisits = 0;
		this.totalValue = 0;
		this.action = a;
		this.parent = parent;
		parent.addChild(this);
		this.children = new LinkedList<Node<T>>();
	}

	public T getData() {
		return data;
	}

	public Node<T> getParent() {
		return parent;
	}
	
	public Double getVisit() {
		return nVisits;
	}
	
	public Double getValue() {
		return totalValue;
	}
	
	public Action getAction() {
		return action;
	}

	public List<Node<T>> getChildren() {
		return children;
	}
	
	public void clearChildren() {
		this.children = new LinkedList<Node<T>>();
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public void addVisit(double num) {
		this.nVisits += num;
	}
	
	public void setValue(double value) {
		this.totalValue = value;
	}
	
	public void setAction(Action a) {
		this.action = a;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public void addChild(Node<T> child) {
		child.setParent(this);
		children.add(child);
	}

	public List<Node<T>> getAncestry() {
		LinkedList<Node<T>> result = new LinkedList<Node<T>>();
		getAncestryHelper(result);
		return result;
	}
	
	private void getAncestryHelper(LinkedList<Node<T>> ancestry) {
		ancestry.addFirst(this);
		if (parent == null) {
			return;
		}
		parent.getAncestryHelper(ancestry);
	}
	
	public List<T> getDataAncestry() {
		List<Node<T>> temp = getAncestry();
		List<T> result = new LinkedList<T>();
		for (Node<T> n : temp) {
			result.add(n.getData());
		}
		return result;
	}
}