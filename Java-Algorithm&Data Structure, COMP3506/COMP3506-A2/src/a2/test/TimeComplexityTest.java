package a2.test;

import java.util.ArrayList;
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

public class TimeComplexityTest {
	
	@Test (timeout = 80000)
	public void RepeatRandom(){
		int i=10000;
		while(i!=0){
			RandomEfficiencyTest();
			i--;
		}
	}
	@Test (timeout = 10000)
	public void RandomEfficiencyTest(){
		//dcm: donation count max value
		//pm: project max cost 
		//ps: project min cost  
		//dm: donation max value
		int dcm=5,pcm=5,ds=20,dm=40,ps=10,pm=40,dcs=3,pcs=3;//(basic test)
		//int dcm=26000,pcm=26000,ds=20,dm=4000,ps=100,pm=40000,dcs=25000,pcs=25000;
		int dc=(int) (Math.random()*(dcm-dcs)+dcs);
		int pc=(int) (Math.random()*(pcm-pcs)+pcs);
		List<Project> projects = new ArrayList<Project>();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		//System.out.println("Random Test: dc="+String.valueOf(dc)+" pc="+String.valueOf(pc));
		for(int i=0;i<pc;i++){
			int r=(int) (Math.random()*(pm-ps)+ps);
			projects.add(new Project("P"+String.valueOf(i), r));
			//System.out.println("P"+String.valueOf(i)+"="+String.valueOf(r));
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
			//System.out.println(tmpString);
		}
		List<Donation> actualDonations = new ArrayList<>(donations);
		Set<Project> actualProjects = new HashSet<>(projects);
		boolean TestResult=IterativeAllocator.canAllocate(actualDonations, actualProjects);
		//System.out.println("IterativeTest Over");
		//System.out.println(String.valueOf(TestResult));
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
