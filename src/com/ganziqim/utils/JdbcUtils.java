package ym.dao;

import java.sql.*;

public class JdbcUtils {
	private static String driverName;	//	驱动名
	private static String url;	//	ַ数据库地址
	private static String userName;	//	一般为root
	private static String password;		//	密码
	
	private JdbcUtils(){	//	这是一个工具类，不需要创建对象
		
	}
	
	static {
		init();
		
		try {
			Class.forName(driverName);	//加载驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void init(){	//	初始化参数
		driverName = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://localhost:3306/bookStore?useUnicode=true&characterEncoding=utf8";	//	？后的参数防止乱码
		userName = "root";
		password = "100677";
	}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, userName, password);
	}
	
	public static void closeAll(ResultSet rs,Statement st,Connection conn){
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}




















