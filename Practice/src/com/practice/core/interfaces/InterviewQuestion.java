package com.practice.core.interfaces;

interface A{
	int a = 10;
}

interface B{
	int a = 20;
}

class c implements A,B{
	public void display(){
		System.out.println(" A is : "+A.a);
	}
}



public class InterviewQuestion {

}
