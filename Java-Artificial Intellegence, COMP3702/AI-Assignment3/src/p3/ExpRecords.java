package p3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	private Map<String, Double> MissingData;

	public Map<String, Double> getMissingData() {
		return MissingData;
	}

	public void setMissingData(Map<String, Double> missingData) {
		MissingData = missingData;
	}

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
	public String OUTMissing(){
		StringBuffer sb=new StringBuffer();
		sb.append(System.lineSeparator());
		List<String>keys=new ArrayList<String>(MissingData.keySet());
		Collections.sort(keys);
		for(String h:keys){
			sb.append(h);
			sb.append(" ");
			sb.append(MissingData.get(h));
			sb.append(System.lineSeparator());
		}
		return sb.toString();
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
		Double count = 1.0;
		if (c.size() == 0) {
			for (List<String> r : data) {
				String recordsItem = r.get(names.indexOf(A));
				if (recordsItem.contains("H")) {
					count += MissingData.get(recordsItem);
				} else {
					count += Double.valueOf(recordsItem);
				}
			}
			//System.out.println("count single:"+((double) count / (double) data.size()));
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
		Double count2 = 1.0;
		for (List<String> r : data) {
			String recordsItem = r.get(names.indexOf(A));
			Double fz,fm=0.0;
			if (recordsItem.contains("H")) {
				fz= MissingData.get(recordsItem);
			} else {
				fz= Double.valueOf(recordsItem);
			}
			Double recordWeight=1.0;
			for (int index = 0; index < c.size(); index++) {
				recordsItem = r.get(names.indexOf(c.get(index)));
				//System.out.println(count+"\t"+count2);
				if (recordsItem.contains("H")) {
					recordWeight=  Math.abs(Double.valueOf(mask.charAt(index)-'1')
							- MissingData.get(recordsItem));
				} else {
					fm +=  Math.abs(Double.valueOf(mask.charAt(index)-'0')
							- Double.valueOf(recordsItem));
				}		
			}
			if(fm==0.0){
				if(fz!=0.0){
					count+=fz;
					count2+=recordWeight*fz;
				}else{
					count2+=recordWeight;
				}
				
			}
		}
		//System.out.println((double) count / (double) count2);
		return ((double) count / (double) count2);
	}
}
