package part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adt.LinkedTree;
import adt.Position;
import adt.Tree;

public class traversal {

	private static Tree<String> getExampleTree() {
		LinkedTree<String> tree = new LinkedTree<String>();
		Position<String> n0 = tree.addRoot("University Expenses");
		Position<String> n1 = tree.insertChild(n0, "Scholarships");
		Position<String> n2 = tree.insertChild(n0, "Salaries and Wages");
		Position<String> n3 = tree
				.insertChild(n0, "Grounds and Infrastructure");
		Position<String> n4 = tree
				.insertChild(n1, "Other Student Scholarships");
		Position<String> n5 = tree.insertChild(n1,
				"Disadvantaged Student Scholarships");
		Position<String> n6 = tree.insertChild(n3, "Grounds");
		Position<String> n7 = tree.insertChild(n3, "Research Infrastructure");
		Position<String> n8 = tree.insertChild(n3, "Teaching Infrastructure");
		Position<String> n9 = tree.insertChild(n6, "Sporting Facilities");
		Position<String> n10 = tree.insertChild(n6, "Parking Facilities");
		Position<String> n11 = tree.insertChild(n6, "Other Facilities");
		Position<String> n12 = tree.insertChild(n9, "Tennis Facilities");
		Position<String> n13 = tree.insertChild(n9, "Swimming Facilities");
		Position<String> n14 = tree.insertChild(n9, "Athletics Facilities");
		Position<String> n15 = tree
				.insertChild(n9, "Other Sporting Facilities");

		return tree;
	}
	
	public static void print(){
		Tree<String> tree = getExampleTree();
		Position<String> root = tree.root();
		List<String> res = new ArrayList<String>();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Tennis Facilities"));
		List<String> types1 = new ArrayList<String>(Arrays.asList(
				"Disadvantaged Student Scholarships", "Scholarships",
				"Athletics Facilities", "Sporting Facilities",
				"Parking Facilities", "Other Facilities",
				"Research Infrastructure", "Teaching Infrastructure"));
		res = summary(tree, types);
		for(String s:res){
			System.out.println(res);
		}
	}
	


	public static List<String> summary(Tree<String> tree, List<String> types) {
		List<String> res = new ArrayList<String>();
		if(types.isEmpty()){
			return res;
		}
		Position<String> root = tree.root();
		res = summaryTree(types, tree, root); 
		return res;
	}
	
	private static List<String> summaryTree(List<String>types, 
			Tree<String>tree, Position<String>node){
		List<String> res = new ArrayList<String>();
		if(types.contains(node.getElement())){
			return new ArrayList<String>(Arrays.asList(node.getElement()));
		}else{
			if(tree.numChildren(node)>0){
				boolean rootValue = true;
				for(Position<String> e:tree.children(node)){
					List<String> child = summaryTree(types, tree, e);
					if(child != null && child.size()>0){
						res.addAll(child);
					}else{
						rootValue = false;
					}
				}
				if(rootValue){
					res = new ArrayList<String>
					(Arrays.asList(node.getElement()));
				}
				return res;
			}
			return null;
		}
	}
	/**
	 * All nodes
	 * @param t
	 * @param n
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<String> Iterator(Tree<String> t, Position<String> n){
		List<String> temp = new ArrayList<String>();
		List<String> allNodes = new ArrayList<String>();
		for(Position<String> index : t.children(n)){
			allNodes.add(index.getElement());
			if(t.children(index)!= null && t.numChildren(index) > 0){
				temp = Iterator(t, index);
				for(String s : temp){
					allNodes.add(s);
				}
			}
		}
		return allNodes;
	}
	
	public static void removeDuplicate(List<String> list) {
		HashSet<String> h = new HashSet<String>(list);
		list.clear();
		list.addAll(h);
		System.out.println(list);
	} 

	private static List<String> summaryA(Tree<String> tree,Position<String> root, List<String> types){
		List<String> rVal=new ArrayList<String>();
		if (types.contains(root.getElement())){
			return new ArrayList<String>(Arrays.asList((String)root.getElement()));
		}else{
			if (tree.numChildren(root)>0){
				boolean relValeRoot=true;
				for(Position<String> e :tree.children(root)){
					List<String> childRval=summaryA(tree,e,types);
					if (childRval!=null && childRval.size()>0 ){
						rVal.addAll(childRval);
					}else{
						relValeRoot=false;
					}
				}
				if(relValeRoot==true){
					rVal=new ArrayList<String>(Arrays.asList((String)root.getElement()));
				}
				return rVal;
			}
			return null;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		print();
	}

}
