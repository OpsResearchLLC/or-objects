/* ***** BEGIN LICENSE BLOCK *****
 * 
 * Copyright (C) 2012 OpsResearch LLC (a Delaware company)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * ***** END LICENSE BLOCK ***** */

package com.opsresearch.orobjects.lib;

import java.util.Properties;

public class Native {
	private static Native singleton = null;

	public static boolean isInitialized() {
		return singleton != null;
	}
	
	public static boolean isCudaInitialized() {
		return false;
	}
	
	public static boolean isOpenclInitialized() {
		return false;
	}

	public static void initialize(Properties properties) {
		if (singleton != null)
			throw new LibError("The native library is already initialized");
		System.out.println("Initializing native extensions");

		singleton = new Native(properties);
	}

	public static void shutdown() {
		System.out.println("Shutting down native extensions");
		singleton = null;
	}

	public static Native instance() {
		if (singleton == null)
			throw new LibError("The native library is not initialized");
		return singleton;
	}

	private Native(Properties props) {
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		System.out.println("os.name: '"+osName+"'  os.arch: '"+osArch+"' os.version: '"+osVersion+"'");
		
		String jniLib = props.getProperty("com.opsresearch.orobjects.lib.native.jni-lib", "orobjects-jni");
		
		String osKey = osName+"/"+osArch+"/"+osVersion;
		String jniProp = "com.opsresearch.orobjects.lib.native.jni-lib."+osKey;
		
		jniLib = props.getProperty(jniProp, jniLib);
		System.out.println("Jni Lib '"+jniLib+"'");
		
		String libPath = System.getProperty("java.library.path");
		System.out.println("Lib Path '"+libPath+"'");
				
		System.loadLibrary(jniLib);
	}
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("com.opsresearch.orobjects.lib.native.jni-lib.Mac OS X/x86_64/10.7.3", "orobjects-jni");
		Native.initialize(props);
		Native.shutdown();
	}

}
