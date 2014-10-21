package part2;

import adt.*;

import java.util.*;

public class ExpenditureTrees {

	/**
	 * This method takes a tree describing a hierarchical classification scheme
	 * of expenditures, and a list of expenditure types from that tree and
	 * returns the <b>summary</b> of that list of types (as described in the
	 * assignment handout). The summary is calculated with respect to the given
	 * tree.
	 * 
	 * The expenditure types in the tree and the list are denoted by non-null
	 * Strings. Two types are the same if their String representations are equal
	 * (using the ".equals()" method). The same type may not occur in two
	 * different positions in the tree. The same type may not appear twice in
	 * the given list of types either. Each type in the list should be equal to
	 * one in the tree.
	 * 
	 * This method assumes that the parameters tree and types are non-null and
	 * that the tree is non-empty. The list of types may be an empty list.
	 * 
	 * The <b>summary</b> of the list of types is returned by the method as a
	 * non-null list of expenditure types. The returned list should not contain
	 * duplicate types (since it denotes a set).
	 * 
	 * This method should not modify its parameters in any way.
	 */
	public static List<String> summary(Tree<String> tree, List<String> types) {
		List<String> res = new ArrayList<String>();
		if(types.isEmpty()){
			return res;
		}
		Position<String> root = tree.root();
		res = summaryTree(types, tree, root); 
		return res;
	}
	/**
	 * Search every level in the tree. The node will be added into result list 
	 * if and only if all children of this arbitrary node are in type list.
	 * And it will do it recursively.
	 * @param types
	 * @param tree
	 * @param node
	 * @return the summary result of the tree.
	 */
	private static List<String> summaryTree(List<String>types, 
			Tree<String>tree, Position<String>node){
		List<String> res = new ArrayList<String>();
		if(types.contains(node.getElement())){
			return new ArrayList<String>(Arrays.asList(node.getElement()));
		}else{
			if(tree.numChildren(node)>0){
				boolean rootValue=true;// Check if the result will be root
				for(Position<String> e:tree.children(node)){
					List<String> childSummary = summaryTree(types, tree, e);
					if(childSummary != null && childSummary.size()>0){
						res.addAll(childSummary);
						rootValue&=(childSummary.get(0).equals(e.getElement()));
					}else{
						rootValue=false;
					}
				}
				if(rootValue){
					res=new ArrayList<String>(Arrays.asList(node.getElement()));
				}
				return res;
			}
			return null;
		}
	}
	
	/**
	 * This method takes a tree describing a hierarchical classification scheme
	 * of expenditures, a <b>concise</b> list of expenditure types from that
	 * tree, and an expenditure type from the tree and returns true if the given
	 * expenditure type is in the <b>closure</b> of the concise list of types,
	 * and false otherwise. (See the assignment handout for definitions.)
	 * 
	 * The expenditure types in the tree and the list are denoted by non-null
	 * Strings. Two types are the same if their String representations are equal
	 * (using the ".equals()" method). The same type may not occur in two
	 * different positions in the tree. The same type may not appear twice in
	 * the given list of types either. Each type in the list should be equal to
	 * one in the tree. The given expenseType should also be equal to one in the
	 * tree.
	 * 
	 * This method assumes that the parameters tree, types and expenseType are
	 * non-null and that the tree is non-empty. The list of types may be an
	 * empty list.
	 * 
	 * This method should not modify its parameters in any way.
	 */
	public static boolean contains(Tree<String> tree, List<String> types,
			String expenseType) {
		if(types.isEmpty()){
			return false;
		}
		Position<String> root = tree.root();
		if(types.contains(expenseType)) return true;
		List<String> summaryList = summaryTree(types, tree, root);
		if(summaryList.contains(expenseType)) return true;
		List<String> NewTypes=new ArrayList<String>(summaryList);
		NewTypes.add(expenseType);
		return  summaryList.equals(summaryTree(NewTypes, tree, root));
	}

	/**
	 * This method takes a tree describing a hierarchical classification scheme
	 * of expenditures, and a list of expenditure types from that tree and
	 * returns the <b>negation</b> of that list of types (as described in the
	 * assignment handout). The negation is calculated with respect to the given
	 * tree.
	 * 
	 * The expenditure types in the tree and the list are denoted by non-null
	 * Strings. Two types are the same if their String representations are equal
	 * (using the ".equals()" method). The same type may not occur in two
	 * different positions in the tree. The same type may not appear twice in
	 * the given list of types either. Each type in the list should be equal to
	 * one in the tree.
	 * 
	 * This method assumes that the parameters tree and types are non-null and
	 * that the tree is non-empty. The list of types may be an empty list.
	 * 
	 * The <b>negation</b> of the list of types is returned by the method as a
	 * non-null list of expenditure types. The returned list should not contain
	 * duplicate types (since it denotes a set).
	 * 
	 * This method should not modify its parameters in any way.
	 */
	public static List<String> negation(Tree<String> tree, List<String> types) {
		List<String> res = new ArrayList<String>();
		if(types.isEmpty()){
			return res;
		}
		return negationTree(types, tree,tree.root());
	}
	/**
	 * The results in negation list plus the elements in 
	 * type list would consist the whole tree
	 * @param tree
	 * @param node
	 * @param types
	 * @return the result for negation tree.
	 */
	public static List<String> negationTree(List<String> types, 
			Tree<String> tree, Position<String> node) {
		if (types.contains(node.getElement())) return null;
		if (tree.numChildren(node)>0){
			boolean parentInstead = true;//Check parent.
			List<String> res = new ArrayList<String>();
			for(Position<String> child : tree.children(node)){
				List<String> childNegation = negationTree(types, tree, child);
				if(childNegation != null){//Check if the list is null or not.
					res.addAll(childNegation);
					if(!childNegation.contains(child.getElement())){
						parentInstead = false;
					}
				}else{
					parentInstead = false;
				}
			}
			if(!parentInstead){
				return res;
			}
		}
		return new ArrayList<String>(Arrays.asList(node.getElement()));
	}
}
