package com.jsp.action.board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.command.BoardModifyCommand;
import com.jsp.controller.XSSHttpRequestParameterAdapter;
import com.jsp.dto.BoardVO;
import com.jsp.service.BoardService;

public class BoardModifyAction implements Action {

	private BoardService boardService;
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "redirect:/board/detail.do?bno=" + request.getParameter("bno");
		
		BoardModifyCommand boardReq = XSSHttpRequestParameterAdapter.execute(request, BoardModifyCommand.class, true);
		
		BoardVO board = boardReq.toBoardVO();
		
		board.setContent(request.getParameter("content"));
		
		boardService.modify(board);
		
		return url;
	}

}
