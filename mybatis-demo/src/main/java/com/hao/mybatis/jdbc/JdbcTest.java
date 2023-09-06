package com.hao.mybatis.jdbc;

import java.sql.*;

/**
 * @author xuh
 * @date 2023/9/6
 */
public class JdbcTest {

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到渠道类，加载驱动失败");
            e.printStackTrace();
        }

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);

        String sql = "select * from user";

        // 1.执行静态语句，通常使用Statement实列实现
        Statement st = conn.createStatement();
        // 2.执行动态语句，通常通过preparedStatement实现
        PreparedStatement ps = conn.prepareStatement(sql);
        // 3.执行数据库存储过程，通常通过CallableStatement实现
        CallableStatement cs = conn.prepareCall("{CALL mysp()}");

        // 执行查询
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getObject("name"));
        }

        // 关闭连接
        conn.close();
    }
}
