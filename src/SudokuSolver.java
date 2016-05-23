/**
 * Created by: Dgaylord
 * Created on: 2/3/2016
 */

import java.io.*;
import java.util.*;

class SudokuSolver {

	private String[][] board;

    public static void main(String[] args) {

        SudokuSolver s = new SudokuSolver();
        s.setup();
    }
    
    void setup() {
    	
    	board = new String[][]{{"0", "0", "0", "7", "9", "0", "0", "0", "0"}, 
    						   {"0", "0", "9", "0", "5", "0", "0", "0", "0"}, 
    						   {"0", "0", "7", "0", "0", "0", "2", "6", "0"}, 
    						   {"0", "1", "0", "0", "4", "0", "0", "0", "0"}, 
    						   {"0", "8", "3", "2", "0", "0", "1", "0", "0"}, 
    						   {"0", "6", "0", "0", "0", "0", "0", "4", "0"}, 
    						   {"0", "0", "6", "0", "0", "0", "0", "0", "0"}, 
    						   {"0", "0", "0", "3", "0", "8", "0", "0", "4"}, 
    						   {"0", "9", "0", "0", "0", "0", "0", "8", "7"}};
    	
        fill();
        solve();
        solve();
        solve();
    }
    
    void addCell(int row, int col, String what) {
    	board[row - 1][col - 1] = what;
    }
    
    void fill() {

    	for(int i = 0; i < board.length; i++) {
    		for(int j = 0; j < board[i].length; j++) {
    			if(board[i][j] == null) {
    				board[i][j] = "0";
    			}
    		}
    	}
    }
    
    void solve() {
    	
    	ArrayList<String>[][] helper = new ArrayList[9][9];
    	boolean solved = true;
    	display();
    	
    	// Set up helper array to hold what values can go in unknown spots
    	for(int i = 0; i < helper.length; i++) {
    		for(int j = 0; j < helper[i].length; j++) {
    			if(board[i][j].equals("0")) {
    				solved = false;
    				helper[i][j] = new ArrayList<String>();
    				for(int k = 1; k < 10; k++) {
		    			if(!check(i, j, "" + k)) {
		    				helper[i][j].add("" + k);
		    			}
    				}
    			}
    		}
    	}
    	display(helper);
    	
    	// If column contains two identical pairs, remove those items from every other helper slot in that column
    	for(int i = 0; i < helper.length; i++) {
    		for(int j = 0; j < helper[i].length; j++) {
    			if(helper[i][j] != null) {
	    			int where = colPair(helper, i, j);
	    			if(where != -1) {
	    				for(int k = 0; k < helper.length; k++) {
	    					if(helper[k][j] != null && k != where && k != i) {
	    						for(int l = 0; l < helper[i][j].size(); l++) {
	    							helper[k][j].remove(helper[i][j].get(l));
	    						}
	    					}
	    				}
	    			}
    			}	
    		}
    	}
    	
    	// If column contains two identical pairs, remove those items from every other helper slot in that row
    	for(int i = 0; i < helper.length; i++) {
    		for(int j = 0; j < helper[i].length; j++) {
    			if(helper[i][j] != null) {
	    			int where = rowPair(helper, i, j);
	    			if(where != -1) {
	    				for(int k = 0; k < helper[0].length; k++) {
	    					if(helper[i][k] != null && k != where && k != j) {
	    						for(int l = 0; l < helper[i][j].size(); l++) {
	    							helper[i][k].remove(helper[i][j].get(l));
	    						}
	    					}
	    				}
	    			}
    			}	
    		}
    	}
    	
    	// If column contains two identical pairs, remove those items from every other helper slot in that group
    	for(int i = 0; i < helper.length; i++) {
    		for(int j = 0; j < helper[i].length; j++) {
    			if(helper[i][j] != null) {
	    			int[] where = groupPair(helper, i, j);
	    			if(where != null) {
	    				for(int k = 0 - (i % 3); k < 3 - (i % 3); k++) {
							for(int l = 0 - (j % 3); l < 3 - (j % 3); l++) {
								if(helper[i + k][j + l] != null && !(k == where[0] && l == where[1]) && !(k == i && l == j)) {
		    						for(int m = 0; m < helper[i][j].size(); m++) {
		    							helper[i + k][j + l].remove(helper[i][j].get(m));
		    						}
								}
							}
						}
	    			}
    			}	
    		}
    	}
    	display(helper);
    	
    	// If only one item exists in a helper slot, that's the unknown
    	for(int i = 0; i < helper.length; i++) {
    		for(int j = 0; j < helper[i].length; j++) {
    			if(helper[i][j] != null) {
	    			if(helper[i][j].size() == 1) {
	    				board[i][j] = helper[i][j].get(0);
	    			}
    			}
    		}
    	}
    	
    	// If the helper slot contains the only instance of a number in a row/column/group, that's the unknown
    	for(int i = 0; i < helper.length; i++) {
    		for(int j = 0; j < helper[i].length; j++) {
    			if(helper[i][j] != null) {
	    			for(int k = 0; k < helper[i][j].size(); k++) {
	    				if(only(helper, i, j, helper[i][j].get(k))) {
	    					board[i][j] = helper[i][j].get(k);
	    				}
	    			}
    			}	
    		}
    	}
    	
    	display();
    	if(solved) {
    		return;
    	} else {
    		return;
    	}
    }
    
