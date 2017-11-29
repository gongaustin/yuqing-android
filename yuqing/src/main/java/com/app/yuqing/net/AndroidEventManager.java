package com.app.yuqing.net;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

public class AndroidEventManager extends EventManager {

	//单例模块
	public static AndroidEventManager getInstance(){
		if(sInstance == null){
			sInstance = new AndroidEventManager();
		}
		return sInstance;
	}
	
	private static AndroidEventManager sInstance;
	
	private ExecutorService	mExecutorService;
	
	private AndroidEventManager(){
		mExecutorService = Executors.newCachedThreadPool();
	}
	
	//消息处理
	private static final int WHAT_EVENT_NOTIFY 	= 1;
	private static final int WHAT_EVENT_PUSH	= 2;
	private static final int WHAT_EVENT_END		= 3;
	
	private static Handler mHandler = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			final int nWhat = msg.what;
			if(nWhat == WHAT_EVENT_END){
				sInstance.onEventRunEnd((Event)msg.obj);
			}else if(nWhat == WHAT_EVENT_PUSH){
				final Event event = (Event)msg.obj;
				if(!sInstance.isEventRunning(event)){
					sInstance.mExecutorService.execute(new Runnable() {
						@Override
						public void run() {
							sInstance.processEvent(event);
							mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, event));
						}
					});
				}else{
					sInstance.addEventListener(event.getEventCode(), new OnEventListener() {
						@Override
						public void onEventRunEnd(Event e) {
							event.setResult(e);
							mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, event));
						}
					}, true);
					
				}
			}else if(nWhat == WHAT_EVENT_NOTIFY){
				sInstance.doNotify((Event)msg.obj);
			}
		}
	};
	
	//执行模块
	private Map<Event, Event> 					mMapRunningEvent = new ConcurrentHashMap<Event, Event>();
	private	SparseArray<List<OnEventRunner>> 	mMapCodeToEventRunner = new SparseArray<List<OnEventRunner>>();
	public boolean	isEventRunning(Event e){
		return mMapRunningEvent.containsKey(e);
	}
	
	public	void 	addEventListener(int eventCode,OnEventListener listener,boolean bOnce){
		if(mIsMapListenerLock){
			addToListenerMap(mMapCodeToEventListenerAddCache, eventCode, listener);
		}else{
			addToListenerMap(mMapCodeToEventListener, eventCode, listener);
		}
		if(bOnce){
			mMapListenerUseOnce.put(calculateHashCode(eventCode, listener), listener);
		}
	}
	
	public  void	removeEventListener(int eventCode,OnEventListener listener){
		if(mIsMapListenerLock){
			addToListenerMap(mMapCodeToEventListenerRemoveCache, eventCode, listener);
		}else{
			List<OnEventListener> listeners = mMapCodeToEventListener.get(eventCode);
			if(listeners != null){
				listeners.remove(listener);
			}
		}
	}
	
	private void	addToListenerMap(SparseArray<List<OnEventListener>> map,
			int nEventCode,OnEventListener listener){
		List<OnEventListener> listeners = map.get(nEventCode);
		if(listeners == null){
			listeners = new LinkedList<AndroidEventManager.OnEventListener>();
			map.put(nEventCode, listeners);
		}
		listeners.add(listener);
	}
	
	protected boolean processEvent(Event event){
		if(isEventRunning(event)){
			return false;
		}
		
		mMapRunningEvent.put(event, event);
		
		try {
			List<OnEventRunner> runners = mMapCodeToEventRunner.get(event.getEventCode());
			if(runners != null){
				for(OnEventRunner runner : runners){
					runner.onEventRun(event);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			event.setFailException(e);
		} finally{
			mMapRunningEvent.remove(event);
		}
		
		return true;
	}
	
	@Override
	public void 	registerEventRunner(int eventCode, OnEventRunner runner) {
		List<OnEventRunner> runners = mMapCodeToEventRunner.get(eventCode);
		if(runners == null){
			runners = new LinkedList<OnEventRunner>();
			mMapCodeToEventRunner.put(eventCode, runners);
		}
		runners.clear();
		runners.add(runner);
	}
	
	public void		removeEventRunner(int eventCode, OnEventRunner runner){
		List<OnEventRunner> runners = mMapCodeToEventRunner.get(eventCode);
		if(runners != null){
			runners.remove(runner);
		}
	}
	
	public void 	clearAllRunners(){
		mMapCodeToEventRunner.clear();
	}
	
	//结果通知模块
	private boolean 		mIsEventNotifying;
	private List<Event> 	mListEventNotify = new LinkedList<Event>();
	private Map<Event, List<OnEventListener>> 	mMapEventToListener = new ConcurrentHashMap<Event, List<OnEventListener>>();
	private boolean 							mIsMapListenerLock = false;
	private SparseArray<List<OnEventListener>> 	mMapCodeToEventListener = new SparseArray<List<OnEventListener>>();
	private SparseArray<OnEventListener> 		mMapListenerUseOnce = new SparseArray<OnEventListener>();
	private SparseArray<List<OnEventListener>> 	mMapCodeToEventListenerAddCache = new SparseArray<List<OnEventListener>>();
	private SparseArray<List<OnEventListener>> 	mMapCodeToEventListenerRemoveCache = new SparseArray<List<OnEventListener>>();
	protected void	onEventRunEnd(Event event){
		notifyEventRunEnd(event);
	}
	
	private void	notifyEventRunEnd(Event event){
		if(mIsEventNotifying){
			mListEventNotify.add(event);
		}else{
			doNotify(event);
		}
	}
	
	private void	doNotify(Event event){
		mIsEventNotifying = true;
		final List<OnEventListener> eventListeners = mMapEventToListener.get(event);
		if(eventListeners != null){
			if(eventListeners.size() > 0){
				final OnEventListener listener = eventListeners.remove(0);
				if(eventListeners.size() == 0){
					mMapEventToListener.remove(event);
				}
				try{
					listener.onEventRunEnd(event);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		mIsMapListenerLock = true;
		List<OnEventListener> list = mMapCodeToEventListener.get(event.getEventCode());
		if(list != null){
			List<OnEventListener> listNeedRemove = null;
			for(OnEventListener listener : list){
				try{
					listener.onEventRunEnd(event);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				int nHashCode = calculateHashCode(event.getEventCode(), listener);
				if(mMapListenerUseOnce.get(nHashCode) != null){
					mMapListenerUseOnce.remove(nHashCode);
					if(listNeedRemove == null){
						listNeedRemove = new ArrayList<EventManager.OnEventListener>();
					}
					listNeedRemove.add(listener);
				}
			}
			if(listNeedRemove != null){
				list.removeAll(listNeedRemove);
			}
		}
		mIsMapListenerLock = false;
		
		mIsEventNotifying = false;
		
		if(mMapCodeToEventListenerAddCache.size() > 0){
			int nSize = mMapCodeToEventListenerAddCache.size();
			for(int nIndex = 0;nIndex < nSize;++nIndex){
				int nCode = mMapCodeToEventListenerAddCache.keyAt(nIndex);
				List<OnEventListener> listCache = mMapCodeToEventListenerAddCache.get(nCode);
				if(listCache.size() > 0){
					List<OnEventListener> listeners = mMapCodeToEventListener.get(nCode);
					if(listeners == null){
						listeners = new LinkedList<AndroidEventManager.OnEventListener>();
						mMapCodeToEventListener.put(nCode, listeners);
					}
					listeners.addAll(listCache);
				}
			}
			mMapCodeToEventListenerAddCache.clear();
		}
		if(mMapCodeToEventListenerRemoveCache.size() > 0){
			int nSize = mMapCodeToEventListenerRemoveCache.size();
			for(int nIndex = 0;nIndex < nSize;++nIndex){
				int nCode = mMapCodeToEventListenerRemoveCache.keyAt(nIndex);
				List<OnEventListener> listCache = mMapCodeToEventListenerRemoveCache.get(nCode);
				if(listCache.size() > 0){
					List<OnEventListener> listeners = mMapCodeToEventListener.get(nCode);
					if(listeners != null){
						listeners.removeAll(listCache);
					}
				}
			}
			mMapCodeToEventListenerRemoveCache.clear();
		}
		
		if(mListEventNotify.size() > 0){
			Event eventNotify = mListEventNotify.get(0);
			mListEventNotify.remove(0);
			mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_NOTIFY, eventNotify));
		}
	}
	
	private int		calculateHashCode(int nEventCode,OnEventListener listener){
		int nResult = nEventCode;
		nResult = nResult * 29 + listener.hashCode();
		return nResult;
	}
	
	//操作模块
	@Override
	public Event 	pushEvent(int eventCode, Object... params) {
		final Event event = new Event(eventCode, params);
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_PUSH, event));
		return event;
	}
	
	public void		pushEventDelayed(int eventCode,long delayMillis,Object... params){
		final Event event = new Event(eventCode, params);
		mHandler.sendMessageDelayed(
				mHandler.obtainMessage(WHAT_EVENT_PUSH, event), delayMillis);
	}
	
	public Event 	pushEventEx(int eventCode,OnEventListener listener,Object... params){
		Event event = new Event(eventCode, params);
		List<OnEventListener> listeners = mMapEventToListener.get(event);
		if(listeners == null){
			listeners = new LinkedList<EventManager.OnEventListener>();
			mMapEventToListener.put(event, listeners);
		}
		listeners.add(listener);
		
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_PUSH, event));
		return event;
	}
	
	@Override
	public Event 	runEvent(int eventCode, Object... params) {
		Event event = new Event(eventCode, params);
		processEvent(event);
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, event));
		return event;
	}
	
	public void		notifyEvent(int eventCode,Object... params){
		Event e = new Event(eventCode, params);
		e.setSuccess(true);
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, e));
	}
	
	public void		cancelAllEvent(){
		mExecutorService.shutdownNow();
		mExecutorService = Executors.newCachedThreadPool();
	}	
}
