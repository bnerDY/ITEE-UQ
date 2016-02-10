package p2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

	public void check() {
		for (String s : nameIndexMap.keySet()) {
			int i = nameIndexMap.get(s);
			System.out.println(vs.get(i).name + "\t=?\t" + s);
		}
	}
	//检查S的父辈当中是否有d
	public boolean NoPath(V s, V d) {
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

	// 检查是否存在环路
	public boolean checkStruct() {
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

	// 检查是否存在环路
	public boolean checkStruct(String s, String d) {
		return NoPath(vs.get(nameIndexMap.get(s)), vs.get(nameIndexMap.get(d)));
	}

	public Set<String> getVnames() {
		return nameIndexMap.keySet();
	}

	public int getEptNodeCount() {
		int c = 0;
		for (V v : vs)
			c += v.cpt.size() + 1;
		return c;
	}
	//获取当前顶点的父辈
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

	// 生成没有边的模型
	public void GenerateNoEdgeModel() {
		for (V v : vs) {
			v.pVs.clear();
			v.cpt.clear();
		}
	}

	// 生成最小生成树模型（BestTree）
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
	//随机选择两个顶点，检查两者之间是否相联，并根据情况随机选择一个操作
	public void ApplyRandomModify() {
		while (true) {
			int index = random.nextInt(vs.size());
			int index2 = random.nextInt(vs.size());
			while (index == index2)
				index2 = random.nextInt(vs.size());
			V s = vs.get(index), d = vs.get(index2);
			if (s.pVs.contains(d.name)) {// 如果S是d的子节点，从s的邻接表中删除d
				LastModfication = "D " + s.name + " " + d.name;// 记录删除边操作
				s.delE(d);
				return;
			}
			if (d.pVs.contains(s.name)) {// 如果d是s的子节点,尝试反转边
				reverseE(d, s);
				if (checkStruct()) {// 检查是否构成环路，如果没有
					LastModfication = "R " + d.name + " " + s.name;// 记录删除边操作
					return;
				}
				// 会构成环路,反转回来
				reverseE(s, d);
			}
			// 两者之间没有联接，就添加边
			if(random.nextBoolean()){//随机交换先后顺序，保持随机性质
				if (NoPath(s, d)) {
					d.addE(s);
					LastModfication = "A " + d.name + " " + s.name;// 记录添加边操作
					return;
				}
				if (NoPath(d, s)) {
					s.addE(d);
					LastModfication = "A " + s.name + " " + d.name;// 记录添加边操作
					return;
				}
			}else{
				if (NoPath(d, s)) {
					s.addE(d);
					LastModfication = "A " + s.name + " " + d.name;// 记录添加边操作
					return;
				}
				if (NoPath(s, d)) {
					d.addE(s);
					LastModfication = "A " + d.name + " " + s.name;// 记录添加边操作
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
			Collections.sort(v.pVs);//按字曲顺序排序，保证等价模型对应的字符串相同
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
