package a2;

import java.util.*;

public class NaiveAllocator {

	/**
	 * @precondition: Neither of the inputs are null or contain null elements.
	 *                The parameter donations is a list of distinct donations
	 *                such that for each d in donations, d.getTotal() equals
	 *                d.getUnspent(); and for each p in projects
	 *                p.allocatedFunding() equals 0.
	 * @postcondition: returns false if there no way to completely fund all of
	 *                 the given projects using the donations, leaving both the
	 *                 input list of donations and set of projects unmodified;
	 *                 otherwise returns true and allocates to each project
	 *                 funding from the donations. The allocation to each
	 *                 project must be complete and may not violate the
	 *                 conditions of the donations.
	 */
	public static boolean canAllocate(List<Donation> donations,
			Set<Project> projects) {
		return canAllocateHelper(donations, projects, 0); 
	}
	/**
	 * 
	 * @param donations
	 * @param projects
	 * @param i 
	 * @return
	 */
	private static boolean canAllocateHelper(List<Donation> donations,
			Set<Project> projects, int i){
		if (isAllCompletelyAllocated(projects)) return true;
		if (i == donations.size()) return false; 
		Donation d = donations.get(i);
		if((d.getUnspent() == 0) || isAllCompletelyAllocated(d.getProjects()))
			return	canAllocateHelper(donations, projects, i+1);
		for (Project p: d.getProjects()){
			if(!p.fullyFunded()) {
				p.allocate(d, 1);
				if (canAllocateHelper(donations, projects, i)) return true;
				else p.deallocate(d, 1);
			}
		}
		return false;
	}
	/**
	 * Boolean function. It is used for checking if the input projects are 
	 * all completly allocated.
	 * @param projects
	 * @return true iff it is all completely allocated. false otherwise.
	 */
	private static boolean isAllCompletelyAllocated(Set<Project> projects){
		for(Project p:projects){
			if(!p.fullyFunded()) return false;
		}
		return true;
	}
}
