package com.inspire.abstestbase;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.jfree.util.Log;
import org.testng.IAttributes;
import org.testng.collections.Maps;

/**
 * @author sachi
 *
 */
public class MyAttributes implements IAttributes {

	private static final long serialVersionUID = 6701159979281335152L;
	private Map<String, Object> m_attributes = Maps.newHashMap();
	private ArrayList<String> tracerIds = new ArrayList<String>();

	@Override
	public Object getAttribute(String name) {
		return m_attributes.get(name);
	}

	@Override
	public Set<String> getAttributeNames() {
		return m_attributes.keySet();
	}

	@Override
	public void setAttribute(String name, Object value) {
		m_attributes.put(name, value);
	}

	@Override
	public Object removeAttribute(String name) {
		return m_attributes.remove(name);
	}

	public void addTracerId(String tracerId) {
		tracerIds.add(tracerId);
	}

	public ArrayList<String> getTracerIdList() {
		return tracerIds;
	}

	public void clearAllAttributes() {
		tracerIds=new ArrayList<String>();
		m_attributes.clear();
	}

}
