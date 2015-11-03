package com.robot.lrcParser;

public class MainTest {

	public static void main(String[] args) {

		LrcParser parser = new LrcParser("c:/lrc/test.lrc");
		parser.setOnTextListener(new OnTextListener() {

			@Override
			public void onText(String text) {
				System.out.println(text);
			}
		});

		parser.setOnCompleteListener(new OnCompleteListener() {

			@Override
			public void onComplete() {
				System.out.println("歌詞結束");
			}
		});

		parser.play();
	}

}
