import java.io.*;
import java.net.*;
import java.sql.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Server {
    private static final int SERVER_PORT = 1234;
    private static final String ADMIN_ADDRESS = "admin";
    private static final String SECRET_KEY = "abcdefgh";

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                if (ADMIN_ADDRESS.equals(IPAddress.getHostName()) && port == SERVER_PORT) {
                    String connectionInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    String[] connectionDetails = connectionInfo.split(",");
                    String databaseName = connectionDetails[0];
                    int sqlPort = Integer.parseInt(connectionDetails[1]);
                    String username = connectionDetails[2];
                    String password = connectionDetails[3];

                    Connection sqlConnection = connectToSQL(databaseName, sqlPort, username, password);
                    if (sqlConnection != null) {
                        String encryptedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        String studentData = decryptData(encryptedData, SECRET_KEY);
                        String[] studentDetails = studentData.split(",");
                        String studentName = studentDetails[0];
                        String studentID = studentDetails[1];
                        double mathScore = Double.parseDouble(studentDetails[2]);
                        double literatureScore = Double.parseDouble(studentDetails[3]);
                        double englishScore = Double.parseDouble(studentDetails[4]);

                        saveToSQL(sqlConnection, studentName, studentID, mathScore, literatureScore, englishScore);

                        double averageScore = (mathScore + literatureScore + englishScore) / 3;
                        String result = studentName + "," + studentID + "," + averageScore;
                        sendData = result.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection connectToSQL(String dbName, int port, String user, String pass) {
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://localhost:" + port + ";databaseName=" + dbName + ";user=" + user + ";password=" + pass +";encrypt=true;trustServerCertificate=true;";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void saveToSQL(Connection connection, String name, String id, double mathScore, double literatureScore, double englishScore) {
        String query = "INSERT INTO students (student_id, name, math_score, literature_score, english_score) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setDouble(3, mathScore);
            statement.setDouble(4, literatureScore);
            statement.setDouble(5, englishScore);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String decryptData(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedDataBytes = cipher.doFinal(encryptedData.getBytes());
        return new String(decryptedDataBytes);
    }
}