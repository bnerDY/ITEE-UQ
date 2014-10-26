package a2.test;

import org.junit.*;

import java.util.*;

import a2.*;

/**
 * Some tests for the part2.NaiveAllocator.canAllocate method. A much more
 * extensive test suite will be used to mark your code, but this should get you
 * started writing your own tests to help you to debug your implementation.
 */
public class NaiveAllocatorTest {

	@Test
	public void basicTestTrue() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 10));
		projects.add(new Project("P1", 10));
		projects.add(new Project("P2", 10));
		donations.add(new Donation("D0", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1)))));
		donations.add(new Donation("D1", 10, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D2", 5, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		donations.add(new Donation("D3", 5, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// allocation should be complete and valid
		checkCompleteAllocation(actualDonations, actualProjects);
	}
/*//		P0=3
			P1=5
			P2=1
			D0:=8  P1  P2
			D1:=3  P2
			D2:=5  P2
			D3:=8  P1
			D4:=5  P0*/
	@Test
	public void basicTestTrue2() {
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		projects.add(new Project("P0", 3));
		projects.add(new Project("P1", 5));
		projects.add(new Project("P2", 1));
		donations.add(new Donation("D0", 8, new HashSet<Project>(Arrays
				.asList(projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 3, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));
		donations.add(new Donation("D2", 5, new HashSet<Project>(Arrays
				.asList(projects.get(2)))));
		donations.add(new Donation("D3", 8, new HashSet<Project>(Arrays
				.asList(projects.get(1)))));
		donations.add(new Donation("D4", 5, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertTrue(NaiveAllocator.canAllocate(actualDonations,
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
		projects.add(new Project("P0", 10));
		projects.add(new Project("P1", 10));
		projects.add(new Project("P2", 10));
		donations.add(new Donation("D0", 10, new HashSet<Project>(Arrays
				.asList(projects.get(0), projects.get(1), projects.get(2)))));
		donations.add(new Donation("D1", 20, new HashSet<Project>(Arrays
				.asList(projects.get(0)))));

		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		Assert.assertFalse(NaiveAllocator.canAllocate(actualDonations,
				actualProjects));
		// no donations should be added or removed from the list of donations
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		// no allocations should have been made
		checkEmptyAllocation(actualDonations, actualProjects);
	}
	@Test
	public void RandomTest(){
		//dcm: donation count max value
		//pm: project max cost ps: project min cost  dm:donation max value
		int dcm=10,pcm=10,ds=20,dm=40,ps=10,pm=40,dcs=3,pcs=3;
		int dc=(int) (Math.random()*(dcm-dcs)+dcs);
		int pc=(int) (Math.random()*(pcm-pcs)+pcs);
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		System.out.println("Random Test: dc="+String.valueOf(dc)+" pc="+String.valueOf(pc));
		for(int i=0;i<pc;i++){
			int r=(int) (Math.random()*(pm-ps)+ps);
			projects.add(new Project("P"+String.valueOf(i), r));
			System.out.println("P"+String.valueOf(i)+"="+String.valueOf(r));
		}
		for(int i=0;i<dc;i++){
			ArrayList<Project> tmp=new ArrayList<Project>();
			int j=(int) (Math.random()*pc);
			while(j==0) j=(int) (Math.random()*pc);
			int rFunding=(int) (Math.random()*(dm-ds)+ds);
			String tmpString="D"+String.valueOf(i)+":="+String.valueOf(rFunding);
			for(int t=0;t<j;t++){
				int tIndex=(int) (Math.random()*pc);
				while (tmp.contains(projects.get(tIndex))) tIndex=(int)( Math.random()*pc);
				tmp.add(projects.get(tIndex));
				 tmpString= tmpString+"  P"+String.valueOf(tIndex);
			}
			donations.add(new Donation("D"+String.valueOf(i),rFunding,new HashSet<Project>(tmp)));
			System.out.println(tmpString);
		}
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		boolean TestResult=NaiveAllocator.canAllocate(actualDonations, actualProjects);
		System.out.println("NaiveTest Over");
		System.out.println(String.valueOf(TestResult));
		Assert.assertEquals(donations, actualDonations);
		// no projects should be added or removed from the set of projects
		Assert.assertEquals(new HashSet<>(projects), actualProjects);
		if( TestResult){
			// allocation should be complete and valid
			checkCompleteAllocation(actualDonations, actualProjects);
		}else{
			// no allocations should have been made
			checkEmptyAllocation(actualDonations, actualProjects);
		}
	}
	
	@Test
	public void RepeatRandom(){
		int i=0;
		while(i!=0){
			RandomTest();
			i--;
		}
	}
	// helper methods

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
