// Christopher Gonzales
//
// This program compresses files by reducing the necessary number of bits per character by using
// a Huffman coding scheme that is generated from a Huffman Tree structure. Using a priority queue
// our program constructs a tree that always has nodes with the lowest frequencies farthest down 
// the tree, and nodes with high frequencies up near the root of the tree. Left subtree paths have 
// a "0" value, and right subtrees have a "1" value. The unique structure that is created is 
// exactly what is needed to make path codes, which exhibit the prefix property. Our program is 
// able to output our tree, in standard order, to a given code file. It is also able to take 
// encoded files and decode them back into their original 8-bit character format using the code 
// file.

import java.io.*;
import java.util.*;

public class HuffmanBinaryTree {
	private HuffmanNode overallRoot;
	public static final int CHAR_MAX = 256;  // max char value to be encoded
	
	// post: constructs the initial Huffman structure with the given frequency counts of ASCII
	//	 characters. An extra node is added to account for our special pseudo-eof character
	public HuffmanBinaryTree(int[] count) {
		Queue<HuffmanNode> nodeQueue = new PriorityQueue<HuffmanNode>();

		for (int i = 0; i < count.length; i++) {
			if (count[i] > 0) 
				nodeQueue.add(new HuffmanNode(i, count[i]));
		}
		nodeQueue.add(new HuffmanNode(CHAR_MAX, 1));
				
		while (nodeQueue.size() > 1) {
			HuffmanNode node1 = nodeQueue.remove();
			HuffmanNode node2 = nodeQueue.remove();
			int frequencySum = node1.frequency + node2.frequency;
			HuffmanNode newRoot = new HuffmanNode(-1, frequencySum, node1, node2);
			nodeQueue.add(newRoot);
		}
		overallRoot = nodeQueue.remove();
	}
	
	// pre : Scanner contains a tree stored in standard format
	// post: constructs a Huffman structure from the given code file. Uses each binary value in 
	//       the Huffman code to reconstruct a pathway from the top of the tree to each character 
	//       node. A binary value of 0 means we shall progress left down the tree, and a value of
	// 	 1 means we progress downward to the right. A dummy(empty) root will be instantiated. 
	public HuffmanBinaryTree(Scanner input) {
		overallRoot = new HuffmanNode(-1);
		
		while (input.hasNextLine()) {
			HuffmanNode curr = overallRoot;
			int character = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			for (int i = 0; i < code.length(); i++) { 
				char binaryChar = code.charAt(i);
				if (binaryChar == '0') {
					curr.left = addNode(character, binaryChar, curr.left); 
					curr = curr.left;
				} else { 
					curr.right = addNode(character, binaryChar, curr.right);
					curr = curr.right;
				}
			}	
		}	
	}
	
	// post: returns a child node, set with given character value, to its parent node.
	private HuffmanNode addNode(int character, char binaryChar, HuffmanNode nextHuffman) {
		if (nextHuffman == null) 
			nextHuffman = new HuffmanNode(character);
					
		return nextHuffman;
	}
	
	// post: writes the tree to the given output file in standard format which consists of line 
	//	 pairs that describe the ASCII value code for each character in the structure. Codes
	//	 will appear in traversal order.
	public void write(PrintStream output) {
		writeHelper(output, overallRoot, "");
	}
	
	// post: writes the tree to the given output file in standard format which consists of line 
	//	 pairs that describe the ASCII value code for each character in the structure. Codes
	//	 will appear in traversal order.
	private void writeHelper(PrintStream output, HuffmanNode root, String path) {
		if (root != null) {
			if (root.isLeaf()) {
				output.println(root.character);
				output.println(path);
			} else {	
				writeHelper(output, root.left, path + "0");
				writeHelper(output, root.right, path + "1");
			}
		}
	}
	
	// pre : input stream contains a legal encoding of characters for this tree's Huffman code
	// post: reads individual bits from the input stream and writes corresponding characters to the given output file. This method stops reading
	//	 if it encounters our pseudo-eof character. 
	public void decode(BitInputStream input, PrintStream output, int eof) {	
		HuffmanNode curr = overallRoot;
		boolean eofFound = false;
		 
		int bit = input.readBit();
		while (bit != -1 && !eofFound) {	
			if (bit == 0) 
				curr = curr.left;
			else 
				curr = curr.right;
			if (curr.isLeaf()){
				if (curr.character == eof) 
					eofFound = true;
				else  {
					output.write(curr.character);
					curr = overallRoot;
				}
			}
			bit = input.readBit();
		}
	}
}
