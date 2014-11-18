package p3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DAGStruct {
	private static class AcesseStatue {
		public static int done = 2;
		public static int doing = 1;
		public static int ini = 0;
	}

	private class V {
		private String name;
		private List<String> pVs = new ArrayList<String>();
		private List<Double> cpt = new ArrayList<Double>();
		public int acesseStatue = AcesseStatue.ini;

		public V(String n) {
			name = n;
			pVs = new ArrayList<String>();
			cpt = new ArrayList<Double>();
		}

		public boolean addE(V v) {
			if (!pVs.contains(v.name)) {
				return pVs.add(v.name);
			}
			return false;
		}

		public boolean delE(V v) {
			if (pVs.contains(v.name)) {
				return pVs.remove(v.name);
			}
			return false;
		}
	}

	private List<V> vs = new ArrayList<V>();
	private Map<String, Integer> nameIndexMap = new HashMap<String, Integer>();
	private Random random = new Random();
	private String LastModfication = new String();

	public void AddV(String vname) {
		V vtmp = new V(vname);
		vs.add(vtmp);
		nameIndexMap.put(vname, vs.indexOf(vtmp));
	}

	public void AddE(String s, String d) {
		vs.get(nameIndexMap.get(s)).pVs.add(d);
	}

	public void deleteE(String s, String d) {
		vs.get(nameIndexMap.get(s)).pVs.remove(d);
	}

	public void reverseE(String s, String d) {
		vs.get(nameIndexMap.get(s)).pVs.remove(d);
		vs.get(nameIndexMap.get(d)).pVs.add(s);
	}

	private boolean reverseE(V s, V d) {
		return s.delE(d) && d.addE(s);
	}

	private boolean dfsSearch(V v) {
		if (v.acesseStatue == AcesseStatue.doing)
			return false;
		if (v.acesseStatue == AcesseStatue.ini) {
			v.acesseStatue = AcesseStatue.doing;
			for (String s : v.pVs) {
				if (!dfsSearch(vs.get(nameIndexMap.get(s))))
					return false;
			}
			v.acesseStatue = AcesseStatue.done;
		}
		return true;
	}

	public Map<String,Double> VariableEliminationInference(Set<String>Q,Map<String, Double> E,Map<String, String> HvarNameMap){
		Map<String,Double> InferenceRslt=new HashMap<String,Double>();
		Set<String> Qrealnames=new HashSet<String>();
		for(String var:Q){
			Qrealnames.add( HvarNameMap.get(var));
		}
		RecurseInference(Qrealnames,E);
		for(String var:Q){
			InferenceRslt.put(var, E.get( HvarNameMap.get(var)));
		}
		return InferenceRslt;
	}
	
	private void RecurseInference(Set<String> Q,Map<String, Double> E){
		for(String var:Q){
			if(E.keySet().contains(var)) continue;
			List<String> pvs=vs.get(nameIndexMap.get(var)).pVs;
			if(pvs.size()==0){
				E.put(var,vs.get(nameIndexMap.get(var)).cpt.get(0) );continue;
			}
			Set<String> H=new HashSet<String>();
			for( String pv: pvs  ){
				if (!E.keySet().contains(pv)){
					H.add(pv);
				}
			}
			if(H.size()>0){
				RecurseInference(H,E);
			}
			List<Double>cpt=vs.get(nameIndexMap.get(var)).cpt;
			Double f=0.0;
			for(int i=0;i<cpt.size();i++){
				Double f1=cpt.get(i);
				String Mask=Integer.toBinaryString(i);
				while(Mask.length()<pvs.size()) Mask="0"+Mask;
				for(int j=0;j<pvs.size();j++){
					f1*=Math.abs(((double)('1'-Mask.charAt(j)))-E.get(pvs.get(j)));
				}
				f+=f1;
			}
			//System.out.println("E="+E);
			//System.out.println("f="+f);
			E.put(var, f);
		}
		return;
	}
	public void check() {
		for (String s : nameIndexMap.keySet()) {
			int i = nameIndexMap.get(s);
			System.out.println(vs.get(i).name + "\t=?\t" + s);
		}
	}
	
	private boolean NoPath(V s, V d) {
		if (s.name.contentEquals(d.name)) {
			return false;
		}
		for (int i = 0; i < s.pVs.size(); i++) {
			String p = s.pVs.get(i);
			V n = vs.get(nameIndexMap.get(p));
			boolean t = NoPath(n, d);
			if (!t)
				return false;
		}
		return true;
	}

	
	private boolean checkStruct() {
		for (V v : vs)
			v.acesseStatue = AcesseStatue.ini;
		for (V v : vs) {
			if (v.acesseStatue == AcesseStatue.ini) {
				if (!dfsSearch(v))
					return false;
			}
		}
		return true;
	}

	public Set<String> getVnames() {
		return nameIndexMap.keySet();
	}

	public int getEptNodeCount() {
		int c = 0;
		for (V v : vs)
			c += v.cpt.size();
		return c;
	}
	
	public List<String> getParentVsOfV(String n) {
		return vs.get(nameIndexMap.get(n)).pVs;
	}

	public void Setcpt(String n, List<Double> cp) {
		vs.get(nameIndexMap.get(n)).cpt = cp;
	}

	public Double Getcp(String s, int index) {
		return vs.get(nameIndexMap.get(s)).cpt.get(index);
	}

	public void GenerateRandomChainModel() {
		GenerateNoEdgeModel();
		List<String> vNames = new ArrayList<String>();
		vNames.addAll(nameIndexMap.keySet());
		Collections.shuffle(vNames);
		for (int i = 1; i < vNames.size(); i++) {
			vs.get(nameIndexMap.get(vNames.get(i))).pVs.add(vs
					.get(nameIndexMap.get(vNames.get(i - 1))).name);
		}
	}

	
	public void GenerateNoEdgeModel() {
		for (V v : vs) {
			v.pVs.clear();
			v.cpt.clear();
		}
	}


	public void GenerateBestTreeModel(Map<Double, List<String>> es) {
		vs.clear();
		nameIndexMap.clear();
		for (Double d : es.keySet()) {
			if (!nameIndexMap.containsKey(es.get(d).get(0))) {
				vs.add(new V(es.get(d).get(0)));
				nameIndexMap.put(es.get(d).get(0), nameIndexMap.size());
			}
			if (!nameIndexMap.containsKey(es.get(d).get(1))) {
				vs.add(new V(es.get(d).get(1)));
				nameIndexMap.put(es.get(d).get(1), nameIndexMap.size());
			}
			AddE(es.get(d).get(0), es.get(d).get(1));
		}
	}

	public void ApplyRandomModify() {
		while (true) {
			int index = random.nextInt(vs.size());
			int index2 = random.nextInt(vs.size());
			while (index == index2)
				index2 = random.nextInt(vs.size());
			V s = vs.get(index), d = vs.get(index2);
			if (s.pVs.contains(d.name)) {
				LastModfication = "D " + s.name + " " + d.name;
				s.delE(d);
				return;
			}
			if (d.pVs.contains(s.name)) {
				reverseE(d, s);
				if (checkStruct()) {
					LastModfication = "R " + d.name + " " + s.name;
					return;
				}
				reverseE(s, d);
			}
			
			if(random.nextBoolean()){
				if (NoPath(s, d)) {
					d.addE(s);
					LastModfication = "A " + d.name + " " + s.name;
					return;
				}
				if (NoPath(d, s)) {
					s.addE(d);
					LastModfication = "A " + s.name + " " + d.name;
					return;
				}
			}else{
				if (NoPath(d, s)) {
					s.addE(d);
					LastModfication = "A " + s.name + " " + d.name;
					return;
				}
				if (NoPath(s, d)) {
					d.addE(s);
					LastModfication = "A " + d.name + " " + s.name;
					return;
				}
			}
		}
	}

	public void undo() {
		V d = vs.get(nameIndexMap.get(LastModfication.split(" ")[1])), s = vs
				.get(nameIndexMap.get(LastModfication.split(" ")[2]));
		switch (LastModfication.split(" ")[0]) {
		case "R":
			reverseE(s, d);
			break;
		case "A":
			d.delE(s);
			break;
		case "D":
			d.addE(s);
			break;
		}
	}

	public String outDAG() {
		StringBuffer buf = new StringBuffer();
		for (V v : vs) {
			buf.append(v.name + " ");
			Collections.sort(v.pVs);
			for (String s : v.pVs) {
				buf.append(s + " ");
			}
			buf.deleteCharAt(buf.length() - 1);
			buf.append(System.lineSeparator());
		}
		return buf.toString();
	}

	public String outCPT() {
		StringBuffer buf = new StringBuffer();
		for (V v : vs) {
			buf.append(v.name + " ");
			for (String s : v.pVs) {
				buf.append(s + " ");
			}
			buf.deleteCharAt(buf.length() - 1);
			buf.append(System.lineSeparator());
			for (Double d : v.cpt) {
				buf.append(d.toString() + " ");
			}
			buf.deleteCharAt(buf.length() - 1);
			buf.append(System.lineSeparator());
		}
		return buf.toString();
	}
}
