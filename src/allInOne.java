import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class allInOne {

	public static void main(String[] args) {
		int[][] m=new int[10][10];  
		addRandomWalls(m,15);
		System.out.println("matrix");
		printMatrix(m); 
		printLine();  
		System.out.println("graph"); 
		Graph g=new Graph();
		printGraphBasedOnMatrix(m,g);
		g.createGraph(m);
		g.addNeighboors(m);
		Point org=new Point(9,0); 
		Point des=new Point(0,9); 
		ArrayList<Point> path=g.BFS(org, des);  
		printLine();
		if(path == null ) { 
			System.out.println("no solution");
		} 
		else {
			System.out.println("path:");
			printPath(path); 
		}
	} 
	static void printPath(ArrayList<Point> path) { 
		for(int i=0;i < path.size();i++) {
			Point current=path.get(i);
			if(i%20 == 0) { 
				System.out.println();
				System.out.print("[" + current.x + "," +current.y +"], ");
			} 
			else { 
				System.out.print("[" + current.x + "," +current.y +"], ");
			}
		}
	}
	
	static void printMatrix(int[][] m){ 
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[0].length; j++) {
				System.out.print("["+m[i][j]+"]");
			}
			System.out.println("");
		}
	} 
	static void addRandomWalls(int [][] m,int amount) { 
		Point walls[] =new Point[amount];
		boolean repetitive=false; 
		ArrayList<Point> again=new ArrayList<Point>();
		for(int i=0;i < walls.length;i++) { 
			repetitive=true; 
			int randomx; 
			int randomy;
			while(repetitive) {
				randomx=(int)(Math.random() *m.length);
				randomy=(int)(Math.random() *m[0].length); 
				boolean repeat=false;
				for(int h=0;h < again.size();h++) { 
					if(again.get(h).x == randomx && again.get(h).y ==randomy) { 
						repeat=true;
					}
				}  
				if(repeat) { 
					continue;
				}
				repetitive=false;
				walls[i] = new Point(randomx,randomy); 
				m[randomx][randomy]=-1;
				again.add(new Point(randomx,randomy));
			} 
		}
	} 
	static void printLine() { 
		for(int i=0; i < 40;i++) { 
			System.out.print("_");
		} 
		System.out.println("");
	}
	static void printGraphBasedOnMatrix(int[][] m,Graph g) {  
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[0].length; j++) { 
				if(m[i][j] != -1) {
					System.out.print("["+m[i][j]+"]");  
				} 
				else { 
					System.out.print("[ ]"); 
				} 
			} 
			
			System.out.println("");
		} 
		
	} 
	static class Graph {  
		ArrayList<Node> nodes; 
		public Graph() { 
			this.nodes=new ArrayList<Node>(); 
		} 
		public void createGraph(int[][] m) { 
			for(int i = 0; i < m.length; i++) {
				for(int j = 0; j < m[0].length; j++) {  
					if(m[i][j] != -1) {
						this.nodes.add(new Node(m[i][j],new Point(j,i))); 
					}
				}
			}
		} 
		public void addNeighboors(int[][] m) { 
			for(int i=0;i< this.nodes.size();i++) { 
				Node current=this.nodes.get(i);  
				Point currentPos=current.pos; 
				Point currentLeft=(current.pos.x > 0) ? new Point(currentPos.x -1,currentPos.y) : null;
				Point currentRight=(current.pos.x < 29) ? new Point(currentPos.x +1,currentPos.y) : null;
				Point currentUp=(current.pos.y > 0) ? new Point(currentPos.x,currentPos.y-1) : null;
				Point currentDown=(current.pos.y < 29) ? new Point(currentPos.x,currentPos.y+1) : null; 
				Node leftN=(currentLeft != null) ? this.Search(currentLeft) : null;
				Node rightN=(currentRight != null) ? this.Search(currentRight) : null;
				Node upN=(currentUp != null) ? this.Search(currentUp) : null;
				Node downN=(currentDown != null) ? this.Search(currentDown) : null; 
				if(leftN != null) { 
					current.add(leftN);
				}
				if(rightN != null) { 
					current.add(rightN);
				}
				if(upN != null) { 
					current.add(upN);
				}
				if(downN != null) { 
					current.add(downN);
				}
			}
		} 
		public Node Search(Point pos) { 
			for(int i=0;i < this.nodes.size();i++) { 
				Node current=this.nodes.get(i); 
				Point currentPos=current.pos; 
				if(currentPos.x == pos.x && currentPos.y == pos.y) {  
					return current;
				}
			} 
			return null;
		} 
		public ArrayList<Node> getNeighboors(Point pos) { 
			for(int i=0;i < this.nodes.size();i++) { 
				Node current=this.nodes.get(i); 
				Point currentPos=current.pos; 
				if(currentPos.x == pos.x && currentPos.y == pos.y) {  
					if(!current.neighboors.isEmpty()) {
						return current.neighboors;
					} 
					else { 
						return null;
					}
				}
			} 
			return null;
		} 
		public ArrayList<Node> getNeighboors(Node n) { 
			Node aux=this.Search(n.pos); 
			if(aux != null) { 
				return aux.neighboors;
			} 
			else {
				return null;
			}
		}
		public ArrayList<Point> BFS(Point org,Point des) {
			Node current=this.Search(org);  
			if(current == null) { 
				return null;
			}
			Node destination=this.Search(des);
			if(destination == null) { 
				return null;
			}
			int currentx=org.x;
			int currenty=org.y;
			int desx=des.x;
			int desy=des.y;
			if(getNeighboors(current) == null) { 
				return null;
			}
			Queue<Node> line = new LinkedList<>(); 
			ArrayList<Point> path=new ArrayList<Point>();   
			Node parent=null; 
			Map<Node,Queue<Node>> parents=new HashMap();  
			WalkedList specificParent=new WalkedList(); 
			line.add(current);  
			specificParent.add(current, null);
			current=line.remove();
			while(!current.equals(destination)) {  
				currentx=current.pos.x;
				currenty=current.pos.y;  
				if(currentx == 0 && currenty == 0) { 
					System.out.println("whoa");
				} 
				if(parents.get(current) != null) { 
					if(parents.get(current).isEmpty()) { 
						parent=null;
					} 
					else {
						parent=parents.get(current).remove(); 
					}
				} 
				else { 
					parent=null;
				}
				if(parent != null) { 
					ArrayList<Node> neighboors; 
					if(getNeighboors(current) != null) { 
						neighboors=getNeighboors(current); 
						for(int i=0 ; i < neighboors.size();i++) { 
							Queue<Node> auxQueue; 
							if(!parent.equals(neighboors.get(i))) {
								line.add(neighboors.get(i)); 
								if(parents.get(neighboors.get(i)) == null) { 
									auxQueue=new  LinkedList<>();
									auxQueue.add(current);
									parents.put(neighboors.get(i), auxQueue);  
									specificParent.add(neighboors.get(i), current);
								} 
								else { 
									auxQueue=parents.get(neighboors.get(i)); 
									auxQueue.add(current);
									specificParent.add(neighboors.get(i), current);
								}
							}
						}
					} 
					else { 
						continue;
					}
				} 
				else { 
					ArrayList<Node> neighboors; 
					if(getNeighboors(current) != null) { 
						neighboors=getNeighboors(current); 
						for(int i=0 ; i < neighboors.size();i++) { 
							line.add(neighboors.get(i));
							Queue<Node> auxQueue; 
							if(parents.get(neighboors.get(i)) == null) { 
								auxQueue=new  LinkedList<>();
								auxQueue.add(current);
								parents.put(neighboors.get(i), auxQueue); 
								specificParent.add(neighboors.get(i), current);
							} 
							else { 
								auxQueue=parents.get(neighboors.get(i)); 
								auxQueue.add(current);
								specificParent.add(neighboors.get(i), current);
							}
						}
					} 
					else { 
						continue;
					}
				} 
				current=line.remove(); 
			} 
			System.out.println();  
			path.add(current.pos); 
			Node lastParent=specificParent.search(current).parent;
			while(lastParent != null) { 
				current=lastParent; 
				path.add(current.pos); 
				lastParent=specificParent.search(current).parent;
			} 
			Collections.reverse(path);
			return path;
		}
	}

	/*
	 * static String nodeToString(Node n) { String res="("; res +=
	 * Integer.toString(n.pos.x); res += ","; res +=Integer.toString(n.pos.y); res
	 * += ")"; return res; }
	 */
	static class Walked { 
		Node parent;
		Node n; 
		public Walked(Node n, Node p) { 
			this.n=n;
			this.parent=p;
		}
	} 
	static class WalkedList { 
		ArrayList<Walked> list; 
		public WalkedList() { 
			this.list=new ArrayList<Walked>();
		}
		public Walked search(Node n) { 
			for(int i=0 ; i <this.list.size();i++) { 
				if(this.list.get(i).n.equals(n)) { 
					return this.list.get(i);
				}
			} 
			return null;
		} 
		public void add(Node n,Node p) { 
			this.list.add(new Walked(n,p));
		}
	}
	static class Node { 
		int value;  
		Point pos;
		ArrayList<Node> neighboors; 
		public Node(int v,Point pos) { 
			this.value=v; 
			this.pos=pos;
			this.neighboors=new ArrayList<Node>();
		}  
		public void add(Node n) { 
			neighboors.add(n);
		}
	}
	static class Point { 
		int x; 
		int y; 
		public Point(int x, int y) {
			this.x=x;
			this.y=y;
		}
	}

}