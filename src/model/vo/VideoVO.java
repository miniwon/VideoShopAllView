package model.vo;

public class VideoVO {
	
	int vNo;					// 비디오번호
	String genre;				// 장르
	String vName;			// 비디오명
	String director;				// 감독
	String actor;					// 배우
	String exp;					// 설명
	
	public int getvNo() {
		return vNo;
	}
	public void setvNo(int vNo) {
		this.vNo = vNo;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getvName() {
		return vName;
	}
	public void setvName(String vName) {
		this.vName = vName;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	

	
	

}