    // return true if what exists in the row or column
    boolean check(int row, int col, String what) {
    	
    	for(int i = 0; i < board.length; i++) {
    		for(int j = 0; j < board[i].length; j++) {
    			if(i == row || j == col) {
    				if (board[i][j].equals(what)) {
    					return true;
    				}
    			}
    		}
    	}
    	for(int i = 0 - (row % 3); i < 3 - (row % 3); i++) {
			for(int j = 0 - (col % 3); j < 3 - (col % 3); j++) {
				if (board[row + i][col + j].equals(what)) {
					return true;
				}
			}
		}
    	return false;
    }
    
    // return true if what only exists in (row, col) of toCheck for a row/column/group
    boolean only(ArrayList<String>[][] toCheck, int row, int col, String what) {
    	
    	boolean exists = false;
    	for(int i = 0; i < toCheck[0].length; i++) {
    		if (toCheck[row][i] != null && i != col) {
	    		if(toCheck[row][i].contains(what)) {
	    			exists = true;
	    		}
    		}
    	}
    	if(!exists) {
    		return true;
    	}
    	
    	exists = false;
    	for(int i = 0; i < toCheck.length; i++) {
    		if (toCheck[i][col] != null && i != row) {
	    		if(toCheck[i][col].contains(what)) {
	    			exists = true;
	    		}
    		}
    	}
    	if(!exists) {
    		return true;
    	}
    	
    	exists = false;
    	for(int i = 0 - (row % 3); i < 3 - (row % 3); i++) {
			for(int j = 0 - (col % 3); j < 3 - (col % 3); j++) {
				if (toCheck[row + i][col + j] != null) {
					if (toCheck[row + i][col + j].contains(what)) {
						exists = true;
					}
				}
			}
		}
		if(!exists) {
    		return true;
    	}
    	
    	return false;
    }
    
    // return the loction that is equal to helper slot at (row, col) in the same column
    int colPair(ArrayList<String>[][] toCheck, int row, int col) {
    	
    	for(int i = 0; i < toCheck.length; i++) {
    		if (toCheck[i][col] != null && i != row) {
	    		if(toCheck[i][col].equals(toCheck[row][col]) && toCheck[i][col].size() == 2 && toCheck[row][col].size() == 2) {
		    		return i;
	    		}
    		}
    	}
    	return -1;
    }
    
    // return the loction that is equal to helper slot at (row, col) in the same row
    int rowPair(ArrayList<String>[][] toCheck, int row, int col) {
    	
    	for(int i = 0; i < toCheck[0].length; i++) {
    		if (toCheck[row][i] != null && i != col) {
	    		if(toCheck[row][i].equals(toCheck[row][col]) && toCheck[row][i].size() == 2 && toCheck[row][col].size() == 2) {
		    		return i;
	    		}
    		}
    	}
    	return -1;
    }
    
    // return the loction that is equal to helper slot at (row, col) in the same group
    int[] groupPair(ArrayList<String>[][] toCheck, int row, int col) {
    	
    	for(int i = 0 - (row % 3); i < 3 - (row % 3); i++) {
			for(int j = 0 - (col % 3); j < 3 - (col % 3); j++) {
				if (toCheck[row + i][col + j] != null) {
					if (toCheck[row + i][col + j].equals(toCheck[row][col]) && toCheck[row + i][col + j].size() == 2 && toCheck[row][col].size() == 2) {
						return new int[]{i, j};
					}
				}
			}
		}
    	return null;
    }
    
    void display() {
    	System.out.println("-------------------------");
    	for(int i = 0; i < board.length; i++) {
    		System.out.print("| ");
    		for(int j = 0; j < board[i].length; j++) {
    			System.out.print(board[i][j] + " ");
    			if((j + 1) % 3 == 0) {
	    			System.out.print("| ");
	    		}
    		}
    		if((i + 1) % 3 == 0) {
    			System.out.println("\n-------------------------");
    		} else {
    			System.out.println();
    		}
    	}
    }
    
    void display(ArrayList<String>[][] help) {
    	System.out.println("||-------|-------|-------||-------|-------|-------||-------|-------|-------||");
    	System.out.println("||-------|-------|-------||-------|-------|-------||-------|-------|-------||");
    	for(int i = 0; i < help.length; i++) {
    		for (int l = 0; l < 3; l++) {
    			System.out.print("||");
	    		for(int j = 0; j < help[i].length; j++) {
	    			for (int k = 1 + (l * 3); k < 4 + (l * 3); k++) {
	    				if (help[i][j] != null) {
			    			if(help[i][j].contains("" + k)) {
			    				System.out.print(" " + k);
			    			} else {
			    				System.out.print("  ");
			    			}
	    				} else {
	    					System.out.print("  ");
	    				}
	    			}
	    			System.out.print(" |");
	    			if((j + 1) % 3 == 0) {
		    			System.out.print("|");
		    		}
	    		}
	    		System.out.println();
    		}
    		System.out.println("||-------|-------|-------||-------|-------|-------||-------|-------|-------||");
	    		if((i + 1) % 3 == 0) {
	    			System.out.println("||-------|-------|-------||-------|-------|-------||-------|-------|-------||");
	    		}
    	}
    }
}