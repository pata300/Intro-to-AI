package com.stephengware.java.games.chess.bot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Collections;


import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.King;
import com.stephengware.java.games.chess.state.PGN;
import com.stephengware.java.games.chess.state.Pawn;
import com.stephengware.java.games.chess.state.Piece;
import com.stephengware.java.games.chess.state.Player;
import com.stephengware.java.games.chess.state.State;

/**
 * A chess bot which selects its next move at random.
 * 
 * @author Stephen G. Ware
 */
public class MyBot extends Bot {

	/** A random number generator */
	private final Random random;
	private Player currentPlayer;
	private int checkCounter = 0;
	private int minCount = 0;
	private int maxCount = 0;
	private int maxDepth = 3;
	
	/**
	 * Constructs a new chess bot named "My Chess Bot" and whose random  number
	 * generator (see {@link java.util.Random}) begins with a seed of 0.
	 */
	public MyBot() {
		super("PGriffi1");
		this.random = new Random(0);
	}

	@Override
	protected State chooseMove(State root) {
						
		//beginning of my code
		//System.out.println("The current state is " + root.toString());
		
		currentPlayer = root.player;
		State nextState = new State();
		Node currentState = new Node(root, 0, 0);

		try {
//			nextState = randMinMax(currentState);
//			//System.out.println("chooseMove " + nextState.toString());
			if(false) {
			//if( root.turn < 5) {
				Boolean moveFound = false;
				ArrayList<State> children = new ArrayList<>();
				Iterator<State> iterator = root.next().iterator();
				while(!root.searchLimitReached() && iterator.hasNext() && !moveFound) {
					nextState = iterator.next();
					
					moveFound = foundPlay(nextState);		
				}
			} else {
				nextState = minMax(currentState);
			}
				
				
//		} catch(Exception e) {
//			try {
//				if(root.board.countPieces(currentPlayer.other()) == 1)
//					maxDepth = 5;
//				else
//					maxDepth = 3;

				
//				nextState = minMax(currentState);
				//System.out.println("chooseMove " + nextState.toString());
//				try {
//					if(nextState.previous.previous.previous.previous == nextState) {
//						ArrayList<State> children = new ArrayList<>();
//						Iterator<State> iterator = root.next().iterator();
//						while(!root.searchLimitReached() && iterator.hasNext())
//							children.add(iterator.next());
//						// Choose one of the children at random.
//						nextState = children.get(random.nextInt(children.size()));
//					}
//				} catch(Exception e) {
//					
//				}
			} catch (Exception x) {
				System.out.println("Tried MiniMax. Threw " + x.getMessage());
				x.printStackTrace();
				
				ArrayList<State> children = new ArrayList<>();
				Iterator<State> iterator = root.next().iterator();
				while(!root.searchLimitReached() && iterator.hasNext())
					children.add(iterator.next());
				// Choose one of the children at random.
				nextState = children.get(random.nextInt(children.size()));
				
			} 
		//}
		

//		System.out.println("The new state is " + nextState);
////		System.out.println("Moves till draw: " + nextState.movesUntilDraw);
//		System.out.println("My bot is player " + currentPlayer);
//		System.out.println("Max calls min " + minCount + "\nMin calls max " + maxCount);
//		System.out.println("The king is at " + nextState.board.getKing(currentPlayer.other()).file + " " + nextState.board.getKing(currentPlayer.other()).rank);
//		System.out.println("The opponent has " + nextState.board.countPieces(currentPlayer.other()));
		
//		Iterator pieceIter = nextState.board.iterator();
//		while(pieceIter.hasNext()) {
//			System.out.println(pieceIter.next());
//		}
		return nextState;
	}//end of chooseMove
	
	
	//get play from playbook
	protected Boolean foundPlay(State currentState) {

		if(currentState.turn == 0){
			if(currentState.board.pieceAt(4, 3)) {
				return true;
			} else if(currentState.board.pieceAt(4, 4)) {
				return true;
			}
		} else if(currentState.turn == 1) {
			if(currentState.board.pieceAt(2, 3) && !currentState.board.pieceAt(5, 0) && currentState.previous.player.toString() == "WHITE") {
				return true;
			} else if(currentState.board.pieceAt(2, 4) && !currentState.board.pieceAt(5, 7) && currentState.previous.player.toString() == "BLACK") {
				return true;
			}
		} else if(currentState.turn == 2) {
			if(currentState.board.pieceAt(5, 2) && !currentState.board.pieceAt(6, 0) && currentState.previous.player.toString() == "WHITE") {
				return true;
			} else if(currentState.board.pieceAt(5, 5) && !currentState.board.pieceAt(6, 7) && currentState.previous.player.toString() == "BLACK") {
				return true;
			}
		} else if(currentState.turn == 3) {
			if(currentState.board.pieceAt(6, 4) && !currentState.board.pieceAt(5, 2) && currentState.previous.player.toString() == "WHITE") {
				return true;
			} else if(currentState.board.pieceAt(6, 3) && !currentState.board.pieceAt(5, 5) && currentState.previous.player.toString() == "BLACK") {
				return true;
			}
		} else if(currentState.turn == 4) {
			if(currentState.board.pieceAt(5, 6, currentPlayer) && !currentState.board.pieceAt(6, 4) && currentState.previous.player.toString() == "WHITE") {
				return true;
			} else if(currentState.board.pieceAt(5, 1) && !currentState.board.pieceAt(6, 3) && currentState.previous.player.toString() == "BLACK") {
				return true;
			}
		}
		
		return false;
	}
	
