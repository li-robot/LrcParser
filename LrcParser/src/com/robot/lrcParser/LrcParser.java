package com.robot.lrcParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcParser {

	public ArrayList<LrcLine> lines = new ArrayList<LrcLine>();

	public final static int SPLIT_LEN_10 = 10;
	public final static int SPLIT_LEN_7 = 7;
	
	private boolean initState = false;

	public LrcParser(String lrcPath) {
		initState = parse(lrcPath);
	}

	public boolean parse(String lrcPath) {
		
		if(lrcPath == null){
			return false;
		}
		
		if(!new File(lrcPath).exists()){
			return false;
		}

		try {
			FileInputStream inputStream = new FileInputStream(new File(lrcPath));
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));

			String tmpStr = null;
			Pattern pattern = Pattern
					.compile("^(\\[\\d{2}\\:\\d{2}\\.\\d{2}\\]|\\[\\d{2}\\:\\d{2}\\]).+");

			while ((tmpStr = bReader.readLine()) != null) {

				LrcLine line = new LrcLine();
				Matcher matcher = pattern.matcher(tmpStr);

				if (matcher.matches()) {

					if (tmpStr.indexOf("]") == 9) {
						line.timeStemp = tmpStr.substring(matcher.start(),
								SPLIT_LEN_10);
						line.lineString = tmpStr.substring(SPLIT_LEN_10);
						line.timeTag = parseTime(line.timeStemp);
					}

					if (tmpStr.indexOf("]") == 6) {
						line.timeStemp = tmpStr.substring(matcher.start(),
								SPLIT_LEN_7);
						line.lineString = tmpStr.substring(SPLIT_LEN_7);
						line.timeTag = parseTime(line.timeStemp);
					}
				}
				lines.add(line);
			}
			bReader.close();
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	private LrcPlayThread playThread;

	public void play() {
		if(initState){
			
			playThread = new LrcPlayThread(lines, mOnTextListener,
					mOnCompleteListener);
			new Thread(playThread).start();
			
		} else {
			System.out.println("init failed!");
		}
		
	}
	
	public void setTimeOffset(long timeOffset){
		if(playThread != null){
			playThread.setLrcOffsetTime(timeOffset);
		}
	}

	private OnTextListener mOnTextListener;

	public void setOnTextListener(OnTextListener mOnTextListener) {
		this.mOnTextListener = mOnTextListener;
	}

	private OnCompleteListener mOnCompleteListener;

	public void setOnCompleteListener(OnCompleteListener mOnCompleteListener) {
		this.mOnCompleteListener = mOnCompleteListener;
	}

	public void stop() {
		if (playThread != null) {
			playThread.stop();
		}
	}

	public boolean isEmpty(String str) {

		if (str == null) {
			return true;
		}
		if ("".equals(str)) {
			return true;
		}
		
		return false;
	}

	public long parseTime(String timeStemp) {

		if (timeStemp == null) {
			return -1;
		}
		
		int startTagIndex = timeStemp.indexOf("[");
		int endTagIndex = timeStemp.indexOf("]");
		String timeStr = timeStemp.substring(startTagIndex, endTagIndex);

		int minTagIndex = timeStr.indexOf(":");
		int miliSecTagIndex = timeStr.indexOf(".");

		String min = timeStr.substring(1, minTagIndex);

		String sec = null;
		String miliSec = null;

		if (miliSecTagIndex > 0) {
			sec = timeStr.substring(minTagIndex + 1, miliSecTagIndex);
			miliSec = timeStr.substring(miliSecTagIndex + 1);
		} else {
			sec = timeStr.substring(minTagIndex + 1, minTagIndex + 3);
		}

		long totalTime = Long.valueOf(min) * 60 * 1000 + Long.valueOf(sec)
				* 1000;
		if (miliSec != null) {
			double ratio = Double.valueOf(miliSec) / 100;
			totalTime += ratio * 1000;
		}

		return totalTime;
	}

}
