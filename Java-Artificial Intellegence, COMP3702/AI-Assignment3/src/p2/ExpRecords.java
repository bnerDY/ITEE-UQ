package p2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpRecords {
	private List<String> names = new ArrayList<String>();

	public List<String> getNames() {
		return names;
	}

	private List<List<String>> data;

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}

	public void setExps(List<String> d) {
		data = new ArrayList<List<String>>();
		for (String s : d) {
			data.add(Arrays.asList(s.split(" ")));
		}
	}

	public void addC(String n) {
		names.add(n);
	}

	public void printData() {
		System.out.println(names);
		for (List<String> it : data) {
			for (String s : it) {
				System.out.print(s + " ");
			}
			System.out.print(System.lineSeparator());
		}
	}

	public int getCPTIndex(List<String> selectdColums, List<String> r) {
		// System.out.println(selectdColums);
		// System.out.println(names);
		// System.out.println(r);
		int i = 0;
		for (String s : selectdColums) {
			if (r.get(names.indexOf(s)).contentEquals("1")) {
				i |= 1;
			}
			i <<= 1;
		}
		i >>>= 1;
		// System.out.println(i);
		return i;
	}

	public Map<Double, List<String>> BuildEdges() {
		Map<Double, List<String>> rVal = new TreeMap<Double, List<String>>();
		for (int i = 0; i < names.size(); i++) {
			for (int j = i + 1; j < names.size(); j++) {
				List<String> edge = new ArrayList<String>();
				edge.add(names.get(i));
				edge.add(names.get(j));
				rVal.put(MI(names.get(i), names.get(j)), edge);
			}
		}
		return rVal;
	}

	public Double MI(String A, String B) {
		int a0 = 0, a1 = 0, b0 = 0, b1 = 0, a0b0 = 0, a0b1 = 0, a1b0 = 0, a1b1 = 0, t = data
				.size();
		double m = 0.0;
		for (List<String> r : data) {
			if (r.get(names.indexOf(A)).contentEquals("1")) {
				a1++;
				if (r.get(names.indexOf(B)).contentEquals("1")) {
					b1++;
					a1b1++;
				} else {
					b0++;
					a1b0++;
				}
			} else {
				a0++;
				if (r.get(names.indexOf(B)).contentEquals("1")) {
					b1++;
					a0b1++;
				} else {
					b0++;
					a0b0++;
				}
			}
		}
		m = ((double) a0b0) / ((double) t)
				* Math.log10(((double) (a0b0 * t)) / ((double) (a0 * b0)))
				+ ((double) a0b1) / ((double) t)
				* Math.log10(((double) (a0b1 * t)) / ((double) (a0 * b1)))
				+ ((double) a1b0) / ((double) t)
				* Math.log10(((double) (a1b0 * t)) / ((double) (a1 * b0)))
				+ ((double) a1b1) / ((double) t)
				* Math.log10(((double) (a1b1 * t)) / ((double) (a1 * b1)));
		return m;
	}

	public Double count(String A, List<String> c, Integer i) {
		int count = 0;
		if (c.size() == 0) {// no parent nodes
			for (List<String> r : data) {
				if (r.get(names.indexOf(A)).contentEquals("1"))
					count++;
			}
			return ((double) count / (double) data.size());
		}
		if (i > Math.pow(2, c.size())) {
			System.out.println("Out Of range");
			System.exit(1);
		}
		String mask = Integer.toBinaryString(i);
		while (mask.length() < c.size()) {
			mask = "0" + mask;
		}
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		for (int j = 0; j < mask.length(); j++) {
			map.put(names.indexOf(c.get(j)), String.valueOf(mask.charAt(j)));
		}
		int count2 = 0;
		for (List<String> r : data) {
			boolean eqs = true;
			for (int index : map.keySet()) {
				if (!r.get(index).contentEquals(map.get(index))) {
					eqs = false;
					break;
				}
			}
			if (eqs) {
				count2++;
				if (r.get(names.indexOf(A)).contentEquals("1"))
					count++;
			}
		}
		return ((double) count / (double) count2);
	}
}
