import java.io.*;
import java.net.*;
import java.util.*;

public class EchoServer{
    public static void main(String[] args){
        try{
            ServerSocket s = new ServerSocket(8189);
            boolean over = false;
            while(!over){
                Socket incoming = s.accept();
                try{
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStream = incoming.getOutputStream();

                    Scanner in = new Scanner(inStream);
                    System.out.println("=== Connection Opened ===");
                    PrintWriter out = new PrintWriter(outStream, true);

                    boolean done = false;
                    while (!done && in.hasNextLine()){
                        String lineIn = in.nextLine();
                        System.out.println(lineIn.trim());

                        out.println(lineIn);

                        if (lineIn.trim().equals("BYE")){
                            done = true;
                            System.out.println(" === Connection Closed === ");
                        }
                    }
                }
                catch (Exception exc1){
                    exc1.printStackTrace();
                }
            }
        }
        catch (Exception exc2){
            exc2.printStackTrace();
        }
    }
}
