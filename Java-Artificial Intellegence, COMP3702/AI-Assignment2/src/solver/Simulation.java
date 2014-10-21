package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import problem.Action;
import problem.Cycle;
import problem.Direction;
import problem.Distractor;
import problem.GridCell;
import problem.Node;
import problem.Opponent;
import problem.Player;
import problem.RaceSim;
import problem.RaceSimTools;
import problem.RaceState;
import problem.Setup;
import problem.Tour;
import problem.Track;
import problem.Cycle.Speed;

public class Simulation {
	
    private static Random random = new Random();
//    private static Long startTime;
//	
//	private static Long endTime;
    public static class Race implements Comparable<Race>{
    	Double profit;
    	Track track;
    	Cycle cycle;
    	public Race(Double profit, Track track, Cycle cycle){
    		this.profit = profit;
    		this.track = track;
    		this.cycle = cycle;
    	}
		@Override
		public int compareTo(Race o) {
			// TODO Auto-generated method stub
			Double myprofit = profit;
			Double oprofit = o.profit;
			return oprofit.compareTo(myprofit);
		}
    }
    
    
	public static List<Race> calculator(Tour tour){
		List<Race> res = new ArrayList<Race>();
		List<Cycle> purchasableCycles = tour.getPurchasableCycles();
		List<Track> allTracks = tour.getTracks();
		for(int i = 0; i < purchasableCycles.size(); i++){
			for (int j = 0; j < allTracks.size(); j++){
				double money = 0;
				//System.out.println(allTracks.size());
				Cycle cycle = purchasableCycles.get(i);
				//System.out.println(cycle);
				Track track = allTracks.get(j);
				
				Map<String, GridCell> startingPositions = 
						track.getStartingPositions();
				String id = "";
				GridCell startPosition = null;				
				for (Map.Entry<String, GridCell> entry : startingPositions.entrySet()) {
					id = entry.getKey();
					startPosition = entry.getValue();
					break;
				}
				Player player = new Player(id, cycle, startPosition);

				List<Player> players = new ArrayList<Player>();
				players.add(new Player(id, cycle, startPosition));
				//System.out.println(players);
				List<Opponent> opponents = track.getOpponents();
				
				List<Distractor> distractors = track.getDistractors();
				Random random = new Random();
				RaceState raceState = new RaceState(players, opponents, distractors);
				RaceSim simulator = new RaceSim(raceState, track, random);
				
				while(!simulator.isFinished()){
					RaceState state = simulator.getCurrentState();
	
					Node<RaceState> root = new Node<RaceState>(state);
					List<Node<RaceState>> nodeList = new ArrayList<Node<RaceState>>();
					nodeList.add(root);
					
					//set start time
//					startTime = System.currentTimeMillis();
					
					//start to simulate
					nextSimulate(nodeList,track);
					
					//set end time
//					endTime = System.currentTimeMillis();
//					
//					//re-simulate if runtime < 1s
//					while((endTime-startTime)/1000<0.000000001) {
//						for(Node<RaceState> node : root.getChildren()) {
//							node.clearChildren();
//						}
//						nextSimulate(root.getChildren(),track);
//						endTime = System.currentTimeMillis();
//					}
					
					for(int a=0;a<10; a++){
						for(Node<RaceState> node : root.getChildren()) {
							node.clearChildren();
						}
						nextSimulate(root.getChildren(),track);
					}
					
					List<Action> actions = new ArrayList<Action>();
					Node<RaceState> selectNode = select(root,track);
					actions.add(selectNode.getAction());
					if(actions.get(0) == null){
						break;
					}
					simulator.stepTurn(actions);
				}
				if (simulator.isFinished()) {
					money -= simulator.getTotalDamageCost();
				}
				money -= track.getRegistrationFee();
				if(simulator.getCurrentStatus() == RaceState.Status.WON){
					money += track.getPrize();
				}
				//money -= cycle.getPrice();
				Race r = new Race(money, track, cycle);
				r.cycle = cycle;
				r.profit = money;
				r.track = track;
				res.add(r); 
			}
		}
		return res;
	}
	private static Node<RaceState> select(Node<RaceState> root, Track track) {
		double maxValue = 0;
		Node<RaceState> selectNode = new Node<RaceState>();
		for(Node<RaceState> node : root.getChildren()) {
			double value = node.getValue()+track.getPrize()
					*Math.sqrt(Math.log(node.getParent().getVisit())/node.getVisit());
			if(value>=maxValue) {
				maxValue = value;
				selectNode = node;
			}
		}
		return selectNode;
	}
	
	//expand the node, add children
	private static void expand(Node<RaceState> root, Track track) {
		Player player = root.getData().getPlayers().get(0);
		GridCell playerCurrentPos = player.getPosition();
		Cycle cycle = player.getCycle();
		List<Action> actions = getNextActionList(playerCurrentPos,
				track,cycle);
		
			for(Action action : actions) {
				List<Action> nextAction = new ArrayList<Action>();
				nextAction.add(action);
				Node<RaceState> child = new Node<RaceState>(RaceSimTools.sampleNextState(
						root.getData(), nextAction, track, random), action, root);
			}	
		
	}
	
