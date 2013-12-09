/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.apis.animation

import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.View

/**
 * A data structure that holds a Shape and various properties that can be used to define
 * how the shape is drawn.
 */
class ShapeHolder {
  private var x: Float = 0
  private var y: Float = 0
  private var shape: ShapeDrawable = null
  private var color: Int = 0
  private var gradient: RadialGradient = null
  private var alpha: Float = 1f
  private var paint: Paint = null

  def setPaint(value: Paint) {
    paint = value
  }

  def getPaint: Paint = {
    return paint
  }

  def setX(value: Float) {
    x = value
  }

  def getX: Float = {
    return x
  }

  def setY(value: Float) {
    y = value
  }

  def getY: Float = {
    return y
  }

  def setShape(value: ShapeDrawable) {
    shape = value
  }

  def getShape: ShapeDrawable = {
    return shape
  }

  def getColor: Int = {
    return color
  }

  def setColor(value: Int) {
    shape.getPaint.setColor(value)
    color = value
  }

  def setGradient(value: RadialGradient) {
    gradient = value
  }

  def getGradient: RadialGradient = {
    return gradient
  }

  def setAlpha(alpha: Float) {
    this.alpha = alpha
    shape.setAlpha(((alpha * 255f) + .5f).asInstanceOf[Int])
  }

  def getWidth: Float = {
    return shape.getShape.getWidth
  }

  def setWidth(width: Float) {
    val s: Shape = shape.getShape
    s.resize(width, s.getHeight)
  }

  def getHeight: Float = {
    return shape.getShape.getHeight
  }

  def setHeight(height: Float) {
    val s: Shape = shape.getShape
    s.resize(s.getWidth, height)
  }

  def this(s: ShapeDrawable) {
    this()
    shape = s
  }
}