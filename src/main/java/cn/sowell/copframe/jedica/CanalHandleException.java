package cn.sowell.copframe.jedica;

public class CanalHandleException extends Exception{

	private boolean isAck = false;
	
	private boolean isBreak = false;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3290805060222895341L;

	public boolean isAck() {
		return isAck;
	}

	public void setAck(boolean isAck) {
		this.isAck = isAck;
	}

	public boolean isBreak() {
		return isBreak;
	}

	public void setBreak(boolean isBreak) {
		this.isBreak = isBreak;
	}
	
}
