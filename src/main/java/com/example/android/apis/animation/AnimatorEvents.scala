package com.example.android.apis.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.widget.CheckBox
import android.widget.TextView
import com.example.android.apis.R
import java.util.ArrayList
import android.animation.ValueAnimator
import android.animation.AnimatorSet
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
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class AnimatorEvents extends SActivity {

  var startText: TextView = _

  var repeatText: TextView = _

  var cancelText: TextView = _

  var endText: TextView = _

  var startTextAnimator: TextView = _

  var repeatTextAnimator: TextView = _

  var cancelTextAnimator: TextView = _

  var endTextAnimator: TextView = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animator_events)
    val container = findViewById(R.id.container).asInstanceOf[LinearLayout]
    val animView = new MyAnimationView(this)
    container.addView(animView)
    startText = findViewById(R.id.startText).asInstanceOf[TextView]
    startText.setAlpha(.5f)
    repeatText = findViewById(R.id.repeatText).asInstanceOf[TextView]
    repeatText.setAlpha(.5f)
    cancelText = findViewById(R.id.cancelText).asInstanceOf[TextView]
    cancelText.setAlpha(.5f)
    endText = findViewById(R.id.endText).asInstanceOf[TextView]
    endText.setAlpha(.5f)
    startTextAnimator = findViewById(R.id.startTextAnimator).asInstanceOf[TextView]
    startTextAnimator.setAlpha(.5f)
    repeatTextAnimator = findViewById(R.id.repeatTextAnimator).asInstanceOf[TextView]
    repeatTextAnimator.setAlpha(.5f)
    cancelTextAnimator = findViewById(R.id.cancelTextAnimator).asInstanceOf[TextView]
    cancelTextAnimator.setAlpha(.5f)
    endTextAnimator = findViewById(R.id.endTextAnimator).asInstanceOf[TextView]
    endTextAnimator.setAlpha(.5f)
    val endCB = findViewById(R.id.endCB).asInstanceOf[CheckBox]
    val starter = findViewById(R.id.startButton).asInstanceOf[Button]
    starter.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.startAnimation(endCB.isChecked)
      }
    })
    val canceler = findViewById(R.id.cancelButton).asInstanceOf[Button]
    canceler.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.cancelAnimation()
      }
    })
    val ender = findViewById(R.id.endButton).asInstanceOf[Button]
    ender.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.endAnimation()
      }
    })
  }

  class MyAnimationView(context: Context) extends View(context) with Animator.AnimatorListener with ValueAnimator.AnimatorUpdateListener {

    val balls = new ArrayList[ShapeHolder]()

    var animation: Animator = _

    var ball: ShapeHolder = createBall(25, 25)

    var endImmediately: Boolean = false

    private def createAnimation() {
      if (animation == null) {
        val yAnim = ObjectAnimator.ofFloat(ball, "y", ball.getY, getHeight - 50f)
          .setDuration(1500)
        yAnim.setRepeatCount(0)
        yAnim.setRepeatMode(ValueAnimator.REVERSE)
        yAnim.setInterpolator(new AccelerateInterpolator(2f))
        yAnim.addUpdateListener(this)
        yAnim.addListener(this)
        val xAnim = ObjectAnimator.ofFloat(ball, "x", ball.getX, ball.getX + 300)
          .setDuration(1000)
        xAnim.setStartDelay(0)
        xAnim.setRepeatCount(0)
        xAnim.setRepeatMode(ValueAnimator.REVERSE)
        xAnim.setInterpolator(new AccelerateInterpolator(2f))
        val alphaAnim = ObjectAnimator.ofFloat(ball, "alpha", 1f, .5f).setDuration(1000)
        val alphaSeq = new AnimatorSet()
        alphaSeq.play(alphaAnim)
        animation = new AnimatorSet()
        animation.asInstanceOf[AnimatorSet].playTogether(yAnim, xAnim)
        animation.addListener(this)
      }
    }

    def startAnimation(endImmediately: Boolean) {
      this.endImmediately = endImmediately
      startText.setAlpha(.5f)
      repeatText.setAlpha(.5f)
      cancelText.setAlpha(.5f)
      endText.setAlpha(.5f)
      startTextAnimator.setAlpha(.5f)
      repeatTextAnimator.setAlpha(.5f)
      cancelTextAnimator.setAlpha(.5f)
      endTextAnimator.setAlpha(.5f)
      createAnimation()
      animation.start()
    }

    def cancelAnimation() {
      createAnimation()
      animation.cancel()
    }

    def endAnimation() {
      createAnimation()
      animation.end()
    }

    private def createBall(x: Float, y: Float): ShapeHolder = {
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
      shapeHolder
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.save()
      canvas.translate(ball.getX, ball.getY)
      ball.getShape.draw(canvas)
      canvas.restore()
    }

    def onAnimationUpdate(animation: ValueAnimator) {
      invalidate()
    }

    def onAnimationStart(animation: Animator) {
      if (animation.isInstanceOf[AnimatorSet]) {
        startText.setAlpha(1f)
      } else {
        startTextAnimator.setAlpha(1f)
      }
      if (endImmediately) {
        animation.end()
      }
    }

    def onAnimationEnd(animation: Animator) {
      if (animation.isInstanceOf[AnimatorSet]) {
        endText.setAlpha(1f)
      } else {
        endTextAnimator.setAlpha(1f)
      }
    }

    def onAnimationCancel(animation: Animator) {
      if (animation.isInstanceOf[AnimatorSet]) {
        cancelText.setAlpha(1f)
      } else {
        cancelTextAnimator.setAlpha(1f)
      }
    }

    def onAnimationRepeat(animation: Animator) {
      if (animation.isInstanceOf[AnimatorSet]) {
        repeatText.setAlpha(1f)
      } else {
        repeatTextAnimator.setAlpha(1f)
      }
    }
  }
}
