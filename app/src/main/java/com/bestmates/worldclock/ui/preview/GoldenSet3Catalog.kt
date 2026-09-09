package com.bestmates.worldclock.ui.preview

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import androidx.core.graphics.PathParser
import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.widget.BackgroundRenderer
import kotlin.math.cos
import kotlin.math.sin

/** Review handles only. Never serialize these IDs into WidgetConfig. */
internal data class AtelierCelestial(
    val id: String,
    val name: String,
    val group: String,
    val sun: SunIconStyle? = null,
    val moon: MoonIconStyle? = null,
    val svg: String? = null,
    val isSun: Boolean = sun != null,
) {
    val retained get() = sun != null || moon != null
}

internal data class AtelierObject(
    val id: String,
    val family: String,
    val name: String,
    val aspect: Float,
    val data: String? = null,
    val retained: WidgetShape? = null,
    // Safe content rectangle in normalized object coordinates, not its decorative bounding box.
    val content: RectF = RectF(.22f, .28f, .78f, .73f),
) {
    fun path(bounds: RectF): Path {
        retained?.let { return BackgroundRenderer.shapePath(it, bounds) }
        val master = requireNotNull(PathParser.createPathFromPathData(requireNotNull(data)))
        master.transform(Matrix().apply { setScale(bounds.width() / 100f, bounds.height() / 100f); postTranslate(bounds.left, bounds.top) })
        return master
    }
}

internal object GoldenSet3Catalog {
    // These existing files are displayed through the production SVG renderer, byte-for-byte unchanged.
    val suns = listOf(
        AtelierCelestial("golden3.sun.clean-fill", "Disc", "Essential", sun = SunIconStyle.BM_SUN_SIMPLE_FILL),
        AtelierCelestial("golden3.sun.clean-outline", "Pearl ring", "Essential", sun = SunIconStyle.BM_SUN_SIMPLE_ROUND),
        AtelierCelestial("golden3.sun.horizon", "Daybreak", "Atmosphere", sun = SunIconStyle.BM_SUN_HORIZON_FILL),
        AtelierCelestial("golden3.sun.cloud", "Cloud light", "Weather", sun = SunIconStyle.BM_SUN_CLOUD_OUTLINE),
        AtelierCelestial("golden3.sun.mist", "Morning mist", "Atmosphere", sun = SunIconStyle.BM_SUN_MIST),
        newSun("fine", "Fine rays", "Essential", circle(32f,32f,11f,false,3f) + rays(32f,32f,18f,25f,12,2.4f)),
        newSun("soft", "Soft rays", "Essential", circle(32f,32f,12f,true) + rays(32f,32f,20f,25f,8,5.2f)),
        newSun("linen", "Linen sun", "Dream", circle(32f,32f,13f,false,3.8f) + rays(32f,32f,21f,26f,16,2.4f)),
        newSun("veil", "Cloud veil", "Weather", circle(38f,23f,10f,true) + rays(38f,23f,15f,19f,8,3.6f) + "<path d='M12 48C5 48 4 36 13 34C13 20 34 19 39 31C52 26 61 46 48 48Z' fill='white' stroke='black' stroke-width='3.8' stroke-linejoin='round'/>"),
    )

    val moons = listOf(
        AtelierCelestial("golden3.moon.clean-fill", "Classic filled", "Essential", moon = MoonIconStyle.BM_MOON_PLAIN_FILL),
        AtelierCelestial("golden3.moon.clean-outline", "Classic outline", "Essential", moon = MoonIconStyle.BM_MOON_PLAIN_OUTLINE),
        AtelierCelestial("golden3.moon.paired", "Paired jewels", "Dream", moon = MoonIconStyle.BM_MOON_STARS_OUTLINE),
        AtelierCelestial("golden3.moon.cloud", "Cloud companion", "Weather", moon = MoonIconStyle.BM_MOON_CLOUD_OUTLINE),
        AtelierCelestial("golden3.moon.horizon", "Nightfall", "Atmosphere", moon = MoonIconStyle.BM_MOON_HORIZON_FILL),
        AtelierCelestial("golden3.moon.layered", "Layered moon", "Charm", moon = MoonIconStyle.BM_MOON_DOUBLE),
        newMoon("broad", "Velvet crescent", "Essential", filled(broadMoon)),
        newMoon("slender", "Silk crescent", "Essential", filled("M38 6C21 9 9 24 13 40C17 57 40 63 54 47C38 54 23 43 22 30C21 19 28 11 38 6Z")),
        newMoon("jewel", "Jewel crescent", "Dream", outline(broadMoon,3.7f) + sparkle(48f,14f,6f)),
        newMoon("cluster", "Constellation", "Dream", filled("M32 8C17 11 8 23 12 38C16 53 35 58 47 46C31 49 20 37 22 24C23 17 27 12 32 8Z") + sparkle(45f,12f,5f) + sparkle(53f,29f,3.5f)),
        newMoon("frost", "Frosted halo", "Dream", filled("M32 9C16 11 8 27 14 41C20 54 37 55 47 45C33 47 24 38 23 27C22 19 26 13 32 9Z") + "<path d='M20 5C5 13 2 36 14 50C26 63 45 58 55 46' fill='none' stroke='black' stroke-width='2.6' stroke-linecap='round'/>") ,
        newMoon("puffy", "Pillow moon", "Charm", filled("M38 7C23 3 8 16 8 33C8 50 24 60 40 55C48 53 54 47 56 40C56 38 54 38 52 39C40 44 27 37 25 25C24 18 29 11 36 10C39 10 40 8 38 7Z")),
        newMoon("mist", "Moonlit mist", "Atmosphere", filled("M34 6C20 7 10 17 12 30C14 40 24 44 34 40C25 35 22 29 23 22C24 15 28 10 34 6Z") + "<path d='M8 47H54M17 55H46' fill='none' stroke='black' stroke-width='4' stroke-linecap='round'/>") ,
        newMoon("symbol", "Crescent & Star", "Symbolic", filled("M35 6C18 6 7 17 7 32C7 48 20 59 36 57C43 56 49 52 53 46C38 51 23 40 23 26C23 17 28 10 35 6Z") + star(47f,22f,8f)),
    )

