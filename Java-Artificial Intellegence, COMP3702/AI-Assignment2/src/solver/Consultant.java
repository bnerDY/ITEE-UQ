package solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import problem.Action;
import problem.Cycle;
import problem.Cycle.Speed;
import problem.Direction;
import problem.GridCell;
import problem.Node;
import problem.Opponent;
import problem.Player;
import problem.RaceSimTools;
import problem.RaceState;
import problem.Tour;
import problem.Track;
import solver.Simulation.Race;

/**
 * Implement your solver here.
 * @author Joshua Song
 *
 */
public class Consultant {
	
	private Random random = new Random();
	
	private Long startTime;
	
	private Long endTime;
	
	private Long s1;
	private Long s2;
	/**
	 * Solves a tour. Replace existing code with your code.
	 * @param tour
	 */
	public void solveTour(Tour tour) {
		
		
		// You should get information from the tour using the getters, and
		// make your plan.

		// Example: Buy the first cycle that is Wild
		
		List<Cycle> purchasableCycles = tour.getPurchasableCycles();
		Simulation simulation = new Simulation();
		System.out.println("Start offline simulation");
		s1 = System.currentTimeMillis();
		List<Race> res = Simulation.calculator(tour);
		List<List<Race>> out = new ArrayList<List<Race>>();
		out = Simulation.sortByCycle(res, tour);
		out = Simulation.sortRes(out, tour);
		List<Race> result = Simulation.finalize(out);
		//System.out.println(result);
		
//		List<List<Race>> out2 = Simulation.sortRes2(res, tour);
//		List<Race> result2 = Simulation.finalize2(out2);
//		//System.out.println(result2);
//		
//		List<Race> finalResult = Simulation.compare(result, result2);
//		//System.out.println(finalResult);
//		
//		List<Cycle> cycleList = new ArrayList<Cycle>();
//		for(Race r : result) {
//			Cycle cycle = r.cycle;
//			if(!tour.getPurchasedCycles().contains(cycle)) {
//				tour.buyCycle(cycle);	
//			}
//			cycleList.add(cycle);
//		}
//		int i = 0;
		Cycle cycle = result.get(0).cycle;
		tour.buyCycle(cycle);
				
		// Example: register for as many tracks as possible
		for(Race r : result){
			tour.registerTrack(r.track, 1);
		}
		s2 = System.currentTimeMillis();
		long timeCost = s2 - s1;
		System.out.println("Time Cost:" + timeCost);
		System.out.println("Offline simulation ends");
		while (!tour.isFinished()) {			
			if (tour.isPreparing()) {	
				// Race hasn't started. Choose a track, then prepare your
				// players by choosing their cycles and start positions
				
				// Example:
				System.out.println(tour.getUnracedTracks().size());
				Track track = tour.getUnracedTracks().get(0);
				ArrayList<Player> players = new ArrayList<Player>();
				Map<String, GridCell> startingPositions = 
						track.getStartingPositions();
				String id = "";
				GridCell startPosition = null;				
				for (Map.Entry<String, GridCell> entry : startingPositions.entrySet()) {
					id = entry.getKey();
					startPosition = entry.getValue();
					break;
				}

//				Cycle cycle = cycleList.get(i);
//				i++;
				players.add(new Player(id, cycle, startPosition));
				
				// Start race
				tour.startRace(track, players);		
			}
			
			// Decide on your next action here. tour.getLatestRaceState() 
			// will probably be helpful.
			
			// Example: Output current position of player, and current state
			RaceState state = tour.getLatestRaceState();
			Track track = tour.getCurrentTrack();
			
			System.out.println();
			System.out.println("Player position: " + 
					state.getPlayers().get(0).getPosition());
			
			if(state.getOpponents().size()>0) {
				System.out.println("Opponent position: " + 
						state.getOpponents().get(0).getPosition());				
			}
			System.out.println(RaceSimTools.stateToString(state, track));
			
			//expand current node
			Node<RaceState> root = new Node<RaceState>(state);
			List<Node<RaceState>> nodeList = new ArrayList<Node<RaceState>>();
			nodeList.add(root);
			
			//set start time
			startTime = System.currentTimeMillis();
			
			//start to simulate
			nextSimulate(nodeList,track);
			
			//set end time
			endTime = System.currentTimeMillis();
			
			//re-simulate if runtime < 1s
			while((endTime-startTime)/1000<0.5) {
				for(Node<RaceState> node : root.getChildren()) {
					node.clearChildren();
				}
				nextSimulate(root.getChildren(),track);
				endTime = System.currentTimeMillis();
			}
			
			// Example: Keep moving forward slowly
			ArrayList<Action> actions = new ArrayList<Action>();
			
			//select node
			Node<RaceState> selectNode = select(root,track);
			actions.add(selectNode.getAction());
			tour.stepTurn(actions);
		}
	}
	
	//return best node
	private Node<RaceState> select(Node<RaceState> root, Track track) {
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
	private void expand(Node<RaceState> root, Track track) {
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
	private Double rollOut(Node<RaceState> startNode, Track track) {
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
	private void update(Node<RaceState> node, Double value) {
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
	private boolean checkRacing(Node<RaceState> node) {
		RaceState.Status currentStatus = node.getData().getStatus();
		return currentStatus==RaceState.Status.RACING;
	}
	
	//simulate nodes of each breadth
	private void nextSimulate(List<Node<RaceState>> nodeList, Track track) {
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

		endTime = System.currentTimeMillis();
		if(nextList.size()>0 && ((endTime - startTime)/1000)<0.5) {
			nextSimulate(nextList,track);
		}
	}
	
/*	//return the valid action list
	private List<Action> getNextActionList(GridCell currentPosition, 
			Track track, Cycle cycle) {
		List<Action> actions = new ArrayList<Action>();
		
		GridCell nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null && RaceSimTools.withinBounds(nextPos, track)) {
			actions.add(Action.FS);
		}
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null && RaceSimTools.withinBounds(nextPos, track) 
				&& cycle.getSpeed() != Speed.SLOW) {
			actions.add(Action.FM);
		}
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null && RaceSimTools.withinBounds(nextPos, track) 
				&& cycle.getSpeed()!=Speed.SLOW && cycle.getSpeed()!=Speed.MEDIUM) {
			actions.add(Action.FF);
		}
		
		if(currentPosition.shifted(Direction.NE) != null 
				&& RaceSimTools.withinBounds(currentPosition.shifted(Direction.NE), track)) {
			actions.add(Action.NE);
		}	
		if(currentPosition.shifted(Direction.SE) != null 
				&& RaceSimTools.withinBounds(currentPosition.shifted(Direction.SE), track)) {
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
				&& cycle.getSpeed()!=Speed.SLOW && cycle.getSpeed()!=Speed.MEDIUM) {
			actions.add(Action.FF);
			return actions;
		}
		
		nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null  
				&& cycle.getSpeed() != Speed.SLOW) {
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
}
