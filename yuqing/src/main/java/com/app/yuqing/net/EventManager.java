package com.app.yuqing.net;

public abstract class EventManager {

	public 	abstract void	registerEventRunner(int eventCode,OnEventRunner runner);
	
	public 	abstract Event 	pushEvent(int eventCode,Object ... params);
	
	public	abstract Event	runEvent(int eventCode,Object ... params);
	
	public static interface OnEventRunner{
		public void onEventRun(Event event) throws Exception;
	}
	
	public static interface OnEventListener{
		public void onEventRunEnd(Event event);
	}
}