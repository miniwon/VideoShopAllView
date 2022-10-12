package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.CustomerDao;
import model.vo.CustomerVO;

public class CustomerDaoImpl implements CustomerDao{

	final static String DRIVER	= "oracle.jdbc.driver.OracleDriver";
	final static String URL		= "jdbc:oracle:thin:@192.168.0.2:1521:xe";
	final static String USER	= "PROJECT5";
	final static String PASS	= "12345";
	
	public CustomerDaoImpl() throws Exception{
	 	// 1. 드라이버로딩
		Class.forName(DRIVER);
		System.out.println("1. 회원 드라이버 로딩 성공");
		
	}
	
	/*
	 * 사용자 입력값을 받아 DB에 저장하는 역할
	 */
	public void insertCustomer(CustomerVO vo) throws Exception{
		// 2. Connection 연결객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DriverManager.getConnection(URL, USER, PASS);
		
		// 3. SQL 문장
			String sql = "INSERT INTO CUSTOMER (TEL, NAME, TEL2, ADDRESS, EMAIL) "
					+ " VALUES (?, ?, ?, ?, ?) ";
			
		// 4. 전송 객체(PreparedStatement)
			ps = con.prepareStatement(sql);
			
		// 4-2. ? 세팅
			ps.setString(1, vo.getCustTel1());
			ps.setString(2, vo.getCustName());
			ps.setString(3, vo.getCustTel2());
			ps.setString(4, vo.getCustAddr());
			ps.setString(5, vo.getCustEmail());
		
		// 5. 전송
			ps.executeUpdate();
		} finally {
		
		// 6. 닫기
			ps.close();
			con.close();
		}

	}
	
	/*
	 * 메서드명	: selectByTel
	 * 인자		: 검색할 전화번호
	 * 리턴값		: 전화번호에 따른 고객 정보
	 * 역할		: 사용자가 입력한 전화번호를 받아서 해당하는 고객 정보를 리턴
	 */
	public CustomerVO selectByTel(String tel) throws Exception{
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;
		CustomerVO dao = new CustomerVO();
		
		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			
		// 3. SQL 문장 만들기
			String sql = " SELECT * FROM CUSTOMER WHERE TEL = ? ";

		// 4. 전송 객체
		ps = con.prepareStatement(sql);
		ps.setString(1, tel);
			
		// 5. 전송 - executeQuery()
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			dao.setCustName(rs.getString("NAME"));
			dao.setCustTel1(rs.getString("TEL"));
			dao.setCustTel2(rs.getString("TEL2"));
			dao.setCustAddr(rs.getString("ADDRESS"));
			dao.setCustEmail(rs.getString("EMAIL"));
		} return dao;
		} finally {
			ps.close();
			con.close();
		}
	}
	
	public int updateCustomer(CustomerVO vo) throws Exception{
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		
		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			
		// 3. SQL 문장
			String sql = " UPDATE CUSTOMER "
					+ " SET NAME = ?, TEL = ?, TEL2 = ?, ADDRESS = ?, EMAIL = ? "
					+ " WHERE TEL = ? ";
		
		// 4. 전송 객체
			ps = con.prepareStatement(sql);
		
		// ? 세팅
			ps.setString(1, vo.getCustName());
			ps.setString(2, vo.getCustTel1());
			ps.setString(3, vo.getCustTel2());
			ps.setString(4, vo.getCustAddr());
			ps.setString(5, vo.getCustEmail());
			ps.setString(6, vo.getCustTel1());
			
		// 5. 전송
			result = ps.executeUpdate();
		} finally {
		// 6. 닫기
			ps.close();
			con.close();
		}
		return result;
	}
}
