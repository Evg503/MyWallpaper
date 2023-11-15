package com.example.mywallpaper

import android.app.Service
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class MyWallPaperService : WallpaperService()
{
    override fun onCreateEngine() = MyWallEngine()

    fun dummyDraw(c: Canvas) {
        c.save()
        c.drawColor(Color.CYAN)
        c.restore()
    }

    inner class MyWallEngine:Engine() {

        fun drawSynchronously() = drawSynchronously(surfaceHolder)

        fun drawSynchronously(holder : SurfaceHolder){

            if (!isVisible) return

            var c: Canvas? = null
            try {
                c = holder.lockCanvas()
                c?.let {
                    dummyDraw(it)
                }
            } finally {
                c?.let {
                    holder.unlockCanvasAndPost(it)
                }
            }
        }
        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
        }

        override fun onDestroy() {
            super.onDestroy()
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder?) {
            super.onSurfaceRedrawNeeded(holder)
        }

        val redrawHandler = RedrawHandler()

        inner class RedrawHandler : Handler(Looper.getMainLooper()) {
            private val redraw = 1

            fun omitRedraw() {
                removeMessages(redraw)
            }

            fun planRedraw() {
                omitRedraw()
                sendEmptyMessage(redraw)
            }

            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    redraw -> drawSynchronously()

                    else -> super.handleMessage(msg)
                }
            }
        }
    }


}