package murach.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

public class SQLGatewayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lần đầu vào trang
        HttpSession session = request.getSession();
        if (session.getAttribute("sqlStatement") == null) {
            session.setAttribute("sqlStatement", "");
        }
        if (session.getAttribute("sqlResult") == null) {
            session.setAttribute("sqlResult", "");
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult;

        // --- cấu hình DB: chỉnh user/pass cho đúng máy bạn ---
        final String JDBC_URL =
                "jdbc:mysql://localhost:3306/newdb"
                        + "?useSSL=false&allowPublicKeyRetrieval=true"
                        + "&serverTimezone=UTC&characterEncoding=UTF-8";
        final String JDBC_USER = "root";        // đổi theo của bạn
        final String JDBC_PASS = "Thuong@123";

        if (sqlStatement == null || sqlStatement.trim().isEmpty()) {
            sqlResult = "<p>Please enter an SQL statement.</p>";
        } else {
            sqlStatement = sqlStatement.trim();
            try {
                // Load driver (an toàn cho các môi trường cũ)
                Class.forName("com.mysql.cj.jdbc.Driver");

                try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                     Statement statement = connection.createStatement()) {

                    // phân loại câu lệnh
                    String head = sqlStatement.length() >= 6
                            ? sqlStatement.substring(0, 6).toLowerCase()
                            : sqlStatement.toLowerCase();

                    if (head.startsWith("select")) {
                        try (ResultSet rs = statement.executeQuery(sqlStatement)) {
                            sqlResult = SQLUtil.getHtmlTable(rs);
                        }
                    } else {
                        int affected = statement.executeUpdate(sqlStatement);
                        sqlResult = (affected == 0)
                                ? "<p>The statement executed successfully.</p>"
                                : "<p>The statement executed successfully.<br>" + affected + " row(s) affected.</p>";
                    }
                }
            } catch (ClassNotFoundException e) {
                sqlResult = "<p>Error loading the database driver:<br>" + e.getMessage() + "</p>";
            } catch (SQLException e) {
                sqlResult = "<p>Error executing the SQL statement:<br>" + e.getMessage() + "</p>";
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
