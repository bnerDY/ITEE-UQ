package p1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class DAGStruct {
	private class V {
		private String name;
		private List<String> pVs=new ArrayList<String>();
		private List<Double> cpt=new ArrayList<Double>();
		public V(String n){
			name=n;
		}
	}
	private List<V> vs = new ArrayList<V>();
	private Map<String,Integer> nameIndexMap=new HashMap<String,Integer>();
	public void AddV(String vname) {
		V vtmp=new V(vname);
		vs.add(vtmp);
		nameIndexMap.put(vname, vs.size()-1);
	}
	public void AddE(String s,String d){
		vs.get(nameIndexMap.get(s)).pVs.add(d);
	}
	public Set<String> getVnames(){
		return nameIndexMap.keySet();
	}
	public List<String> getParentVsOfV(String n){
		return vs.get(nameIndexMap.get(n)).pVs;
	}
	public void Setcpt(String n,List<Double> cp){
		vs.get(nameIndexMap.get(n)).cpt=cp;
	}
	public Double Getcp(String s,int index){
		return vs.get(nameIndexMap.get(s)).cpt.get(index);
	}
	public String outDAG(){
		StringBuffer buf=new StringBuffer();
		for(V v:vs){
			buf.append(v.name+" ");
			for(String s:v.pVs){
				buf.append(s+" ");
			}
			buf.deleteCharAt(buf.length()-1);
			buf.append(System.lineSeparator());
			for(Double d:v.cpt){
				buf.append(d.toString()+" ");
			}
			buf.deleteCharAt(buf.length()-1);
			buf.append(System.lineSeparator());
		}
		return buf.toString();
	}
}
