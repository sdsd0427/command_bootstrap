package com.jsp.command;

import com.jsp.dto.PdsVO;

public class PdsModifyCommand {
	private String pno;          // 게시판번호
	private String title="";     // 제목
	private String writer;	  // 작성자 (회원)
	private String content="";   // 내용 (html)
	
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public PdsVO toPdsVO() {
		PdsVO pds = new PdsVO();
		
		pds.setPno(Integer.parseInt(this.pno));
		pds.setTitle(this.title);
		pds.setContent(this.content);
		pds.setWriter(this.writer);
		
		return pds;
		
	}
	
}
