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

package com.raywenderlich.snowy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.raywenderlich.snowy.model.Tutorial
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private var tutorialPagerAdapter: TutorialPagerAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.kotlin_title)))
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.android_name)))
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.rxkotlin_name)))
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.kitura_name)))
    tutorialPagerAdapter = TutorialPagerAdapter(getTutorialData(), supportFragmentManager)
    viewPager.adapter = tutorialPagerAdapter
    viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager.currentItem = tab.position
      }

      override fun onTabUnselected(tab: TabLayout.Tab) {

      }

      override fun onTabReselected(tab: TabLayout.Tab) {

      }
    })
  }

  private fun getTutorialData(): List<Tutorial> {
    val tutorialList = arrayListOf<Tutorial>()
    tutorialList.add(Tutorial(getString(R.string.kotlin_title), getString(R.string.kotlin_url),
        getString(R.string.kotlin_desc)))
    tutorialList.add(Tutorial(getString(R.string.android_name), getString(R.string.android_url),
        getString(R.string.android_desc)))
    tutorialList.add(Tutorial(getString(R.string.rxkotlin_name), getString(R.string.rxkotlin_url),
        getString(R.string.rxkotlin_desc)))
    tutorialList.add(Tutorial(getString(R.string.kitura_name), getString(R.string.kitura_url),
        getString(R.string.kitura_desc)))
    return tutorialList
  }
}
