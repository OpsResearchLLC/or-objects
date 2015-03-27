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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "or_lib_SerializedObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializedObject<T> {

	@XmlElement(name = "bytes")
	private final byte[] _bytes;

	public SerializedObject(T obj) throws IOException, NoSuchAlgorithmException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.close();
		baos.close();
		_bytes = baos.toByteArray();
	}
	
	public SerializedObject(T obj, int sizeEstimate) throws IOException, NoSuchAlgorithmException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(sizeEstimate);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.close();
		baos.close();
		_bytes = baos.toByteArray();
	}

	@SuppressWarnings("unchecked")
	public T getInstance() throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(_bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (T) ois.readObject();
	}

	public byte[] getBytes(){
		return _bytes;
	}

}
