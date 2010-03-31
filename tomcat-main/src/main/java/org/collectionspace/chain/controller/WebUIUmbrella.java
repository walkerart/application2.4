package org.collectionspace.chain.controller;

import java.util.HashMap;
import java.util.Map;

import org.collectionspace.chain.csp.webui.main.WebUI;
import org.collectionspace.csp.api.ui.UIException;
import org.collectionspace.csp.api.ui.UIUmbrella;

// XXX expiry. For now it will take an age for our sessions to impact on memory.
public class WebUIUmbrella implements UIUmbrella {
	private Map<String,WebUISession> sessions=new HashMap<String,WebUISession>();
	private WebUI ui;
	
	public WebUIUmbrella(WebUI ui) { this.ui=ui; }
	
	WebUISession getSession(String in) {
		WebUISession out=sessions.get(in);
		if(out==null)
			return null;
		out.setOld();
		return out;
	}
	
	WebUISession createSession() throws UIException {
		WebUISession out=new WebUISession(this);
		sessions.put(out.getID(),out);
		return out;
	}
	
	WebUI getWebUI() { return ui; }
}