	protected State minMax(Node n) {
			//System.out.println("\nStarting MinMax");
		Node best = findMax(n);
		//System.out.println("MiniMax: " + best.getRoot().toString());
		State nextState = null;
		//System.out.println("best node is " + best.getRoot().toString());
		//System.out.println("best node depth is " + best.getDepth());
		if(best.getRoot().previous == null)
			return best.getRoot();
		else {
			
		for(int i = 1; i < best.getDepth(); i++) {
				best.setRoot(best.getRoot().previous);
				//System.out.println("minMax loop: " + best.getRoot().toString());
			}
			
			nextState = best.getRoot();
		}
//			return best.getRoot().previous;
		//System.out.println("Next state loop: " + nextState.toString());
		return nextState;
	}
	
	
	
	protected State randMinMax(Node n) {
		//System.out.println("\nStarting MinMax");
	Node best = findRandMax(n);
	//System.out.println("MiniMax: " + best.getRoot().toString());
	State nextState = null;
	//System.out.println("best node is " + best.getRoot().toString());
	//System.out.println("best node depth is " + best.getDepth());
	if(best.getRoot().previous == null)
		return best.getRoot();
	else {
		
	for(int i = 1; i < best.getDepth(); i++) {
			best.setRoot(best.getRoot().previous);
			//System.out.println("minMax loop: " + best.getRoot().toString());
		}
		
		nextState = best.getRoot();
	}
//		return best.getRoot().previous;
		//System.out.println("Next state loop: " + nextState.toString());
		return nextState;
	}
	
	
	
	protected Node findMax(Node n) {
			//System.out.println("\n***Finding Max of " + n.getRoot().toString());
			maxCount++;
		//if n is a leaf node, return the utility of n
			//System.out.println("Depth is " + depth);
		if(n.isLeaf || n.getRoot().searchLimitReached()) {
			//System.out.println("***RETURNING MAX***");
			return n;
		}
		
		//best = -1000
		int best = -1000;
		int oldBest = -1000;
		Node bestChild = new Node();
		Node childNode = new Node();
		
		//populating list of possible next states
		LinkedList<Node> childList = new LinkedList();
		Iterator<State> iter = n.getRoot().next().iterator();
		while(!n.getRoot().searchLimitReached() && iter.hasNext()) {
				//System.out.println("Creating children of player " + n.getRoot().player);
			//if(n.getDepth() == 0)
				//System.out.println("MAX: Creating new node of depth " + n.getDepth() + " at move " + iter.next().toString());
			Node temp = new Node(iter.next(), n.getUtility(), n.getDepth() + 1);
			
			if(temp.isTerminal())
				return temp;
			
			childList.add(temp);
//				System.out.println("\tChild list move: " + temp.getRoot().toString());
//				System.out.println("\tChild list utility: " + temp.getUtility());
			
			//trying refactor
//			childNode = findMin(temp, ++depth);
//			depth--;
//			//best = max(best, childValue)
//			best = Math.max(best, childNode.getUtility());
//				//System.out.println("Max best: " + best);
//			if(best == childNode.getUtility())
//				bestChild = childNode;
		}
			//System.out.println("\tChild list size: " + childList.size());
			//System.out.println("\tChild list content: " + childList.toString());
		
		ArrayList<Node> bestList = new ArrayList();
		
		//for every child node of n "child"
		Iterator<Node> childIter = childList.iterator();
		while(childIter.hasNext()) {
			//childValue = findMin(child)
			//System.out.println("Max working with " + n);
			//System.out.println("findMax child loop: " +childIter.next().getRoot().toString() + " is being given to findMin.");
			childNode = findMin(childIter.next());
			//depth--;
			//best = max(best, childValue)
			best = Math.max(best, childNode.getUtility());
			
//			if(best > oldBest && childNode.getRoot() != null){
//				oldBest = best;
//				//System.out.println("Clearing list");
//				bestList.clear();
//				//System.out.println("Adding " + childNode.getRoot().toString());
//				bestList.add(childNode);
//				//System.out.println("After clear: " + bestList.toString());
//			} else if(best == childNode.getUtility()) {
//				bestList.add(childNode);
//				//System.out.println("After equal: " + bestList.toString());
//			}
			
			if(best == childNode.getUtility())
				bestChild = childNode;
					//System.out.println("\tBest Child: " + bestChild.getRoot().toString());
		}
		//System.out.println("Best: " + best);
			
		//return best
		//return bestChild;
//		System.out.println("The max best list size is: " + bestList.size());
//		if(bestList.size() == 0) {
//			System.out.println(bestChild.getRoot().toString());
//			return bestChild;
//		}
//		return bestList.get(random.nextInt(bestList.size()));
		return bestChild;
	}
	
	
	
