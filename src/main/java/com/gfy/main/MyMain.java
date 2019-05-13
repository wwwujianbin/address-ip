package com.gfy.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jianbin
 * @date 2019/05/12
 */
public class MyMain {
    public static void main(String[] args) {
        // 声明Connection对象
        Connection con = null;
        ResultSet rs = null;
        if (args.length < 4) {
            System.out.println("-----------------输入参数异常");

        } else {
            String ip = args[0];
            String port = args[1];
            String username = args[2];
            String password = args[3];
            String limit = args[4];

            // 驱动程序名
            String driver = "com.mysql.jdbc.Driver";
            // URL指向要访问的数据库名mydata
            String url = String.format("jdbc:mysql://%s:%s/pvsa", ip, port);
            // 遍历查询结果集
            try {
                // 加载驱动程序
                Class.forName(driver);
                // 1.getConnection()方法，连接MySQL数据库！！
                con = DriverManager.getConnection(url, username, password);
                if (!con.isClosed())
                    System.out.println("Succeeded connecting to the Database!");
                // 2.创建statement类对象，用来执行SQL语句！！
                Statement statement = con.createStatement();
                // 要执行的SQL语句
                String sql = String.format(
                    "SELECT USER_NAME, LOGIN_TIME ,LOGIN_IP  FROM sa_log_login ORDER BY login_time DESC limit %s",
                    limit);
                // 3.ResultSet类，用来存放获取的结果集！！
                rs = statement.executeQuery(sql);
                System.out.println("-----------------");
                System.out.println("执行结果如下所示:");
                System.out.println("-----------------");
                System.out.println("用户" + "\t" + "登录时间" + "\t" + "登录IP" + "\t" + "所属地区");
                System.out.println("-----------------");

                String userName = "";
                String loginTime = "";
                String loginIP = "";
                String loginArea = "-";
                while (rs.next()) {
                    // 获取userName这列数据
                    userName = rs.getString("USER_NAME").trim();
                    // 获取loginTime这列数据
                    loginTime = rs.getString("LOGIN_TIME").trim();
                    loginIP = rs.getString("LOGIN_IP").trim();
                    if (loginIP != null && loginIP != "" && !"127.0.0.1".equals(loginIP)) {
                        loginArea = GetAddressByIp.GetAddressByIp(loginIP);
                    }

                    // 输出结果
                    System.out.println(userName + "\t" + loginTime + "\t" + loginIP + "\t" + loginArea);
                }

            } catch (ClassNotFoundException e) {
                // 数据库驱动类异常处理
                System.out.println("Sorry,can`t find the Driver!");
                e.printStackTrace();
            } catch (SQLException e) {
                // 数据库连接失败异常处理
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                System.out.println("数据库数据成功获取！！");
                if (null != rs) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (null != con) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }

    }

}
