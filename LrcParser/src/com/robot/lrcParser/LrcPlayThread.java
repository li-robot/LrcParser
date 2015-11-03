package com.robot.lrcParser;

import java.util.ArrayList;

public class LrcPlayThread implements Runnable {

	private ArrayList<LrcLine> lines;

	private boolean runFlag = true;

	private OnTextListener mListener;
	private OnCompleteListener mOnCompleteListener;

	public LrcPlayThread(ArrayList<LrcLine> lines, OnTextListener mListener,
			OnCompleteListener mOnCompleteListener) {
		this.lines = lines;
		this.mListener = mListener;
		this.mOnCompleteListener = mOnCompleteListener;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();

		while (runFlag) {
			long endTime = System.currentTimeMillis();

			if (endTime - startTime - lines.get(0).timeTag <= 20
					&& endTime - startTime - lines.get(0).timeTag >= -20) {
				if (lines.get(0).lineString != null && mListener != null) {
					mListener.onText(lines.get(0).lineString);
				}
				if (lines.size() > 0) {
					lines.remove(0);
					if (lines.size() == 0) {
						return;
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
