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

	public ArrayList selectVideo(int idx, String word) throws Exception {
		ArrayList data = new ArrayList();
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			String[] colNames = {"VNAME", "DIRECTOR"};
			String sql = null;
			// 3. SQL 문장
			// '%?%' 안에 공백 안 들어가게 조심, word가 없으면 알아서 LIKE '%%'로 들어가서 전체 검색
			sql = " SELECT VNO, VNAME, DIRECTOR, ACTOR FROM VIDEO WHERE " + colNames[idx] + " LIKE '%" + word + "%' " ;
			//				if (comSearch.equals("제목")) { sql = " SELECT VNO, VNAME, DIRECTOR, ACTOR FROM VIDEO WHERE VNAME LIKE ? "; }
			//				if (comSearch.equals("감독")) { sql = " SELECT VNO, VNAME, DIRECTOR, ACTOR FROM VIDEO WHERE DIRECTOR LIKE ? "; }
			ps = con.prepareStatement(sql);
			//			ps.setString(1, "%" + tfSearch + "%");
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
	} // selectVideo()

	/*
	 * 메서드명	: selectByVNum
	 * 인자		: 비디오 번호
	 * 리턴값		: 비디오 번호에 따른 비디오 정보
	 * 역할		: 비디오 번호를 넘겨받아 해당 비디오 번호의 비디오 정보를 리턴
	 */
	public VideoVO		selectByVNum(int vNum) throws Exception {
		// 2. 연결 객체 얻어 오기
		Connection con = null;
		PreparedStatement ps = null;
		VideoVO vo = new VideoVO();
		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			// 3. SQL 문장
			String sql = " SELECT * FROM VIDEO WHERE VNO = ? ";

			// 4. 전송 객체
			ps = con.prepareStatement(sql);
			ps.setInt(1, vNum);

			// 5. 전송
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				vo.setvNo(vNum);
				vo.setvName(rs.getString("VNAME"));
				vo.setGenre(rs.getString("GENRE"));
				vo.setExp(rs.getString("EXP"));
				vo.setDirector(rs.getString("DIRECTOR"));
				vo.setActor(rs.getString("ACTOR"));

			}
			return vo;
		} finally {
			ps.close();
			con.close();
		}
	} // selectByVNum()
	
	public int			modifyVideo(VideoVO vo) throws Exception {
		// 2. Connection 연결객체 얻어오기
		Connection con = null;
		PreparedStatement ps = null;
		int count;

		
		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			// 3. sql 문장 만들기
			String sql = " UPDATE VIDEO SET GENRE = ?, VNAME = ?, DIRECTOR = ?, ACTOR = ?, EXP = ? WHERE VNO = ?  ";

			// 4. sql 전송객체 (PreparedStatement)
			ps = con.prepareStatement(sql);

			// 4-2. ? 세팅
			ps.setString(1, vo.getGenre());
			ps.setString(2, vo.getvName());
			ps.setString(3, vo.getDirector());
			ps.setString(4, vo.getActor());
			ps.setString(5, vo.getExp());
			ps.setInt(6, vo.getvNo());

			// 5. sql 전송
			count = ps.executeUpdate();
			
		} finally {
			// 6. 닫기
			ps.close();
			con.close();
		} // finally
		return count;
	} // modifyVideo()
	
	public int			deleteVideo(int vNum) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		int count;

		
		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			// 3. sql 문장 만들기
			String sql = " DELETE FROM VIDEO WHERE VNO = ?  ";

			// 4. sql 전송객체 (PreparedStatement)
			ps = con.prepareStatement(sql);

			// 4-2. ? 세팅
			ps.setInt(1, vNum);

			// 5. sql 전송
			count = ps.executeUpdate();
			
		} finally {
			// 6. 닫기
			ps.close();
			con.close();
		} // finally
		return count;
	} // deleteVideo()

}
