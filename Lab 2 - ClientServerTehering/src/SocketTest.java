import java.io.*;
import java.net.*;
import java.util.*;

public class SocketTest {
    public static void main(String[] args){
        try{
            Socket s = new Socket("127.0.0.1", 8189);
            try{
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                OutputStream outStream = s.getOutputStream();
                PrintWriter out = new PrintWriter(outStream, true);
                System.out.println("Welcome");

                while(in.hasNextLine()){
                    String line = in.nextLine();
                    System.out.println(line);
                }
            }
            finally {
                s.close();
            }
        }
        catch (IOException ioexc){
            ioexc.printStackTrace();
        }
    }
}

// Output: 59845 22-09-23 17:51:17 50 0 0 459.3 UTC(NIST) *