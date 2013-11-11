package com.example.android.apis.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
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
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class ReversingAnimation extends SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_reversing)
    val container = findViewById(R.id.container).asInstanceOf[LinearLayout]
    val animView = new MyAnimationView(this)
    container.addView(animView)
    val starter = findViewById(R.id.startButton).asInstanceOf[Button]
    starter.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.startAnimation()
      }
    })
    val reverser = findViewById(R.id.reverseButton).asInstanceOf[Button]
    reverser.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        animView.reverseAnimation()
      }
    })
  }

  class MyAnimationView(context: Context) extends View(context) with ValueAnimator.AnimatorUpdateListener {

    val balls = new ArrayList[ShapeHolder]()

    var bounceAnim: ValueAnimator = null

    var ball: ShapeHolder = createBall(25, 25)

    private def createAnimation() {
      if (bounceAnim == null) {
        bounceAnim = ObjectAnimator.ofFloat(ball, "y", ball.getY, getHeight - 50f)
          .setDuration(1500)
        bounceAnim.setInterpolator(new AccelerateInterpolator(2f))
        bounceAnim.addUpdateListener(this)
      }
    }

    def startAnimation() {
      createAnimation()
      bounceAnim.start()
    }

    def reverseAnimation() {
      createAnimation()
      bounceAnim.reverse()
    }

    def seek(seekTime: Long) {
      createAnimation()
      bounceAnim.setCurrentPlayTime(seekTime)
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
  }
}
