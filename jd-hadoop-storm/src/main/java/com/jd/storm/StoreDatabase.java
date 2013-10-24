package com.jd.storm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StoreDatabase {
    public static Connection connection;
    public static Statement stmt;
    static {
        String dbDriver = "com.mysql.jdbc.Driver";
        String dbUrl = "jdbc:mysql://10.10.224.151:3306/db_userjoy_0?useUnicode=true&amp;characterEncoding=UTF-8";
        String user = "snstWriter";
        String password = "123456";
        try {
            Class.forName(dbDriver);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insertRow(String word){
        int effectRows=0;
        String sql=String.format("insert into words(word)values('%s')", word);
        try{
            stmt=connection.createStatement();
            effectRows=stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("数据插入失败");
        }
        return effectRows;
    }
}