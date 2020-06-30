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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.raywenderlich.snowy.model.Tutorial
import com.raywenderlich.snowy.utils.SnowFilter
import kotlinx.android.synthetic.main.fragment_tutorial.*
import kotlinx.coroutines.*
import java.net.URL

class TutorialFragment : Fragment() {

    companion object {

        const val TUTORIAL_KEY = "TUTORIAL"

        fun newInstance(tutorial: Tutorial): TutorialFragment {
            val fragmentHome = TutorialFragment()
            val args = Bundle()
            args.putParcelable(TUTORIAL_KEY, tutorial)
            fragmentHome.arguments = args
            return fragmentHome
        }
    }

    //Initiating a parent job
    private val parentJob = Job()

    // Defining coroutine exception handler to log exceptions
    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            //Creating a coroutine on the main thread to show error message on the UI
            coroutineScope.launch(Dispatchers.Main) {
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = getString(R.string.error_message)
            }
            //GlobeScope won't be destroyed with the UI, so exceptions can be logged here as well
            GlobalScope.launch { println("Caught $throwable") }
        }

    //Defining the scope with main thread and parent job
    //Any exceptions in a coroutine started, they will be logged and displayed in a text view
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tutorial = arguments?.getParcelable(TUTORIAL_KEY) as Tutorial
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)
        view.findViewById<TextView>(R.id.tutorialName).text = tutorial.name
        view.findViewById<TextView>(R.id.tutorialDesc).text = tutorial.description
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tutorial = arguments?.getParcelable(TUTORIAL_KEY) as Tutorial

        //Launch: Implementing methods on Dispatcher.Main - used for UI-related events
        coroutineScope.launch(Dispatchers.Main) {
            //The green arrow in the next 2 lines indicate that these are suspension points

            //await() is a suspending function
            //await() suspends launch until the methods return a value
            //await() can only be called from withing a coroutine scope or another suspending function
//            val originalBitmap = getOriginalBitmapAsync(tutorial).await()
//            val snowFilterBitmap = loadSnowFilterAsync(originalBitmap).await()

            //No need to call await when using withContext
            val originalBitmap = getOriginalBitmapAsync(tutorial)
            val snowFilterBitmap = loadSnowFilterAsync(originalBitmap)

            loadImage(snowFilterBitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Cancels and clears up all the coroutines launched with the coroutinesScope
        parentJob.cancel()
        //Once a job is cancelled, it cannot be reused. Need to create a new one
    }

    //coroutineScope.async: Gets executed on a worker thread
    //Getting original bitmap on Dispatchers.IO - used for networking-related work
    //withContext: Instead of deferring the value, the functions are marked with suspend
    private suspend fun getOriginalBitmapAsync(tutorial: Tutorial): Bitmap =
        withContext(Dispatchers.IO) {
            URL(tutorial.url).openStream().use {
                return@withContext BitmapFactory.decodeStream(it)
            }
        }

    //coroutineScope.async: Gets executed on a worker thread
    //Applying snow effect filter on Dispatcher.Default - user for CPU-intensive work
    //withContext: Instead of deferring the value, the functions are marked with suspend
    private suspend fun loadSnowFilterAsync(originalBitmap: Bitmap): Bitmap =
        withContext(Dispatchers.Default) {
            SnowFilter.applySnowEffect(originalBitmap)
        }

    //Set image with applied snow filter
    private fun loadImage(snowFilterBitmap: Bitmap) {
        progressBar.visibility = View.GONE
        snowFilterImage.setImageBitmap(snowFilterBitmap)
    }
}
