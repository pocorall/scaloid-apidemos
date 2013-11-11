package com.example.android.apis.animation

import android.animation.Animator
import com.example.android.apis.R
import java.util.ArrayList
import android.animation.ValueAnimator
import android.animation.ObjectAnimator
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
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import AnimationSeeking._
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

object AnimationSeeking {

  private val DURATION = 1500

  private val RED = 0xffFF8080

  private val BLUE = 0xff8080FF

  private val CYAN = 0xff80ffff

  private val GREEN = 0xff80ff80

  private val BALL_SIZE = 100f

}

class AnimationSeeking extends SActivity {

  private var mSeekBar: SeekBar = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_seeking)
    val container = findViewById(R.id.container).asInstanceOf[LinearLayout]
    val animView = new MyAnimationView(this)
    container.addView(animView)
    val starter = findViewById(R.id.startButton).asInstanceOf[Button]
    starter.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.startAnimation()
      }
    })
    mSeekBar = findViewById(R.id.seekBar).asInstanceOf[SeekBar]
    mSeekBar.setMax(DURATION)
    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (animView.getHeight != 0) {
          animView.seek(progress)
        }
      }
    })
  }

  class MyAnimationView(context: Context) extends View(context) with ValueAnimator.AnimatorUpdateListener with Animator.AnimatorListener {

    val balls = new ArrayList[ShapeHolder]()

    var animation: AnimatorSet = null

    var bounceAnim: ValueAnimator = null

    var ball: ShapeHolder = addBall(200, 0)

    private def createAnimation() {
      if (bounceAnim == null) {
        bounceAnim = ObjectAnimator.ofFloat(ball, "y", ball.getY, getHeight - BALL_SIZE)
          .setDuration(1500)
        bounceAnim.setInterpolator(new BounceInterpolator())
        bounceAnim.addUpdateListener(this)
      }
    }

    def startAnimation() {
      createAnimation()
      bounceAnim.start()
    }

    def seek(seekTime: Long) {
      createAnimation()
      bounceAnim.setCurrentPlayTime(seekTime)
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
      canvas.translate(ball.getX, ball.getY)
      ball.getShape.draw(canvas)
    }

    def onAnimationUpdate(animation: ValueAnimator) {
      invalidate()
      val playtime = bounceAnim.getCurrentPlayTime
    }

    def onAnimationCancel(animation: Animator) {
    }

    def onAnimationEnd(animation: Animator) {
      balls.remove(animation.asInstanceOf[ObjectAnimator].getTarget)
    }

    def onAnimationRepeat(animation: Animator) {
    }

    def onAnimationStart(animation: Animator) {
    }
  }
}
