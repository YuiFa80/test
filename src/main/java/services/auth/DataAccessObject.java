package services.auth;

import java.sql.*;
import java.util.ArrayList;

import beans.Employees;

public class DataAccessObject {
	//1.ojdbc8를 tomcat의 lib에 넣는다.
	
	ResultSet rs;
	PreparedStatement psmt;

	DataAccessObject() {
		
		rs = null;
		psmt = null;
		this.getConnection();
	}

	
	// Driver Loading & Create Connection
	Connection getConnection() { //return타입이기에 connection를 파라메타에 넣으면 안됨 [#아래 메소드와 비교!!]
		Connection connection = null; //2.connection함
//		String url = "jdbc:oracle:thin:@14.37.242.53:1521:XE";
//		String user = "HANYUI";
//		String passwd = "99172181";
		String[] total = {"jdbc:oracle:thin:@14.37.242.53:1521:XE","HANYUI","99172181"};

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver"); // 3-1.driver등록
			connection = DriverManager.getConnection(total[0], total[1], total[2]); // 3-2.connection 얻기

			System.out.println("OracleDateBase 연결 성공");
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("OracleDateBase 연결 실패");
		}
		return connection;
		
	}

	// Transaction상태변경
	void modifyTranStatus(Connection connection, boolean status) {//return타입이기아니기에 connection를 파라메타에 넣음 [#별도 선언 필요無]

		try {
			if (connection != null && connection.isClosed()) {
				connection.setAutoCommit(status);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

	// Transaction처리
	void setTransaction(Connection connection, boolean tran) {

		try {
			if (connection != null && connection.isClosed()) {
				if (tran) {
					connection.commit();
				} else {
					connection.rollback();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// Close Connection
	void closeConnection(Connection connection) {

		try {
			if (connection != null && connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

	/* convertToBoolean */
	boolean convertToBoolean(int value) {
		return (value > 0) ? true : false;
	}

//=============================================================================

	/* isSeCode */
	boolean isSrCode(Connection connection, Employees emp) {

		ResultSet rs = null;
		boolean result = false;

		String sql = "SELECT COUNT(*) FROM SR WHERE SR_CODE = ?";

		try {

			this.psmt = connection.prepareStatement(sql);
			this.psmt.setNString(1, emp.getSrCode());

			rs = this.psmt.executeQuery();// query(sql)를 날릴때 <DML-->>excuteUpdate>

			while (rs.next()) {// 읽을것이 있다 next 없다면 false

				result = this.convertToBoolean(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!rs.isClosed())
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/* isEmCode && comparePassword */
	boolean isEmployees(Connection connection, Employees emp) {
		
		ResultSet rs = null;
		boolean result = false;
		
		String sql = "SELECT COUNT(*) FROM EM WHERE EM_CODE = ? AND EM_PASSWORD = ?";
		
		try {
			this.psmt = connection.prepareStatement(sql);
			this.psmt.setNString(1, emp.getEmCode());
			this.psmt.setNString(2, emp.getEmPassword());

			rs = this.psmt.executeQuery();
			
			while (rs.next()) {
				
				result = this.convertToBoolean(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!rs.isClosed())
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	/* insAccessHistory */
	boolean insAccessHistory(Connection connection, Employees aH) {

		boolean result = false;

		String query = "INSERT INTO AH(AH_SRCODE, AH_EMCODE, AH_ACCESSTIME, AH_ACCESSTYPE) "
					+ "VALUES(?,?,DEFAULT,?)";

		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, aH.getSrCode());
			psmt.setNString(2, aH.getEmCode());
			psmt.setInt(3, aH.getAccessType());
			
			result = this.convertToBoolean(psmt.executeUpdate());
			
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (!psmt.isClosed()) {
					psmt.isClosed();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/* getAccessInfo */
	ArrayList<Employees> getAccessInfo(Connection connection, Employees emp){
		ArrayList<Employees> list = new ArrayList<Employees>();
		ResultSet rs = null;
		/* 상점명(상점코드) 직원명(직원코드) 최근접속기록 */
		String sql = "SELECT SRCODE, SRNAME, EMCODE, EMNAME, ACCESSTIME FROM ACCESSINFO "
				+ "WHERE ACCESSTIME = (SELECT TO_CHAR(MAX(AH_ACCESSTIME), 'YYYY-MM-DD HH24:MI:SS') "
				+ "                     FROM ACCESSHISTORY "
				+ "						WHERE AH_SRCODE=? AND AH_EMCODE=?)";
		
		try {
			this.psmt = connection.prepareStatement(sql);
			this.psmt.setNString(1, emp.getSrCode());
			this.psmt.setNString(2, emp.getEmCode());
			
			rs = this.psmt.executeQuery();
			while(rs.next()) {
				Employees em = new Employees();
				em.setSrCode(rs.getNString("SRCODE"));
				em.setSrName(rs.getNString("SRNAME"));
				em.setEmCode(rs.getNString("EMCODE"));
				em.setEmName(rs.getNString("EMNAME"));
				em.setDate(rs.getNString("ACCESSTIME"));
				
				list.add(em);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try { if(!rs.isClosed()) rs.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return list;
	}
	

}
