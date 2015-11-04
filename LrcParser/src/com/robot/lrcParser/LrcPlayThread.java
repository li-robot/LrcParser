package com.robot.lrcParser;

import java.util.ArrayList;

public class LrcPlayThread implements Runnable {

	private ArrayList<LrcLine> lines;

	private boolean runFlag = true;

	private OnTextListener mListener;
	private OnCompleteListener mOnCompleteListener;
	
	private long lrcOffsetTime = 0;

	public LrcPlayThread(ArrayList<LrcLine> lines, OnTextListener mListener,
			OnCompleteListener mOnCompleteListener) {
		this.lines = lines;
		this.mListener = mListener;
		this.mOnCompleteListener = mOnCompleteListener;
	}
	
	public void setLrcOffsetTime(long offsetTime){
		this.lrcOffsetTime = offsetTime;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();

		while (runFlag) {
			long endTime = System.currentTimeMillis() + lrcOffsetTime;
			
			for(int i=0; i<lines.size(); i++){
				
				if (endTime - startTime - lines.get(i).timeTag <= 20
						&& endTime - startTime - lines.get(i).timeTag >= -20) {
					
					if (lines.get(i).lineString != null && mListener != null ) {
						mListener.onText(lines.get(i).lineString);
						
						lines.remove(i);
				    }
				}
			}
		}

		if (mOnCompleteListener != null) {
			mOnCompleteListener.onComplete();
		}
	}
	

	public void stop() {
		this.runFlag = false;
	}
}
