package a2.test;

import org.junit.*;

import java.util.*;

import a2.*;

/**
 * Some tests for the part2.IterativeAllocator.canAllocate method. A much more
 * extensive test suite will be used to mark your code, but this should get you
 * started writing your own tests to help you to debug your implementation.
 */
public class IterativeAllocatorTest {
	/*	P0=24
		P1=34
		P2=39
		D0:=30  P1  P2
		D1:=37  P1  P2
		D2:=26  P1  P0
		D3:=30  P2*/

	
	@Test
	public void ddTestTrue() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 24));
		projects.add(new Project("P1", 34));
		projects.add(new Project("P2", 39));
		donations.add(new Donation("D0", 30, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 37, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 26, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(0)))));
		donations.add(new Donation("D3", 30, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void basicTestTrue() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void NormalTestTrue1() {
/*				P0=19
				P1=26
				P2=20
				D0:=27  P1  P2
				D1:=35  P1  P0
				D2:=22  P1*/
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 19));
		projects.add(new Project("P1", 26));
		projects.add(new Project("P2", 20));
		donations.add(new Donation("D0", 27, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 35, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(0)))));
		donations.add(new Donation("D2", 22, new HashSet<Project>(Arrays
				.asList(projects.get(1)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void basicTestTrue0() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void basicTestTrue1() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P10", 100));
		projects.add(new Project("P11", 100));
		projects.add(new Project("P12", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0),projects.get(2), projects.get(1)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2), projects.get(1)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void basicTestTrue2() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2), projects.get(1)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void basicTestTrue3() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2), projects.get(1)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void basicTestFalse() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 200, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	@Test   
	public void normalTestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P22", 100));
		projects.add(new Project("P3", 100));
		projects.add(new Project("P4", 100));
		projects.add(new Project("P5", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(5)))));
		donations.add(new Donation("D1", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),projects.get(3), projects.get(4), projects.get(5)))));
		donations.add(new Donation("D2", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2),projects.get(3), projects.get(4), projects.get(5)))));
		donations.add(new Donation("D3", 50, new HashSet<Project>(Arrays
				.asList(projects.get(0),projects.get(1)))));
		donations.add(new Donation("D4", 100, new HashSet<Project>(Arrays
				.asList(projects.get(4), projects.get(5)))));
		donations.add(new Donation("D5", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(3)))));
		donations.add(new Donation("D6", 50, new HashSet<Project>(Arrays
				.asList(projects.get(2),projects.get(3), projects.get(5)))));
		donations.add(new Donation("D7", 50, new HashSet<Project>(Arrays
				.asList(projects.get(1),projects.get(2),projects.get(3), projects.get(4)))));
		donations.add(new Donation("D8", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1),projects.get(3), projects.get(4), projects.get(5)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void MostSimpleTrue() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		donations.add(new Donation("D0", 100, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void MostSimpleTrue1() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		donations.add(new Donation("D0", 200, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void MostSimpleTrue2() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 100));
		projects.add(new Project("P1", 100));
		projects.add(new Project("P2", 100));
		projects.add(new Project("P3", 100));
		donations.add(new Donation("D0", 400, new HashSet<Project>(Arrays
				.asList(projects.get(0),projects.get(1),projects.get(2),projects.get(3)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
	@Test
	public void MostSimpleFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 300));
		donations.add(new Donation("D1", 200, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	@Test
	public void MostSimpleFalse1(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 300));
		projects.add(new Project("P1", 300));
		donations.add(new Donation("D1", 300, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	@Test
	public void Cycle2() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0",21));
		projects.add(new Project("P1",33));
		projects.add(new Project("P2",13));
		projects.add(new Project("P3",32));
		projects.add(new Project("P4",27));
		projects.add(new Project("P5",28));
		projects.add(new Project("P6",16));
		projects.add(new Project("P7",21));
		projects.add(new Project("P8",35));
		projects.add(new Project("P9",19));
		projects.add(new Project("P10",11));
		projects.add(new Project("P11",11));
		projects.add(new Project("P12",24));
		projects.add(new Project("P13",28));
		projects.add(new Project("P14",17));
		projects.add(new Project("P15",15));
		projects.add(new Project("P16",20));
		projects.add(new Project("P17",38));		
		donations.add(new Donation("D0",24,new HashSet<Project>(Arrays.asList(projects.get(0),projects.get(7),projects.get(6),projects.get(14),projects.get(13),projects.get(15),projects.get(12),projects.get(1),projects.get(3),projects.get(9),projects.get(5),projects.get(16),projects.get(11),projects.get(8),projects.get(17),projects.get(10)))));
		donations.add(new Donation("D1",35,new HashSet<Project>(Arrays.asList(projects.get(3),projects.get(13),projects.get(16),projects.get(9),projects.get(2),projects.get(7),projects.get(5),projects.get(11),projects.get(6),projects.get(0),projects.get(8),projects.get(1),projects.get(4)))));
		donations.add(new Donation("D2",24,new HashSet<Project>(Arrays.asList(projects.get(2),projects.get(7),projects.get(3),projects.get(16),projects.get(6),projects.get(15),projects.get(9),projects.get(0),projects.get(5),projects.get(14),projects.get(8),projects.get(11),projects.get(10),projects.get(4),projects.get(1),projects.get(12),projects.get(13)))));
		donations.add(new Donation("D3",39,new HashSet<Project>(Arrays.asList(projects.get(3),projects.get(0),projects.get(5),projects.get(2),projects.get(14),projects.get(17),projects.get(10),projects.get(12),projects.get(1)))));
		donations.add(new Donation("D4",31,new HashSet<Project>(Arrays.asList(projects.get(8),projects.get(16),projects.get(17),projects.get(1),projects.get(3),projects.get(2),projects.get(10),projects.get(12),projects.get(4),projects.get(7),projects.get(14)))));
		donations.add(new Donation("D5",23,new HashSet<Project>(Arrays.asList(projects.get(15),projects.get(7),projects.get(12),projects.get(2),projects.get(14),projects.get(8)))));
		donations.add(new Donation("D6",30,new HashSet<Project>(Arrays.asList(projects.get(0),projects.get(13),projects.get(12),projects.get(16),projects.get(11),projects.get(1),projects.get(6),projects.get(17),projects.get(5),projects.get(4),projects.get(9),projects.get(14),projects.get(8),projects.get(10),projects.get(7),projects.get(3),projects.get(2)))));
		donations.add(new Donation("D7",20,new HashSet<Project>(Arrays.asList(projects.get(6),projects.get(5),projects.get(8),projects.get(16),projects.get(3),projects.get(7),projects.get(0),projects.get(10),projects.get(11),projects.get(17),projects.get(14)))));
		donations.add(new Donation("D8",28,new HashSet<Project>(Arrays.asList(projects.get(2)))));
		donations.add(new Donation("D9",28,new HashSet<Project>(Arrays.asList(projects.get(5),projects.get(8),projects.get(14),projects.get(7),projects.get(15),projects.get(3),projects.get(17),projects.get(0)))));
		donations.add(new Donation("D10",22,new HashSet<Project>(Arrays.asList(projects.get(15),projects.get(8),projects.get(7),projects.get(3),projects.get(6),projects.get(0)))));
		donations.add(new Donation("D11",24,new HashSet<Project>(Arrays.asList(projects.get(13),projects.get(9),projects.get(14),projects.get(6),projects.get(15),projects.get(11),projects.get(7),projects.get(10),projects.get(16),projects.get(3),projects.get(12),projects.get(0)))));
		donations.add(new Donation("D12",29,new HashSet<Project>(Arrays.asList(projects.get(14),projects.get(9),projects.get(5),projects.get(11),projects.get(0),projects.get(8),projects.get(3),projects.get(12)))));
		donations.add(new Donation("D13",30,new HashSet<Project>(Arrays.asList(projects.get(15),projects.get(16),projects.get(12),projects.get(10),projects.get(13),projects.get(4),projects.get(17),projects.get(2),projects.get(6),projects.get(9),projects.get(7),projects.get(5),projects.get(3),projects.get(8),projects.get(14),projects.get(11),projects.get(1)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}

	
	/**
	 * Helper method to check that each project has been completely allocated by
	 * the given donations, and that the total spent on each donation is equal
	 * to that spent on the given projects.
	 **/
	private void checkCompleteAllocation(List<Donation> donations,
			Set<Project> projects) {

		// the amount spent from each donation by all of the combined projects
		Map<Donation, Integer> totalSpent = new HashMap<>();

		// check that each project has been completely (and properly) allocated
		// and calculate totalSpent
		for (Project p : projects) {
			Assert.assertTrue(p.fullyFunded());
			for (Map.Entry<Donation, Integer> allocation : p.getAllocations()
					.entrySet()) {
				Donation d = allocation.getKey();
				int amount = allocation.getValue();
				Assert.assertTrue(amount > 0);
				Assert.assertTrue(d.canBeUsedFor(p));
				Assert.assertTrue(donations.contains(d));
				if (totalSpent.containsKey(d)) {
					totalSpent.put(d, totalSpent.get(d) + amount);
				} else {
					totalSpent.put(d, amount);
				}
			}
		}
		// check that the remaining funds in each donation are correct, assuming
		// that no funds were spent from each donation to begin with.
		for (Donation d : donations) {
			if (totalSpent.containsKey(d)) {
				Assert.assertTrue(d.getUnspent() >= 0);
				Assert.assertEquals(d.getUnspent(),
						d.getTotal() - totalSpent.get(d));
			} else {
				Assert.assertEquals(d.getUnspent(), d.getTotal());
			}
		}
	}

	/**
	 * Helper method to check that no allocations have been made for any project
	 * in projects and that all donations have not been spent at all.
	 **/
	private void checkEmptyAllocation(List<Donation> donations,
			Set<Project> projects) {
		for (Project p : projects) {
			Assert.assertEquals(p.getCost(), p.neededFunds());
		}
		for (Donation d : donations) {
			Assert.assertEquals(d.getUnspent(), d.getTotal());
		}
	}
}
