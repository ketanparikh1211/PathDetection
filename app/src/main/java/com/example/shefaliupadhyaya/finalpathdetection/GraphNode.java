package com.example.shefaliupadhyaya.finalpathdetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

class Edge{
	int source,destination,direction,weight;
	Edge(int source,int destination,int direction,int weight){
		this.source=source;
		this.destination=destination;
		this.direction=direction;
		this.weight=weight;
	}
	int getDir(int s, int d){
		return d;
	
	}
}


class Node {
    ArrayList<Edge> adjacencyList=new ArrayList<>();
}


class EdgeComparator implements Comparator<Edge> {
	@Override
    public int compare(Edge e1, Edge e2) {
		return e1.weight-e2.weight;
    }
}


class Graph{
	Node list[];
	int vertices;

	Graph(int v){
		this.vertices=v;
		list=new Node[v];
		for(int i=0;i<v;i++){
			list[i]=new Node();
		}
	}


	public int getDirection(int s,int d){
		ArrayList<Edge> a=list[s].adjacencyList;
		int index = 0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).destination==d)
				index=i;
		}
		Edge e=a.get(index);
		return e.direction;
	}

	public int getSteps(int s , int d){
        ArrayList<Edge> a=list[s].adjacencyList;
        int index = 0;
        for(int i=0;i<a.size();i++){
            if(a.get(i).destination==d)
                index=i;
        }
        Edge e=a.get(index);
        return e.weight;
    }
	public void addEdge(int s , int d , int w, int dir){
		Edge e1= new Edge(s,d,dir,w);
		Edge e2= new Edge(d,s,(dir+4)%9,w);
		list[s].adjacencyList.add(e1);
		list[d].adjacencyList.add(e2);
	}
	public void displayEdge(){
		for(int i=0;i<vertices;i++){
			for(int j=0;j<list[i].adjacencyList.size();j++){
				System.out.println("Source: "+i+" Destination: "+list[i].adjacencyList.get(j).destination);
			}
		}
	}
	public int shortestPath(int source, int destination,int visited[],int weight[],int path[]){
		weight[source]=0;
		path[source]=-1;
		Queue<Integer> queue =new LinkedList<>();
		queue.add(source);
		while(!queue.isEmpty()){
			int currentVertex=queue.remove();
			visited[currentVertex]=1;
			Collections.sort(list[currentVertex].adjacencyList, new EdgeComparator());
			
			for(int i=0;i<list[currentVertex].adjacencyList.size();i++){
				int adjVertex=list[currentVertex].adjacencyList.get(i).destination;
				
				if(visited[adjVertex]==0){
					int weigh=list[currentVertex].adjacencyList.get(i).weight;
					
					if(weigh+weight[currentVertex]<weight[adjVertex]){
						weight[adjVertex]=weigh+weight[currentVertex];
						path[adjVertex]=currentVertex;
					}
					
					queue.add(adjVertex);
				}
				
			}
		}
		
		return weight[destination];
	}
	
}

class StepDir{
    int step;
    int dir;
}

public class GraphNode {
    ArrayList<Integer> a;
    ArrayList<StepDir> sd;
	public GraphNode(int source, int destination){
		Graph graph=new Graph(8);

		graph.addEdge(0, 1, 15, 3);
		graph.addEdge(1, 2, 10, 3);
		graph.addEdge(1, 3, 10, 5);
		graph.addEdge(2, 6, 4, 4);
		graph.addEdge(1, 5, 10, 2);
		graph.addEdge(6, 7, 2, 3);
		graph.addEdge(3, 4, 3, 4);
		//graph.displayEdge();
		int visited[]=new int[8];
		int weightage[]=new int[8];
		for(int i=0;i<8;i++){
			weightage[i]=Integer.MAX_VALUE;
		}
		int []path=new int[8];
        sd=new ArrayList<>();
		System.out.println("Steps covered: "+graph.shortestPath(source, destination,visited,weightage,path));
		int i=destination;
		a=new ArrayList<>();
		while(i!=-1){
			a.add(0,i);
			i=path[i];
		}
		
		for(i=0;i<a.size()-1;i++){
			int s=a.get(i);
			int d=a.get(i+1);
			int dir=graph.getDirection(s, d);
            int step=graph.getSteps(s,d);
			StepDir qw=new StepDir();
            qw.dir=dir;
            qw.step=step;
            sd.add(qw);
		}
		
	}
	public ArrayList<Integer> path(){
        return a;
    }
    public ArrayList<StepDir> getInfo(){
            return sd;
    }
}