	//calculate the value
	private static Double rollOut(Node<RaceState> startNode, Track track) {
		double money = 0;
		RaceState.Status currentStatus = startNode.getData().getStatus();
		if(currentStatus!=RaceState.Status.RACING) {
			money -= startNode.getData().getTotalDamageCost();
			if(currentStatus==RaceState.Status.WON) {
				money += track.getPrize();
			}
		} else {
			Player player = startNode.getData().getPlayers().get(0);
			GridCell playerCurrentPos = player.getPosition();
			Cycle cycle = player.getCycle();
			//get possible actions
			List<Action> actions = getNextActionList(playerCurrentPos,
					track,cycle);	
			List<Action> nextAction = new ArrayList<Action>();
			/*if(nextAction.size()==0){
				money = -10000;
				return money ;
			}*/
			//get a random action from action list	
			nextAction.add(actions.get(random.nextInt(actions.size())));
			//sample next state using random action
			Node<RaceState> child = new Node<RaceState>(RaceSimTools.sampleNextState(
					startNode.getData(), nextAction, track, random), startNode);
			money -= child.getData().getTotalDamageCost();
			//continue to calculate money until racing is finished
			money += rollOut(child,track);
		}
		return money;
	}
	
	//update the value and visit times
	private static void update(Node<RaceState> node, Double value) {
		List<Node<RaceState>> nodeAncestry = node.getAncestry();
		for(Node<RaceState> p : nodeAncestry) {
			p.addVisit(1);
		}
		
		if(nodeAncestry.size()>=2) {
			Node<RaceState> targetNode = nodeAncestry.get(1);
			targetNode.setValue((value+targetNode.getValue())/targetNode.getVisit());
		}
	}
	
	//check the current state is finished or not
	private static boolean checkRacing(Node<RaceState> node) {
		RaceState.Status currentStatus = node.getData().getStatus();
		return currentStatus==RaceState.Status.RACING;
	}
	
	//simulate nodes of each breadth
	private static void nextSimulate(List<Node<RaceState>> nodeList, Track track) {
		List<Node<RaceState>> nextList = new ArrayList<Node<RaceState>>();
		for(Node<RaceState> node : nodeList) {
			if(checkRacing(node)) {
				//expand the node
				expand(node,track);
				for(Node<RaceState> childNode : node.getChildren()) {
					//calculate money(value) by simulation
					double value = rollOut(childNode,track);
					//update value to this node
					update(childNode,value);
					nextList.add(childNode);
				}
			}
		}
//		endTime = System.currentTimeMillis();
//		if(nextList.size()>0 && ((endTime - startTime)/1000)<0.000000001) {
//			nextSimulate(nextList,track);
//		}
	}
	
/*	//return the valid action list
	private static List<Action> getNextActionList(GridCell currentPosition, 
			Track track, Cycle cycle) {
		List<Action> actions = new ArrayList<Action>();
		
		GridCell nextPos = currentPosition;
		
		nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null 
				&& (!RaceSimTools.isObstacle(nextPos, track) && !cycle.isWild())) {
			actions.add(Action.FS);
			return actions;
		}
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null 
				&& cycle.getSpeed() != Speed.SLOW 
				&& (!RaceSimTools.isObstacle(nextPos, track) && !cycle.isWild())) {
			actions.add(Action.FM);
			return actions;
		}
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null 
				&& cycle.getSpeed()!=Speed.SLOW && cycle.getSpeed()!=Speed.MEDIUM
				&& (!RaceSimTools.isObstacle(nextPos, track) && !cycle.isWild())) {
			actions.add(Action.FF);
		}
		
		nextPos = currentPosition.shifted(Direction.NE);
		if(nextPos != null 
				&& (!RaceSimTools.isObstacle(nextPos, track) && !cycle.isWild())) {
			actions.add(Action.NE);
		}	
		
		nextPos = currentPosition.shifted(Direction.SE);
		if(nextPos != null 
				&& (!RaceSimTools.isObstacle(nextPos, track) && !cycle.isWild())) {
			actions.add(Action.SE);
		}
			
		return actions;
	}*/
	
	private static List<Action> getNextActionList(GridCell currentPosition, 
			Track track, Cycle cycle) {
		List<Action> actions = new ArrayList<Action>();
		
		GridCell nextPos = currentPosition;
				
		nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		nextPos = nextPos.shifted(Direction.E);
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null  
				&& cycle.getSpeed()!=Speed.SLOW && cycle.getSpeed()!=Speed.MEDIUM
				) {
			actions.add(Action.FF);
			return actions;
		}
		
		nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null  
				&& cycle.getSpeed() != Speed.SLOW ) {
			actions.add(Action.FM);
			return actions;
		}
		
		nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null ) {
			actions.add(Action.FS);
			
		}
		
		nextPos = currentPosition.shifted(Direction.NE);
		if(nextPos != null) {
			actions.add(Action.NE);
		}	
		
		nextPos = currentPosition.shifted(Direction.SE);
		if(nextPos != null) {
			actions.add(Action.SE);
		}
		
		return actions;
	}
	
	
	public static List<List<Race>>sortByCycle (List<Race> res, Tour tour){
		List<List<Race>> out = new ArrayList<List<Race>>();
		List<Cycle> purchasableCycles = tour.getPurchasableCycles();
		for (int i = 0; i < purchasableCycles.size(); i++) {
			List<Race>  r = new ArrayList<Race>();
			for(Race c : res){
				if(c.cycle.equals(purchasableCycles.get(i))){
					r.add(c);
				}
			}
			Collections.sort(r);
			out.add(r);
		}
		//System.out.println(out);
		return out;
	}

	public static List<List<Race>>sortRes (List<List<Race>> res, Tour tour){
		List<List<Race>> out = new ArrayList<List<Race>>();
		for(int i = 0 ; i < res.size(); i++){
			double money = tour.getCurrentMoney();
			money -= res.get(i).get(0).cycle.getPrice();
			List<Race> r = new ArrayList<Race>();
			for(Race c : res.get(i)){
				if(money>=c.track.getRegistrationFee()){
					money-=c.track.getRegistrationFee();
					r.add(c);
				}
				if(r.size()==3) {
					break;
				}
			}
			out.add(r);
		}
		return out;
	}
	
	public static List<Race> finalize(List<List<Race>> res){
		List<Race> out = new ArrayList<Race>();
		double maxProfit = 0;
		System.out.println(res.size());
		for(int i = 0 ; i < res.size(); i++){
			double currentProfit = 0;
			for(Race c : res.get(i)){
				currentProfit+=c.profit;
			}
			if(currentProfit>maxProfit) {
				maxProfit = currentProfit;
				out = res.get(i);
			}
			else if (currentProfit==maxProfit && currentProfit != 0) {
				if(res.get(i).get(0).cycle.getPrice() < out.get(0).cycle.getPrice()) {
					maxProfit = currentProfit;
					out = res.get(i);
				}
			}
		}
		return out;
	}
	
	
	public static List<List<Race>>sortRes2(List<Race> res, Tour tour){
		List<List<Race>> out = new ArrayList<List<Race>>();
		for(int i=0; i< res.size(); i++){
			List<Race> firstList = new ArrayList<Race>();
			double money = tour.getCurrentMoney();
			Race firstRace = res.get(i);
			money -= firstRace.cycle.getPrice();
			if(money>=firstRace.track.getRegistrationFee()){
				money-=firstRace.track.getRegistrationFee();
				firstList.add(firstRace);
				for(int ii=i+1; ii< res.size(); ii++){
					List<Race> secList = firstList;
					Race secRace = res.get(ii);
					if(secRace.cycle!=firstRace.cycle) {
						money -= secRace.cycle.getPrice();
					}
					if(money>=secRace.track.getRegistrationFee()) {
						money -= secRace.track.getRegistrationFee();
						secList.add(secRace);	
						for(int iii=ii+1; iii< res.size(); iii++){
							List<Race> thirdList = secList;
							Race thirdRace = res.get(iii);
							if(thirdRace.cycle!=firstRace.cycle && thirdRace.cycle!=secRace.cycle) {
								money -= thirdRace.cycle.getPrice();
							}
							if(money>=thirdRace.track.getRegistrationFee()) {
								money -= thirdRace.track.getRegistrationFee();
								thirdList.add(thirdRace);
							} 
							if(!out.contains(thirdList)) {
								out.add(thirdList);
							}
							if(iii==res.size()-1) {
								break;
							} else {
								continue;
							}	
						}
					} else {
						if(!out.contains(secList)) {
							out.add(secList);	
						}
						if(ii==res.size()-1) {
							break;
						} else {
							continue;
						}
					}
				}
			} else {
				if(i==res.size()-1) {
					break;
				} else {
					continue;
				}
			}
		}
		return out;
	}
	
	public static List<Race> finalize2(List<List<Race>> res){
		double maxProfit = 0;
		List<Race> bestRaceList = new ArrayList<Race>();
		for(int i=0; i<res.size(); i++) {
			double totalProfit = 0;
			List<Race> list = res.get(i);
			for(Race race : list) {
				totalProfit += race.profit;
			}
			if(totalProfit>maxProfit) {
				maxProfit = totalProfit;
				bestRaceList = list; 
			} else if (totalProfit==maxProfit) {
				double cycleMoney = 0;
				double cycleMoney1 = 0;
				for(Race r: list) {
					cycleMoney += r.cycle.getPrice();
				}
				for(Race r: bestRaceList) {
					cycleMoney1 += r.cycle.getPrice();
				}
				if(cycleMoney>=cycleMoney1) {
					maxProfit = totalProfit;
					bestRaceList = list; 
				}
			}
		}
		return bestRaceList;
	}
	
	public static List<Race> compare(List<Race> list, List<Race> list1) {
		List<Race> out = new ArrayList<Race>();
		double cycleMoney = 0;
		double cycleMoney1 = 0;
		for(Race r: list) {
			cycleMoney += r.cycle.getPrice();
		}
		for(Race r: list1) {
			cycleMoney1 += r.cycle.getPrice();
		}
		if(cycleMoney>=cycleMoney1) {
			out = list; 
		}
		return out;
	}
}
