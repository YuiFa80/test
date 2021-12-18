package webpos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Action;
import services.auth.Authentication;


@WebServlet({"/Access","/AccessOut"})//@어너테이션,여러개 요청시 배열처리
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public FrontController() {
        super();
       
    }

	//get 속도 빠름
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//post방식과 틀리게 servers에서 server.xml의 63번줄에 인코딩 문구를 넣어야 제대로 된 인코딩이 됨
		this.doProcess(request, response);
		
	}

	//post 보안, 속도 느림
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); //post는 직접 왼쪽문구를 해야 인코딩이 됨
		this.doProcess(request, response);

		
		
	}
	//Get과 Post방식을 한번에 처리!!
	private void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Action action = new Action();
//		request.getContextPath(); >> /webpos
//		request.getRequestURI(); >> /webpos/Access ::상기부분(Path)만 빼면 발신 정보처를 알수있다.
//		request.getRequestURL(); >> http://localhost/webpos/Access
		String jobCode = request.getRequestURI().substring(request.getContextPath().length() + 1); // >>jobCode생성
		Authentication auth = null;

		if (jobCode.equals("Access")) {
			auth = new Authentication(request);
			action = auth.backController(1);

		} else if (jobCode.equals("AccessOut")) {

			auth = new Authentication(request);
			action = auth.backController(-1);

		} else {

		}

		if (action.isRedirect()) {
			response.sendRedirect(action.getPage());
			
		} else {

			RequestDispatcher dp = request.getRequestDispatcher(action.getPage()); 
			// 동적으로 사용할때 "RequestDispatcher"는 new는 하지않음!!import만
			
			dp.forward(request, response); // 컨테이너 내용을 다시 web server로 보냄(해야함!!)
		}
	}

}
