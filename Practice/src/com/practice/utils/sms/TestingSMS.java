package com.practice.utils.sms;

public class TestingSMS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SMS160by2 sms160by2=new SMS160by2();
		
		try {
			sms160by2.login("9291522819", "13046433");
			sms160by2.send("9966929683", "testinMessage");
		} catch (NotAuthenticatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
