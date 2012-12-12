// Christopher Gonzales
//
// The HuffmanNode class is used to store the ASCII value and frequency information for one
// node in a Huffman tree structure. If our HuffmanNode is a leaf node, its character field will
// be an character value(-1 otherwise) and empty left and right data fields. If it is not a leaf 
// node, then our character and frequency fields will be set to default values with the references
// to other HuffmanNodes in the left and/or right fields.

public class HuffmanNode implements Comparable<HuffmanNode> {
	public int character;			// value of character being stored. -1 means no character
	public int frequency;			// num of times the character appears in a document
	public HuffmanNode left;		// pointer to the left node
	public HuffmanNode right;		// pointer to the right node
	
	// post: constructs a node that can store a given numerical value representation
	public HuffmanNode(int character) {
		this(character, 0);
	}

	// post: constructs a node with given value & frequency 
	public HuffmanNode(int character, int frequency) {
		this(character, frequency, null, null);
	}
	
	// post: constructs a node with given character & frequency values that will contain
	//       pointers to other nodes
	public HuffmanNode(int character, int frequency, HuffmanNode left, HuffmanNode right) {
		this.character = character;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}
	
	// post: returns true if the node has empty left and right fields. False otherwise.
	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}
	
	// post: returns a positive int if frequency is greater than the other node's frequency. 
	//	     returns a negative if frequency is smaller than the other node's frequency. 
	//  	 returns 0 if frequency values of both nodes are equal.
	public int compareTo(HuffmanNode other) {
		return this.frequency - other.frequency;
	}
}
