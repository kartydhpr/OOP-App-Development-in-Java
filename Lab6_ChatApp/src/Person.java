// Karteikay Dhuper
// CNIT 325
// This is the Person class. It holds the attributes and behaviors related to a ChatApp Sender/Receiver.

public class Person {
    String username;
    int IDNum;
    String IPAddress;
    int port;

    public Person(String name, int id, String ip, int po){
        this.username = name;
        this.IDNum = id;
        this.IPAddress = ip;
        this.port = po;
    }

    // getters for person class attributes
    public String getUsername() {
        return username;
    }

    public int getIDNum() {
        return IDNum;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public int getPort() {
        return port;
    }

    // setters for person class attributes
    public void setUsername(String username) {
        this.username = username;
    }

    public void setIDNum(int IDNum) {
        this.IDNum = IDNum;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
