package part2.test;

import org.junit.*;

import part2.ExpenditureTrees;
import adt.*;

import java.util.*;

/**
 * Basic tests for the methods summary, contains and negation from class
 * ExpenditureTrees. A much more extensive test suite will be performed for
 * assessment of your code, but this should get you started.
 */
public class ExpenditureTreeTest {

	/**
	 * A basic test for the summary method.
	 */
	@Test
	public void testSummary() {
		Tree<String> tree = getExampleTree();

		List<String> types = new ArrayList<String>(Arrays.asList(
				"Disadvantaged Student Scholarships", "Scholarships",
				"Athletics Facilities", "Sporting Facilities",
				"Parking Facilities", "Other Facilities",
				"Research Infrastructure", "Teaching Infrastructure"));

		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				"Scholarships", "Grounds and Infrastructure"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));

	}
	@Test
	public void testNode(){
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Scholarships",
				"Salaries and Wages", "Grounds and Infrastructure"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				"University Expenses"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testExternal(){
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Scholarships",
				 "Grounds and Infrastructure", "Tennis Facilities"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				"Scholarships",
				 "Grounds and Infrastructure"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testSingle(){
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Tennis Facilities"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "Tennis Facilities"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	/**
	 * wrost tree test
	 */
	@Test
	public void testS1(){
		Tree<String>tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"b"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "a"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS2(){
		Tree<String>tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"b","c"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "a"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS3(){
		Tree<String>tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"j"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "a"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS4(){
		Tree<String>tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"b","c","d","e","f","g","h","i","j"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "a"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	/**
	 * binary tree test.
	 */
	@Test
	public void testS5(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"o","n"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "g"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS6(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"b","c"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "a"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS7(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"n"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "n"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS8(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"e","f","g","h","i"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "a"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS9(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"e","f"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "e","f"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS10(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"e","d"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "b"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS11(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"f","g"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "c"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS12(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"h","i","e","f"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "b","f"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS13(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"j","k","d","f"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "b","f"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS14(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"n","o","f"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "c"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	@Test
	public void testS15(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"l","m","o"));
		List<String> expectedSummary = new ArrayList<String>(Arrays.asList(
				 "f","o"));
		List<String> actualSummary = ExpenditureTrees.summary(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedSummary),
				new HashSet<String>(actualSummary));
	}
	/**
	 * A basic test for the contains method.
	 */
	@Test
	public void testContains() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Scholarships", "Grounds and Infrastructure"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"Salaries and Wages"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"University Expenses"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"Parking Facilities"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"Other Student Scholarships"));
		Assert.assertTrue(ExpenditureTrees
				.contains(tree, types, "Scholarships"));

	}
	@Test
	public void testC1() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Scholarships","Salaries and Wages",
				"Grounds and Infrastructure"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"University Expenses"));
	}
	/**
	 * wrost tree test.
	 */
	@Test
	public void testC2() {
		Tree<String> tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"c"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"a"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"b"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"d"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"e"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types, 
				"f"));

	}
	/**
	 * binary tree test
	 */
	@Test
	public void testC3(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"b","c"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"a"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"d"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"e"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"f"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"g"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"h"));
	}
	@Test
	public void testC4(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"o"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"a"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"b"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"c"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"d"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"e"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"f"));
	}
	@Test
	public void testC5(){
		Tree<String>tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"f","e"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"f"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"e"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"a"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"b"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"c"));
		Assert.assertFalse(ExpenditureTrees.contains(tree, types,
				"d"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"e"));
		Assert.assertTrue(ExpenditureTrees.contains(tree, types,
				"f"));
	}

	/**
	 * A basic test for the negation method.
	 */
	@Test
	public void testNegation() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Parking Facilities", "Scholarships"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"Salaries and Wages", "Sporting Facilities",
				"Other Facilities", "Research Infrastructure",
				"Teaching Infrastructure"));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);

		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN1() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Tennis Facilities"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"Scholarships","Salaries and Wages", "Swimming Facilities",
				"Athletics Facilities","Other Sporting Facilities",
				"Parking Facilities","Other Facilities",
				"Research Infrastructure","Teaching Infrastructure"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN2() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Parking Facilities"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"Scholarships","Salaries and Wages", 
				"Sporting Facilities", "Other Facilities",
				"Research Infrastructure","Teaching Infrastructure"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN3() {
		Tree<String> tree = getExampleTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"Research Infrastructure"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"Scholarships","Salaries and Wages", 
				"Grounds", "Teaching Infrastructure"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	/**
	 * Wrost tree cases.
	 */
	@Test
	public void testN4() {
		Tree<String> tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"e"));
		List<String> expectedNegation = new ArrayList<String>();
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN5() {
		Tree<String> tree = wrostTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"j"));
		List<String> expectedNegation = new ArrayList<String>();
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN6() {
		Tree<String> tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"d"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"e", "c"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN7() {
		Tree<String> tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"h"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"e", "c", "i"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN8() {
		Tree<String> tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"h", "i"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"e", "c"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN9() {
		Tree<String> tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"j"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"d", "c","k"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN10() {
		Tree<String> tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"j","i"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"c","k","h"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	@Test
	public void testN11() {
		Tree<String> tree = binaryTree();
		List<String> types = new ArrayList<String>(Arrays.asList(
				"m","n"));
		List<String> expectedNegation = new ArrayList<String>(Arrays.asList(
				"l","o","b"
				));
		List<String> actualNegation = ExpenditureTrees.negation(tree, types);
		Assert.assertEquals(new HashSet<String>(expectedNegation),
				new HashSet<String>(actualNegation));
	}
	
	// helper methods tree implementations.

	/**
	 * Creates and returns example tree from handout.
	 */
	private Tree<String> getExampleTree() {
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
	/**
	 * wrost cases tree
	 *
	 */
	private Tree<String> wrostTree(){
		LinkedTree<String> wrostTree = new LinkedTree<String>();
		Position<String> n0 = wrostTree.addRoot("a");
		Position<String> n1 = wrostTree.insertChild(n0, "b");
		Position<String> n2 = wrostTree.insertChild(n1, "c");
		Position<String> n3 = wrostTree.insertChild(n2, "d");
		Position<String> n4 = wrostTree.insertChild(n3, "e");
		Position<String> n5 = wrostTree.insertChild(n4, "f");
		Position<String> n6 = wrostTree.insertChild(n5, "g");
		Position<String> n7 = wrostTree.insertChild(n6, "h");
		Position<String> n8 = wrostTree.insertChild(n7, "i");
		Position<String> n9 = wrostTree.insertChild(n8, "j");
		return wrostTree;
	}
	/**
	 * binary tree
	 * 
	 */
	private Tree<String> binaryTree(){
		LinkedTree<String> binaryTree = new LinkedTree<String>();
		Position<String> n0 = binaryTree.addRoot("a");
		Position<String> n1 = binaryTree.insertChild(n0, "b");
		Position<String> n2 = binaryTree.insertChild(n0, "c");
		Position<String> n3 = binaryTree.insertChild(n1, "d");
		Position<String> n4 = binaryTree.insertChild(n1, "e");
		Position<String> n5 = binaryTree.insertChild(n2, "f");
		Position<String> n6 = binaryTree.insertChild(n2, "g");
		Position<String> n7 = binaryTree.insertChild(n3, "h");
		Position<String> n8 = binaryTree.insertChild(n3, "i");
		Position<String> n9 = binaryTree.insertChild(n4, "j");
		Position<String> n10 = binaryTree.insertChild(n4, "k");
		Position<String> n11 = binaryTree.insertChild(n5, "l");
		Position<String> n12 = binaryTree.insertChild(n5, "m");
		Position<String> n13 = binaryTree.insertChild(n6, "n");
		Position<String> n14 = binaryTree.insertChild(n6, "o");
		return binaryTree;
	}
}
