import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class OOSocketTest {

    // Define data (variables) required
    int port = 8189;
    String hostIP = "127.0.0.1";

    // Defined method to store the code
    public void myClient(String hostIP, int port){
        System.out.println("Client started...");
        try{
            Socket s = new Socket(hostIP, port);
            try{
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                OutputStream outStream = s.getOutputStream();
                PrintWriter sender = new PrintWriter(outStream, true);
                sender.println("Hello");

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

    public static void main(String[] args){
        OOSocketTest st = new OOSocketTest();
        st.myClient(st.hostIP, st.port);
    }
}
