/*
 * Copyright (C) 2008 The Android Open Source Project
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
package com.example.android.apis.os

/** Class that implements the text to morse code coversion */
object MorseCodeConverter {
  private final val SPEED_BASE: Long = 100
  private[os] final val DOT: Long = SPEED_BASE
  private[os] final val DASH: Long = SPEED_BASE * 3
  private[os] final val GAP: Long = SPEED_BASE
  private[os] final val LETTER_GAP: Long = SPEED_BASE * 3
  private[os] final val WORD_GAP: Long = SPEED_BASE * 7
  /** The characters from 'A' to 'Z' */
  private final val LETTERS: Array[Array[Long]] = Array[Array[Long]](Array[Long](DOT, GAP, DASH), Array[Long](DASH, GAP, DOT, GAP, DOT, GAP, DOT), Array[Long](DASH, GAP, DOT, GAP, DASH, GAP, DOT), Array[Long](DASH, GAP, DOT, GAP, DOT), Array[Long](DOT), Array[Long](DOT, GAP, DOT, GAP, DASH, GAP, DOT), Array[Long](DASH, GAP, DASH, GAP, DOT), Array[Long](DOT, GAP, DOT, GAP, DOT, GAP, DOT), Array[Long](DOT, GAP, DOT), Array[Long](DOT, GAP, DASH, GAP, DASH, GAP, DASH), Array[Long](DASH, GAP, DOT, GAP, DASH), Array[Long](DOT, GAP, DASH, GAP, DOT, GAP, DOT), Array[Long](DASH, GAP, DASH), Array[Long](DASH, GAP, DOT), Array[Long](DASH, GAP, DASH, GAP, DASH), Array[Long](DOT, GAP, DASH, GAP, DASH, GAP, DOT), Array[Long](DASH, GAP, DASH, GAP, DOT, GAP, DASH), Array[Long](DOT, GAP, DASH, GAP, DOT), Array[Long](DOT, GAP, DOT, GAP, DOT), Array[Long](DASH), Array[Long](DOT, GAP, DOT, GAP, DASH), Array[Long](DOT, GAP, DOT, GAP, DOT, GAP, DASH), Array[Long](DOT, GAP, DASH, GAP, DASH), Array[Long](DASH, GAP, DOT, GAP, DOT, GAP, DASH), Array[Long](DASH, GAP, DOT, GAP, DASH, GAP, DASH), Array[Long](DASH, GAP, DASH, GAP, DOT, GAP, DOT))
  /** The characters from '0' to '9' */
  private final val NUMBERS: Array[Array[Long]] = Array[Array[Long]](Array[Long](DASH, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DASH), Array[Long](DOT, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DASH), Array[Long](DOT, GAP, DOT, GAP, DASH, GAP, DASH, GAP, DASH), Array[Long](DOT, GAP, DOT, GAP, DOT, GAP, DASH, GAP, DASH), Array[Long](DOT, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DASH), Array[Long](DOT, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DOT), Array[Long](DASH, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DOT), Array[Long](DASH, GAP, DASH, GAP, DOT, GAP, DOT, GAP, DOT), Array[Long](DASH, GAP, DASH, GAP, DASH, GAP, DOT, GAP, DOT), Array[Long](DASH, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DOT))
  private final val ERROR_GAP: Array[Long] = Array[Long](GAP)

  /** Return the pattern data for a given character */
  private[os] def pattern(c: Char): Array[Long] = {
    if (c >= 'A' && c <= 'Z') {
      return LETTERS(c - 'A')
    }
    if (c >= 'a' && c <= 'z') {
      return LETTERS(c - 'a')
    }
    else if (c >= '0' && c <= '9') {
      return NUMBERS(c - '0')
    }
    else {
      return ERROR_GAP
    }
  }

  private[os] def pattern(str: String): Array[Long] = {
    var lastWasWhitespace: Boolean = false
    val strlen: Int = str.length
    var len: Int = 1
    lastWasWhitespace = true

    var i: Int = 0
    while (i < strlen) {
      {
        val c: Char = str.charAt(i)
        if (Character.isWhitespace(c)) {
          if (!lastWasWhitespace) {
            len += 1
            lastWasWhitespace = true
          }
        }
        else {
          if (!lastWasWhitespace) {
            len += 1
          }
          lastWasWhitespace = false
          len += pattern(c).length
        }
      }
      ({
        i += 1; i - 1
      })
    }

    val result: Array[Long] = new Array[Long](len + 1)
    result(0) = 0
    var pos: Int = 1
    lastWasWhitespace = true

    i = 0
    while (i < strlen) {
      {
        val c: Char = str.charAt(i)
        if (Character.isWhitespace(c)) {
          if (!lastWasWhitespace) {
            result(pos) = WORD_GAP
            pos += 1
            lastWasWhitespace = true
          }
        }
        else {
          if (!lastWasWhitespace) {
            result(pos) = LETTER_GAP
            pos += 1
          }
          lastWasWhitespace = false
          val letter: Array[Long] = pattern(c)
          System.arraycopy(letter, 0, result, pos, letter.length)
          pos += letter.length
        }
      }
      ({
        i += 1; i - 1
      })
    }

    return result
  }


}