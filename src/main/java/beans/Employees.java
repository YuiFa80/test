package beans;

public class Employees {
	private String srCode;
	private String srName;
	private String emCode;
	private String emPassword;
	private String emName;
	private String date;
	private int accessType;
	
	//-----------------------------------------	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSrCode() {
		return srCode;
	}
	public void setSrCode(String srCode) {
		this.srCode = srCode;
	}
	public String getEmCode() {
		return emCode;
	}
	public void setEmCode(String emCode) {
		this.emCode = emCode;
	}
	public String getEmPassword() {
		return emPassword;
	}
	public void setEmPassword(String emPassword) {
		this.emPassword = emPassword;
	}
	public String getEmName() {
		return emName;
	}
	public void setEmName(String emName) {
		this.emName = emName;
	}
	public String getSrName() {
		return srName;
	}
	public void setSrName(String srName) {
		this.srName = srName;
	}
	public int getAccessType() {
		return accessType;
	}
	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}

}
