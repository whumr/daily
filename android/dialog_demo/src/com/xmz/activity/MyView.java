package com.xmz.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

public class MyView extends View {

	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.WHITE);

		Paint paint = new Paint();

		paint.setAntiAlias(true);

		paint.setColor(Color.RED);

		paint.setStyle(Paint.Style.STROKE);

		paint.setStrokeWidth(3);

		canvas.drawCircle(40, 40, 30, paint);

		canvas.drawRect(10, 90, 70, 150, paint);

		canvas.drawRect(10, 170, 70, 200, paint);

		canvas.drawOval(new RectF(10, 220, 70, 250), paint);

		Path path = new Path();

		path.moveTo(10, 330);

		path.lineTo(70, 330);

		path.lineTo(40, 270);

		path.close();

		canvas.drawPath(path, paint);

		Path path1 = new Path();

		path1.moveTo(10, 410);

		path1.lineTo(70, 410);

		path1.lineTo(55, 350);

		path1.lineTo(25, 350);

		path1.close();

		canvas.drawPath(path1, paint);

		paint.setColor(Color.BLUE);

		paint.setStyle(Paint.Style.FILL);

		canvas.drawCircle(120, 40, 30, paint);

		canvas.drawRect(90, 90, 150, 150, paint);

		canvas.drawRect(90, 170, 150, 200, paint);

		RectF re2 = new RectF(90, 220, 150, 250);

		canvas.drawOval(re2, paint);

		Path path2 = new Path();

		path2.moveTo(90, 330);

		path2.lineTo(150, 330);

		path2.lineTo(120, 270);

		path2.close();

		canvas.drawPath(path2, paint);

		Path path3 = new Path();

		path3.moveTo(90, 410);

		path3.lineTo(150, 410);

		path3.lineTo(135, 350);

		path3.lineTo(105, 350);

		path3.close();

		canvas.drawPath(path3, paint);

		Shader mShader = new LinearGradient(0, 0, 100, 100,

		new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW },

		null, Shader.TileMode.REPEAT);

		// Shader.TileMode三种模式

		// REPEAT:沿着渐变方向循环重复

		// CLAMP:如果在预先定义的范围外画的话，就重复边界的颜色

		// MIRROR:与REPEAT一样都是循环重复，但这个会对称重复

		paint.setShader(mShader);// 用Shader中定义定义的颜色来话

		canvas.drawCircle(200, 40, 30, paint);

		canvas.drawRect(170, 90, 230, 150, paint);

		canvas.drawRect(170, 170, 230, 200, paint);

		RectF re3 = new RectF(170, 220, 230, 250);

		canvas.drawOval(re3, paint);

		Path path4 = new Path();

		path4.moveTo(170, 330);

		path4.lineTo(230, 330);

		path4.lineTo(200, 270);

		path4.close();

		canvas.drawPath(path4, paint);

		Path path5 = new Path();

		path5.moveTo(170, 410);

		path5.lineTo(230, 410);

		path5.lineTo(215, 350);

		path5.lineTo(185, 350);

		path5.close();

		canvas.drawPath(path5, paint);

		paint.setTextSize(24);

		canvas.drawText("圆形", 240, 50, paint);

		canvas.drawText("正方形", 240, 120, paint);

		canvas.drawText("长方形", 240, 190, paint);

		canvas.drawText("椭圆形", 240, 250, paint);

		canvas.drawText("三角形", 240, 320, paint);

		canvas.drawText("梯形", 240, 390, paint);

	}

}