	protected Node findRandMax(Node n) {
		//System.out.println("\n***Finding Max of " + n.getRoot().toString());
		maxCount++;
	//if n is a leaf node, return the utility of n
		//System.out.println("Depth is " + depth);
	if(n.isLeaf || n.getRoot().searchLimitReached()) {
		//System.out.println("***RETURNING MAX***");
		return n;
	}
	
	//best = -1000
	int best = -1000;
	int oldBest = -1000;
	Node bestChild = new Node();
	Node childNode = new Node();
	
	//populating list of possible next states
	LinkedList<Node> childList = new LinkedList();
	Iterator<State> iter = n.getRoot().next().iterator();
	while(!n.getRoot().searchLimitReached() && iter.hasNext()) {
			//System.out.println("Creating children of player " + n.getRoot().player);
		//if(n.getDepth() == 0)
			//System.out.println("MAX: Creating new node of depth " + n.getDepth() + " at move " + iter.next().toString());
		Node temp = new Node(iter.next(), n.getUtility(), n.getDepth() + 1);
		
		if(temp.isTerminal())
			return temp;
		
		childList.add(temp);
//			System.out.println("\tChild list move: " + temp.getRoot().toString());
//			System.out.println("\tChild list utility: " + temp.getUtility());
		
		//trying refactor
//		childNode = findMin(temp, ++depth);
//		depth--;
//		//best = max(best, childValue)
//		best = Math.max(best, childNode.getUtility());
//			//System.out.println("Max best: " + best);
//		if(best == childNode.getUtility())
//			bestChild = childNode;
	}
		//System.out.println("\tChild list size: " + childList.size());
		//System.out.println("\tChild list content: " + childList.toString());
	
	ArrayList<Node> bestList = new ArrayList();
	
	//for every child node of n "child"
	Iterator<Node> childIter = childList.iterator();
	while(childIter.hasNext()) {
		//childValue = findMin(child)
		//System.out.println("Max working with " + n);
		//System.out.println("findMax child loop: " +childIter.next().getRoot().toString() + " is being given to findMin.");
		childNode = findMin(childIter.next());
		//depth--;
		//best = max(best, childValue)
		best = Math.max(best, childNode.getUtility());
		
		if(best > oldBest && childNode.getRoot() != null){
			oldBest = best;
			//System.out.println("Clearing list");
			bestList.clear();
			//System.out.println("Adding " + childNode.getRoot().toString());
			bestList.add(childNode);
			//System.out.println("After clear: " + bestList.toString());
		} else if(best == childNode.getUtility()) {
			bestList.add(childNode);
			//System.out.println("After equal: " + bestList.toString());
		}
		
//		if(best == childNode.getUtility())
//			bestChild = childNode;
				//System.out.println("\tBest Child: " + bestChild.getRoot().toString());
	}
	
		
	//return best
	//return bestChild;
//	System.out.println("The max best list size is: " + bestList.size());
//	if(bestList.size() == 0) {
//		System.out.println(bestChild.getRoot().toString());
//		return bestChild;
//	}
	return bestList.get(random.nextInt(bestList.size()));
}
	
	
	
