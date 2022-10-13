package model;

import java.util.ArrayList;

public interface RentDao {

	// 대여
	public int rentVideo(String tel, int vnum) throws Exception;
	
	// 반납
	public int returnVideo(int vnum) throws Exception;
	
	// 미납목록검색
	public ArrayList selectList() throws Exception;
	
	// 전화번호 입력 후 엔터를 누르면 고객명 출력
	public String selectByTel(String tel) throws Exception;
	
}