
#**概述**

这是一个歌词解析器，支持lrc解析


- 初始化解析器：

```java
LrcParser parser = new LrcParser("c:/lr/test.lrc");
```


- 注册歌词监听

```java
parser.setOnTextListener(new OnTextListener() {
		@Override
		public void onText(String text) {
			System.out.println(text);
		}
});
```

- 歌词结束回调：

```java
parser.setOnCompleteListener(new OnCompleteListener() {
		@Override
		public void onComplete() {
			System.out.println("歌詞結束");
		}
});
```

- 开始播放：

```java
parser.play();
```

- 歌词跳跃：

```java
parser.setTimeOffset(180 * 1000);
```