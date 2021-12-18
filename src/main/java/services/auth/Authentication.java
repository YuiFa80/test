package services.auth;//로그인, 로그아웃관련

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.Action;
import beans.Employees;

public class Authentication {
	private HttpServletRequest req;
	private DataAccessObject dao;
	
	private Employees emp;
	
	public Authentication(HttpServletRequest req) {
		this.req = req;
		dao = null;
		
	}
	
	public Action backController(int jobCode) { //Action -->>로그인 > 동적페이지, 로그아웃 > 정적페이지
		Action action = null;
		switch(jobCode) {
		case 1:
			action = this.accessCtl();
			break;
		case -1:
			action = this.accessOutCtl();
			break;
		default:
		}
		return action;
	}
	
	private Action accessCtl() {
		Action action = new Action();
		ArrayList<Employees> list = null;
		
		boolean tran = false;

		// 1.추출해서 bean에 저장 -->>Employees :: srCode, emCode, emPassword
		this.emp = new Employees();
		this.emp.setSrCode(this.req.getParameter("srCode"));
		this.emp.setEmCode(this.req.getParameter("emCode"));
		this.emp.setEmPassword(this.req.getParameter("emPassword"));
		this.emp.setAccessType(9);

		// 2.DAO 연동(insert(1,0) -->> boolean , select -->>ArrayList ,update(update된 갯수-->)
		dao = new DataAccessObject();
		Connection conn = dao.getConnection();
		dao.modifyTranStatus(conn, false);
		// 2-1::STORES에서 SRCode 존재여부 확인
		// 2-2::EMPLOYEES에서 EMCODE 존재여부 확인
		// 2-3::EMPLOYEES에서 PASSWORD 일치 여부확인 :: RETURN : 1 >> 2-4
		// 2-4::ACCESSHISTORY:INSERT::RETURN:1
		// 2-5::정보취합 -->>ARRAYLIST<EMPLOYEES>
		// 성공했으면 main.jsp
		// 실패했으면 index.html

		if (dao.isSrCode(conn, emp)) {
			
			if (dao.isEmployees(conn, emp)) {
				
				if (dao.insAccessHistory(conn, emp)) {
					if ((list = dao.getAccessInfo(conn, emp)) != null) {
						tran = true;
						req.setAttribute("accessInfo", list);
						
					}
				}
			}
		}

		action.setPage(tran ? "main.jsp" : "index.html");
		action.setRedirect(tran ? false : true);

		dao.setTransaction(conn, tran);
		dao.modifyTranStatus(conn, true);
		dao.closeConnection(conn);

		return action;
	}
	
	private Action accessOutCtl() {
		Action action = new Action();
		boolean tran = false;
		
		
		//1.추출해서 bean에 저장 -->>Employees :: srCode, emCode
		this.emp = new Employees();
		
		this.emp.setSrCode(this.req.getParameter("srCode"));
		this.emp.setEmCode(this.req.getParameter("emCode"));
		this.emp.setAccessType(-9);
		
		dao = new DataAccessObject();
		Connection conn = dao.getConnection();
		dao.modifyTranStatus(conn, false);
		
		if(dao.insAccessHistory(conn, emp)) {
			tran = true;
		}
		
		action.setRedirect(tran);
		action.setPage("index.html");
		
		return action;
	}
}
