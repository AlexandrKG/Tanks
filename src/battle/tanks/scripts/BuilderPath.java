package battle.tanks.scripts;


import java.util.LinkedList;
import java.util.List;

import battle.tanks.AbstractTank;
import battle.BattleField;
import battle.Direction;


public class BuilderPath {
	private Node root;
	private int size;
	private boolean[][] greenField;
	private BattleField bf;
	private AbstractTank tank;
	
	private int startV;
	private int startH;
	private int finishV;
	private int finishH; 
	
	private List<Node> queueToWork;
	private List<Node> shortPath;
	private boolean pathExist = true;
	
	public BuilderPath() {
		size = 0;
		queueToWork = new LinkedList<>();
		shortPath = new LinkedList<>();
	}
	
	public void installBuilderPath(AbstractTank tank) {
		this.tank = tank;
		this.bf = tank.getBf();
		greenField = new boolean[this.bf.RECT_NUMBER_Y][this.bf.RECT_NUMBER_X];
		initGreenField();
	}
	
	private void initGreenField() {
		for (int v = 0; v < bf.RECT_NUMBER_Y; v++) {
			for (int h = 0; h < bf.RECT_NUMBER_X; h++) {
				greenField[v][h] = false;
				if(bf.getBattleField()[v][h].equals(" ")) {
					greenField[v][h] = true;
					continue;
				}
				if(bf.getBattleField()[v][h].equals("W") && tank.isUnderwater() ) {
					greenField[v][h] = true;
					continue;
				}				
				for(String str : tank.getDestroyableObst()){
					if(bf.getBattleField()[v][h].equals(str)) {
						greenField[v][h] = true;
					}
				}
			}
		}
	}

	public void resetBuilderPath() {
		size = 0;
		queueToWork = new LinkedList<>();
		shortPath = new LinkedList<>();
	}

	private class Node {
		
		int v;
		int h;
		int number;
		Direction direction;
		Node parent;
		
		public Node(int v,int h, Node parent) {
			this.v = v;
			this.h = h;
			this.parent = parent;
			number = v*bf.RECT_NUMBER_X +h;
			
		}
	}

	private boolean buildTree() {
		root = new Node(startV, startH, null);
		size++;
		Node ancestorNode = root;

		int heirV;
		int heirH;
		Node heirNode;
		Node endNode = null;
		boolean flagStop = false;

		while (!(ancestorNode.v == finishV && ancestorNode.h == finishH)) {

			// top
			heirV = ancestorNode.v - 1;
			heirH = ancestorNode.h;
			if (isPossible(heirV, heirH, ancestorNode)) {
				heirNode = new Node(heirV, heirH, ancestorNode);
				flagStop = (heirNode.v == finishV && heirNode.h == finishH);
				heirNode.direction = Direction.TOP;
				if (flagStop) {
					endNode = heirNode;
					break;
				}
				queueToWork.add(heirNode);
				size++;
			}
			// bottom
			heirV = ancestorNode.v + 1;
			heirH = ancestorNode.h;
			if (isPossible(heirV, heirH, ancestorNode)) {
				heirNode = new Node(heirV, heirH, ancestorNode);
				flagStop = (heirNode.v == finishV && heirNode.h == finishH);
				heirNode.direction = Direction.BOTTOM;
				if (flagStop) {
					endNode = heirNode;
					break;
				}
				queueToWork.add(heirNode);
				size++;
			}
			// right
			heirV = ancestorNode.v;
			heirH = ancestorNode.h + 1;
			if (isPossible(heirV, heirH, ancestorNode)) {
				heirNode = new Node(heirV, heirH, ancestorNode);
				flagStop = (heirNode.v == finishV && heirNode.h == finishH);
				heirNode.direction = Direction.RIGHT;
				if (flagStop) {
					endNode = heirNode;
					break;
				}
				queueToWork.add(heirNode);
				size++;
			}
			// left
			heirV = ancestorNode.v;
			heirH = ancestorNode.h - 1;
			if (isPossible(heirV, heirH, ancestorNode)) {
				heirNode = new Node(heirV, heirH, ancestorNode);
				flagStop = (heirNode.v == finishV && heirNode.h == finishH);
				heirNode.direction = Direction.LEFT;
				if (flagStop) {
					endNode = heirNode;
					break;
				}
				queueToWork.add(heirNode);
				size++;
			}

			
			if (queueToWork.size() == 0) {
				pathExist = false;
				break;
			} else {
				ancestorNode = queueToWork.remove(0);
			}
			System.out.println(size);
		}

		if (pathExist) {
			Node temp = endNode;
			Direction dir1 = endNode.direction;
			Direction dir2;
			while (temp != null) {
				buildPath(temp);
				if (temp.parent != null) {
					dir2 = temp.parent.direction;
					temp.parent.direction = dir1;
					dir1 = dir2;
				}
				temp = temp.parent;
			}
			endNode.direction = null;
			System.out.println("Path has built!");
			return true;
		} else {
			return false;
		}
		
	}
	
	private boolean isPossible(int v,int h, Node ancestor) {
		
		//boundary
		if(v < 0 || v >= bf.RECT_NUMBER_Y || h < 0 || h >= bf.RECT_NUMBER_X) {
			return false;
		}
		// rock
		if(!greenField[v][h]) {
			return false;
		}
		// not loop
		Node temp = ancestor.parent;
		while(temp != null) {
			if(v == temp.v && h == temp.h) {
				return false;
			}
			temp = temp.parent;
		}
		
		return true;
	}
	
	private void buildPath(Node n) {
		shortPath.add(0, n);
	}

	public List<Node> getShortPath() {
		return shortPath;
	}
	
	public int getShortPathSize() {
		return shortPath.size();
	}
	public Direction getShortPathDirection(int index) {
		return shortPath.get(index).direction;
	}
	
	public Direction poopShortPathDirection() {
		Direction dir = shortPath.get(0).direction;
		shortPath.remove(0);
		return dir;
	}
	
	public boolean setPath(int beginV, int beginH,int endV, int endH) {
		startV = beginV;
		startH = beginH;
		finishV = endV;
		finishH = endH; 
		if(buildTree()) {
			printPath();
			return true;
		} else {
			System.err.println("Can't build Path!");
			return false;
		}
	}
	
	public void printPath() {
		if(shortPath.size() !=0 ) {
			for(Node n : shortPath) {
				System.out.println("v="+n.v+"; h="+n.h+"; Dir="+n.direction);
			}
		} else {
			System.out.println("Can't build Path!");
		}
	}
}
