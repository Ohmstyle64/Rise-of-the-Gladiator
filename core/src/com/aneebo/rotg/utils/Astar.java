package com.aneebo.rotg.utils;

import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.BinaryHeap.Node;
import com.badlogic.gdx.utils.IntArray;

/** @author Nathan Sweet 
 *  @author Kevin
 *  */
public class Astar {
	private static final int LATERAL = 10;
	private boolean[] validityMap;
	private final int width, height;
	private final BinaryHeap<PathNode> open;
	private final PathNode[] nodes;
	int runID;
	private final IntArray path = new IntArray();
	private int targetX, targetY;

	public Astar (int width, int height) {
		this.width = width;
		this.height = height;
		validityMap = new boolean[width * height];
		open = new BinaryHeap<PathNode>(width * 4, false);
		nodes = new PathNode[width * height];
	}
	
	public Astar (int width, int height, boolean[] validityMap) {
		this.width = width;
		this.height = height;
		if(validityMap.length != width * height) throw new IllegalArgumentException("map dimensions need to be width times height");
		this.validityMap = validityMap;
		open = new BinaryHeap<PathNode>(width * 4, false);
		nodes = new PathNode[width * height];
	}

	/** Returns x,y pairs that are the path from the target to the start. */
	public IntArray getPath (int startX, int startY, int targetX, int targetY) {
		this.targetX = targetX;
		this.targetY = targetY;

		path.clear();
		open.clear();

		runID++;
		if (runID < 0) runID = 1;

		int index = startY * width + startX;
		PathNode root = nodes[index];
		if (root == null) {
			root = new PathNode(0);
			root.x = startX;
			root.y = startY;
			nodes[index] = root;
		}
		root.parent = null;
		root.pathCost = 0;
		open.add(root, 0);

		int lastColumn = width - 1, lastRow = height - 1;
		while (open.size > 0) {
			PathNode node = open.pop();
			if (node.x == targetX && node.y == targetY) {
				while (node != root) {
					path.add(node.x);
					path.add(node.y);
					node = node.parent;
				}
				break;
			}
			node.closedID = runID;
			int x = node.x;
			int y = node.y;
			if (x < lastColumn) {
				addNode(node, x + 1, y, LATERAL);
			}
			if (x > 0) {
				addNode(node, x - 1, y, LATERAL);
			}
			if (y < lastRow) addNode(node, x, y + 1, LATERAL);
			if (y > 0) addNode(node, x, y - 1, LATERAL);
		}
		return path;
	}

	private void addNode (PathNode parent, int x, int y, int cost) {
		if (!isValid(x, y)) return;

		int pathCost = parent.pathCost + cost;
		float score = pathCost + Math.abs(x - targetX) + Math.abs(y - targetY);

		int index = y * width + x;
		PathNode node = nodes[index];
		if (node != null && node.runID == runID) { // Node already encountered for this run.
			if (node.closedID != runID && pathCost < node.pathCost) { // Node isn't closed and new cost is lower.
				// Update the existing node.
				open.setValue(node, score);
				node.parent = parent;
				node.pathCost = pathCost;
			}
		} else {
			// Use node from the cache or create a new one.
			if (node == null) {
				node = new PathNode(0);
				node.x = x;
				node.y = y;
				nodes[index] = node;
			}
			open.add(node, score);
			node.runID = runID;
			node.parent = parent;
			node.pathCost = pathCost;
		}
	}

	protected boolean isValid (int x, int y) {
		return !validityMap[x + y * width];
	}
	
	public boolean isValidPosition(int i) {
		return validityMap[i];
	}
	
	public void setValidityMap(boolean[] validityMap) {
		this.validityMap = validityMap;
	}
	
	public void setPositionValidity(int x, int y, boolean valid) {
		validityMap[x + y * width] = valid;
	}
	
	public boolean[] getValidityMap() {
		return validityMap;
	}

	public int getWidth () {
		return width;
	}

	public int getHeight () {
		return height;
	}

	static private class PathNode extends Node {
		int runID, closedID, x, y, pathCost;
		PathNode parent;

		public PathNode (float value) {
			super(value);
		}
	}
}
