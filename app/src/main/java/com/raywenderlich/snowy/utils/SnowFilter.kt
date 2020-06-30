/*
 * Copyright (c) 2019 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  distribute, sublicense, create a derivative work, and/or sell copies of the
 *  Software in any work that is designed, intended, or marketed for pedagogical or
 *  instructional purposes related to programming, coding, application development,
 *  or information technology.  Permission for such use, copying, modification,
 *  merger, publication, distribution, sublicensing, creation of derivative works,
 *  or sale is expressly withheld.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.raywenderlich.snowy.utils

import android.graphics.Bitmap
import android.graphics.Color
import java.util.*

object SnowFilter {
  var COLOR_MAX = 0xff

  fun applySnowEffect(source: Bitmap): Bitmap {
    // get image size
    val width = source.width
    val height = source.height
    val pixels = IntArray(width * height)
    // get pixel array from source
    source.getPixels(pixels, 0, width, 0, 0, width, height)
    // random object
    val random = Random()

    var R: Int
    var G: Int
    var B: Int
    var index: Int
    var threshHold: Int
    // iteration through pixels
    for (y in 0 until height) {
      for (x in 0 until width) {
        // get current index in 2D-matrix
        index = y * width + x
        // get color
        R = Color.red(pixels[index])
        G = Color.green(pixels[index])
        B = Color.blue(pixels[index])
        // generate threshold
        threshHold = random.nextInt(COLOR_MAX)
        if (R > threshHold && G > threshHold && B > threshHold) {
          pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX)
        }
      }
    }
    // output bitmap
    val bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    bitmapOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmapOut
  }
}