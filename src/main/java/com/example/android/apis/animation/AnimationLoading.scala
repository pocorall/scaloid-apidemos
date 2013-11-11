package com.example.android.apis.animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import com.example.android.apis.R
import java.util.ArrayList
import android.animation.Animator
import android.animation.ValueAnimator
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
import android.widget.Button
import android.widget.LinearLayout
import AnimationLoading._
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

object AnimationLoading {

  private val DURATION = 1500

  private val BALL_SIZE = 100f

}

class AnimationLoading extends SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_loading)
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

    var animation: Animator = null

    addBall(50, 50)

    addBall(200, 50)

    addBall(350, 50)

    addBall(500, 50, Color.GREEN)

    private def createAnimation() {
      val appContext = AnimationLoading.this
      if (animation == null) {
        val anim = AnimatorInflater.loadAnimator(appContext, R.anim.object_animator).asInstanceOf[ObjectAnimator]
        anim.addUpdateListener(this)
        anim.setTarget(balls.get(0))
        val fader = AnimatorInflater.loadAnimator(appContext, R.anim.animator).asInstanceOf[ValueAnimator]
        fader.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

          def onAnimationUpdate(animation: ValueAnimator) {
            balls.get(1).setAlpha(animation.getAnimatedValue.asInstanceOf[java.lang.Float])
          }
        })
        val seq = AnimatorInflater.loadAnimator(appContext, R.anim.animator_set).asInstanceOf[AnimatorSet]
        seq.setTarget(balls.get(2))
        val colorizer = AnimatorInflater.loadAnimator(appContext, R.anim.color_animator).asInstanceOf[ObjectAnimator]
        colorizer.setTarget(balls.get(3))
        animation = new AnimatorSet()
        animation.asInstanceOf[AnimatorSet].playTogether(anim, fader, seq, colorizer)
      }
    }

    def startAnimation() {
      createAnimation()
      animation.start()
    }

    private def createBall(x: Float, y: Float): ShapeHolder = {
      val circle = new OvalShape()
      circle.resize(BALL_SIZE, BALL_SIZE)
      val drawable = new ShapeDrawable(circle)
      val shapeHolder = new ShapeHolder(drawable)
      shapeHolder.setX(x)
      shapeHolder.setY(y)
      shapeHolder
    }

    private def addBall(x: Float, y: Float, color: Int) {
      val shapeHolder = createBall(x, y)
      shapeHolder.setColor(color)
      balls.add(shapeHolder)
    }

    private def addBall(x: Float, y: Float) {
      val shapeHolder = createBall(x, y)
      val red = (100 + Math.random() * 155).toInt
      val green = (100 + Math.random() * 155).toInt
      val blue = (100 + Math.random() * 155).toInt
      val color = 0xff000000 | red << 16 | green << 8 | blue
      val paint = shapeHolder.getShape.getPaint
      val darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4
      val gradient = new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
      paint.setShader(gradient)
      balls.add(shapeHolder)
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
      val ball = balls.get(0)
      ball.setY(animation.getAnimatedValue.asInstanceOf[java.lang.Float])
    }
  }
}
