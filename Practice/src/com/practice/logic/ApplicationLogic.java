package com.practice.logic;

import java.util.ArrayList;
import java.util.List;

import com.practice.beans.StudentBean;
import com.practice.utils.RandomUtil;

public class ApplicationLogic {
public List<StudentBean> getStudentData(){
		int limit = 20;
		List<StudentBean> beansArray = new ArrayList<StudentBean>(limit);
		StudentBean bean;
		int min=101;
		int max=105;
		for(int i=1; i<= limit; i++)
		{
			bean = new StudentBean();
			bean.setStudentName("Student "+i);
			bean.setMessageType(RandomUtil.randInt(min, max));
			bean.setRefferenceName(""+i);
			beansArray.add(bean);
		}
		return beansArray;
	}
}
