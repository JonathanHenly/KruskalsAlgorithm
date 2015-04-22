import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * 
 * @author Jonathan A. Henly
 * @version 1.00 2015-04-17
 */
public class KruskalMinSpan {
	// Declare private constants
	private static final int MAX_ARGUMENTS = 1;

	// Declare private members
	private Edge[] edges;
	private Edge[] parseEdges;
	private HashMap<Edge, String> allEdges;
	private int minWeight;

	/**
	 * The {@code Edge} class represents a weighted edge between two nodes.
	 * 
	 * @author Jonathan A. Henly
	 * @version 1.00 2015-04-17
	 */
	private class Edge implements Comparable<Edge> {
		// Declare private members
		private String nOne; // first node
		private String nTwo; // second node
		private int weight; // edge weight

		/**
		 * Overloaded constructor for the {@code Edge} class.
		 * 
		 * @param first
		 *            - the first node.
		 * @param second
		 *            - the second node.
		 * @param weight
		 *            - the weight of this {@code Edge}.
		 */
		public Edge(String first, String second, int weight) {
			this.nOne = first;
			this.nTwo = second;
			this.weight = weight;
		}

		/**
		 * Accessor for the first node of this edge.
		 * 
		 * @return the first node of this edge.
		 */
		public String getFirstNode() {
			return this.nOne;
		}

		/**
		 * Accessor for the second node of this edge.
		 * 
		 * @return the second node of this edge.
		 */
		public String getSecondNode() {
			return this.nTwo;
		}

		/**
		 * Accessor for this edge's weight.
		 * 
		 * @return the weight of this edge.
		 */
		public int getWeight() {
			return this.weight;
		}

		/**
		 * Hash code algorithm taken from {@code java.util.List.hashCode()}
		 * method.
		 * <p>
		 * The algorithm simply adds all the hash codes of the primary key
		 * fields, multiplyihg each intermediate result by 31 (so that it's not
		 * a simple sum of hash codes).
		 * 
		 * @see {@link java.util.List#hashCode java.util.List.hashCode()}
		 */
		public int hashCode() {
			int hash = 0;

			hash = hash + 31 * (this.nOne.hashCode() + this.nTwo.hashCode());
			hash = 31 * hash + this.weight;

			return hash;
		}

		/**
		 * Compares this object to the specified object. The result is
		 * {@code true} if and only if the argument is not {@code null} and is
		 * an {@code Edge} object that contains the same node values and weight
		 * value as this object.
		 *
		 * @param obj
		 *            - the object to compare with.
		 * @return {@code true} if the objects are the same; {@code false}
		 *         otherwise.
		 */
		public boolean equals(Object obj) {
			// if the object is compared with itself then return true
			if (obj == this) {
				return true;
			}

			/*
			 * Check if obj is an instance of Edge or not,
			 * "null instanceof [type]" also returns false.
			 */
			if (!(obj instanceof Edge)) {
				return false;
			}

			// typecast obj to Complex so that we can compare data members
			Edge that = (Edge) obj;

			/*
			 * First we check if this weight equals that weight. Then we check
			 * if this nOne equals that nOne and this nTwo equals that nTwo, and
			 * its flip: if this nOne equals that nTwo and this nTwo equals that
			 * nOne.
			 */
			if (this.getWeight() == that.getWeight()) {
				if (this.getFirstNode().equals(that.getFirstNode())) {
					if (this.getSecondNode().equals(that.getSecondNode())) {
						return true;
					}
				} else if (this.getFirstNode().equals(that.getSecondNode())) {
					if (this.getSecondNode().equals(that.getFirstNode())) {
						return true;
					}
				}
			}

			return false;
		}

		/**
		 * Compares two {@code Edge} objects numerically. The comparison is
		 * based on the difference between this {@code Edge}'s weight and the
		 * argument {@code Edge}'s weight.
		 * 
		 * @param that
		 *            - the {@code Edge} to be compared.
		 * @return the value 0 if the argument {@code Edge} has the same weight
		 *         as this {@code Edge}; a value less than 0 if this
		 *         {@code Edge} has a weight less than the {@code Edge}
		 *         argument; and a value greater than 0 if this {@code Edge} has
		 *         a weight greater than the {@code Edge} argument (signed
		 *         comparison).
		 */
		public int compareTo(Edge that) {
			return (this.getWeight() < that.getWeight()) ? -1 : ((this
					.getWeight() == that.getWeight()) ? 0 : 1);
		}
		
		@Override
		public String toString() {
			return String.format("%s,%d,%s", this.nOne, this.weight, this.nTwo);
		}

	}

	/**
	 * Entry point of the JVM.
	 * 
	 * @param args
	 *            - arguments passed to the program via command line.
	 */
	public static void main(String[] args) {
		// Declare local variables
		KruskalMinSpan minSpan = null;

		minSpan = new KruskalMinSpan();

		minSpan.buildGraph(new Scanner(getInputFile(args)));
		
	}

	/**
	 * Default constructor.
	 */
	public KruskalMinSpan() {
		this.edges = null;
		this.parseEdges = null;
		this.allEdges = new HashMap<Edge, String>();
		this.minWeight = 0;
	}

