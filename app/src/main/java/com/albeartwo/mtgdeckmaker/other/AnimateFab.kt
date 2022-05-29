package com.albeartwo.mtgdeckmaker.other

import android.view.View
import android.view.ViewTreeObserver

import com.google.android.material.floatingactionbutton.FloatingActionButton




class AnimateFab {

    companion object {

    fun showFabWithAnimation(fab : FloatingActionButton , delay : Int) {
        fab.visibility = View.INVISIBLE
        fab.scaleX = 0.0f
        fab.scaleY = 0.0f
        fab.alpha = 0.0f
        fab.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw() : Boolean {
                fab.viewTreeObserver.removeOnPreDrawListener(this)
                fab.postDelayed({ fab.show() } , delay.toLong())
                return true
            }
        })
    }}

}