	protected Node findMin(Node n) {
			//System.out.println("\n***Finding Min of " + n.getRoot().toString());
				minCount++;		
			//if n is a leaf node, return the utility of n
			//System.out.println("Depth is " + depth);
			
		if(n.isLeaf || n.getRoot().searchLimitReached()) {
			//System.out.println("***RETURNING MIN***");
			return n;
		}
		
		//System.out.println("Min working with " + n);
		
		//generate other player's moves
		LinkedList<Node> childList = new LinkedList<Node>();
		n.getRoot().player.other();
		Iterator<State> iter = n.getRoot().next().iterator();
		while(!n.getRoot().searchLimitReached() && iter.hasNext()) {
				//System.out.println("Creating children of player " + n.getRoot().player);
			//System.out.println("MIN: Creating new node of depth " + n.getDepth() + " at move " + iter.next().toString());
			Node temp = new Node(iter.next(), n.getUtility(), n.getDepth() + 1);
			
//				System.out.println("\tChild list move: " + temp.getRoot().toString());
//				System.out.println("\tChild list utility: " + temp.getUtility());
			childList.add(temp);
		}
		
		
		//best = 1000
		int best = 1000;
		int oldBest = 1000;
		Node bestChild = new Node();
		ArrayList<Node> bestList = new ArrayList();
		//for every child node of n "child"
		Iterator<Node> childIter = childList.iterator();
		while(childIter.hasNext()) {
			//childValue = findMin(child)
			Node childNode = findMax(childIter.next());
			//depth--;
			//best = min(best, childValue)
			best = Math.min(best, childNode.getUtility());
//			if(childNode.getRoot().previous != null)	
//				childNode.setRoot(childNode.getRoot().previous);
				//System.out.println("Min best: " + best);
//			if(best < oldBest){
//				oldBest = best;
//				//System.out.println("Clearing list");
//				bestList.clear();
//				//System.out.println("Adding " + childNode.getRoot().toString());
//				bestList.add(childNode);
//			} else if(best == childNode.getUtility()) {
//				bestList.add(childNode);
//			}
			if(best == childNode.getUtility())
				bestChild = childNode;
					//System.out.println("\tMin Best Child: " + bestChild.getRoot().toString());
		}
		
		//return best
		//return bestChild;
		//System.out.println("The best list size is: " + bestList.size());
		
		//return bestList.get(random.nextInt(bestList.size()));
		return bestChild;
	}
	
	protected class Node {
//		static private int nodeCount = 0;
		private State root;
		private int utility;
		private int depth;
		private boolean isLeaf = false;
		private boolean isTerminal = false;
		
		public Node() {
			this.root = null;
			this.utility = 0;
		}
		
		public Node(State root, int utility, int depth) {
			
			this.root = root;
			this.utility = utility;
			this.depth = depth;
			//nodeCount++;
//			System.out.println("Node " + root.toString() + " has been created");
//			System.out.println("Node " + root.toString() + " utility is " + utility);
			setUtility(utility);
//			if(nodeCount > 100) {
//				this.setUtility(utility);
//			} else {
//				Iterator<State> iter = root.next().iterator();
//				
//				while(!root.searchLimitReached() && iter.hasNext()) {
//					Node child = new Node(iter.next(), 0);
//					child.setUtility(this.utility);
//					childList.add(child);
//				}
//					
//			}
	
		}
		
