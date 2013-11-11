package com.example.android.apis.animation

import android.widget.Button
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
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import java.util.ArrayList
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class AnimationCloning extends SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_cloning)
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

    private var mDensity: Float = getContext.getResources.getDisplayMetrics.density

    val ball0 = addBall(50f, 25f)

    val ball1 = addBall(150f, 25f)

    val ball2 = addBall(250f, 25f)

    val ball3 = addBall(350f, 25f)

    private def createAnimation() {
      if (animation == null) {
        val anim1 = ObjectAnimator.ofFloat(balls.get(0), "y", 0f, getHeight - balls.get(0).getHeight)
          .setDuration(500)
        val anim2 = anim1.clone()
        anim2.setTarget(balls.get(1))
        anim1.addUpdateListener(this)
        val ball2 = balls.get(2)
        val animDown = ObjectAnimator.ofFloat(ball2, "y", 0f, getHeight - ball2.getHeight)
          .setDuration(500)
        animDown.setInterpolator(new AccelerateInterpolator())
        val animUp = ObjectAnimator.ofFloat(ball2, "y", getHeight - ball2.getHeight, 0f)
          .setDuration(500)
        animUp.setInterpolator(new DecelerateInterpolator())
        val s1 = new AnimatorSet()
        s1.playSequentially(animDown, animUp)
        animDown.addUpdateListener(this)
        animUp.addUpdateListener(this)
        val s2 = s1.clone().asInstanceOf[AnimatorSet]
        s2.setTarget(balls.get(3))
        animation = new AnimatorSet()
        animation.playTogether(anim1, anim2, s1)
        animation.playSequentially(s1, s2)
      }
    }

    private def addBall(x: Float, y: Float): ShapeHolder = {
      val circle = new OvalShape()
      circle.resize(50f * mDensity, 50f * mDensity)
      val drawable = new ShapeDrawable(circle)
      val shapeHolder = new ShapeHolder(drawable)
      shapeHolder.setX(x - 25f)
      shapeHolder.setY(y - 25f)
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
      for (i <- 0 until balls.size) {
        val shapeHolder = balls.get(i)
        canvas.save()
        canvas.translate(shapeHolder.getX, shapeHolder.getY)
        shapeHolder.getShape.draw(canvas)
        canvas.restore()
      }
    }

    def startAnimation() {
      createAnimation()
      animation.start()
    }

    def onAnimationUpdate(animation: ValueAnimator) {
      invalidate()
    }
  }
}
