package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.RentDao;


public class RentDaoImpl implements RentDao {

	final static String DRIVER	= "oracle.jdbc.driver.OracleDriver";
	final static String URL		= "jdbc:oracle:thin:@192.168.0.2:1521:xe";
	final static String USER	= "PROJECT5";
	final static String PASS	= "12345";	

	public RentDaoImpl() throws Exception{
		// 1. 드라이버로딩
		Class.forName(DRIVER);
		System.out.println("3. 현황 드라이버 로딩 성공");
	}

	/*
	 * 메서드명	: rentVideo
	 * 인자		: 고객 전화번호, 비디오 번호
	 * 리턴값		: 없음
	 * 역할		: 입력받은 전화번호와 입력받은 비디오 번호를 이용하여 대여 현황 생성
	 */
	@Override
	public int rentVideo(String tel, int vNum) throws Exception {
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;
		String returnOX = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			// 3. SQL 문장 만들기
			String sql;

			sql = " SELECT RSTATUS FROM STATUS WHERE VNO = ? AND RSTATUS = 'X' ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, vNum);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				//	returnOX = rs.getString("RSTATUS");


				//if (returnOX.equals("X")) {
				//	JOptionPane.showMessageDialog(null, "대여 중인 비디오입니다");
				return -1;
			} else {

				sql = " INSERT INTO STATUS(RNO, TEL, VNO, RDATE, RSTATUS) "
						+ " VALUES ( SEQ_STATUS_RNO.NEXTVAL, ?, ?, SYSDATE, ? )";

				// 4. 전송 객체
				ps = con.prepareStatement(sql);
				ps.setString(1, tel);
				ps.setInt(2, vNum);
				ps.setString(3, "X");

				// 5. 전송
				int result = ps.executeUpdate();
				return result;
			}
		}  finally {
			// 6. 닫기
			ps.close();
			con.close();
		}
	}

	/*
	 * 메서드명	: returnVideo
	 * 인자		: 비디오 번호
	 * 리턴값		: 없음
	 * 역할		: 입력받은 비디오 번호를 이용하여 반납 처리
	 */	
	@Override
	public int returnVideo(int vNum) throws Exception {
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			String sql;
			sql = " SELECT RSTATUS FROM STATUS WHERE VNO = ? AND RSTATUS = 'X' ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, vNum);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				// 3. SQL 문장
				sql = " UPDATE STATUS SET RSTATUS = 'O' "
						+ " WHERE VNO = ? AND RSTATUS = 'X' ";

				// 4. 전송 객체
				ps = con.prepareStatement(sql);

				// ? 세팅
				ps.setInt(1, vNum);

				// 5. 전송
				return ps.executeUpdate();

			} else {
				return -1;

			}}  finally {
				// 6. 닫기
				ps.close();
				con.close();
			}			

	}


	/*
	 * 메서드명	: selectList
	 * 인자		: 없음
	 * 리턴값		: 목록 전체
	 * 역할		: 
	 */
	@Override
	public ArrayList selectList() throws Exception {
		ArrayList data = new ArrayList();

		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			String sql = null;
			// 3. SQL 문장
			sql     = " SELECT  V.VNO AS 비디오번호, V.VNAME AS 제목, C.NAME AS 고객명, C.TEL AS 전화번호, S.RDATE + 3 AS 반납예정일, '미납' AS 반납여부 "
					+ " FROM    CUSTOMER C INNER JOIN STATUS S "
					+ "         ON C.TEL = S.TEL "
					+ "         INNER JOIN VIDEO V "
					+ "         ON S.VNO = V.VNO "
					+ " WHERE   S.RSTATUS = 'X' " ;

			ps = con.prepareStatement(sql);

			// 4. 전송 객체

			// 5. 전송
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList temp = new ArrayList();
				temp.add(rs.getInt("비디오번호"));
				temp.add(rs.getString("제목"));
				temp.add(rs.getString("고객명"));
				temp.add(rs.getString("전화번호"));
				temp.add(rs.getString("반납예정일"));
				temp.add(rs.getString("반납여부"));
				data.add(temp);
			};
			return data;
		} finally {
			ps.close();
			con.close();
		}
	} // selectList()

	@Override
	public String selectByTel(String tel) throws Exception {
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;
		String nameOutput = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			String sql = " SELECT NAME FROM CUSTOMER WHERE TEL = ? ";

			ps = con.prepareStatement(sql);
			ps.setString(1, tel);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				nameOutput = rs.getString("NAME");
			} return nameOutput;
		} finally {
			ps.close();
			con.close();
		}
	}


}
