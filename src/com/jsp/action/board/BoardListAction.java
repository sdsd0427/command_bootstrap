package com.jsp.action.board;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.command.SearchCriteria;
import com.jsp.command.SearchCriteriaCommand;
import com.jsp.controller.HttpRequestParameterAdapter;
import com.jsp.service.BoardService;

public class BoardListAction implements Action {

	private BoardService boardService;
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "/board/list";
		SearchCriteria cri = null;
		
		try {
			//입력
			SearchCriteriaCommand command = HttpRequestParameterAdapter.execute(request, SearchCriteriaCommand.class);
			cri = command.toSearchCriteria();
			
			Map<String, Object> dataMap = boardService.getBoardList(cri);
			request.setAttribute("dataMap", dataMap);
			
			//출력
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}

}
