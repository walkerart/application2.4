package org.collectionspace.chain.csp.persistence.services;

import org.collectionspace.chain.csp.schema.Record;
import org.collectionspace.chain.csp.schema.Spec;
import org.collectionspace.csp.api.core.CSPRequestCache;
import org.collectionspace.csp.api.persistence.ExistException;
import org.collectionspace.csp.api.persistence.UnderlyingStorageException;
import org.collectionspace.csp.api.persistence.UnimplementedException;
import org.collectionspace.csp.helper.persistence.ContextualisedStorage;
import org.json.JSONObject;

public class DirectRedirector implements ContextualisedStorage {
	private Spec spec;

	DirectRedirector(Spec spec) { this.spec=spec; }
	
	public String autocreateJSON(ContextualisedStorage root,CSPRequestCache cache, String filePath, JSONObject jsonObject)
			throws ExistException, UnimplementedException, UnderlyingStorageException {
		throw new UnimplementedException("direct uses get only");
	}

	public void createJSON(ContextualisedStorage root,CSPRequestCache cache,String filePath,JSONObject jsonObject)
		throws ExistException, UnimplementedException, UnderlyingStorageException {
		throw new UnimplementedException("direct uses get only");
	}

	public void deleteJSON(ContextualisedStorage root, CSPRequestCache cache,String filePath)
		throws ExistException, UnimplementedException, UnderlyingStorageException {
		throw new UnimplementedException("direct uses get only");
	}

	public String[] getPaths(ContextualisedStorage root, CSPRequestCache cache,String rootPath, JSONObject restrictions)
		throws ExistException, UnimplementedException, UnderlyingStorageException {
		throw new UnimplementedException("direct uses get only");
	}

	public void updateJSON(ContextualisedStorage root, CSPRequestCache cache,String filePath, JSONObject jsonObject) 
		throws ExistException, UnimplementedException, UnderlyingStorageException {
		throw new UnimplementedException("direct uses get only");
	}
	
	public JSONObject retrieveJSON(ContextualisedStorage root,CSPRequestCache cache, String path)
		throws ExistException, UnimplementedException, UnderlyingStorageException {
		/* Find relevant controller, and call */
		String[] url=path.split("/");
		Record r=spec.getRecordByServicesUrl(url[0]);
		if(!r.isType("authority"))
			throw new UnimplementedException("Only authorities supported at direct at the moment");
		return root.retrieveJSON(root,cache,r.getID()+"/_direct/"+url[1]+"/"+url[3]);
	}
}