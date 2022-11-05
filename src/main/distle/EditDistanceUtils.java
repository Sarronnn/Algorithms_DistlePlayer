package main.distle;

import java.util.*;

public class EditDistanceUtils {

	/**
	 * Returns the completed Edit Distance memoization structure, a 2D array of ints
	 * representing the number of string manipulations required to minimally turn
	 * each subproblem's string into the other. t[r,c]
	 * 
	 * @param s0 String to transform into other = Row
	 * @param s1 Target of transformation = Column
	 * @return Completed Memoization structure for editDistance(s0, s1)
	 */
	public static int[][] getEditDistTable(String s0, String s1) {

		int rowSize = s0.length();
		int columnSize = s1.length();
		int[][] table = new int[s0.length() + 1][s1.length() + 1];

		// Gutters:
		for (int r = 0; r < rowSize + 1; r++) {
			table[r][0] = r;
		}

		for (int c = 0; c < columnSize + 1; c++) {
			table[0][c] = c;
		}
		// Fill out table:
		for (int r = 1; r < rowSize + 1; r++) {
			for (int c = 1; c < columnSize + 1; c++) {

				// deletion case
				if (r >= 1) {
					table[r][c] = table[r - 1][c] + 1;

				}
				// insertion case
				if (c >= 1) {
					table[r][c] = Math.min(table[r][c - 1] + 1, table[r][c]);

				}
				// replacement case
				if (r >= 1 && c >= 1) {
					if (s0.charAt(r - 1) == s1.charAt(c - 1)) {
						table[r][c] = Math.min(table[r - 1][c - 1], table[r][c]);
					} else {
						table[r][c] = Math.min(table[r - 1][c - 1] + 1, table[r][c]);

					}
				}
				// transposition case
				if (r >= 2 && c >= 2 && s0.charAt(r - 1) == s1.charAt(c - 2) && s0.charAt(r - 2) == s1.charAt(c - 1)) {
					table[r][c] = Math.min(table[r - 2][c - 2] + 1, table[r][c]);

				}

			}
		}

		return table;
	}

	/**
	 * Returns one possible sequence of transformations that turns String s0 into
	 * s1. The list is in top-down order (i.e., starting from the largest subproblem
	 * in the memoization structure) and consists of Strings representing the String
	 * manipulations of:
	 * <ol>
	 * <li>"R" = Replacement</li>
	 * <li>"T" = Transposition</li>
	 * <li>"I" = Insertion</li>
	 * <li>"D" = Deletion</li>
	 * </ol>
	 * In case of multiple minimal edit distance sequences, returns a list with ties
	 * in manipulations broken by the order listed above (i.e., replacements
	 * preferred over transpositions, which in turn are preferred over insertions,
	 * etc.)
	 * 
	 * @param s0    String transforming into other
	 * @param s1    Target of transformation
	 * @param table Precomputed memoization structure for edit distance between s0,
	 *              s1
	 * @return List that represents a top-down sequence of manipulations required to
	 *         turn s0 into s1, e.g., ["R", "R", "T", "I"] would be two replacements
	 *         followed by a transposition, then insertion.
	 */
	public static List<String> getTransformationList(String s0, String s1, int[][] table) {
		// [!] TODO!
		List<String> transformation = new ArrayList<String>();
		int row = s0.length();
		int column = s1.length();
		// table [row][column];

		// if edit is 0 base case
		// helper method: recursive
		//
		while (table[row][column] != 0) {
			// String bestaction = getMin(table, row, column, s0, s1);

			int best = Integer.MAX_VALUE;
			int nextRow = row;
			int nextColumn = column;
			String action = "R";

			// if (row >=1 && column >=1) {
			// if its replacement
			if (row >= 1 && column >= 1 && table[row - 1][column - 1] < best) {
				best = table[row - 1][column - 1];
				action = "R";
				// table[row][column] = table[row - 1][column -1];
				nextRow = row - 1;
				nextColumn = column - 1;

			}
			// if its transposition

			if (row >= 2 && column >= 2 && table[row - 2][column - 2] < best) {
				if (s0.charAt(row - 1) == s1.charAt(column - 2) && s0.charAt(row - 2) == s1.charAt(column - 1)) {
					best = table[row - 2][column - 2];
					action = "T";
					nextRow = row - 2;
					nextColumn = column - 2;

				}
			}
			// if its insertion
			if (column >= 1 && table[row][column - 1] < best) {
				best = table[row][column - 1];
				action = "I";
				nextColumn = column - 1;
				

			}
			//if its deletion
			if (row >= 1 && table[row - 1][column] < best) {
				best = table[row - 1][column];
				action = "D";
				nextRow = row -1; 
				

			}

			// }
			
			
			if (!(action.equals("R") && s0.charAt(row-1) == s1.charAt(column-1))) {
				transformation.add(action);
				
			}
			row = nextRow;
			column = nextColumn;
			
		}

		return transformation;

	}

	/**
	 * Helper Method
	 */
	/**
	 * private static String getMin(int[][] table, int row, int column, String s0,
	 * String s1) { int best = Integer.MAX_VALUE; int replacement =
	 * table[row-1][column-1]; int deletion = table[row-1][column]; int insertion =
	 * table[row][column-1]; int transposition = table[row-2][column -2]; String
	 * action = "R";
	 * 
	 * if (row >=1 && column >=1) {
	 * 
	 * if (replacement < best) { best = replacement; action = "R";
	 * 
	 * }
	 * 
	 * if (s0.charAt(row-1) == s1.charAt(column-2) && s0.charAt(row-2) ==
	 * s1.charAt(column-1)) { if (transposition < best) { best = transposition;
	 * action = "T"; } } if (insertion < best) { best = insertion; action = "I"; }
	 * if (deletion < best) { best = deletion; action = "D"; }
	 * 
	 * 
	 * } return action;
	 * 
	 * }
	 */
	/**
	 * Returns the edit distance between the two given strings: an int representing
	 * the number of String manipulations (Insertions, Deletions, Replacements, and
	 * Transpositions) minimally required to turn one into the other.
	 * 
	 * @param s0 String to transform into other
	 * @param s1 Target of transformation
	 * @return The minimal number of manipulations required to turn s0 into s1
	 */
	public static int editDistance(String s0, String s1) {
		if (s0.equals(s1)) {
			return 0;
		}
		return getEditDistTable(s0, s1)[s0.length()][s1.length()];
	}

	/**
	 * See {@link #getTransformationList(String s0, String s1, int[][] table)}.
	 */
	public static List<String> getTransformationList(String s0, String s1) {
		return getTransformationList(s0, s1, getEditDistTable(s0, s1));
	}

}
