package a2;

import java.util.*;

public class IterativeAllocator {

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
		int totalUnspent = 0;
		Set<Donation> ds = new HashSet<Donation>(); 
		// pre-processiong.
		for (Donation d : donations) {
			for (Project p : d.getProjects()) {
				if (!p.fullyFunded()) {
					p.allocate(d, Math.min(p.neededFunds(), d.getUnspent()));
				}
				if (d.getUnspent() == 0)
					break;
			}
			if (d.getUnspent() > 0) {
				totalUnspent += d.getUnspent();
				ds.add(d);
			}
		}
		while (!isAllCompletelyAllocated(projects)) {
			Set<Project> zps = new  HashSet<Project>();
			Set<Project> fp = new  HashSet<Project>();
			for (Project p : projects) { 
				while (!p.fullyFunded()) {
					List<Project> path = new ArrayList<Project>();
					if (totalUnspent < p.neededFunds()) {
						undo(projects); 
						return false;
					}
					int x = search(ds, projects, path, p, zps, fp); 
					if (x > 0) {
						x = Math.min(x, p.neededFunds());
						if (!p.equals(path.get(0)))
							p.transfer(x, path.get(0));
						for (int i = 0; i < path.size() - 1; i++) { 
							path.get(i).transfer(x, path.get(i + 1));
						}
						Project pn = path.get(path.size() - 1);
						if (!doAlloacte(donations, pn, x)) {
							undo(projects);
							return false;
						}
						totalUnspent -= x;
					} else {
						undo(projects);
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * Boolean function. It is used for checking if the input projects are 
	 * all completly allocated.
	 * @param projects
	 * @return true iff it is all completely allocated. false otherwise.
	 */
	private static boolean isAllCompletelyAllocated(Set<Project> projects) {
		for (Project p : projects) {
			if (!p.fullyFunded()) return false;
		}
		return true;
	}
	/**
	 * 
	 * @param ds -> donations
	 * @param ps  -> projects
	 * @param path -> previous project within current search path
	 * @param p -> current project within current search path
	 * @param zerops -> projects that doesnt have enough fund
	 * @param fp -> the path that can not have enough fund
	 * @return the avalibale donation fund within the current search path.
	 */
	private static int search(Set<Donation> ds, Set<Project> ps, 
			List<Project> path, Project p, Set<Project> zerops, Set<Project> fp) {
		if (!zerops.contains(p)) {
			for (Donation d : ds) {
				if (d.canBeUsedFor(p) && d.getUnspent() > 0) {
					if (!path.contains(p))
						path.add(p);
					return d.getUnspent();
				}
			}
			zerops.add(p);
		}
		for(Project i : ps){
			if (!path.contains(i) && !i.equals(p) && !fp.contains(i)) {
				int cm = commonEdges(i, p);
				if (cm > 0) {
					path.add(i);
					int x = search(ds, ps, path, i, zerops,fp);
					if (x > 0) {
						return Math.min(x, cm);
					}
					path.remove(i);
					fp.add(i);
				}
			}
		}
		return 0;
	}
	/**
	 * Deallocate all project.
	 * @param projects
	 */
	private static void undo(Set<Project> projects) {
		for (Project p : projects) {
			p.deallocateAll();
		}
		return;
	}
	/**
	 * Link the edges between project in donation and project avaliable.
	 * @param p
	 * @param q
	 * @return number of edges (avaliable to fund)
	 */
	private static int commonEdges(Project p, Project q) {
		int temp = 0;
		for (Donation d : p.getAllocations().keySet()) {
			if (d.canBeUsedFor(q)) {
				temp += p.getAllocations().get(d);
			}
		}
		return temp;
	}
	/**
	 * Boolean Function. Check if can do allocate or not.
	 * @param ds -> donations
	 * @param p  -> project can be allocated
	 * @param x  -> fund
	 * @return true iff can do allocate. false otherwise.
	 */
	private static boolean doAlloacte(List<Donation> ds, Project p, int x) {
		for (Donation d : ds) {
			if (d.canBeUsedFor(p) && d.getUnspent() > 0 && x > 0) {
				if (d.getUnspent() >= x) {
					p.allocate(d, x);
					return true;
				} else {
					x = x - d.getUnspent();
					p.allocate(d, d.getUnspent());
				}
			}
		}
		return false;
	}
}
