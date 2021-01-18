import javax.swing.*;
import java.io.*;
/**
 * Main method gets file from user and applies to huffman tree
 *
 * @author Cathal Mullen
 * @version 12/3/2019
 */
public class Main
{
    public static void main(String[] args) throws IOException, FileNotFoundException {
        String s = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            s = selectedFile.getName();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
        
        
        HuffmanCoding hf = new HuffmanCoding(s);
        hf.encodeMsg();
        hf.showTable();
        hf.displayTree();
        hf.numBits();
        hf.decode();
        
        

    }
}
