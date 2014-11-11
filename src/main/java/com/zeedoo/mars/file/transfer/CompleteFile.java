package com.zeedoo.mars.file.transfer;

import com.google.common.base.Objects;

/**
 * Represents a file that is complete (but data integrity might or might not be verified)
 * @author nzhu
 *
 */
public class CompleteFile {
	
	private String md5Hash;
	
	// Complete base64 encoded data
	private String data;
	
	private String decodedFilePath;

	public CompleteFile(String md5Hash, String data) {
		this.md5Hash = md5Hash;
		this.data = data;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}
	
	public String getDecodedFilePath() {
		return decodedFilePath;
	}

	public void setDecodedFilePath(String decodedFilePath) {
		this.decodedFilePath = decodedFilePath;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int hashCode(){
		return Objects.hashCode(md5Hash, data, decodedFilePath);
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof CompleteFile) {
			CompleteFile that = (CompleteFile) object;
			return Objects.equal(this.md5Hash, that.md5Hash)
				&& Objects.equal(this.data, that.data)
				&& Objects.equal(this.decodedFilePath, that.decodedFilePath);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("md5Hash", md5Hash)
			.add("data", data)
			.add("decodedFilePath", decodedFilePath)
			.toString();
	}
}
