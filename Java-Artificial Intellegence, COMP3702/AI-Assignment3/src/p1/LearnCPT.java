package p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LearnCPT {
	private static BufferedReader br;
	private static BufferedWriter out;
	/**
	 * Compute the likelihood
	 * @param struct
	 * @param exps
	 * @return likelihood value
	 */
	public static Double ComputelikeliHood(DAGStruct struct, ExpRecords exps) {
		Double likeli = 0.0,tmp=0.0;
		for (List<String> r : exps.getData()) {
			for (int i = 0; i < r.size(); i++) {
				tmp=struct.Getcp(exps.getNames().get(i), exps
						.getCPTIndex(
								struct.getParentVsOfV(exps.getNames().get(i)),
								r));
				if(r.get(i).contentEquals("0")){tmp=1.0-tmp;}
				likeli*=tmp;
			}
		}
		return likeli;
	}
	/**
	 * Compute the loglikelihood. BASE 10
	 * @param struct
	 * @param exps
	 * @return loglikelihood value
	 */
	public static Double ComputeLoglikeliHood(DAGStruct struct, ExpRecords exps) {
		Double likeli = 0.0,tmp=0.0;
		for (List<String> r : exps.getData()) {
			for (int i = 0; i < r.size(); i++) {
				tmp=struct.Getcp(exps.getNames().get(i), exps
						.getCPTIndex(
								struct.getParentVsOfV(exps.getNames().get(i)),
								r));
				if(r.get(i).contentEquals("0")){tmp=1.0-tmp;}
				tmp= Math.log10(tmp);
				likeli+=tmp;
			}
		}
		return likeli;
	}
	/**
	 * File input and processing
	 * @param pathname
	 * @param OutFilePath
	 */
	public static void LearnCPTFromNoMissingData(String pathname,
			String OutFilePath) {
		try { 
			File filename = new File(pathname); 
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(filename)); 
			br = new BufferedReader(reader);
			String line = "";
			line = br.readLine(); 
			String[] KN = line.split(" ");
			DAGStruct Struct = new DAGStruct();
			ExpRecords exps = new ExpRecords();
			for (int k = Integer.valueOf(KN[0]); k > 0; k--) {
				line = br.readLine(); 
				List<String> list = Arrays.asList(line.split(" "));
				Struct.AddV(list.get(0));
				exps.addC(list.get(0));
				for (int i = 1; i < list.size(); i++) {
					Struct.AddE(list.get(0), list.get(i));
				}
			}
			List<String> data = new ArrayList<String>();
			for (int n = Integer.valueOf(KN[1]); n > 0; n--) {
				line = br.readLine(); 
				data.add(line);
			}
			br.close();
			exps.setExps(data);
			for (String s : Struct.getVnames()) {
				List<Double> cp = new ArrayList<Double>();
				for (int i = 0; i < Math
						.pow(2, Struct.getParentVsOfV(s).size()); i++) {
					cp.add(Double.valueOf(exps.count(s,
							Struct.getParentVsOfV(s), i)));
				}
				Struct.Setcpt(s, cp);
			}

			File writename = new File(OutFilePath);
			writename.createNewFile();
			out = new BufferedWriter(new FileWriter(writename));
			out.write(Struct.outDAG().trim());
			out.write(System.lineSeparator()+String.valueOf(ComputeLoglikeliHood(Struct, exps)));
			out.flush(); 
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LearnCPTFromNoMissingData("data/CPTNoMissingData-d1.txt",
				"result/cpt-CPTNoMissingData-d1.txt");
		LearnCPTFromNoMissingData("data/CPTNoMissingData-d2.txt",
				"result/cpt-CPTNoMissingData-d2.txt");
		LearnCPTFromNoMissingData("data/CPTNoMissingData-d3.txt",
				"result/cpt-CPTNoMissingData-d3.txt");
	}
}
