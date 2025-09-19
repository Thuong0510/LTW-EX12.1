package murach.controller;

import java.sql.*;

public class SQLUtil {

    public static String getHtmlTable(ResultSet results) throws SQLException {
        StringBuilder htmlTable = new StringBuilder();
        ResultSetMetaData rsmd = results.getMetaData();
        int columnCount = rsmd.getColumnCount();

        htmlTable.append("<table>");

        // Thêm header row
        htmlTable.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlTable.append("<th>")
                    .append(rsmd.getColumnName(i))
                    .append("</th>");
        }
        htmlTable.append("</tr>");

        // Thêm các dòng dữ liệu
        while (results.next()) {
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                String value = results.getString(i);
                if (value == null) value = ""; // tránh null
                htmlTable.append("<td>")
                        .append(value)
                        .append("</td>");
            }
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}
