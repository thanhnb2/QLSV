import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP{
    public static void main(String[] args) throws IOException{
        try (DatagramSocket serverSocket = new DatagramSocket(9876)) {
            while(true){
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                System.out.println(sentence);


            }
        }catch (Exception e) {
           System.out.println("error: "+ e);
        }
    }
}