    val objects = listOf(
        AtelierObject("golden3.heart.classic", "Heart", "Classic", 1f, retained = WidgetShape.HEART, content = RectF(.24f,.29f,.76f,.64f)),
        obj("Heart","Puffy",1.18f,"M50 94C41 91 2 68 2 35C2 5 33 1 50 23C67 1 98 5 98 35C98 68 59 91 50 94Z",RectF(.22f,.30f,.78f,.68f)),
        obj("Heart","Long",.78f,"M50 98C40 84 3 60 3 29C3 4 34 0 50 21C66 0 97 4 97 29C97 60 60 84 50 98Z",RectF(.24f,.27f,.76f,.61f)),
        obj("Heart","Ribbon",1.1f,"M44 96C28 77 1 57 5 28C8 4 34 1 48 24C64 0 97 8 97 32C97 66 60 77 44 96Z",RectF(.23f,.30f,.77f,.65f)),
        obj("Heart","Soft",1.35f,"M50 91C38 89 3 69 3 36C3 8 34 4 50 24C66 4 97 8 97 36C97 69 62 89 50 91Z",RectF(.23f,.30f,.77f,.68f)),
        obj("Cloud","Classic",1.6f,"M23 88C9 88 2 77 2 63C2 48 12 37 26 38C24 18 39 9 52 13C64 15 70 24 72 36C87 32 98 43 98 61C98 78 87 88 73 88Z",RectF(.21f,.38f,.79f,.76f)),
        obj("Cloud","Calm",2.05f,"M23 88C8 88 2 73 4 58C6 42 19 32 36 40C42 16 66 15 73 40C92 36 99 51 97 65C96 80 85 88 72 88Z",RectF(.18f,.43f,.82f,.77f)),
        obj("Cloud","Puffy",1.25f,"M25 93C10 93 1 77 3 61C4 51 10 44 19 40C16 17 36 3 52 14C65 1 88 16 86 38C103 50 99 74 87 84C72 97 40 93 25 93Z",RectF(.24f,.36f,.76f,.75f)),
        obj("Cloud","Dream",1.65f,"M27 92C10 90 0 77 4 62C6 49 17 45 26 47C21 22 43 9 60 19C72 13 91 26 88 43C104 58 98 79 82 84C63 89 48 94 27 92Z",RectF(.23f,.42f,.79f,.77f)),
        obj("Arch","Classic",.9f,arch(42f,4f),RectF(.18f,.32f,.82f,.81f)),
        obj("Arch","Soft",1.25f,arch(38f,14f),RectF(.18f,.30f,.82f,.80f)),
        obj("Arch","Tall",.66f,arch(30f,5f),RectF(.15f,.33f,.85f,.75f)),
        obj("Arch","Gallery",1.6f,arch(55f,8f),RectF(.18f,.40f,.82f,.81f)),
        obj("Stationery","Ticket",1.65f,ticket(4f,10f),RectF(.18f,.22f,.82f,.79f)),
        obj("Stationery","Atelier",1.35f,ticket(10f,17f),RectF(.23f,.22f,.77f,.79f)),
        obj("Stationery","Postcard",1.6f,"M13 3H84Q97 3 97 17V81Q97 97 82 97H18Q3 97 3 82V16Q3 3 13 3Z",RectF(.17f,.21f,.83f,.79f)),
        obj("Stationery","Note",.94f,"M16 3H82Q97 3 97 17V76Q97 86 88 91L77 97H17Q3 97 3 83V17Q3 3 16 3Z",RectF(.18f,.22f,.82f,.77f)),
        obj("Organic","Pebble",1.3f,"M16 17C33 0 67 0 86 20C103 40 99 72 81 88C62 103 20 97 7 77C-2 62 1 34 16 17Z",RectF(.21f,.27f,.79f,.73f)),
        AtelierObject("golden3.organic.petal", "Organic", "Petal", .88f, retained = WidgetShape.PETAL, content = RectF(.29f,.20f,.71f,.52f)),
    )

