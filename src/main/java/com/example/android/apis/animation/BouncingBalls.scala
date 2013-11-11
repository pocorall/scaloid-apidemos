package com.example.android.apis.animation

import android.graphics.drawable.ColorDrawable
import com.example.android.apis.R
import android.animation._
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import java.util.ArrayList
import BouncingBalls._
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

object BouncingBalls {

    private val RED = 0xffFF8080

    private val BLUE = 0xff8080FF

    private val CYAN = 0xff80ffff

    private val GREEN = 0xff80ff80

}

class BouncingBalls extends SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.bouncing_balls)
    val container = findViewById(R.id.container).asInstanceOf[LinearLayout]
    container.addView(new MyAnimationView(this))
  }

  class MyAnimationView(context: Context) extends View(context) {

    val balls = new ArrayList[ShapeHolder]()

    var animation: AnimatorSet = null

    val colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", RED, BLUE)

    colorAnim.setDuration(3000)

    colorAnim.setEvaluator(new ArgbEvaluator())

    colorAnim.setRepeatCount(ValueAnimator.INFINITE)

    colorAnim.setRepeatMode(ValueAnimator.REVERSE)

    colorAnim.start()

    override def onTouchEvent(event: MotionEvent): Boolean = {
      if (event.getAction != MotionEvent.ACTION_DOWN && event.getAction != MotionEvent.ACTION_MOVE) {
        return false
      }
      val newBall = addBall(event.getX, event.getY)
      val startY = newBall.getY
      val endY = getHeight - 50f
      val h = getHeight.toFloat
      val eventY = event.getY
      val duration = (500 * ((h - eventY) / h)).toInt
      val bounceAnim = ObjectAnimator.ofFloat(newBall, "y", startY, endY)
      bounceAnim.setDuration(duration)
      bounceAnim.setInterpolator(new AccelerateInterpolator())
      val squashAnim1 = ObjectAnimator.ofFloat(newBall, "x", newBall.getX, newBall.getX - 25f)
      squashAnim1.setDuration(duration / 4)
      squashAnim1.setRepeatCount(1)
      squashAnim1.setRepeatMode(ValueAnimator.REVERSE)
      squashAnim1.setInterpolator(new DecelerateInterpolator())
      val squashAnim2 = ObjectAnimator.ofFloat(newBall, "width", newBall.getWidth, newBall.getWidth + 50)
      squashAnim2.setDuration(duration / 4)
      squashAnim2.setRepeatCount(1)
      squashAnim2.setRepeatMode(ValueAnimator.REVERSE)
      squashAnim2.setInterpolator(new DecelerateInterpolator())
      val stretchAnim1 = ObjectAnimator.ofFloat(newBall, "y", endY, endY + 25f)
      stretchAnim1.setDuration(duration / 4)
      stretchAnim1.setRepeatCount(1)
      stretchAnim1.setInterpolator(new DecelerateInterpolator())
      stretchAnim1.setRepeatMode(ValueAnimator.REVERSE)
      val stretchAnim2 = ObjectAnimator.ofFloat(newBall, "height", newBall.getHeight, newBall.getHeight - 25)
      stretchAnim2.setDuration(duration / 4)
      stretchAnim2.setRepeatCount(1)
      stretchAnim2.setInterpolator(new DecelerateInterpolator())
      stretchAnim2.setRepeatMode(ValueAnimator.REVERSE)
      val bounceBackAnim = ObjectAnimator.ofFloat(newBall, "y", endY, startY)
      bounceBackAnim.setDuration(duration)
      bounceBackAnim.setInterpolator(new DecelerateInterpolator())
      val bouncer = new AnimatorSet()
      bouncer.play(bounceAnim).before(squashAnim1)
      bouncer.play(squashAnim1).`with`(squashAnim2)
      bouncer.play(squashAnim1).`with`(stretchAnim1)
      bouncer.play(squashAnim1).`with`(stretchAnim2)
      bouncer.play(bounceBackAnim).after(stretchAnim2)
      val fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f, 0f)
      fadeAnim.setDuration(250)
      fadeAnim.addListener(new AnimatorListenerAdapter() {

        override def onAnimationEnd(animation: Animator) {
          balls.remove(animation.asInstanceOf[ObjectAnimator].getTarget)
        }
      })
      val animatorSet = new AnimatorSet()
      animatorSet.play(bouncer).before(fadeAnim)
      animatorSet.start()
      true
    }

    private def addBall(x: Float, y: Float): ShapeHolder = {
      val circle = new OvalShape()
      circle.resize(50f, 50f)
      val drawable = new ShapeDrawable(circle)
      val shapeHolder = new ShapeHolder(drawable)
      shapeHolder.setX(x - 25f)
      shapeHolder.setY(y - 25f)
      val red = (Math.random() * 255).toInt
      val green = (Math.random() * 255).toInt
      val blue = (Math.random() * 255).toInt
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
      for (i <- 0 until balls.size) {
        val shapeHolder = balls.get(i)
        canvas.save()
        canvas.translate(shapeHolder.getX, shapeHolder.getY)
        shapeHolder.getShape.draw(canvas)
        canvas.restore()
      }
    }
  }
}
