// Karteikay Dhuper
// CNIT 325
// This is the chatroom class this renders the main GUI and utilizes
// the Person and Message class to execute the mainline client-server
// logic for the chat app.

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Chatroom extends JFrame implements ActionListener, MouseListener, KeyListener {

    // Client-Server Variables
    Person currentPerson;
    Person receiver;
    Message m = new Message();
    Time t = new Time();
    String currentMessage;

    Person karty = new Person("Karty", 1, "10.186.173.143", 8189);
    Person jack = new Person("Jack", 2, "10.186.173.143", 8180);

    // Frame Variables
    JPanel pnlContain;
    JTextField txtPort;
    JTextArea txtHistory, txtMessage;
    JLabel lblHistory, lblMessage;
    JComboBox comboReceiver;
    JButton btnSendMessage;

    LineBorder lineBorder = new LineBorder(Color.orange , 3, true);
    LineBorder lineBorderMessaActive = new LineBorder(Color.green , 3, true);
    Color bgColor = new Color(0,40,80);
    Color toolColor = new Color(0,25,51);


    // Menu and Tool bar
    JMenuBar menuBar;
    JToolBar toolbar;
    JMenuItem menuSendMessage, menuClearMessage;
    JButton toolSendBtn;
    JButton toolClearBtn;
    JComboBox toolChangeUser;
    JRadioButtonMenuItem person1MenuReceiver, person2MenuReceiver, person1MenuSender, person2MenuSender;
    ButtonGroup buttGroup1, buttGroup2;

    // Person p1;

    String[] receiverList = {"Pick Recipient", "Jack", "Karty"}; // combo box list population
    String[] senderList = {"Pick Sender", "Jack", "Karty"}; // combo box list population

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

    public Chatroom(){
        // Menu Bar
        menuBar = new JMenuBar();
        menuSendMessage = new JMenuItem("Send Message");
        menuSendMessage.addActionListener(this);
        menuSendMessage.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        menuClearMessage = new JMenuItem("Clear Message");
        menuClearMessage.addActionListener(this);
        menuClearMessage.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu( "File" );
        menuBar.add(fileMenu);
        fileMenu.add(menuSendMessage);
        fileMenu.add(menuClearMessage);

        JMenu senderMenu = new JMenu( "Sender" );
        person1MenuSender = new JRadioButtonMenuItem("Jack");
        person1MenuSender.addActionListener(this);
        person1MenuSender.setAccelerator(KeyStroke.getKeyStroke('1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        person2MenuSender = new JRadioButtonMenuItem("Karty");
        person2MenuSender.addActionListener(this);
        person2MenuSender.setAccelerator(KeyStroke.getKeyStroke('2', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuBar.add(senderMenu);
        senderMenu.add(person1MenuSender);
        senderMenu.add(person2MenuSender);

        JMenu receiverMenu = new JMenu("Receiver");
        person1MenuReceiver = new JRadioButtonMenuItem("Jack");
        person1MenuReceiver.addActionListener(this);
        person1MenuReceiver.setAccelerator(KeyStroke.getKeyStroke('3', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        person2MenuReceiver = new JRadioButtonMenuItem("Karty");
        person2MenuReceiver.addActionListener(this);
        person2MenuReceiver.setAccelerator(KeyStroke.getKeyStroke('4', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuBar.add(receiverMenu);
        receiverMenu.add(person1MenuReceiver);
        receiverMenu.add(person2MenuReceiver);

        // Tool bar
        toolbar = new JToolBar();
        toolbar.setBackground(toolColor);
        toolSendBtn = new JButton("Send Message");
        toolSendBtn.addActionListener(this);
        toolClearBtn = new JButton("Clear Message");
        toolClearBtn.addActionListener(this);
        toolChangeUser = new JComboBox(senderList);
        toolbar.add(toolSendBtn);
        toolbar.add(toolChangeUser);
        toolbar.add(toolClearBtn);

        // Main frame
        comboReceiver = new JComboBox(receiverList);
        comboReceiver.addActionListener(this);

        lblHistory = new JLabel(" _______________ Chat History: _______________ ");
        lblHistory.setForeground(Color.white);
        txtHistory = new JTextArea(30,30);
        txtHistory.setEditable(false);
        txtHistory.setLineWrap(true);
        txtHistory.setBackground(Color.black);
        txtHistory.setForeground(Color.green);
        txtHistory.setBorder(lineBorder);
        txtHistory.addMouseListener(this);

        lblMessage = new JLabel("Message:");
        lblMessage.setForeground(Color.white);
        txtMessage = new JTextArea(3,30);
        txtMessage.setLineWrap(true);
        txtMessage.setBackground(Color.black);
        txtMessage.setForeground(Color.white);
        txtMessage.setBorder(lineBorder);
        txtMessage.addKeyListener(this);
        txtMessage.addMouseListener(this);

        btnSendMessage = new JButton("Send");
        btnSendMessage.addActionListener(this);

        Container cp = getContentPane();
        pnlContain = new JPanel();
        pnlContain.add(toolbar);
        pnlContain.add(comboReceiver);
        pnlContain.add(lblHistory);
        pnlContain.add(txtHistory);
        pnlContain.add(lblMessage);
        pnlContain.add(txtMessage);
        pnlContain.add(btnSendMessage);
        pnlContain.setBackground(bgColor);
        cp.add(pnlContain);
        cp.addMouseListener(this);

    }

    public void actionPerformed(ActionEvent evt){

    // Menu Items
        // Menu Item Sender Changed
        if(evt.getSource() == person1MenuSender){
            currentPerson = jack;
            toolChangeUser.setSelectedIndex(1);
        }
        if(evt.getSource() == person2MenuSender){
            currentPerson = karty;
            toolChangeUser.setSelectedIndex(2);
        }
        // Menu Item Receiver Changed
        if(evt.getSource() == person1MenuReceiver){
            receiver = jack;
            comboReceiver.setSelectedIndex(1);
        }
        if(evt.getSource() == person2MenuReceiver){
            receiver = karty;
            comboReceiver.setSelectedIndex(2);
        }

    // Toolbar Clear and Change User
        if(evt.getSource() == toolClearBtn || evt.getSource() == menuClearMessage){
            txtMessage.setText("");
        }

        if(evt.getSource()==toolChangeUser){
            JComboBox comboSender = (JComboBox)evt.getSource();
            String senderList = (String) comboSender.getSelectedItem();
            if(senderList.equals("Jack")){
                currentPerson = jack;
                receiver = karty;
                toolChangeUser.setSelectedIndex(1);
                person1MenuSender.setSelected(true);
            }
            if(senderList.equals("Karty")){
                currentPerson = karty;
                receiver = jack;
                toolChangeUser.setSelectedIndex(2);
                person2MenuSender.setSelected(true);
            }
        }

    // Frame items
        if(evt.getSource() == comboReceiver){
            JComboBox comboReceiver = (JComboBox)evt.getSource();
            String msgReceiver = (String)comboReceiver.getSelectedItem();
            if(msgReceiver.equals("Jack")){
                receiver = jack;
                currentPerson = karty;
                person1MenuReceiver.setSelected(true);
            }

            if(msgReceiver.equals("Karty")){
                receiver = karty;
                currentPerson = jack;
                person2MenuReceiver.setSelected(true);
            }
        }
    //All send buttons
        if(evt.getSource() == btnSendMessage || evt.getSource() == toolSendBtn || evt.getSource() == menuSendMessage ){
            m.setMessage(txtMessage.getText());
            currentMessage = m.getMessage();
            sendMessage(receiver, currentMessage);

            txtMessage.setText("");
            txtMessage.requestFocus();
            txtHistory.append("   "+currentPerson.getUsername()+" @"+dtf.format(LocalDateTime.now())+": "+currentMessage+"\n");
        }
    }

    // Key Listeners
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if(!Character.isLetter(keyChar) && !Character.isDigit(keyChar) && keyChar != ' '){ // if not a character or digit don't display
            e.consume();
        }
    }

    public void keyPressed(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();  // for return key
        if(keyChar == ';' || keyCode == 13){
            btnSendMessage.requestFocus();
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int position = txtMessage.getCaretPosition();
        if(keyCode >= 65 && keyCode <=91 || keyCode >= 48 && keyCode <= 57 || keyCode == 32){
            txtMessage.setCaretPosition(position);
        }
        else{
            txtMessage.setText(txtMessage.getText().toLowerCase());
        }

    }

    //Mouse Listeners
    public void mouseClicked(MouseEvent e) {} public void mousePressed(MouseEvent e){}public void mouseReleased(MouseEvent e) {} //irellevant abstract methods

    public void mouseEntered(MouseEvent e) {
        if(e.getSource() == btnSendMessage){
            txtMessage.requestFocus();
        }
        if(e.getSource()==txtMessage){
            txtMessage.setBorder(lineBorderMessaActive);
        }
    }

    public void mouseExited(MouseEvent e) {
        if(e.getSource() == btnSendMessage){
            pnlContain.requestFocus();
        }
        if(e.getSource()==txtMessage){
            txtMessage.setBorder(lineBorder);
        }
    }

    public void sendMessage(Person p, String message) { // code for client connecting to a server
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

    public String receiveMessage(int port){ // code for server allowing connections from client
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
                            txtHistory.append("   "+receiver.getUsername()+" @"+dtf.format(LocalDateTime.now())+": "+currentMessage+"\n");
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
//        Scanner portNum = new Scanner(System.in);
//        System.out.println("What is your (server) port number: ");
//        int portNumber = portNum.nextInt();

        Chatroom app = new Chatroom();
        app.setVisible(true);
        app.setTitle("  Super Secret Chat App");
        app.setSize(390,750);
        app.setResizable(false);
        app.receiveMessage(8189);
    }
}



