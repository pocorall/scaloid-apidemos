package com.example.android.apis.animation

import android.animation._
import android.view.animation.AccelerateInterpolator
import com.example.android.apis.R
import java.util.ArrayList
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.LinearLayout
import MultiPropertyAnimation._
import org.scaloid.common.SActivity

//remove if not needed
import scala.collection.JavaConversions._

object MultiPropertyAnimation {

  private val DURATION = 1500

  private val BALL_SIZE = 100f
}

class MultiPropertyAnimation extends SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_multi_property)
    val container = findViewById(R.id.container).asInstanceOf[LinearLayout]
    val animView = new MyAnimationView(this)
    container.addView(animView)
    val starter = findViewById(R.id.startButton).asInstanceOf[Button]
    starter.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.startAnimation()
      }
    })
  }

  class MyAnimationView(context: Context) extends View(context) with ValueAnimator.AnimatorUpdateListener {

    val balls = new ArrayList[ShapeHolder]()

    var animation: AnimatorSet = null

    var bounceAnim: Animator = null

    var ball: ShapeHolder = null

    addBall(50, 0)

    addBall(150, 0)

    addBall(250, 0)

    addBall(350, 0)

    private def createAnimation() {
      if (bounceAnim == null) {
        var ball: ShapeHolder = null
        ball = balls.get(0)
        val yBouncer = ObjectAnimator.ofFloat(ball, "y", ball.getY, getHeight - BALL_SIZE)
          .setDuration(DURATION)
        yBouncer.setInterpolator(new BounceInterpolator())
        yBouncer.addUpdateListener(this)
        ball = balls.get(1)
        var pvhY = PropertyValuesHolder.ofFloat("y", ball.getY, getHeight - BALL_SIZE)
        val pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f)
        val yAlphaBouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY, pvhAlpha)
          .setDuration(DURATION / 2)
        yAlphaBouncer.setInterpolator(new AccelerateInterpolator())
        yAlphaBouncer.setRepeatCount(1)
        yAlphaBouncer.setRepeatMode(ValueAnimator.REVERSE)
        ball = balls.get(2)
        val pvhW = PropertyValuesHolder.ofFloat("width", ball.getWidth, ball.getWidth * 2)
        val pvhH = PropertyValuesHolder.ofFloat("height", ball.getHeight, ball.getHeight * 2)
        val pvTX = PropertyValuesHolder.ofFloat("x", ball.getX, ball.getX - BALL_SIZE / 2f)
        val pvTY = PropertyValuesHolder.ofFloat("y", ball.getY, ball.getY - BALL_SIZE / 2f)
        val whxyBouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhW, pvhH, pvTX, pvTY)
          .setDuration(DURATION / 2)
        whxyBouncer.setRepeatCount(1)
        whxyBouncer.setRepeatMode(ValueAnimator.REVERSE)
        ball = balls.get(3)
        pvhY = PropertyValuesHolder.ofFloat("y", ball.getY, getHeight - BALL_SIZE)
        val ballX = ball.getX
        val kf0 = Keyframe.ofFloat(0f, ballX)
        val kf1 = Keyframe.ofFloat(.5f, ballX + 100f)
        val kf2 = Keyframe.ofFloat(1f, ballX + 50f)
        val pvhX = PropertyValuesHolder.ofKeyframe("x", kf0, kf1, kf2)
        val yxBouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY, pvhX)
          .setDuration(DURATION / 2)
        yxBouncer.setRepeatCount(1)
        yxBouncer.setRepeatMode(ValueAnimator.REVERSE)
        bounceAnim = new AnimatorSet()
        bounceAnim.asInstanceOf[AnimatorSet].playTogether(yBouncer, yAlphaBouncer, whxyBouncer, yxBouncer)
      }
    }

    def startAnimation() {
      createAnimation()
      bounceAnim.start()
    }

    private def addBall(x: Float, y: Float): ShapeHolder = {
      val circle = new OvalShape()
      circle.resize(BALL_SIZE, BALL_SIZE)
      val drawable = new ShapeDrawable(circle)
      val shapeHolder = new ShapeHolder(drawable)
      shapeHolder.setX(x)
      shapeHolder.setY(y)
      val red = (100 + Math.random() * 155).toInt
      val green = (100 + Math.random() * 155).toInt
      val blue = (100 + Math.random() * 155).toInt
      val color = 0xff000000 | red << 16 | green << 8 | blue
      val paint = drawable.getPaint
      val darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4
      val gradient = new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
      paint.setShader(gradient)
      shapeHolder.setPaint(paint)
      balls.add(shapeHolder)
      shapeHolder
    }

    protected override def onDraw(canvas: Canvas) {
      for (ball <- balls) {
        canvas.translate(ball.getX, ball.getY)
        ball.getShape.draw(canvas)
        canvas.translate(-ball.getX, -ball.getY)
      }
    }

    def onAnimationUpdate(animation: ValueAnimator) {
      invalidate()
    }
  }
}
