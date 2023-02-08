// Karteikay Dhuper
// CNIT 325
// This is the chatroom class this renders the main GUI and utilizes
// the Person and Message class to execute the mainline client-server
// logic for the chat app.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Chatroom extends JFrame implements ActionListener  {
    // Client-Server Variables
    Person currentPerson;
    Person receiver;
    Message m = new Message();
    String currentMessage;

    Person karty = new Person("Karty", 1, "10.186.170.195", 8189);
    Person jack = new Person("Jack", 2, "10.186.170.195", 8180);

    // Frame Variables
    JPanel pnlContain;
    JTextField txtPort;
    JTextArea txtHistory, txtMessage;
    JLabel lblHistory, lblMessage;
    JComboBox comboReceiver;
    JButton btnSendMessage;
    Person p1;

    String[] receiverList = {"Pick receiver", "Jack", "Karty"}; // combo box list population

    public Chatroom(){
        Container cp = getContentPane();
        comboReceiver = new JComboBox(receiverList);
        comboReceiver.addActionListener(this);

        lblHistory = new JLabel(" _______________ Chat History: _______________ ");
        txtHistory = new JTextArea(30,30);
        txtHistory.setEditable(false);

        lblMessage = new JLabel("Message:");
        txtMessage = new JTextArea(3,30);

        btnSendMessage = new JButton("Send");
        btnSendMessage.addActionListener(this);

        pnlContain = new JPanel();
        pnlContain.add(comboReceiver);
        pnlContain.add(lblHistory);
        pnlContain.add(txtHistory);
        pnlContain.add(lblMessage);
        pnlContain.add(txtMessage);
        pnlContain.add(btnSendMessage);
//        pnlContain.setBackground(Color.black);
        cp.add(pnlContain);
    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == comboReceiver){
            JComboBox comboReceiver = (JComboBox)evt.getSource();
            String msgReceiver = (String)comboReceiver.getSelectedItem();
            if(msgReceiver.equals("Jack")){
                receiver = jack;
                currentPerson = karty;
            }

            if(msgReceiver.equals("Karty")){
                receiver = karty;
                currentPerson = jack;
            }
        }
        if(evt.getSource() == btnSendMessage){
            m.setMessage(txtMessage.getText());
            currentMessage = m.getMessage();
            sendMessage(receiver, currentMessage);

            txtMessage.setText("");
            txtMessage.requestFocus();
            txtHistory.append(currentPerson.getUsername()+": "+currentMessage+"\n");
        }
    }

    public void sendMessage(Person p, String message) {
        try {
            Socket s = new Socket(p.getIPAddress(), p.getPort());
            try {
                OutputStream outStream = s.getOutputStream();
                PrintWriter sender = new PrintWriter(outStream, true);
                sender.println(message);
                System.out.println("Message Sent...");
            } finally {
                s.close();
            }
        } catch (IOException ioexc) {
            ioexc.printStackTrace();
        }
    }

    public String receiveMessage(int port){
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
                    System.out.println("=== Message Received ===");

                    boolean done = false; // second loops keeps checking if client has more to say
                    while (!done && in.hasNextLine()){
                        String lineIn = in.nextLine();

                        if (lineIn.trim().equals("TERMINATE")){
                            done = true;
                            System.out.println(" === Connection Closed === ");
                        }
                        else{ // as long as received message isnt TERMINATE then instream is added to the text area
                            m.setMessage(lineIn);
                            currentMessage = m.getMessage();
                            txtHistory.append(receiver.getUsername()+": "+currentMessage+"\n");
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
        return currentMessage;
    }

    public static void main(String [] args) {
        Scanner portNum = new Scanner(System.in);
        System.out.println("What is your (server) port number: ");
        int portNumber = portNum.nextInt();

        Chatroom window = new Chatroom();
        window.setVisible(true);
        window.setTitle("  Super Secret Chat App");
        window.setSize(390,700);
        window.setResizable(false);
        window.receiveMessage(portNumber);
    }
}



