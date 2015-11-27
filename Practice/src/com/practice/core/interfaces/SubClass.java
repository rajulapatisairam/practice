package com.practice.core.interfaces;

public class SubClass implements InterfaceA, InterfaceB {
	public void checkLocalVariable(){
		System.out.println("\n "+a);
	}

	@Override
	public void method() {
		System.out.println(" in Method() ");
		
	}
}
