package friends;

import java.util.ArrayList;
import java.util.HashMap;

import structures.Queue;
import structures.Stack;

public class Friends {
	/**
	 * Finds the shortest chain of people from p1 to p2. Chain is returned as a
	 * sequence of names starting with p1, and ending with p2. Each pair (n1,n2) of
	 * consecutive names in the returned chain is an edge in the graph.
	 * 
	 * @param g  Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there
	 *         is no path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		if (p1 == null || p2 == null || g == null) {
			return null;
		}

		ArrayList<String> shortFriendsList = new ArrayList<String>();

		Person person1 = g.members[g.map.get(p1)];
		Person person2 = g.members[g.map.get(p2)];

		Queue<Person> myQ = new Queue<>();
		HashMap<Person, Person> prev = new HashMap<Person, Person>();

		myQ.enqueue(person1);
		prev.put(person1, null);

		while (!myQ.isEmpty()) {
			Person p = myQ.dequeue();
			if (p.equals(person2)) {
				break;
			}

			Friend f = p.first;
			while (f != null) {

				Person pe = g.members[f.fnum];
				if (!prev.containsKey(pe)) {
					myQ.enqueue(pe);
					prev.put(pe, p);
				}
				f = f.next;
			}

		}

		if (prev.get(person2) == null) {
			return null;
		}
		Person p = person2;
		while (p != null) {
			shortFriendsList.add(0, p.name);
			p = prev.get(p);
		}
		return shortFriendsList;
	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g      Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there
	 *         is no student in the given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		if (school == null || g == null) {
			return null;
		}

		// Return ArrayList of groups of cliques.
		ArrayList<ArrayList<String>> cliquesList = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		for (boolean isVisited : visited) {

			int i = 0;
			while (visited[i] != isVisited) {
				i++;
			}

			// If not visited.
			if (!isVisited) {
				Person p = g.members[i];

				ArrayList<String> tmp = cliquesBFS(g, p, school, visited);
				if (school.equals(p.school)) {
					cliquesList.add(tmp);
				}
			}
		}
		return cliquesList;

	}

	private static ArrayList<String> cliquesBFS(Graph g, Person mainP, String school, boolean[] visited) {

		if (!school.equals(mainP.school)) {
			visited[g.map.get(mainP.name)] = true;
			return new ArrayList<String>();
		}

		ArrayList<String> cur = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();

		q.enqueue(mainP);

		while (!q.isEmpty()) {

			Person person = q.dequeue();
			visited[g.map.get(person.name)] = true;

			if (!cur.contains(person.name)) {
				cur.add(person.name);
			}

			Friend f = person.first;
			while (f != null) {

				Person p = g.members[f.fnum];
				if (!visited[g.map.get(p.name)] && school.equals(p.school)) {
					q.enqueue(p);
				}
				f = f.next;
			}
		}
		return cur;
	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no
	 *         connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		/** COMPLETE THIS METHOD **/

		String[] names = new String[g.members.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = g.members[i].name;
		}

		ArrayList<String> allConnectors = new ArrayList<String>();
		int[] dfsnum = new int[g.members.length], prev = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		int n = 0;
		for (int i = 0; i < visited.length; i++) {
			boolean b = visited[i];

			if (b == false) {
				Person currentmember = g.members[i];

				DFSConnectors(g, currentmember, allConnectors, visited, n, dfsnum, prev);

			}
		}

		for (int i = 0; i < allConnectors.size(); i++) {
			Person currentperson = g.members[g.map.get(allConnectors.get(i))];

			ArrayList<Person> friends = new ArrayList<Person>();
			Friend anotherone = currentperson.first;
			while (anotherone != null) {
				Person np = g.members[anotherone.fnum];
				friends.add(np);
				anotherone = anotherone.next;
			}
			// friendsList is number of edges
			if (friends.size() == 1) {
				allConnectors.remove(i);
			}

		}
		return allConnectors;
	}

	private static void DFSConnectors(Graph g, Person currentPer, ArrayList<String> connectors, boolean[] visited,
			int n, int[] dfsN, int[] prev) {
		int curIdx = g.map.get(currentPer.name);
		prev[curIdx] = n;
		dfsN[curIdx] = n;
		visited[curIdx] = true;
		n++;

		ArrayList<Person> friends = new ArrayList<Person>();
		Friend anotherone = currentPer.first;
		while (anotherone != null) {
			Person np = g.members[anotherone.fnum];
			friends.add(np);
			anotherone = anotherone.next;
		}

		for (int i = 0; i < friends.size(); i++) {
			int friendIdx = g.map.get(friends.get(i).name);
			if (visited[friendIdx]) {
				prev[curIdx] = Math.min(prev[curIdx], dfsN[friendIdx]);
			} else {
				DFSConnectors(g, friends.get(i), connectors, visited, n, dfsN, prev);

				if (dfsN[curIdx] > prev[friendIdx]) {
					prev[curIdx] = Math.min(prev[curIdx], prev[friendIdx]);
				} else {

					String temp = currentPer.name;

					if (dfsN[curIdx] == 0) {

						if (i == friends.size() - 1) {

							if (!connectors.contains(temp))
								connectors.add(temp);
						}

					}

					else {

						if (!connectors.contains(temp))
							connectors.add(temp);
					}
				}
			}
		}

	}

}