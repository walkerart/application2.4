package org.collectionspace.chain.csp.persistence.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.collectionspace.bconfigutils.bootstrap.BootstrapConfigLoadFailedException;
import org.collectionspace.chain.csp.persistence.services.ReturnedDocument;
import org.collectionspace.chain.csp.persistence.services.ServicesConnection;
import org.collectionspace.chain.csp.persistence.services.ServicesStorage;
import org.collectionspace.csp.api.persistence.ExistException;
import org.collectionspace.chain.util.json.JSONUtils;
import org.dom4j.Node;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class TestServiceThroughAPI extends ServicesBaseClass {
	// XXX refactor
	private JSONObject getJSON(String in) throws IOException, JSONException {
		String path=getClass().getPackage().getName().replaceAll("\\.","/");
		InputStream stream=Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"/"+in);
		System.err.println(path);
		assertNotNull(stream);
		String data=IOUtils.toString(stream);
		stream.close();		
		return new JSONObject(data);
	}

	// XXX refactor
	@SuppressWarnings("unchecked")
	private void deleteAll() throws Exception {
		ReturnedDocument all=conn.getXMLDocument(RequestMethod.GET,"collectionobjects/");
		if(all.getStatus()!=200)
			throw new ConnectionException("Bad request during identifier cache map update: status not 200");
		List<Node> objects=all.getDocument().selectNodes("collection-object-list/collection-object-list-item");
		for(Node object : objects) {
			String csid=object.selectSingleNode("csid").getText();
			conn.getNone(RequestMethod.DELETE,"collectionobjects/"+csid,null);
		}
	}
	
	@Before public void checkServicesRunning() throws ConnectionException, BootstrapConfigLoadFailedException {
		setup();
	}
	
	@Test public void testObjectsPut() throws Exception {
		deleteAll();
		ServicesStorage ss=new ServicesStorage(base+"/cspace-services/");
		String name=ss.autocreateJSON("collection-object",getJSON("obj3.json"));
		JSONObject js=ss.retrieveJSON("collection-object/"+name);
		assertTrue(JSONUtils.checkJSONEquiv(js,getJSON("obj3.json")));
	}

	@Test public void testObjectsPost() throws Exception {
		deleteAll();
		ServicesStorage ss=new ServicesStorage(base+"/cspace-services/");
		String name=ss.autocreateJSON("collection-object",getJSON("obj3.json"));
		ss.updateJSON("collection-object/"+name,getJSON("obj4.json"));
		JSONObject js=ss.retrieveJSON("collection-object/"+name);
		assertTrue(JSONUtils.checkJSONEquiv(js,getJSON("obj4.json")));
	}

	@Test public void testObjectsDelete() throws Exception {
		deleteAll();
		ServicesStorage ss=new ServicesStorage(base+"/cspace-services/");
		String name=ss.autocreateJSON("collection-object",getJSON("obj3.json"));
		JSONObject js=ss.retrieveJSON("collection-object/"+name);
		assertTrue(JSONUtils.checkJSONEquiv(js,getJSON("obj3.json")));
		ss.deleteJSON("collection-object/"+name);
		try {
			ss.retrieveJSON("collection-object/"+name);
			assertFalse(true); // XXX use JUnit exception annotation
		} catch(ExistException e) {}
	}
	
	// XXX factor out
	private static void assertArrayContainsString(String[] a,String b) {
		for(String x: a) {
			if(x.equals(b))
				return;
		}
		assertFalse(true);
	}
	
	@Test public void testObjectsList() throws Exception {
		deleteAll();
		ServicesStorage ss=new ServicesStorage(base+"/cspace-services/");
		String name1=ss.autocreateJSON("collection-object",getJSON("obj3.json"));
		String name2=ss.autocreateJSON("collection-object",getJSON("obj4.json"));
		ss.createJSON("collection-object/123",getJSON("obj4.json"));
		String[] names=ss.getPaths("collection-object");
		assertArrayContainsString(names,name1);
		assertArrayContainsString(names,name2);
		assertArrayContainsString(names,"123");
	}
	
	@Test public void testHackCSPACE264() throws Exception {
		deleteAll();
		ServicesStorage ss=new ServicesStorage(base+"/cspace-services/");
		ss.createJSON("collection-object/def",getJSON("obj3.json"));
		JSONObject js=ss.retrieveJSON("collection-object/def");
		assertTrue(JSONUtils.checkJSONEquiv(js,getJSON("obj3.json")));
	}
}