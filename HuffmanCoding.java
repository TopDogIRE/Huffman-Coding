import java.io.*; 
import java.util.*; 
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Huffman Tree
 * 
 *
 * @author Cathal Mullen
 * @version 12/3/2019
 */
public class HuffmanCoding {

    private HNode root;
    private HashMap<Character, String> coding = new HashMap<Character, String>();
    private String path;
    private int strLen;
    private int encodeLen;

    /**
     * Constructer for huffman tree.
     * Huffman coding provides a solution to the long 8 digit 
     * ASCII value each character has by assigning codes to symbols
     * based on how often the symbols are used (their 'frequency'),
     * with frequently-used symbols getting shorter codes
     * than infrequently-used symbols. This is accomplished
     * by using a specific algorithm to construct a binary
     * tree with each 'leaf' node representing a symbol and
     * each 'edge' representing a bit value (0 or 1). 
     */
    public HuffmanCoding(String s) {
        File file = new File(s);
        if(!file.canRead()) // if can't be read
            throw new IllegalArgumentException();
        path = s; // allocate intstance variable
        HashMap<Character, Integer> charMap = new HashMap<Character, Integer>(); //Initialize map
        PriorityQueue<HNode> pQ = new PriorityQueue<HNode>(); //Initailize PQ
        System.out.println("Original Text: ");
        try{
            String line = new String(Files.readAllBytes(Paths.get(path)));
            strLen = line.length();
            char[] strArray = line.toCharArray();
            for(char c : strArray) { 
                if (charMap.containsKey(c)) { 
                    charMap.put(c, charMap.get(c) + 1); 
                } 
                else{
                    charMap.put(c, 1); 
                } 
            }
            System.out.print(line);

            System.out.println("\nFrequency Table: ");
            for(HashMap.Entry<Character, Integer> x : charMap.entrySet()){
                int fre = x.getValue();
                char c = x.getKey();

                if(c == ' '){
                    System.out.println("' ' = " + fre);
                }
                else if(c == '\n'){
                    System.out.println("\\n = " + fre);
                }
                else if(x.getKey() == '\r'){ 
                    System.out.println("\\r" + " = " + x.getValue());
                }
                else{
                    System.out.println(c + " = " + fre);
                }
                HNode n = new HNode(fre,c); 
                pQ.add(n); 
            }

            while(pQ.size() > 1){
                HNode one = pQ.remove(); // Remove from the start of the queue
                HNode two = pQ.remove();
                HNode p = new HNode(one.freq+two.freq, '~'); // Create a parent node
                p.left = one; // Assign the node a child node
                p.right = two; 
                pQ.add(p); // Add Node back to priority queue
            }
            root = pQ.remove(); // Save the root
            System.out.println();
        }
        catch(FileNotFoundException e){
            System.out.println("Cannot find File");
        }catch(IOException e){
            System.out.print("Error");
        }catch(Exception e){
            System.out.print("Error");
        }

    }
    
    /**
     * Calcuates the number if bits per symbol in percentage
     */
    public void numBits(){
        double compression = (encodeLen/(strLen * 8.0)) * 100.0;
        String strDouble = String.format("%.2f", compression);
        System.out.println("Average number of bits per symbol: " + strDouble + "%");
    }
    
    /**
     * To encode a message, you first need to generate a 
     * lookup table that allows you to determine
     * the code given a symbol. For this "proof of concept" program,
     * we will represent the encoded message as a string containing 
     * a series of 0's and 1's. Therefore, each 'code' can also be a string.
     * The codes can be determined for each character by using an appropriate
     * tree traversal.
     * 
     * @throws IOException if file cannot be found
     */
    public void encodeMsg() throws IOException{
        String result = "";
        priMsg(root, result);
        String str = backBuilder();
        encodeLen = str.length();
        System.out.println("Encoded Text: \n");
        for(int i = 0; i < str.length(); i++){
            System.out.print(str.charAt(i));
            if(i % 80 == 0 && i != 0){ // Break up encoded string onto different lines
                System.out.println();
            }
        }
    }

    /**
     * Helper method to read the string from the file
     */
    private String backBuilder() throws IOException, FileNotFoundException{
        StringBuilder str = new StringBuilder();
        String s = new String(Files.readAllBytes(Paths.get(path)));
        for(int i = 0; i < s.length(); i++){
            char a  = s.charAt(i);
            str.append(coding.get(a));
        }
        return str.toString();
    }
    
