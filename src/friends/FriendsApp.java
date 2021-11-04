package friends;

import java.io.*;
import java.util.*;

// Testing client for Friends
public class FriendsApp {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Expecting graph text file as input");
			return;
		}

		String filename = args[0];
		try {
			Graph g = new Graph(new Scanner(new File(filename)));

			// Update p1 and p2 to refer to people on Graph g
			// sam and sergei are from sample graph
			String p1 = "sam";
			String p2 = "sergei";
			ArrayList<String> shortestChain = Friends.shortestChain(g, p1, p2);

			// Testing Friends.shortestChain
			System.out.println("Shortest chain from " + p1 + " to " + p2);
			for (String s : shortestChain) {
				System.out.println(s);
			}
			System.out.println();
			// ADD test for Friends.cliques() here
			System.out.println("Cliques: rutgers");
			String school = "rutgers";
			ArrayList<ArrayList<String>> c = Friends.cliques(g, school);
			for (ArrayList<String> a : c) {
				System.out.println(a);
			}
			System.out.println();
			school = "penn state";
			System.out.println("Cliques: " + school);
			c = Friends.cliques(g, school);
			for (ArrayList<String> a : c) {
				System.out.println(a);
			}
			System.out.println();
			school = "cornell";
			System.out.println("Cliques: " + school);
			c = Friends.cliques(g, school);
			for (ArrayList<String> a : c) {
				System.out.println(a);
			}
			// ADD test for Friends.connectors() here
			System.out.println();
			System.out.println("Connectors: ");
			ArrayList<String> t = Friends.connectors(g);

			for (String a : t) {
				System.out.println(a);
			}
		} catch (FileNotFoundException e) {

			System.out.println(filename + " not found");
		}
	}
}
