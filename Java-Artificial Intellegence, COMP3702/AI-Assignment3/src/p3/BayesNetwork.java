package p3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BayesNetwork {
	private static BufferedReader br;
	private static BufferedWriter out;
	private static int C = 5;
	private static long runTime = 3 * 60 * 1000;
	private static int threshold = -10000;
	private static Double logLikeHood;

	public static Double scoring(DAGStruct struct, ExpRecords exps) {
		logLikeHood = ComputeLoglikeliHood(struct, exps);
		return logLikeHood - C * struct.getEptNodeCount();
	}

	public static Double ComputelikeliHood(DAGStruct struct, ExpRecords exps) {
		Double likeli = 0.0, tmp = 0.0;
		for (List<String> r : exps.getData()) {
			for (int i = 0; i < r.size(); i++) {
				tmp = struct.Getcp(exps.getNames().get(i), exps.getCPTIndex(
						struct.getParentVsOfV(exps.getNames().get(i)), r));
				if (r.get(i).contentEquals("0")) {
					tmp = 1.0 - tmp;
				}
				likeli *= tmp;
			}
		}
		return likeli;
	}

	public static Double ComputeLoglikeliHood(DAGStruct struct, ExpRecords exps) {
		// return Math.log10(ComputelikeliHood(struct,exps));
		Double likeli = 0.0, tmp = 0.0, tmp1 = 1.0;
		for (List<String> r : exps.getData()) {
			tmp1 = 1.0;
			for (int i = 0; i < r.size(); i++) {
				tmp = struct.Getcp(exps.getNames().get(i), exps.getCPTIndex(
						struct.getParentVsOfV(exps.getNames().get(i)), r));
				if (r.get(i).contentEquals("0")) {
					tmp = 1.0 - tmp;
				}
				tmp1 *= tmp;
			}
			likeli += Math.log10(tmp1);
			;
		}
		return likeli;
	}

	
	public static void ComputeCPT(DAGStruct Struct, ExpRecords exps) {
		for (String s : Struct.getVnames()) {
			List<Double> cp = new ArrayList<Double>();
			for (int i = 0; i < Math.pow(2, Struct.getParentVsOfV(s).size()); i++) {
				cp.add(Double.valueOf(exps.count(s, Struct.getParentVsOfV(s), i)));
			}
			// System.out.println(cp);
			Struct.Setcpt(s, cp);
		}
	}

	public static void SetOut(String args) throws Exception {

		PrintStream ps = new PrintStream(new FileOutputStream(args));
		System.setOut(ps);
	}

	
	public static long computeMaxModelCount(long c) {
		long rVal = (long) Math.pow(3, c * (c - 1) / 2);
		long ringCount = 0;
		for (long i = 3; i <= c; i++) {
			ringCount = ringCount * i + 2;
		}
		return rVal - ringCount;
	}

	public static void LearnStructureCPTFromWithMissingData(String pathname,
			String OutFilePath, boolean tl) {
		Double curScore = 1.0;
		Double bestScore = 1.0;
		boolean greedy = tl;
		try { 
			File filename = new File(pathname); 
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(filename)); 
			br = new BufferedReader(reader);
			String line = "";
			StringBuffer OUTString = new StringBuffer();
			line = br.readLine();
			String[] KN = line.split(" ");
			ExpRecords exps = new ExpRecords();
			DAGStruct Struct = new DAGStruct();
			line = br.readLine(); 
			List<String> list = Arrays.asList(line.split(" "));
			for (int i = 0; i < list.size(); i++) {
				exps.addC(list.get(i));
				Struct.AddV(list.get(i));
			}
			List<String> data = new ArrayList<String>();
			Map<String, Double> MissingData = new HashMap<String, Double>();
			List<String> MissingDataLines = new ArrayList<String>();
			Map<String, String> HvarNameMap = new HashMap<String, String>();
			boolean esitmateMissing = false;
			for (int n = Integer.valueOf(KN[1]); n > 0; n--) {
				line = br.readLine(); 
				data.add(line);
				if (line.contains("H")) {
					int i = 0;
					for (String w : line.split(" ")) {
						if (w.contains("H")) {
							esitmateMissing = true;
							MissingData.put(w, 0.5);
							HvarNameMap.put(w, exps.getNames().get(i));
						}
						i++;
					}
					MissingDataLines.add(line);
				}
			}
			br.close();
			exps.setExps(data);
			exps.setMissingData(MissingData);
			long startMili = System.currentTimeMillis();
			Struct.GenerateBestTreeModel(exps.BuildEdges());
			ComputeCPT(Struct, exps);
			bestScore = scoring(Struct, exps);
			OUTString = new StringBuffer();
			OUTString.append(Struct.outDAG());
			OUTString.append(Struct.outCPT().trim());
			OUTString.append(System.lineSeparator() + logLikeHood + " "
					+ String.valueOf(bestScore));
			Set<String> ModelSet = new HashSet<String>();
			long PossibleModelCount, searchedCount = 0;
			PossibleModelCount = computeMaxModelCount(exps.getNames().size());
			System.out.println("Possible Model counts: " + PossibleModelCount);
			do {
				boolean esitmateMissingNotFinished = esitmateMissing;
				
				Struct.ApplyRandomModify();
				String Model = Struct.outDAG();
				if (ModelSet.contains(Model)) {
					if (ModelSet.size() >= PossibleModelCount) {
						System.out.println("Possible Model counts: " + PossibleModelCount);
						break;
					} else {
						int triedTimes = 0;
						do {
							triedTimes++;
							if (triedTimes > 20) {
								break;
							}
							Struct.ApplyRandomModify();
							Model = Struct.outDAG();
						} while (((System.currentTimeMillis() - startMili) < runTime)
								&& ModelSet.contains(Model));
						if (triedTimes > 20) {
							System.out
									.println("No new model is found after 20 tries");
							break;
						}
					}
				}
				if (ModelSet.size() < 3000)
					ModelSet.add(Model);
				searchedCount++;
				System.out.println(Model);
				System.out.println("Search times: " + searchedCount);
				int infereceCount = 0;
				do {
					ComputeCPT(Struct, exps);
					Map<String, Double> newMap = new HashMap<String, Double>();
					for (String r : MissingDataLines) {
						Map<String, Double> E = new HashMap<String, Double>();
						Set<String> Q = new HashSet<String>();
						int i = 0;
						for (String w : r.split(" ")) {
							if (w.contains("H")) {
								Q.add(w);
							} else {
								E.put(exps.getNames().get(i), Double.valueOf(w));
							}
							i++;
						}
						newMap.putAll(Struct.VariableEliminationInference(Q, E,
								HvarNameMap));
					}
					Map<String, Double> oldMap = exps.getMissingData();
					boolean esitmateMissingFinished = true;
					infereceCount++;
					System.out.println("infereceCount=" + infereceCount);
					for (String h : oldMap.keySet()) {
						System.out.println(h + "=" + newMap.get(h)+"?"+oldMap.get(h));
						esitmateMissingFinished &= (Math.abs(oldMap.get(h)-
								newMap.get(h))<0.000001);
					}
					esitmateMissingNotFinished = !esitmateMissingFinished;
					if (esitmateMissingNotFinished) {
						exps.setMissingData(new HashMap<String, Double>());
						exps.getMissingData().putAll(newMap);
					}
				} while (esitmateMissingNotFinished
						&& ((System.currentTimeMillis() - startMili) < runTime));
				curScore = scoring(Struct, exps);
				if (curScore > bestScore) {
					bestScore = curScore;
					OUTString = new StringBuffer();
					OUTString.append(Struct.outDAG());
					OUTString.append(Struct.outCPT().trim());
					OUTString.append(System.lineSeparator() + logLikeHood + " "
							+ String.valueOf(bestScore));
					greedy = tl;
				} else {
					if (greedy && (curScore - bestScore) > (bestScore / 5.5)) {
						Struct.undo();
						greedy = false;
					}
				}
				System.out.println("Current Score: " + logLikeHood + ":" + curScore);
				System.out.println("Best Score: " + bestScore);
				System.out.println("Threshold: " + threshold);
			} while (((System.currentTimeMillis() - startMili) < runTime)
					&& (bestScore < threshold));

			File writename = new File(OutFilePath);
			writename.createNewFile();
			out = new BufferedWriter(new FileWriter(writename));
			out.write(OUTString.toString());
			out.write(exps.OUTMissing());
			out.flush(); 
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// PrintStream sysout = System.out;
		// SetOut("data/log.txt");
		C = 5;
		threshold = -25;
		LearnStructureCPTFromWithMissingData("data/someMissingData-d1.txt",
				"result/bn-someMissingData-d1.txt", false);
		C = 5;
		threshold = -100;
		LearnStructureCPTFromWithMissingData("data/someMissingData-d2.txt",
				"result/bn-someMissingData-d2.txt", false);
		C = 1;
		threshold = -9600;
		LearnStructureCPTFromWithMissingData("data/someMissingData-d3.txt",
				"result/bn-someMissingData-d3.txt", false);
	}
}