		private void setUtility(int parentUtility) {
			//System.out.println("Calculating utility...");
			//System.out.println(this.root.previous);
			
			int currentCount = root.board.countPieces();
			int previousCount = 0;
			
			if(root.previous != null) {
				previousCount = root.previous.board.countPieces();
				//System.out.println("the current count: " + currentCount + "\nthe previous count: " + previousCount);
			}
			
			if(this.depth >= maxDepth) {
				//System.out.println("This node is a leaf.");
				this.isLeaf = true;
			}
			
//			if(this.root.over == true) {
//				isLeaf = true;
//				isTerminal = true;
//				System.out.println("\tGAME OVER");
//				
//				this.utility = parentUtility + 10;
//			}
			
//			if(this.root.over == true && currentPlayer == this.root.player) {
//				//System.out.println("Over is true");
//				this.isLeaf = true;
//				this.utility = parentUtility - 5;
//			}
			
			if(this.root.over == true) {
				//System.out.println("Over is true");
				this.isLeaf = true;
				//this.isTerminal = true;
				if(currentPlayer != this.root.player)
					this.utility = parentUtility + 30;
				else
					this.utility = parentUtility - 30;
			} 
			
			//checks if the state is a terminal state
			if(this.depth == 1 && this.root.check == true && this.root.over == true && currentPlayer != this.root.player) {
				this.isTerminal = true;
				System.out.println(this.root.toString());
				this.utility = parentUtility + 1000;
			}
			
			if(!isTerminal) {
				
				//when I'm being checked
				if(this.root.check == true && currentPlayer == this.root.player) {
						//System.out.println(this.root.player + " is in check");
//					if(checkCounter >= 2) {
//						checkCounter = 0;
//						this.utility = parentUtility + 5;
//					} else {
//					if(this.depth == 2 && this.root.previous.previous.check == true) {
//						this.utility = parentUtility - 2;	
//						checkCounter++;
//					}
//					else
					if(inDanger())
						this.utility = parentUtility + 1;
					else 
						this.utility = parentUtility - 1;
					//}
//					System.out.println(this.utility);
				}
				
				//when my opponent is being checked
				if(this.root.check == true && currentPlayer != this.root.player) {
					//System.out.println(this.root.toString());
//					if(this.depth == 4 && this.root.previous.previous.check == true) {
//						this.utility = parentUtility + 2;	
//						checkCounter++;
//					}
					
					if(inDanger())
						this.utility = parentUtility - 1;
					else
						this.utility = parentUtility + 1;
//					if(this.depth == 4)
//						this.utility++;
//					if(checkCounter >= 20) {
//						checkCounter = 0;
//						this.utility = parentUtility - 5;
//					} else {	
//						this.utility = parentUtility + 2;
//						checkCounter++;
//					}
				}
				
				//when a piece is lost
				if(this.root.previous != null && currentCount < previousCount && this.root.board.countPieces(currentPlayer.other()) > 1) {
						//System.out.println("\n\t\t" + this.root.player + " will take a piece.");
							//System.out.println("the current count: " + currentCount + "\nthe previous count: " + previousCount);
					int weight = 0;
					int prevWeight = 0;
					
					Iterator pieceIter = this.root.board.iterator();
					while(pieceIter.hasNext()) {
						String currentPiece = pieceIter.next().toString();
//						System.out.println(currentPiece);
						if(currentPiece == "P") {
							weight += 2;
						} else if(currentPiece == "K") {
							weight += 5;
						} else if(currentPiece == "B") {
							weight += 5;
						} else if(currentPiece == "Q") {
							weight += 10;
						} else if(currentPiece == "R") {
							weight += 3;
						}
							
					}
					
					Iterator prevPieceIter = this.root.previous.board.iterator();
					while(prevPieceIter.hasNext()) {
						//System.out.println(prevPieceIter.next().toString());
						//Object currentPiece = prevPieceIter.next().;
						String currentPiece = prevPieceIter.next().toString();
						
						if(currentPiece == "P") {
							prevWeight += 2;
						} else if(currentPiece == "K") {
							prevWeight += 5;
						} else if(currentPiece == "B") {
							prevWeight += 5;
						} else if(currentPiece == "Q") {
							prevWeight += 10;
						} else if(currentPiece == "R") {
							prevWeight += 3;
						}
					}
					
					//System.out.println("Prev: "+ prevWeight + "\n" + "Current weight: " + weight);
					
					int diffWeight = prevWeight - weight;
					
					if(currentPlayer == this.root.player && diffWeight == 10) {
						this.utility = parentUtility - 50;
					} else if(currentPlayer == this.root.player) {
						this.utility = parentUtility - diffWeight; //I lose piece
					}
					
					if(currentPlayer != this.root.player && inDanger()) {
						this.utility = parentUtility + (diffWeight - 2);
						//System.out.println("In danger! " + this.root.toString());
					}  else if(currentPlayer != this.root.player) {
						//System.out.println("Safe! " + this.root.toString());
						this.utility = parentUtility + diffWeight; //Opponent loses piece
						//System.out.println(this.utility);
					} 
						
					
						checkCounter = 0;
						
				} 
				
//				if(this.root.previous != null && currentCount > previousCount && this.root.board.countPieces(currentPlayer.other()) > 1) {
//					this.utility = parentUtility + 5;
//				}
				
				//when my opponent loses a piece
//				if(this.root.previous != null && currentCount < previousCount && currentPlayer != this.root.player) {
//					//System.out.println("\n\t\t" + this.root.player + " will take a piece.");
//					//System.out.println("the current count: " + currentCount + "\nthe previous count: " + previousCount);
//					int weight = 0;
//					int prevWeight = 0;
//					
//					Iterator pieceIter = this.root.board.iterator();
//					while(pieceIter.hasNext()) {
//						String currentPiece = pieceIter.next().toString();
//						if(currentPiece == "P") {
//							weight += 1;
//						} else if(currentPiece == "K") {
//							weight += 5;
//						} else if(currentPiece == "B") {
//							weight += 5;
//						} else if(currentPiece == "Q") {
//							weight += 10;
//						} else if(currentPiece == "R") {
//							weight += 3;
//						}
//							
//					}
//					
//					Iterator prevPieceIter = this.root.previous.board.iterator();
//					while(prevPieceIter.hasNext()) {
//						//System.out.println(prevPieceIter.next().toString());
//						//Object currentPiece = prevPieceIter.next().;
//						String currentPiece = prevPieceIter.next().toString();
//						
//						if(currentPiece == "P") {
//							prevWeight += 1;
//						} else if(currentPiece == "K") {
//							prevWeight += 5;
//						} else if(currentPiece == "B") {
//							prevWeight += 5;
//						} else if(currentPiece == "Q") {
//							prevWeight += 10;
//						} else if(currentPiece == "R") {
//							prevWeight += 3;
//						}
//					}
//					
//					//System.out.println("Prev: "+ prevWeight + "\n" + "Current weight: " + weight);
//					
//					int diffWeight = prevWeight - weight;
//					
//					if(checkForLose(this.root))
//						this.utility = parentUtility + 1;
//					else
//						this.utility = parentUtility + diffWeight;
//					
//					//this.utility = parentUtility + 4;
//					checkCounter = 0;
//				}
				
				//when my opponent has only a single piece
//				if(this.root.board.countPieces(currentPlayer.other()) == 1 && this.root.check == false && currentPlayer != this.root.player ) {
//					this.utility = parentUtility + 8;
//				}
				
				//System.out.println(this.utility);
				
			}
			
			
				
//			if(this.root.board.countPieces(this.root.player.other()) == 1 && this.root.check == false) {
//				this.utility = parentUtility + 5;
//			}
			
			
				
				
//			if(this.root.previous != null && this.root.previous.check == true) {
//				System.out.println(this.root.previous.player + " was in check");
//				this.utility = parentUtility + 1;
//			}
			
//			if(this.root.previous != null && this.root.board.countPieces(this.root.player) < this.root.previous.board.countPieces(this.root.player)) {
//				//System.out.println("\n" + this.root.player + " will lose a piece.");
//				this.utility = parentUtility + 1;
//				checkCounter = 0;
//			}
			
			
			
			
				//System.out.println("Node " + root.toString() + " has a utility of " + this.utility);
		}
		
		private Boolean inDanger() {
			State currentState;
			Iterator<State> iter = root.next().iterator();
			while(!this.root.searchLimitReached() && iter.hasNext()) {
				currentState = iter.next();
				if(currentState.board.countPieces() < currentState.previous.board.countPieces()) {
					//System.out.println(this.root.player + " will lose piece at " + currentState.toString());
					return true;
				}
			}
			
			return false;
		}
		
		private int getUtility() {
			return this.utility;
		}
		
		private State getRoot() {
			return this.root;
		}
		
		private void setRoot(State root) {
			this.root = root;
		}
		
		private int getDepth() {
			return this.depth;
		}
		
//		private int getNodeCount() {
//			return this.nodeCount;
//		}
		
		private boolean isLeaf() {
			return this.isLeaf;
		}
		
		private boolean isTerminal() {
			return this.isTerminal;
		}
		
//		private void addChild(Node child) {
//			childList.add(child);
//		}
//		
//		private Node getChild(int index) {
//			return childList.get(index);
//		}
//		
//		private Iterator childIter() {
//			return childList.iterator();
//		}
//		
//		private boolean isEmpty() {
//			if(childList.isEmpty())
//				return true;
//			return false;
//		}
		
	}
	

}
