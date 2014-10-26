package a2.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import a2.Donation;
import a2.IterativeAllocator;
import a2.Project;

public class aTestTask3 {

	@Test
	public void a1TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 0));
		donations.add(new Donation("D0", 0, new HashSet<Project>(Arrays
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
	public void a1TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 0));
		projects.add(new Project("P1", 10));
		
		donations.add(new Donation("D0", 0, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 0, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	
	@Test
	public void a2TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 20));
		projects.add(new Project("P1", 30));
		projects.add(new Project("P2", 20));
		
		donations.add(new Donation("D0", 20, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D1", 30, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D2", 20, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkEmptyAllocation(actualDonations, actualProjects);
		
	}
	@Test
	public void a2TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 20));
		projects.add(new Project("P1", 30));
		projects.add(new Project("P2", 20));
		
		donations.add(new Donation("D0", 20, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D1", 30, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D2", 20, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(2)))));
		
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
	public void a3TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 20));
		projects.add(new Project("P1", 20));
		donations.add(new Donation("D0", 19, new HashSet<Project>(Arrays
				.asList(projects.get(1)))));
		donations.add(new Donation("D1", 13, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D2", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 5, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
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
	public void a3TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 20));
		projects.add(new Project("P1", 20));
		donations.add(new Donation("D0", 19, new HashSet<Project>(Arrays
				.asList(projects.get(1)))));
		donations.add(new Donation("D1", 13, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D2", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 4, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	
	@Test
	public void a4TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 55));
		donations.add(new Donation("D0", 1, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D1", 2, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D2", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 4, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D4", 5, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D5", 6, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D6", 7, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D7", 8, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D8", 9, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D9", 10, new HashSet<Project>(Arrays
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
	public void a4TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 56));
		donations.add(new Donation("D0", 1, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D1", 2, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D2", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 4, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D4", 5, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D5", 6, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D6", 7, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D7", 8, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D8", 9, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D9", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkEmptyAllocation(actualDonations, actualProjects);
		
	}
	
	@Test
	public void a5TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P1", 1));
		projects.add(new Project("P2", 1));
		projects.add(new Project("P3", 1));
		projects.add(new Project("P4", 1));
		projects.add(new Project("P5", 1));
		projects.add(new Project("P6", 1));
		projects.add(new Project("P7", 1));
		projects.add(new Project("P8", 1));
		projects.add(new Project("P9", 1));
		projects.add(new Project("P10", 1));
		donations.add(new Donation("D0", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0),projects.get(1),projects.get(2)
						,projects.get(3),projects.get(4),projects.get(5),
						projects.get(6),projects.get(7),projects.get(8),
						projects.get(9)))));
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
	public void a5TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P1", 1));
		projects.add(new Project("P2", 1));
		projects.add(new Project("P3", 1));
		projects.add(new Project("P4", 1));
		projects.add(new Project("P5", 1));
		projects.add(new Project("P6", 2));
		projects.add(new Project("P7", 1));
		projects.add(new Project("P8", 1));
		projects.add(new Project("P9", 1));
		projects.add(new Project("P10", 1));
		donations.add(new Donation("D0", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0),projects.get(1),projects.get(2)
						,projects.get(3),projects.get(4),projects.get(5),
						projects.get(6),projects.get(7),projects.get(8),
						projects.get(9)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkEmptyAllocation(actualDonations, actualProjects);
		
	}
	
	@Test
	public void a6TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P1", 12));
		projects.add(new Project("P2", 1));
		projects.add(new Project("P3", 1));
		projects.add(new Project("P4", 1));
		projects.add(new Project("P5", 1));
		projects.add(new Project("P6", 1));
		projects.add(new Project("P7", 1));
		donations.add(new Donation("D0", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(2)))));
		donations.add(new Donation("D2", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(3)))));
		donations.add(new Donation("D3", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(4)))));
		donations.add(new Donation("D4", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(5)))));
		donations.add(new Donation("D5", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(6)))));
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
	public void a6TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P1", 12));
		projects.add(new Project("P2", 1));
		projects.add(new Project("P3", 1));
		projects.add(new Project("P4", 1));
		projects.add(new Project("P5", 1));
		projects.add(new Project("P6", 1));
		projects.add(new Project("P7", 1));
		donations.add(new Donation("D0", 2, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(2)))));
		donations.add(new Donation("D2", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(3)))));
		donations.add(new Donation("D3", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(4)))));
		donations.add(new Donation("D4", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(5)))));
		donations.add(new Donation("D5", 3, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(6)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	
	@Test
	public void a7TestTrue(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P1", 10));
		projects.add(new Project("P2", 10));
		projects.add(new Project("P3", 10));
		projects.add(new Project("P4", 10));
		projects.add(new Project("P5", 10));
		projects.add(new Project("P6", 10));
		projects.add(new Project("P7", 10));
		donations.add(new Donation("D0", 11, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D1", 11, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D2", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D3", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D4", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D5", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		
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
	public void a7TestFalse(){
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P1", 10));
		projects.add(new Project("P2", 10));
		projects.add(new Project("P3", 10));
		projects.add(new Project("P4", 10));
		projects.add(new Project("P5", 10));
		projects.add(new Project("P6", 10));
		projects.add(new Project("P7", 10));
		donations.add(new Donation("D0", 11, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D1", 11, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D2", 11, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D3", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D4", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		donations.add(new Donation("D5", 12, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2),
						projects.get(3),projects.get(4),projects.get(5),projects.get(6)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(IterativeAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
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