    /** On-device structural check: closed nonempty contours, finite in-bounds geometry and unique review IDs. */
    fun validate(): List<String> = buildList {
        val ids = (suns + moons).map { it.id } + objects.map { it.id }
        if (ids.distinct().size != ids.size) add("Duplicate candidate IDs")
        objects.forEach { obj ->
            val p = obj.path(RectF(0f,0f,100f,100f))
            val measure = PathMeasure(p, false)
            if (!measure.isClosed || measure.length <= 0f) add("${obj.name}: open or empty contour")
            val point = FloatArray(2)
            repeat(256) { i ->
                if (!measure.getPosTan(measure.length*i/256f, point, null) || point.any { !it.isFinite() }) add("${obj.name}: invalid path")
                if(point.any { it < -.1f || it > 100.1f }) add("${obj.name}: bounds")
            }
        }
    }
}

private fun obj(family:String,name:String,aspect:Float,path:String,content:RectF) = AtelierObject("golden3.${family.lowercase()}.${name.lowercase()}",family,name,aspect,path,content=content)
private fun arch(shoulder:Float,corner:Float) = "M3 $shoulder C3 13 24 3 50 3C76 3 97 13 97 $shoulder V${97-corner}Q97 97 ${97-corner} 97H${3+corner}Q3 97 3 ${97-corner}Z"
private fun ticket(depth:Float,corner:Float) = "M${3+corner} 3H${97-corner}Q97 3 97 ${3+corner}V36C97 42 ${97-depth} 42 ${97-depth} 50C${97-depth} 58 97 58 97 64V${97-corner}Q97 97 ${97-corner} 97H${3+corner}Q3 97 3 ${97-corner}V64C3 58 ${3+depth} 58 ${3+depth} 50C${3+depth} 42 3 42 3 36V${3+corner}Q3 3 ${3+corner} 3Z"
private const val broadMoon = "M38 7C24 3 9 14 7 29C5 46 19 59 35 57C46 56 55 48 58 37C47 46 31 40 27 27C24 18 29 10 38 7Z"
private fun svg(body:String) = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 64 64'>$body</svg>"
private fun filled(path:String) = "<path d='$path' fill='black' stroke='black' stroke-width='1.4' stroke-linejoin='round'/>"
private fun outline(path:String,width:Float) = "<path d='$path' fill='none' stroke='black' stroke-width='$width' stroke-linecap='round' stroke-linejoin='round'/>"
private fun newSun(id:String,name:String,group:String,body:String) = AtelierCelestial("golden3.sun.$id",name,group,svg=svg(body),isSun=true)
private fun newMoon(id:String,name:String,group:String,body:String) = AtelierCelestial("golden3.moon.$id",name,group,svg=svg(body))
private fun circle(x:Float,y:Float,r:Float,fill:Boolean,width:Float=3.5f) = "<circle cx='$x' cy='$y' r='$r' fill='${if(fill) "black" else "none"}' stroke='black' stroke-width='$width'/>"
private fun rays(x:Float,y:Float,inner:Float,outer:Float,count:Int,width:Float):String = buildString {
    append("<g stroke='black' stroke-width='$width' stroke-linecap='round'>")
    repeat(count) { i -> val a = i*Math.PI*2/count; append("<path d='M${x+cos(a)*inner} ${y+sin(a)*inner}L${x+cos(a)*outer} ${y+sin(a)*outer}'/>") }
    append("</g>")
}
private fun sparkle(x:Float,y:Float,r:Float) = "<path d='M$x ${y-r}Q${x+r*.24f} ${y-r*.24f} ${x+r} $y Q${x+r*.24f} ${y+r*.24f} $x ${y+r} Q${x-r*.24f} ${y+r*.24f} ${x-r} $y Q${x-r*.24f} ${y-r*.24f} $x ${y-r}Z' fill='black'/>"
private fun star(x:Float,y:Float,r:Float):String = buildString {
    append("<path d='")
    repeat(10) { i -> val a = -Math.PI/2+i*Math.PI/5; val radius=if(i%2==0) r else r*.43f; append("${if(i==0) "M" else "L"}${x+cos(a)*radius} ${y+sin(a)*radius}") }
    append("Z' fill='black'/>")
}
