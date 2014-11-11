package com.zeedoo.mars.dao;

import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;

@Component
public class FileTransferTaskDao extends EntityDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferTaskDao.class);

	public FileTransferTask get(String sunMacAddress, String fileId, FileType fileType) {
		URI uri = UriBuilder.fromPath(CoreApiPath.FILE_TRANSFER_TASK.getPath()).build();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (sunMacAddress != null) {
			params.add("sunMacAddress", sunMacAddress);
		}
		if (fileId != null) {
			params.add("fileId", fileId);
		}
		if (fileType != null) {
			params.add("fileType", fileType.name());
		}
		return coreApiClient.getWithQueryParams(uri.toASCIIString(), FileTransferTask.class, params);
	}
	
	public FileTransferTask insertOrReplace(FileTransferTask task) {
		URI uri = UriBuilder.fromPath(CoreApiPath.FILE_TRANSFER_TASK.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), FileTransferTask.class, task);
	}
	
	public FileTransferTask update(FileTransferTask task) {
		URI uri = UriBuilder.fromPath(CoreApiPath.FILE_TRANSFER_TASK.getPath()).build();
		FileTransferTask result = coreApiClient.put(uri.toASCIIString(), FileTransferTask.class, task);
		if (result == null) {
			LOGGER.warn("Updating FileTransferTask has returned a NULL entity with request entity={}", task);
		}
		return result;
	}
	
	public boolean delete(String sunMacAddress, String fileId, FileType fileType) {
		URI uri = UriBuilder.fromPath(CoreApiPath.FILE_TRANSFER_TASK.getPath()).build();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (sunMacAddress != null) {
			params.add("sunMacAddress", sunMacAddress);
		}
		if (fileId != null) {
			params.add("fileId", fileId);
		}
		if (fileType != null) {
			params.add("fileType", fileType.name());
		}
		boolean result = coreApiClient.deleteWithQueryParams(uri.toASCIIString(), params);
		return result;
	}
}
