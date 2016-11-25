package com.ganziqim.utils;

import java.sql.*;
import java.util.*;

public class Dao {
	protected boolean pmdKnownBroken = false;
	private Connection conn;

	public Dao(Connection conn) {
        this.conn = conn;
	}

	public Dao(boolean pmdKnownBroken, Connection conn) {
		this.pmdKnownBroken = pmdKnownBroken;
        this.conn = conn;
	}

	public List<Map<String, Object>> excuteQuery(String sql, Object[] params) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			stmt = conn.prepareStatement(sql);

			// 填充参数
			fillStatement(stmt, params);

			rs = stmt.executeQuery(); // 得到结果集

			resultList = resultSetHander(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	public int excuteUpdate(String sql, Object[] params) {
		PreparedStatement stmt = null;
		int rs = 0;

		try {
			conn.setAutoCommit(false); // 关闭事务
			stmt = conn.prepareStatement(sql); // 创建PreparedStatement对象
			fillStatement(stmt, params); // 填充参数

			// 执行查询
			rs = stmt.executeUpdate();

			// 提交事务
			conn.commit();
			// 把事务设置为原来的状态
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// 捕获异常时事务回滚
			try {
				conn.rollback();
				if (!conn.getAutoCommit())
					conn.setAutoCommit(true);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}

		return rs;
	}

	private List<Map<String, Object>> resultSetHander(ResultSet rs) throws SQLException {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		// 遍历结果集
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();

			for (int i = 0; i < cols; i++)
				map.put(rsmd.getColumnLabel(i + 1), rs.getObject(i + 1));

			resultList.add(map);
		}

		return resultList;
	}

	private void fillStatement(PreparedStatement stmt, Object[] params) throws SQLException {
		// 检测参数的个数是否合法，但是有的数据库驱动不支持 stmt.getParameterMetaData()这个方法。
		// 因此我们有一个一个pmdKnownBroken 变量来标识当前数据驱动是否支持该方法的调用。
		ParameterMetaData pmd = null;

		if (!pmdKnownBroken) {
			pmd = stmt.getParameterMetaData();
			int paramsCount = params == null ? 0 : params.length;
			int stmtCount = pmd.getParameterCount();

			if (stmtCount != paramsCount) {
				System.out.println("stmtCount:" + stmtCount + ",paramsCount:"
						+ paramsCount);
				throw new SQLException("Wrong number of parameters: expected "
						+ stmtCount + ", was given " + paramsCount);
			}
		}

		if (params == null)
			return;

		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) { // 数据库下标是从1开始而不是0
				stmt.setObject(i + 1, params[i]);
			} else {
				int sqlType = Types.VARCHAR;

				if (!pmdKnownBroken) {
					try {
						sqlType = pmd.getParameterType(i + 1);
					} catch (SQLException e) {
						pmdKnownBroken = true;
					}
				}

				stmt.setNull(i + 1, sqlType);
			}
		}
	}

	public int[] batch(String sqlTemplate,List<Object[]> list) {
		PreparedStatement ps = null;
		int[] rs = null;

		try{
			ps = conn.prepareStatement(sqlTemplate);

			conn.setAutoCommit(false);

			for(int i=0;i<list.size();i++){
				Object[] os = list.get(i);
				for(int j = 0;j<os.length;j++)
					ps.setObject(j+1, os[j]);

				ps.addBatch();
			}

			rs = ps.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch(SQLException e){
			e.printStackTrace();
			try{
				conn.rollback();conn.setAutoCommit(true);
			}catch(SQLException e1){
				e1.printStackTrace();
			}
		}
		return rs;
	}
}
