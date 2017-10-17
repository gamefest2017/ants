package com.ibm.sk.ant;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.ibm.sk.ant.facade.AntFactory;

public class AntLoader {

	public static AntFactory[] getImplementations() {
		Reflections reflections = new Reflections("");
		Set<Class<? extends AntFactory>> classes = reflections.getSubTypesOf(AntFactory.class);

		List<AntFactory> ret = new ArrayList<>();

		for (Class<? extends AntFactory> it : classes) {
			try {
				ret.add(it.getConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}

		return ret.stream().toArray(AntFactory[]::new);
	}

//	public static void load1(JarFile jarfile) {
//		try {
//			URL[] urls = new URL [] {new URL("jar:file:" + jarfile + "!/")};
//			URLClassLoader ucl = URLClassLoader.newInstance(urls);
//			Enumeration<JarEntry> e = jarfile.entries();
//			
//			while (e.hasMoreElements()) {
//			    JarEntry je = e.nextElement();
//			    if(je.isDirectory() || !je.getName().endsWith(".class")){
//			        continue;
//			    }
//
//			    // -6 because of .class
//			    String className = je.getName().substring(0, je.getName().length()-6);
//			    className = className.replace('/', '.');
//			    Class<?> c = ucl.loadClass(className);
//			}
//			
//		} catch (MalformedURLException | ClassNotFoundException ex) {
//			ex.printStackTrace();
//		}
//	}
//	
//	public static void load(JarFile jarfile) {
//		try {
//			URL[] urls = new URL [] {new URL("jar:file:" + jarfile + "!/")};
//			URLClassLoader ucl = URLClassLoader.newInstance(urls);
//			Enumeration<JarEntry> e = jarfile.entries();
//			
//			while (e.hasMoreElements()) {
//				JarEntry je = e.nextElement();
//				if (!je.isDirectory() && je.getName().endsWith(".class")) {
//					String className = je.getName().substring(0, je.getName().length() - 6).replace('/', '.');
//					ucl.loadClass(className);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
