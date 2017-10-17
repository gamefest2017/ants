package com.ibm.sk.ant;

import com.ibm.sk.ant.facade.AntFactory;

public class ReflectionTest {
	
	public static void main(String [] args) {
		AntFactory [] ants1 = AntLoader.getImplementations();
		System.out.println("Ants size: " + ants1.length);
		
//		try {
//			AntLoader.load(new JarFile("./testlib/TestAntProject.jar"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		AntFactory [] ants2 = AntLoader.getImplementations();
//		System.out.println("Ants size: " + ants2.length);
	}

}
