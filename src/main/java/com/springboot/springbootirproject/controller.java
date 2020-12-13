package com.springboot.springbootirproject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.model.eachRecord;

import Classes.Document;
import IndexingLucene.MyIndexReader;
import PseudoRFSearch.PseudoRFRetrievalModel;
import SearchLucene.ExtractQuery;
//ctrl+shift+H 可以搜索jar包里的文件 ctrl+shift+r 搜索工程里的文件
@Controller
public class controller {
	@GetMapping("/")
	public ModelAndView home() {

		ModelAndView mv = new ModelAndView();
//		mv.addObject("name",name);
		mv.setViewName("home");
		return mv;
	}
	@GetMapping("search")
	public ModelAndView search(@RequestParam("input")String input) {
		if(input.length()==0) {
			String Error = "Please input something";
			ModelAndView empty = new ModelAndView();
			empty.addObject("error",Error);
			empty.setViewName("home");
			return empty;
		} else {
	//		StringBuilder sb = new StringBuilder();
			List<eachRecord> recordList = new ArrayList<eachRecord>();
			System.out.println(input);
			try {
			MyIndexReader myireader = new MyIndexReader();
			ExtractQuery extractQuery = new ExtractQuery(input);
			PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(myireader);
			
			long startTime = System.currentTimeMillis();
			Classes.Query query = extractQuery.getQuery();
			List<Classes.Document> rankedList = PRFSearchModel.RetrieveQuery(query, 5, 5, 0.4);
			if(rankedList != null) {
	//			StringBuilder sb = new StringBuilder();
				int rank = 1;
				for(Document doc : rankedList) {				
					System.out.println(doc);
					String Name= doc.getRestName();
					String url = doc.getRestUrl();
					String Content = doc.getOriginalContent();
					eachRecord record =new eachRecord();
					record.setRname(Name);
					record.setRurl(url);
					record.setRcontent(Content); 
					recordList.add(record);
	//				sb.append(Name+'\n'+url+'\n'+Content+'\n'+'\n');
				}
			}
		long endTime = System.currentTimeMillis(); 
		
		eachRecord userinput = new eachRecord();
		userinput.setRname(input);
		recordList.add(userinput);
		
		// 3. Output running time
		System.out.println("Queries search time: " + (endTime - startTime) / 60000.0 + " min");
		double time = (endTime - startTime) / 1000.0;
		eachRecord queryTime = new eachRecord();
		queryTime.setRcontent(String.valueOf(time));
		recordList.add(queryTime);
		
		myireader.close();
		}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		ModelAndView mv = new ModelAndView();
		mv.addObject("input", recordList); // put all information we want into recordList
		                                  
		mv.setViewName("search"); // and render it into corresponding view 
		return mv;
	}
	}

}
