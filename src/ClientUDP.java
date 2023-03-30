import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP{

    public static void main(String args[]) throws Exception{
        try (DatagramSocket clienSocket = new DatagramSocket(6789)) {
            InetAddress ipAddress = InetAddress.getByName("localhost");
            while(true){
                byte[] sendData= new byte[1024];
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                String sentence = inFromUser.readLine();
                sendData = sentence.getBytes();
                DatagramPacket senPacket = new  DatagramPacket(sendData, sendData.length, ipAddress, 9876);
                clienSocket.send(senPacket);
            }
        }
    }
}