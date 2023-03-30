import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Client {
    public static void main(String[] args) {
        new ClientGUI();
    }
}

class ClientGUI extends JFrame implements ActionListener {
    JTextField addressField, portField, dbNameField, userField, passField;
    JTextField nameField, idField, mathField, literatureField, englishField;
    JTextArea resultArea;
    JButton connectButton, sendButton;
    DatagramSocket socket;
    InetAddress serverAddress;
    int serverPort;

    ClientGUI() {
        setLayout(new FlowLayout());

        addressField = new JTextField(10);
        portField = new JTextField(4);
        dbNameField = new JTextField(10);
        userField = new JTextField(10);
        passField = new JTextField(10);
        nameField = new JTextField(10);
        idField = new JTextField(10);
        mathField = new JTextField(3);
        literatureField = new JTextField(3);
        englishField = new JTextField(3);
        resultArea = new JTextArea(10, 30);
        connectButton = new JButton("Connect");
        sendButton = new JButton("Send");

        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Port:"));
        add(portField);
        add(connectButton);
        add(new JLabel("DB Name:"));
        add(dbNameField);
        add(new JLabel("User:"));
        add(userField);
        add(new JLabel("Password:"));
        add(passField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("ID:"));
        add(idField);
        add(new JLabel("Math:"));
        add(mathField);
        add(new JLabel("Literature:"));
        add(literatureField);
        add(new JLabel("English:"));
        add(englishField);
        add(sendButton);
        add(resultArea);

        connectButton.addActionListener(this);
        sendButton.addActionListener(this);

        setTitle("Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) {
            try {
                String address = addressField.getText();
                int port = Integer.parseInt(portField.getText());
                socket = new DatagramSocket();
                serverAddress = InetAddress.getByName(address);
                serverPort = port;

                if ("admin".equals(address) && port == 1234) {
                    JOptionPane.showMessageDialog(this, "Connect Successful");
                } else {
                    JOptionPane.showMessageDialog(this, "Connect False");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == sendButton) {
            try {
                String dbName = dbNameField.getText();
                String user = userField.getText();
                String pass = passField.getText();
                String name = nameField.getText();
                String id = idField.getText();
                String math = mathField.getText();
                String literature = literatureField.getText();
                String english = englishField.getText();

                String data = dbName + "," + user + "," + pass + "," + name + "," + id + "," + math + "," + literature + "," + english;
                byte[] sendData = data.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                socket.send(sendPacket);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                resultArea.append(receivedData + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}