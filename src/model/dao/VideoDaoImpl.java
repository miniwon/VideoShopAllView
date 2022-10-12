package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.VideoDao;
import model.vo.VideoVO;

public class VideoDaoImpl implements VideoDao {

	final static String DRIVER = "oracle.jdbc.driver.OracleDriver";
	final static String URL = "jdbc:oracle:thin:@192.168.0.2:1521:xe";
	final static String USER = "PROJECT5";
	final static String PASS = "12345";

	public VideoDaoImpl() throws Exception {
		// 1. 드라이버로딩
		Class.forName(DRIVER);
		System.out.println("2. 비디오 드라이버 로딩 성공");

	}

	public void insertVideo(VideoVO vo, int count) throws Exception {
		// 2. Connection 연결객체 얻어오기
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			// 3. sql 문장 만들기
			String sql = " INSERT INTO VIDEO (VNO, GENRE, VNAME, DIRECTOR, ACTOR, EXP) "
					+ " VALUES ( SEQ_VIDEO_VNO.NEXTVAL, ?, ?, ?, ?, ?)  ";

			// 4. sql 전송객체 (PreparedStatement)
			ps = con.prepareStatement(sql);

			// 4-2. ? 세팅
			ps.setString(1, vo.getGenre());
			ps.setString(2, vo.getvName());
			ps.setString(3, vo.getDirector());
			ps.setString(4, vo.getActor());
			ps.setString(5, vo.getExp());

			// 5. sql 전송
			for (int i = 0; i < count; i++) {
				ps.executeUpdate();
			} // for
		} finally {
			// 6. 닫기
			ps.close();
			con.close();
		} // finally

	}

	public ArrayList selectVideo(String comSearch, String tfSearch) throws Exception {
		ArrayList data = new ArrayList();
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			String sql = null;
			// 3. SQL 문장
			if (tfSearch.equals("")) {
				sql = " SELECT VNO, VNAME, DIRECTOR, ACTOR FROM VIDEO ";
				ps = con.prepareStatement(sql);
			} else {
				if (comSearch.equals("제목")) { sql = " SELECT VNO, VNAME, DIRECTOR, ACTOR FROM VIDEO WHERE VNAME LIKE ? "; }
				if (comSearch.equals("감독")) { sql = " SELECT VNO, VNAME, DIRECTOR, ACTOR FROM VIDEO WHERE DIRECTOR LIKE ? "; }
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + tfSearch + "%");
			} 
			// 4. 전송 객체
//			ps = con.prepareStatement(sql);

			// 5. 전송
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList temp = new ArrayList();
				temp.add(rs.getInt("VNO"));
				temp.add(rs.getString("VNAME"));
				temp.add(rs.getString("DIRECTOR"));
				temp.add(rs.getString("ACTOR"));
				data.add(temp);
			};
			return data;
		} finally {
			ps.close();
			con.close();
		}
	}

}
