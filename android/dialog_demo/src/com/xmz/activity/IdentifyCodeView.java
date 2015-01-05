package com.xmz.activity;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class IdentifyCodeView extends View {

	private static final char[] CHARS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
		'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};
//	private static final int LINE_COLOR_RATE = 5;
//	private static final int TEXT_COLOR_RATE = 3;
	
	public static final int DEFAULT_CODE_LENGTH = 4;
	private static final int DEFAULT_FONT_SIZE = 50;
	
	private static final int DEFAULT_LINE_NUMBER = 5;
	
	private static final int BASE_PADDING_LEFT = 20, RANGE_PADDING_LEFT = 30, BASE_PADDING_TOP = 40, RANGE_PADDING_TOP = 50;
	
	private static final int DEFAULT_WIDTH = 120, DEFAULT_HEIGHT = 50;
	
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT, padding_left, padding_top;
	private int codeLength = DEFAULT_CODE_LENGTH;
	private String code;
	private Random random = new Random();
	
	public IdentifyCodeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Log.i("IdentifyCodeView", "IdentifyCodeView(Context context)");
	}

	public IdentifyCodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		Log.i("IdentifyCodeView", "IdentifyCodeView(Context context, AttributeSet attrs, int defStyle)");
	}

	public IdentifyCodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.width = attrs.getAttributeIntValue(null, "width", DEFAULT_WIDTH);
		this.height = attrs.getAttributeIntValue(null, "height", DEFAULT_HEIGHT);
		Log.i("IdentifyCodeView", "IdentifyCodeView(Context context, AttributeSet attrs)");
//		Toast.makeText(context, attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width"), 0).show();
	}

	public IdentifyCodeView(Context context, int width, int height) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new LayoutParams(width, height));
		this.width = width;
		this.height = height;
		Log.i("IdentifyCodeView", "IdentifyCodeView(Context context, int width, int height)");
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		code = createCode();
		canvas.drawColor(Color.WHITE);
//		canvas.drawColor(randomColor(BACKGROUND_COLOR_RATE));
		Paint paint = new Paint();
		paint.setTextSize(DEFAULT_FONT_SIZE);
		
		for (int i = 0; i < code.length(); i++) {
			randomTextStyle(paint);
			randomPadding();
			canvas.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
		}

		for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
			drawLine(canvas, paint);
		}
		
		Toast.makeText(getContext(), code, 0).show();
	}
	
	public void invalidate() {
		padding_left = 0;
		super.invalidate();
	}
	
	public String getCode() {
		return code;
	}
	
	private String createCode() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}
	
	private void drawLine(Canvas canvas, Paint paint) {
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(2);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}
	
	private int randomColor() {
		return randomColor(1);
	}

	private int randomColor(int rate) {
		int red = random.nextInt(256) / rate;
		int green = random.nextInt(256) / rate;
		int blue = random.nextInt(256) / rate;
		return Color.rgb(red, green, blue);
	}
	
	private void randomTextStyle(Paint paint) {
		int color = randomColor();
		paint.setColor(color);
		paint.setFakeBoldText(random.nextBoolean());  //true为粗体，false为非粗体
		float skewX = random.nextInt(11) / 10;
		skewX = random.nextBoolean() ? skewX : -skewX;
		paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
//		paint.setUnderlineText(true); //true为下划线，false为非下划线
//		paint.setStrikeThruText(true); //true为删除线，false为非删除线
	}
	
	private void randomPadding() {
		padding_left += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
		padding_top = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
	}
}
