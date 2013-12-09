package com.example.android.apis.graphics

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ScrollView
import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
//remove if not needed
import scala.collection.JavaConversions._

class DensityActivity extends Activity {
  // Scaloid >>
  val LAYOUT_INFLATER_SERVICE = android.content.Context.LAYOUT_INFLATER_SERVICE
  val ScrollView_LayoutParams_MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT
  val LinearLayout_LayoutParams_MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT
  val LinearLayout_LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  // Scaloid <<

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val li = getSystemService(LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    this.setTitle(R.string.density_title)
    val root = new LinearLayout(this)
    root.setOrientation(LinearLayout.VERTICAL)
    var layout = new LinearLayout(this)
    addBitmapDrawable(layout, R.drawable.logo120dpi, true)
    addBitmapDrawable(layout, R.drawable.logo160dpi, true)
    addBitmapDrawable(layout, R.drawable.logo240dpi, true)
    addLabelToRoot(root, "Prescaled bitmap in drawable")
    addChildToRoot(root, layout)
    layout = new LinearLayout(this)
    addBitmapDrawable(layout, R.drawable.logo120dpi, false)
    addBitmapDrawable(layout, R.drawable.logo160dpi, false)
    addBitmapDrawable(layout, R.drawable.logo240dpi, false)
    addLabelToRoot(root, "Autoscaled bitmap in drawable")
    addChildToRoot(root, layout)
    layout = new LinearLayout(this)
    addResourceDrawable(layout, R.drawable.logo120dpi)
    addResourceDrawable(layout, R.drawable.logo160dpi)
    addResourceDrawable(layout, R.drawable.logo240dpi)
    addLabelToRoot(root, "Prescaled resource drawable")
    addChildToRoot(root, layout)
    layout = li.inflate(R.layout.density_image_views, null).asInstanceOf[LinearLayout]
    addLabelToRoot(root, "Inflated layout")
    addChildToRoot(root, layout)
    layout = li.inflate(R.layout.density_styled_image_views, null).asInstanceOf[LinearLayout]
    addLabelToRoot(root, "Inflated styled layout")
    addChildToRoot(root, layout)
    layout = new LinearLayout(this)
    addCanvasBitmap(layout, R.drawable.logo120dpi, true)
    addCanvasBitmap(layout, R.drawable.logo160dpi, true)
    addCanvasBitmap(layout, R.drawable.logo240dpi, true)
    addLabelToRoot(root, "Prescaled bitmap")
    addChildToRoot(root, layout)
    layout = new LinearLayout(this)
    addCanvasBitmap(layout, R.drawable.logo120dpi, false)
    addCanvasBitmap(layout, R.drawable.logo160dpi, false)
    addCanvasBitmap(layout, R.drawable.logo240dpi, false)
    addLabelToRoot(root, "Autoscaled bitmap")
    addChildToRoot(root, layout)
    layout = new LinearLayout(this)
    addResourceDrawable(layout, R.drawable.logonodpi120)
    addResourceDrawable(layout, R.drawable.logonodpi160)
    addResourceDrawable(layout, R.drawable.logonodpi240)
    addLabelToRoot(root, "No-dpi resource drawable")
    addChildToRoot(root, layout)
    layout = new LinearLayout(this)
    addNinePatchResourceDrawable(layout, R.drawable.smlnpatch120dpi)
    addNinePatchResourceDrawable(layout, R.drawable.smlnpatch160dpi)
    addNinePatchResourceDrawable(layout, R.drawable.smlnpatch240dpi)
    addLabelToRoot(root, "Prescaled 9-patch resource drawable")
    addChildToRoot(root, layout)
    setContentView(scrollWrap(root))
  }

  private def scrollWrap(view: View): View = {
    val scroller = new ScrollView(this)
    scroller.addView(view, new android.widget.FrameLayout.LayoutParams(ScrollView_LayoutParams_MATCH_PARENT, ScrollView_LayoutParams_MATCH_PARENT))
    scroller
  }

  private def addLabelToRoot(root: LinearLayout, text: String) {
    val label = new TextView(this)
    label.setText(text)
    root.addView(label, new LinearLayout.LayoutParams(LinearLayout_LayoutParams_MATCH_PARENT, LinearLayout_LayoutParams_WRAP_CONTENT))
  }

  private def addChildToRoot(root: LinearLayout, layout: LinearLayout) {
    root.addView(layout, new LinearLayout.LayoutParams(LinearLayout_LayoutParams_MATCH_PARENT, LinearLayout_LayoutParams_WRAP_CONTENT))
  }

  private def addBitmapDrawable(layout: LinearLayout, resource: Int, scale: Boolean) {
    var bitmap: Bitmap = null
    bitmap = loadAndPrintDpi(resource, scale)
    val view = new View(this)
    val d = new BitmapDrawable(getResources, bitmap)
    if (!scale) d.setTargetDensity(getResources.getDisplayMetrics)
    view.setBackgroundDrawable(d)
    view.setLayoutParams(new LinearLayout.LayoutParams(d.getIntrinsicWidth, d.getIntrinsicHeight))
    layout.addView(view)
  }

  private def addResourceDrawable(layout: LinearLayout, resource: Int) {
    val view = new View(this)
    val d = getResources.getDrawable(resource)
    view.setBackgroundDrawable(d)
    view.setLayoutParams(new LinearLayout.LayoutParams(d.getIntrinsicWidth, d.getIntrinsicHeight))
    layout.addView(view)
  }

  private def addCanvasBitmap(layout: LinearLayout, resource: Int, scale: Boolean) {
    var bitmap: Bitmap = null
    bitmap = loadAndPrintDpi(resource, scale)
    val view = new ScaledBitmapView(this, bitmap)
    view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout_LayoutParams_WRAP_CONTENT, LinearLayout_LayoutParams_WRAP_CONTENT))
    layout.addView(view)
  }

  private def addNinePatchResourceDrawable(layout: LinearLayout, resource: Int) {
    val view = new View(this)
    val d = getResources.getDrawable(resource)
    view.setBackgroundDrawable(d)
    Log.i("foo", "9-patch #" + java.lang.Integer.toHexString(resource) +
      " w=" +
      d.getIntrinsicWidth +
      " h=" +
      d.getIntrinsicHeight)
    view.setLayoutParams(new LinearLayout.LayoutParams(d.getIntrinsicWidth * 2, d.getIntrinsicHeight * 2))
    layout.addView(view)
  }

  private def loadAndPrintDpi(id: Int, scale: Boolean): Bitmap = {
    var bitmap: Bitmap = null
    if (scale) {
      bitmap = BitmapFactory.decodeResource(getResources, id)
    } else {
      val opts = new BitmapFactory.Options()
      opts.inScaled = false
      bitmap = BitmapFactory.decodeResource(getResources, id, opts)
    }
    bitmap
  }

  private class ScaledBitmapView(context: Context, private var mBitmap: Bitmap)
    extends View(context) {

    protected override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
      val metrics = getResources.getDisplayMetrics
      setMeasuredDimension(mBitmap.getScaledWidth(metrics), mBitmap.getScaledHeight(metrics))
    }

    protected override def onDraw(canvas: Canvas) {
      super.onDraw(canvas)
      canvas.drawBitmap(mBitmap, 0.0f, 0.0f, null)
    }
  }
}
