package	 view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import model.VideoDao;
import model.dao.CustomerDaoImpl;
import model.dao.VideoDaoImpl;
import model.vo.VideoVO;


public class VideoView extends JPanel 
{	
	//	member field
	JTextField	tfVideoNum, tfVideoTitle, tfVideoDirector, tfVideoActor;
	JComboBox	comVideoJanre;
	JTextArea	taVideoContent;

	JCheckBox	cbMultiInsert;
	JTextField	tfInsertCount;

	JButton		bVideoInsert, bVideoModify, bVideoDelete;

	JComboBox	comVideoSearch;
	JTextField	tfVideoSearch;
	JTable		tableVideo;
	
	VideoTableModel tbModelVideo;
	
	// 비즈니스 로직
	VideoDao model;
	
	

	//##############################################
	//	constructor method
	public VideoView(){
		addLayout(); 	// 화면설계
		initStyle();
		eventProc();
		connectDB();	// DB연결
	}
	
	public void connectDB(){	// DB연결
		
		try{
			model = new VideoDaoImpl();
		} catch (Exception e) {
			System.out.println("실패: " + e.getMessage());
		}
		
	}
	
	public void eventProc(){
		// 체크박스가 눌렸을 때 tfInseftCount 가 쓸수있게됨
		cbMultiInsert.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
			/*	if(cbMultiInsert.isSelected()){
					tfInsertCount.setEditable(true);
				}
				else
					tfInsertCount.setEditable(false);*/
				
				tfInsertCount.setEditable( cbMultiInsert.isSelected() );
			}						
		});	
		
		ButtonEventHandler btnHandler = new ButtonEventHandler();

		// 이벤트 등록
		bVideoInsert.addActionListener(btnHandler);
		bVideoModify.addActionListener(btnHandler);
		bVideoDelete.addActionListener(btnHandler);
		tfVideoSearch.addActionListener(btnHandler);
		// 검색한 열을 클릭했을 때
		
		tableVideo.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent ev){
				
				try{
					int row = tableVideo.getSelectedRow();
					int col = 0;	// 검색한 열을 클릭했을 때 클릭한 열의 비디오번호(0열이 비디오번호)
					// Object -> Integer -> int 형변환
					int vNum = ((Integer)tableVideo.getValueAt(row, col)).intValue();
					//JOptionPane.showMessageDialog(null, vNum);
					VideoVO vo = model.selectByVNum(vNum);
					tfVideoNum.setText(String.valueOf(vo.getvNo()));
					tfVideoTitle.setText(vo.getvName());
					tfVideoDirector.setText(vo.getDirector());
					tfVideoActor.setText(vo.getActor());
					taVideoContent.setText(vo.getExp());
//					comVideoJanre.setSelectedIndex(Arrays.asList(cbJanreStr).indexOf(vo.getGenre()));
					comVideoJanre.setSelectedItem(vo.getGenre());
				}catch(Exception ex){
					System.out.println("실패 : "+ ex.getMessage());
				}
				
			}
		});
	}		
	
	// 버튼 이벤트 핸들러 만들기
	class ButtonEventHandler implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			Object o = ev.getSource();
			
			if(o==bVideoInsert){  
				registVideo();					// 비디오 등록
			}
			else if(o==bVideoModify){  
				modifyVideo();					// 비디오 정보 수정
			}
			else if(o==bVideoDelete){  
				deleteVideo();					// 비디오 정보 삭제
			}
			else if(o==tfVideoSearch){
				searchVideo();					// 비디오 검색
			}
		}
	}
	
	// 입고 클릭시  - 비디오 정보 등록
	public void registVideo(){
		// JOptionPane.showMessageDialog(null, "입고");
		// 1. 사용자 입력값 얻어 오기
		String genre = (String)comVideoJanre.getSelectedItem();
		int count = Integer.parseInt(tfInsertCount.getText());
		String title = tfVideoTitle.getText();
		String director = tfVideoDirector.getText();
		String actor = tfVideoActor.getText();
		String exp = taVideoContent.getText();
		
		// 2. 1번의 값들을 VideoVO에 지정
		VideoVO vo = new VideoVO();
		vo.setGenre(genre);
		vo.setActor(actor);
		vo.setDirector(director);
		vo.setExp(exp);
		vo.setvName(title);
		
		// 3. 모델의 insertVideo() 호출
		try {
			model.insertVideo(vo, count);
			JOptionPane.showMessageDialog(null, "비디오 입고 완료");
		
		// 4. 화면 지우기
			clearText();
		} catch (Exception e) {
			System.out.println("비디오 입고 실패: " + e.getMessage());
		}
		


	}
	
	void clearText() {
		tfInsertCount.setText("1");
		tfVideoTitle.setText(null);
		tfVideoDirector.setText(null);
		tfVideoActor.setText(null);
		taVideoContent.setText(null);
		cbMultiInsert.setSelected(false);
		comVideoJanre.setSelectedIndex(0);
		tfInsertCount.setEditable(false);
		
		
		
	}
	
	public void initStyle(){   
		tfVideoNum.setEditable(false); // 입력하지 못하게 만듬.
		tfInsertCount.setEditable(false);
		
		tfInsertCount.setHorizontalAlignment(JTextField.RIGHT);
	}
	
	// 수정 클릭시 - 비디오 정보 수정
	public void modifyVideo(){
		// 1. 사용자 입력값 얻어 오기
		String genre 	= (String)comVideoJanre.getSelectedItem();
		String title 	= tfVideoTitle.getText();
		String director = tfVideoDirector.getText();
		String actor 	= tfVideoActor.getText();
		String exp 		= taVideoContent.getText();
		int vNum	 	= Integer.parseInt(tfVideoNum.getText()); 
		int count 		= 0;
		// 2. 1번의 값들을 VideoVO에 지정
		VideoVO vo = new VideoVO();
		vo.setGenre(genre);
		vo.setActor(actor);
		vo.setDirector(director);
		vo.setExp(exp);
		vo.setvName(title);
		vo.setvNo(vNum);
		
		// 3. 모델의 insertVideo() 호출
		try {
			count = model.modifyVideo(vo);
			JOptionPane.showMessageDialog(null, "비디오 수정 " + count + "개 완료");
		
		// 4. 화면 지우기
			clearText();
		} catch (Exception e) {
			System.out.println("비디오 수정 실패: " + e.getMessage());
		}
	}
	
	// 삭제 클릭시 - 비디오 정보 삭제
	public void deleteVideo(){
		int vNum	= Integer.parseInt(tfVideoNum.getText());
		int count	= 0; 
		
		try {
			count = model.deleteVideo(vNum);
			JOptionPane.showMessageDialog(null, "비디오 삭제 " + count + "개 완료");
			clearText();
		} catch (Exception e) {
			if (e.getMessage().contains("(PROJECT5.FK_STATUS_VNO) violated - child record found"))
			{
				JOptionPane.showMessageDialog(null, "이 비디오는 삭제가 불가능합니다");
			} else {
			System.out.println("비디오 삭제 실패: " + e.getMessage());
		}
		}
	}
	
	// 비디오현황검색
		public void searchVideo(){
			// 사용자가 선택하거나 입력한 값들 얻어 오기
//			String comSearch = (String)comVideoSearch.getSelectedItem();
//			String tfSearch = tfVideoSearch.getText();
			int idx = comVideoSearch.getSelectedIndex();
			String word = tfVideoSearch.getText();
			try {
//			tbModelVideo.data = model.selectVideo(comSearch, tfSearch);
			tbModelVideo.data = model.selectVideo(idx, word);
			tbModelVideo.fireTableDataChanged(); 	// 모델 쪽에서 데이터 변경을 뷰 쪽으로 신호
			JOptionPane.showMessageDialog(null, "검색 완료");
			} catch (Exception e) {
				System.out.println(
						"검색 실패: " + e.getMessage());
			}
			
		}
		
	//  화면설계 메소드
	public void addLayout(){
		//멤버변수의 객체 생성
		tfVideoNum = new JTextField();
		tfVideoTitle = new JTextField();
		tfVideoDirector = new JTextField();
		tfVideoActor = new JTextField();
		
		String []cbJanreStr = {"멜로","엑션","스릴","코미디"};
		comVideoJanre = new JComboBox(cbJanreStr);
		taVideoContent = new JTextArea();
		
		cbMultiInsert = new JCheckBox("다중입고");
		tfInsertCount = new JTextField("1",5);
	
		bVideoInsert = new JButton("입고");
		bVideoModify = new JButton("수정");
		bVideoDelete = new JButton("삭제");
		
		String []cbVideoSearch = {"제목","감독"};
		comVideoSearch = new JComboBox(cbVideoSearch);
		tfVideoSearch = new JTextField(15);
		
		tbModelVideo = new VideoTableModel();
		tableVideo = new JTable(tbModelVideo);
		// tableVideo.setModel(tbModelVideo);
		
		
		
		//************화면구성************
		//왼쪽영역
		JPanel p_west = new JPanel();
		p_west.setLayout(new BorderLayout());
			// 왼쪽 가운데
			JPanel p_west_center = new JPanel();	
			p_west_center.setLayout(new BorderLayout());
				// 왼쪽 가운데의 윗쪽
				JPanel p_west_center_north = new JPanel();
				p_west_center_north.setLayout(new GridLayout(5,2));
				p_west_center_north.add(new JLabel("비디오번호"));
				p_west_center_north.add(tfVideoNum);
				p_west_center_north.add(new JLabel("장르"));
				p_west_center_north.add(comVideoJanre);
				p_west_center_north.add(new JLabel("제목"));
				p_west_center_north.add(tfVideoTitle);
				p_west_center_north.add(new JLabel("감독"));
				p_west_center_north.add(tfVideoDirector);
				p_west_center_north.add(new JLabel("배우"));
				p_west_center_north.add(tfVideoActor);
				
				// 왼쪽 가운데의 가운데
				JPanel p_west_center_center = new JPanel();
				p_west_center_center.setLayout(new BorderLayout());
				// BorderLayout은 영역 설정도 해야함
				p_west_center_center.add(new JLabel("설명"),BorderLayout.WEST);
				p_west_center_center.add(taVideoContent,BorderLayout.CENTER);
			
			// 왼쪽 화면에 붙이기
			p_west_center.add(p_west_center_north,BorderLayout.NORTH);
			p_west_center.add(p_west_center_center,BorderLayout.CENTER);
			p_west_center.setBorder(new TitledBorder("비디오 정보입력"));
			
			// 왼쪽 아래
			JPanel p_west_south = new JPanel();		
			p_west_south.setLayout(new GridLayout(2,1));
			
			JPanel p_west_south_1 = new JPanel();
			p_west_south_1.setLayout(new FlowLayout());
			p_west_south_1.add(cbMultiInsert);
			p_west_south_1.add(tfInsertCount);
			p_west_south_1.add(new JLabel("개"));
			p_west_south_1.setBorder(new TitledBorder("다중입력시 선택하시오"));
			// 입력 수정 삭제 버튼 붙이기
			JPanel p_west_south_2 = new JPanel();
			p_west_south_2.setLayout(new GridLayout(1,3));
			p_west_south_2.add(bVideoInsert);
			p_west_south_2.add(bVideoModify);
			p_west_south_2.add(bVideoDelete);
			
			p_west_south.add(p_west_south_1);
			p_west_south.add(p_west_south_2);
		
		p_west.add(p_west_center,BorderLayout.CENTER);
		p_west.add(p_west_south, BorderLayout.SOUTH);   // 왼쪽부분완성
		
		//---------------------------------------------------------------------
		// 화면구성 - 오른쪽영역
		JPanel p_east = new JPanel();
		p_east.setLayout(new BorderLayout());
		
		JPanel p_east_north = new JPanel();
		p_east_north.add(comVideoSearch);
		p_east_north.add(tfVideoSearch);
		p_east_north.setBorder(new TitledBorder("비디오 검색"));
		
		p_east.add(p_east_north,BorderLayout.NORTH);
		p_east.add(new JScrollPane(tableVideo),BorderLayout.CENTER);
		// 테이블을 붙일때에는 반드시 JScrollPane() 이렇게 해야함 
		
		
		// 전체 화면에 왼쪽 오른쪽 붙이기
		setLayout(new GridLayout(1,2));
		
		add(p_west);
		add(p_east);
		
	}
	
	//화면에 테이블 붙이는 메소드 
	class VideoTableModel extends AbstractTableModel { 
		  
		ArrayList data = new ArrayList();
		
		String [] columnNames = {"비디오번호","제목","감독","배우"};

		    public int getColumnCount() { 
		        return columnNames.length; 
		    } 
		     
		    public int getRowCount() { 
		        return data.size(); 
		    } 

		    public Object getValueAt(int row, int col) { 
				ArrayList temp = (ArrayList)data.get( row );
		        return temp.get( col ); 
		    }
		    
		    public String getColumnName(int col){
		    	return columnNames[col];
		    }
	}
}