    /**
     * Recursive method in code the text
     */
    private void priMsg(HNode n, String result){
        if(n == null) return;  //Base case
        if(n.left == null && n.right == null){
            coding.put(n.letter, result);
        }
        priMsg(n.left, result + "0");
        priMsg(n.right, result + "1");
    }

    /**
     * Display the value of each characher in the form of 0's and 1's
     */
    public void showTable(){
        System.out.println("\nCode Table: ");
        for(HashMap.Entry<Character, String> x : coding.entrySet()){
            // if statements to check symbols that aren't clearly defined when printed out
            if(x.getKey() == '\n'){
                System.out.println("\\n" + " = " + x.getValue());
            }
            else if(x.getKey() == ' '){
                System.out.println("' '" + " = " + x.getValue());
            }
            else if(x.getKey() == '\r'){ 
                System.out.println("\\r" + " = " + x.getValue());
            }
            else{
                System.out.println(x.getKey() + " = "+ x.getValue());
            }
        }
    }

    /**
     * Displaying huffman tree
     */
    public void displayTree(){
        System.out.println("\nHuffman Tree: ");
        printTree(root);
    }

    /**
     * Private method to print tree
     *  Displays the root first in desending order
     */
    private static void printTree(HNode root){
        int height = numLevel(root);
        Queue<HNode> level = new LinkedList<>();
        Queue<HNode> newLevel = new LinkedList<>(); 
        level.add(root);
        System.out.println("...........................................");
        
        while(!level.isEmpty()){
            Iterator<HNode> itr = level.iterator();
            while(itr.hasNext()){
                String space = "";
                int e = height;
                for(; e > 0; e--){
                    space += "  ";
                }
                
                HNode node = itr.next();
                if(node.left != null){
                    newLevel.add(node.left);
                }
                if(node.right != null){
                    newLevel.add(node.right);
                }
                
                // checking for characters that aren't clearly defined when printed
                if(node.letter == '\n'){
                    System.out.print(space + "\\n"+"("+node.freq+")");
                }
                else if(node.letter == '\r'){
                    System.out.print(space + "\\r"+"("+node.freq+")");
                }
                else if(node.letter == ' '){
                    System.out.print(space + "' '"+"("+node.freq+")");
                }
                else{
                    System.out.print(space +"("+node.freq+")"+ node.letter);
                }
            }
            System.out.println();
            level = newLevel;
            newLevel = new LinkedList<HNode>();
        }
        System.out.println("...........................................");
    } 
    
    /**
     * return the number of levels in the tree
     */
    private static int numLevel(HNode root){
        if(root == null)return 0;
        int x = numLevel(root.left);
        int y = numLevel(root.right);
        if(x>y)return x+1;
        else{return y+1;}
        
    }

    /**
     * The Huffman Tree can be used directly to decode
     * the message. Starting at the root and at the beginning
     * of the 'coded' message, and use each '0' or '1' to
     * choose which edge to follow in the tree. Each time
     * you reach a leaf, add that symbol to your decoded
     * message, and then reset your pointer to the root.
     */
    public void decode()throws IOException, IndexOutOfBoundsException{

        System.out.print("Decoded Text: \n");
        String str = backBuilder().toString();
        HNode n = root;
        if (n == null)  return;

        char[] arr = str.toCharArray();
        int index = 0;
        String rst = "";
        while (index < arr.length) {
            n = root;
            while (n != null) {
                if (n.left == null && n.right == null) {
                    rst += n.letter;
                    break;//break inner while
                } else {
                    char c = arr[index];
                    if (c == '0') {
                        n = n.left;
                    } else {
                        n = n.right;
                    }
                    index++;
                }
            }
        }
        System.out.println(rst);
    }
}

/**
 * Each node in the Huffman Tree is an instance
 * of class HNode. HNode object stores
 * the following: the character (symbol) it represents
 * (if it is a leaf node), the 'frequency' (or 'weight')
 * associated with the node (an int value), and links to
 * the node's left and right children. Class HNode
 * implements the Comparable interface; comparisons are
 * resolved based on the 'frequency' value. HNode could be
 * a top-level class or an inner class.
 */
class HNode implements Comparable<HNode>{
    HNode left;
    HNode right;
    int freq;
    Character letter;

    /**
     * Construct Diafram for Nodes
     */
    public HNode(int freq, Character letter){
        if (freq < 1){
            throw new IllegalArgumentException();
        }
        this.freq = freq;
        this.letter = letter;

        this.left = null;
        this.right = null;
    }

    public int compareTo( HNode p){
        if(p.freq > this.freq) return -1;
        if(p.freq < this.freq) return 1;

        return 0; // When they are equal
    }

}