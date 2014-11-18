package p2;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BayesNetwork {
	private static BufferedReader br;
	private static BufferedWriter out;
	private static int C = 5;
	private static long runTime = 3 * 60 * 1000;
	private static int threshold = -10000;
	private static Double logLikeHood;
	/**
	 * Score function
	 * @param struct
	 * @param exps
	 * @return Score
	 */
	public static Double scoring(DAGStruct struct, ExpRecords exps) {
		logLikeHood = ComputeLoglikeliHood(struct, exps);
		return logLikeHood - C * struct.getEptNodeCount();
	}
	/**
	 * Compute the likelihood
	 * @param struct
	 * @param exps
	 * @return likelihood value
	 */
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
	/**
	 * Compute the loglikelihood. BASE 10
	 * @param struct
	 * @param exps
	 * @return loglikelihood value
	 */
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
	
	public static void ComputeEPT(DAGStruct Struct, ExpRecords exps) {
		for (String s : Struct.getVnames()) {
			List<Double> cp = new ArrayList<Double>();
			for (int i = 0; i < Math.pow(2, Struct.getParentVsOfV(s).size()); i++) {
				cp.add(Double.valueOf(exps.count(s, Struct.getParentVsOfV(s), i)));
			}
			Struct.Setcpt(s, cp);
		}
	}

	public static void SetOut(String args) throws Exception {
		PrintStream ps = new PrintStream(new FileOutputStream(args));
		System.setOut(ps);
	}
	/**
	 * Compute the maximum models
	 * @param c
	 * @return maximum models
	 */
	public static long computeMaxModelCount(long c) {
		long rVal = (long) Math.pow(3, c * (c - 1) / 2);
		long ringCount = 0;
		for (long i = 3; i <= c; i++) {
			ringCount = ringCount * i + 2;
		}
		return rVal - ringCount;
	}

	/**
	 * File input and processing. 
	 * @param pathname
	 * @param OutFilePath
	 * @param ini represents the DAG structure.
	 * 			  0->search the best one by score. 
	 * 			  1->best tree. 
	 * 			  2->no edges model. 
	 * 			  3->random chain.
	 * @param tl perform a greedy method or not
	 */
	public static void LearnStructureCPTFromNoMissingData(String pathname,
			String OutFilePath, int ini, boolean tl) {
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
			for (int n = Integer.valueOf(KN[1]); n > 0; n--) {
				line = br.readLine(); 
				data.add(line);
			}
			br.close();
			exps.setExps(data);
			long startMili = System.currentTimeMillis();
			switch (ini) {
			case 0:
				Struct.GenerateBestTreeModel(exps.BuildEdges());
				ComputeEPT(Struct, exps);
				Double Score1 = scoring(Struct, exps);
				Struct.GenerateNoEdgeModel();
				ComputeEPT(Struct, exps);
				Double Score2 = scoring(Struct, exps);
				if (Score2 > Score1) {
					bestScore = Score2;
				} else {
					bestScore = Score1;
				}
				Struct.GenerateRandomChainModel();
				ComputeEPT(Struct, exps);
				Double Score3 = scoring(Struct, exps);
				if (Score3 > bestScore) {
					bestScore = Score3;
					break;
				} else {
					if (Score2 > Score1) {
						Struct.GenerateNoEdgeModel();
					} else {
						Struct.GenerateBestTreeModel(exps.BuildEdges());
					}
					ComputeEPT(Struct, exps);
				}
				break;
			case 1:
				Struct.GenerateBestTreeModel(exps.BuildEdges());
				ComputeEPT(Struct, exps);
				bestScore = scoring(Struct, exps);
				break;
			case 2:
				Struct.GenerateNoEdgeModel();
				ComputeEPT(Struct, exps);
				bestScore = scoring(Struct, exps);
				break;
			case 3:
				Struct.GenerateRandomChainModel();
				ComputeEPT(Struct, exps);
				bestScore = scoring(Struct, exps);
				break;
			default:
				System.out.println("Error ,unknown Iniate Model Code");
				System.exit(1);
			}
			OUTString = new StringBuffer();
			OUTString.append(Struct.outDAG());
			OUTString.append(Struct.outCPT().trim());
			OUTString.append(System.lineSeparator() + logLikeHood + " "
					+ String.valueOf(bestScore));
			Set<String> ModelSet = new HashSet<String>();
			long PossibleModelCount, searchedCount = 0;
			PossibleModelCount = computeMaxModelCount(exps.getNames().size());
			System.out.println("Possible Model counts:" + PossibleModelCount);
			do {
				Struct.ApplyRandomModify();
				String Model = Struct.outDAG();
				if (ModelSet.contains(Model)) {
					if (ModelSet.size() >= PossibleModelCount) {
						System.out.println("Possible Model counts:" + PossibleModelCount);
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
							System.out.println("No new model is found after 20 tries");
							break;
						}
					}
				}
				if (ModelSet.size() < 3000)
					ModelSet.add(Model);
				searchedCount++;
				System.out.println(Model);
				System.out.println("Search times: " + searchedCount);
				ComputeEPT(Struct, exps);
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
			out.flush(); 
			out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		//Log base 10.
		PrintStream sysout = System.out;
		SetOut("data/log.txt");//logfile for checking.
		/**
		 * Task 4
		 */
		C = 5;
		threshold = 10;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d1.txt",
				"result/bn-noMissingData-d1.txt", 0, false);
		// SetOut("data/log1.txt"); for test output
		C = 5;
		threshold = -4;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d2.txt",
				"result/bn-noMissingData-d2.txt", 0, false);
		// SetOut("data/log2.txt"); for test output
		C = 1;
		threshold = -9565;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d3.txt",
				"result/bn-noMissingData-d3.txt", 0, true);
		
		/**
		 * Task 5 Please feel free to modify the variable C and threshold below.
		 * Comparing strategy: 
		 * 1. C is constant, change the threshold
		 * 2. Threshold is constant, change the C
		 * Conclusion: 
		 * C is constant. It will take longer search time(more search times)if 
		 * the threshold is bigger. It also requires a better Bayes Network structure 
		 * Threshold is constant. It requires a simpler Bayes Networks structure
		 * (Less CPT relations between nodes) if the C is bigger.
		 * 
		 */
		System.setOut(sysout);//logfile.
		C = 6;
		threshold = -41;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d1.txt",
				"result/Task5-bn-noMissingData-d1.txt", 0, true);
		C = 6;
		threshold = -115;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d2.txt",
				"result/Task5-bn-noMissingData-d2.txt", 0, true);
		C = 2;
		threshold = -9583;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d3.txt",
				"result/Task5-bn-noMissingData-d3.txt", 0, true);
		
		
		/**
		 * Task 6
		 * the result file ends with "-n" is no edge model.
		 * the result file ends with "-r" is random chain model.
		 * A larger threshold value makes the search time longer(more search times). 
		 * The following two tasks are for comparing with different model of 
		 * bayes network which include no edge, random chain and best tree.
		 * In terms of comparing the scoring and structure complexity for different models.
		 * I set the same threshold and Constant C for different models set.
		 * I.e. for data set1 -> C = 5, threshold = -41; 
		        for data set2 -> C = 5, threshold = -41
		        for data set3 -> C = 1, threshold = -961
		   Conclusion: (I will also put the answer in the PDF file.)
		   Random chain gives a better score and more complex 
		   Bayes Network structure on "more complex CPT relations" than the No Edge model.
		   
		   No Edge model gives the same score based on simpler CPT relations as 
		   Random chain gives.
		   
		 */
		//For no edge model. 
		C = 5;
		threshold = -41;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d1.txt",
				"result/Task6-bn-noMissingData-d1-n.txt", 2, true);
		C = 5;
		threshold = -11;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d2.txt",
				"result/Task6-bn-noMissingData-d2-n.txt", 2, true);
		C = 1;
		threshold = -961;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d3.txt",
				"result/Task6-bn-noMissingData-d3-n.txt", 2, true);
		//For random chain.
		C = 5;
		threshold = -41;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d1.txt",
				"result/Task6-bn-noMissingData-d1-r.txt", 3, true);
		C = 5;
		threshold = -11;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d2.txt",
				"result/Task6-bn-noMissingData-d2-r.txt", 3, true);
		C = 1;
		threshold = -961;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d3.txt",
				"result/Task6-bn-noMissingData-d3-r.txt", 3, true);
		
		/**
		 * Task7 
		 * the result file ends with "-b" is best tree model.
		 * I set the same threshold and Constant C as it in Task6.
		 * I.e. for data set1 -> C = 5, threshold = -41; 
		        for data set2 -> C = 5, threshold = -41
		        for data set3 -> C = 1, threshold = -961
		   Conclusion:
		   The score generated by Best tree model is 
		   between the Random chain and No edge model on more complex CPT relations
		   
		   Those three models almost have the same score on simple CPT relations.
		 */
		//Best tree model
		C = 5;
		threshold = -41;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d1.txt",
				"result/Task7-bn-noMissingData-d1-b.txt", 1, true);
		C = 5;
		threshold = -11;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d2.txt",
				"result/Task7-bn-noMissingData-d2-b.txt", 1, true);
		C = 1;
		threshold = -961;
		LearnStructureCPTFromNoMissingData("data/noMissingData-d3.txt",
				"result/Task7-bn-noMissingData-d3-b.txt", 1, true);
		
	}
}
