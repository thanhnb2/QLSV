import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Quan_ly_sinh_vien {
    public static void main(String[] args) {
        // Thay đổi thông tin kết nối phù hợp với cấu hình của bạn
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSV;encrypt=true;trustServerCertificate=true;";
        String username = "sa";
        String password = "sa";

        try {
            // Load driver SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Kết nối đến SQL Server
            Connection connection = DriverManager.getConnection(url, username, password);

            System.out.println("Kết nối đến SQL Server thành công!");

            // Tạo đối tượng Statement để thực hiện truy vấn
            Statement statement = connection.createStatement();

            // Thực hiện truy vấn và lưu kết quả vào ResultSet
            String sql = "SELECT MaSV, HoTen, DiemToan, DiemVan, DiemTiengAnh FROM SinhVien";
            ResultSet resultSet = statement.executeQuery(sql);

            // Duyệt qua các bản ghi trong ResultSet và in ra thông tin
            while (resultSet.next()) {
                String maSV = resultSet.getString("MaSV");
                String hoTen = resultSet.getString("HoTen");
                double diemToan = resultSet.getDouble("DiemToan");
                double diemVan = resultSet.getDouble("DiemVan");
                double diemAnh = resultSet.getDouble("DiemTiengAnh");

                System.out.printf("MaSV: %s, HoTen: %s, DiemToan: %.2f, DiemVan: %.2f, DiemTiengAnh: %.2f%n",
                        maSV, hoTen, diemToan, diemVan, diemAnh);
            }

            // Đóng kết nối
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver SQL Server: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối đến SQL Server: " + e.getMessage());
        }
    }
}