	/**
	 * Reads data from a file (most likely a character file) line-by-line using
	 * a {@code Scanner} object and builds a graph from said data.
	 * 
	 * @param inFile
	 *            - the {@code Scanner} to read data from.
	 * 
	 * @see {@link java.util.Scanner}
	 */
	public void buildGraph(Scanner inFile) {
		this.edges = this.parseLines(inFile, 0);
		Arrays.sort(this.edges);
		
		for(Map.Entry<Edge, String> entry : allEdges.entrySet()) {
			System.out.println(entry.toString());
		}
		
	}
	
	/**
	 * A recursive helper function for the {@code buildGraph(Scanner)} method.
	 * 
	 * @param data
	 *            - contents of the file in a {@code Scanner} object.
	 * @param count
	 *            - {@code int} to keep track of the recursive depth.
	 * 
	 * @return an array of {@code Edge} objects parsed from the passed in
	 *         {@code Scanner}.
	 * 
	 * @see {@linkplain #parseLines(Scanner, int)}
	 * @see {@linkplain java.util.Scanner}
	 */
	private Edge[] parseLines(Scanner data, int count) {
		if (data.hasNextLine()) {
			Scanner line = new Scanner(data.nextLine());
			Edge[] tmpEdges;

			line.useDelimiter("\\s*,\\s*");
			tmpEdges = parseLine(line.next(), line, 0);

			if (data.hasNextLine()) {
				count = count + tmpEdges.length;
				parseLines(data, count);
			} else {
				data.close();
				count = count + tmpEdges.length;
				this.edges = new Edge[count];
			}

			for (int i = 0; i < tmpEdges.length; i++) {
				count = count - 1;
				this.edges[count] = tmpEdges[i];
			}

			// System.out.println("c: " + count + "\ne: "
					// + Arrays.toString(this.edges));
		}
		
		return this.edges;
	}

	/**
	 * A recursive helper function for the {@code parseLines(Scanner, int)} method.
	 * 
	 * @param nOne
	 *            - placeholder for the first node in the {@code Edge} object.
	 * @param line
	 *            - one line from the {@code Scanner} object.
	 * @param count
	 *            - {@code int} to keep track of the recursive depth.
	 * 
	 * @return an array of {@code Edge} objects parsed from the passed in
	 *         {@code Scanner}.
	 * 
	 * @see {@linkplain #parseLines(Scanner, int)}
	 * @see {@linkplain java.util.Scanner}
	 */
	private Edge[] parseLine(String nOne, Scanner line, int count) {
		if (line.hasNext()) {
			int weight = line.nextInt();
			String nTwo = line.next();
			Edge edge = new Edge(nOne, nTwo, weight);

			if (!this.allEdges.containsKey(edge)) {
				this.allEdges.put(edge, edge.nOne);
			} else {
				edge = null;
				count -= 1;
			}

			if (line.hasNext()) {
				parseLine(nOne, line, ++count);
			} else {
				line.close();
				count += 1;
				this.parseEdges = new Edge[count];
			}

			if (edge != null) {
				this.parseEdges[count - 1] = edge;
			}
		}
		
		return this.parseEdges;
	}
	
	
	public void findRoot(String nOne, String nTwo) {

	}

	/**
	 * If a file path was passed to the program as an argument via
	 * <em><strong>{@code java KruskalMinSpan file-path}</strong></em> then
	 * attempt to open the file for reading. If that fails then get the filename
	 * from the fail safe approach.
	 * <p>
	 * If too many arguments were supplied to the program then inform the user
	 * and proceed to the fail safe approach.
	 * 
	 * @param args
	 *            - arguments passed to the program via command line.
	 * @return {@code FileReader} object to be read from.
	 * @see {@link KruskalMinSpan#failSafeApproach failSafeApproach}
	 * @see {@link java.io.FileReader}
	 */
	private static FileReader getInputFile(String[] args) {

		if (args.length > MAX_ARGUMENTS) {
			String thisClassName = (new Object() {
			}).getClass().getEnclosingClass().getSimpleName();
			System.out.println("[Error] Too many arguments were passed to '"
					+ thisClassName + "'");
		} else if (args.length == MAX_ARGUMENTS) {
			try {
				return new FileReader(args[0]);
			} catch (FileNotFoundException e) {
				outputException(e);
			}
		}

		return failSafeApproach();
	}

	/**
	 * Fail safe approach for ensuring the correct file will be opened by the
	 * program for reading.
	 * 
	 * @return {@code FileReader} object to be read from.
	 * @see {@link java.io.FileReader}
	 */
	private static FileReader failSafeApproach() {
		// Declare local constants
		final String DIRECTIONS = "Enter the name of the input file or \"quit"
				+ "\" to exit the program.\nExample: path/to/file.txt"
				+ "\nFilename: ";

		// Declare local variables
		Scanner input = new Scanner(System.in);
		String filename = "";
		FileReader file = null;

		do {
			System.out.print(DIRECTIONS);

			filename = input.next();

			if ("quit".equals(filename.toLowerCase())) {
				System.out.println("Exiting the program. Have a nice day!");
				System.exit(0);
			}

			try {
				file = new FileReader(filename);
				input.close();
				return file;
			} catch (FileNotFoundException e) {
				outputException(e);
			}
		} while (true);
	}

	/**
	 * Helper function that prints exceptions to output.
	 * 
	 * @param e
	 *            - the {@code Exception} to print to output.
	 * @see {@link java.lang.Exception java.lang.Exception}
	 */
	private static void outputException(Exception e) {
		System.out.println("[Error] " + e.getClass().getSimpleName() + " - "
				+ e.getMessage());
	}

}
