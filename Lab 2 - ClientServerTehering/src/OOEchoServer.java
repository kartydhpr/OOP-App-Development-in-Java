import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class OOEchoServer {

    // Define relevant data (variables) here
    int port = 8189;

    // Define relevant behavior(methods) here
    public void myServer(int port){
        try{
            ServerSocket s = new ServerSocket(port); // instantiates a ServerSocket object as s
            System.out.println("Server Started...");
            boolean over = false;
            while(!over){ // loop remains open indefintely
                Socket incoming = s.accept();
                try{
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStream = incoming.getOutputStream();

                    Scanner in = new Scanner(inStream);
                    System.out.println("=== Client connected ===");
                    PrintWriter out = new PrintWriter(outStream, true);

                    boolean done = false; // second loops keeps checking if client has more to say
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

    public static void main(String[] args){
        OOEchoServer es = new OOEchoServer();
        es.myServer(es.port);
    }
}
