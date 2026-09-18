package com.bestmates.worldclock.ui.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import com.bestmates.worldclock.widget.SolarIconRenderer
import com.caverock.androidsvg.SVG

internal enum class WidgetStudyMaterial(val label:String, val opacity:Int) {
    Mist("Mist Glass", 168), Milk("Milk Crystal", 235), Frost("Dream Frost", 194), Veil("Jelly Veil", 215)
}

/** Static transparent bitmap composition, suitable for a future RemoteViews image layer.
 * This renderer is a review prototype; existing widget output/configuration never calls it.
 */
internal object GoldenSet3Renderer {
    fun celestial(context:Context, asset:AtelierCelestial, size:Int, tint:Int):Bitmap {
        if (asset.retained) return SolarIconRenderer.render(context,size,asset.isSun,asset.sun ?: SunIconStyle.BM_SUN_SIMPLE_FILL,asset.moon ?: MoonIconStyle.BM_MOON_PLAIN_FILL,tint)
        val large=(size*3).coerceAtLeast(120)
        val raw=Bitmap.createBitmap(large,large,Bitmap.Config.ARGB_8888)
        SVG.getFromString(requireNotNull(asset.svg)).apply {
            setDocumentWidth(large.toFloat()); setDocumentHeight(large.toFloat())
        }.renderToCanvas(Canvas(raw),RectF(0f,0f,large.toFloat(),large.toFloat()))
        val result=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888)
        val paint=Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply { colorFilter=PorterDuffColorFilter(tint,PorterDuff.Mode.SRC_IN) }
        Canvas(result).drawBitmap(raw,null,RectF(0f,0f,size.toFloat(),size.toFloat()),paint)
        raw.recycle()
        return result
    }

    fun objectBitmap(context:Context, obj:AtelierObject, material:WidgetStudyMaterial, dark:Boolean, celestial:AtelierCelestial?, content:Boolean=true, silhouette:Boolean=false):Bitmap {
        val bitmap=Bitmap.createBitmap(720,540,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        val height=minOf(448f,628f/obj.aspect)
        val width=height*obj.aspect
        val bounds=RectF((720-width)/2f,(540-height)/2f,(720+width)/2f,(540+height)/2f)
        val path=obj.path(bounds)
        val paint=Paint(Paint.ANTI_ALIAS_FLAG)
        val ink=if(dark) 0xfff7edf5.toInt() else 0xff403347.toInt()
        if(silhouette) {
            paint.color=if(dark) 0xffbaa3ca.toInt() else 0xff9177a4.toInt(); canvas.drawPath(path,paint)
            return bitmap
        }
        paint.color=0x01000000
        paint.setShadowLayer(if(dark) 8f else 14f,0f,6f,if(dark) 0x121d1228 else 0x243d2449)
        canvas.drawPath(path,paint); paint.clearShadowLayer()
        // Paint's alpha also multiplies shader colors; do not carry the shadow seed's 1/255 alpha.
        paint.color=Color.WHITE
        val alpha=material.opacity
        val top=if(dark) Color.argb(alpha,78,62,91) else when(material) {
            WidgetStudyMaterial.Veil -> Color.argb(alpha,233,202,226)
            WidgetStudyMaterial.Frost -> Color.argb(alpha,236,229,250)
            else -> Color.argb(alpha,255,249,246)
        }
        val bottom=if(dark) Color.argb(alpha,48,37,63) else when(material) {
            WidgetStudyMaterial.Veil -> Color.argb(alpha,196,171,216)
            WidgetStudyMaterial.Frost -> Color.argb(alpha,210,208,235)
            else -> Color.argb(alpha,226,216,233)
        }
        paint.shader=LinearGradient(0f,bounds.top,0f,bounds.bottom,top,bottom,Shader.TileMode.CLAMP)
        canvas.drawPath(path,paint)
        canvas.save(); canvas.clipPath(path)
        paint.shader=RadialGradient(bounds.centerX()-.12f*width,bounds.top+.22f*height,width*.65f,if(dark) 0x147e7199 else 0x80ffffff.toInt(),Color.TRANSPARENT,Shader.TileMode.CLAMP)
        canvas.drawRect(bounds,paint)
        // A directional edge, generated from the same path; no independent contour to drift.
        paint.style=Paint.Style.STROKE; paint.strokeWidth=2.7f
        paint.shader=LinearGradient(0f,bounds.top,0f,bounds.bottom,if(dark) 0x608f7ca3 else 0xd9ffffff.toInt(),if(dark) 0x907a628e.toInt() else 0x808f709e.toInt(),Shader.TileMode.CLAMP)
        canvas.drawPath(path,paint)
        paint.shader=null; paint.style=Paint.Style.FILL
        if(content) {
            val safe=obj.content
            val contentWidth=width*safe.width()
            val cx=bounds.left+width*safe.centerX()
            val topY=bounds.top+height*safe.top
            val contentHeight=height*safe.height()
            paint.typeface=Typeface.create("sans-serif",Typeface.NORMAL); paint.textAlign=Paint.Align.CENTER; paint.color=ink
            paint.textSize=minOf(24f,contentWidth/6f)
            canvas.drawText("Tokyo",cx-if(celestial!=null) 12f else 0f,topY+paint.textSize,paint)
            paint.typeface=Typeface.create("sans-serif",Typeface.BOLD)
            paint.textSize=minOf(57f,contentHeight*.41f)
            val measured=paint.measureText("18:40")
            if(measured>contentWidth) paint.textSize*=contentWidth/measured
            canvas.drawText("18:40",cx,topY+contentHeight*.61f,paint)
            paint.typeface=Typeface.create("sans-serif",Typeface.NORMAL); paint.textSize=minOf(20f,contentWidth/7f)
            canvas.drawText("Tue, Sep 8",cx,topY+contentHeight*.84f,paint)
            celestial?.let {
                val iconSize=minOf(30f,contentHeight*.18f).toInt().coerceAtLeast(20)
                val icon=celestial(context,it,iconSize,ink)
                // Stay inside the safe band: an icon above it can collide with a Heart's notch.
                canvas.drawBitmap(icon,cx+contentWidth*.30f-iconSize/2f,topY+1f,null); icon.recycle()
            }
        }
        canvas.restore()
        return bitmap
    }
}
