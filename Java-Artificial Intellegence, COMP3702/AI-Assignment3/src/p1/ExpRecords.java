package p1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

	public int getCPTIndex(List<String> selectdColums, List<String> r) {// 根据names查表生成selectdColums指定的列的CPTindex
		//System.out.println(selectdColums);
		//System.out.println(names);
		//System.out.println(r);
		int i = 0;
		for (String s : selectdColums) {
			if (r.get(names.indexOf(s)).contentEquals("1")) {
				i |= 1;
			}
			i <<= 1;
		}
		i>>>=1;
		//System.out.println(i);
		return i;
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
