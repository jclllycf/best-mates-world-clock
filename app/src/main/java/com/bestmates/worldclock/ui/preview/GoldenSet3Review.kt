@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
package com.bestmates.worldclock.ui.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.BestMatesCanvasReview
import com.bestmates.worldclock.BestMatesProductionToolbarReview
import com.bestmates.worldclock.ProductionPinStudy
import com.bestmates.worldclock.ProductionRotateStudy
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign

@Composable
internal fun GoldenSet3Review() {
    var page by rememberSaveable { mutableStateOf("Objects") }
    var family by rememberSaveable { mutableStateOf("Heart") }
    var objectId by rememberSaveable { mutableStateOf(GoldenSet3Catalog.objects.first().id) }
    var materialIndex by rememberSaveable { mutableIntStateOf(1) }
    var celestialId by rememberSaveable { mutableStateOf(GoldenSet3Catalog.moons.first().id) }
    val obj=GoldenSet3Catalog.objects.first { it.id==objectId }
    val celestial=(GoldenSet3Catalog.suns+GoldenSet3Catalog.moons).first { it.id==celestialId }
    val material=WidgetStudyMaterial.entries[materialIndex]
    val failures=remember { GoldenSet3Catalog.validate() }
    ReviewPanel {
        ReviewTitle("A little world of your own", "Golden Set 3 · visual candidates")
        ReviewText("Tokyo · 18:40 · Tue, Sep 8 are fixed review specimens. Try a silhouette, choose a material, then see it on a wallpaper.")
        if(failures.isNotEmpty()) ReviewText(failures.joinToString(), true)
    }
    ReviewChoices(listOf("Objects","Celestial","Orbit","Work mat","Wallpapers","Sources"),page) { page=it }
    when(page) {
        "Objects" -> {
            ReviewChoices(GoldenSet3Catalog.objects.map { it.family }.distinct(),family) { next ->
                family=next; objectId=GoldenSet3Catalog.objects.first { it.family==next }.id
            }
            ReviewPanel {
                ReviewTitle("${obj.family} / ${obj.name}",if(obj.retained!=null) "Existing canonical master" else "Original candidate · intrinsic proportions")
                ObjectScene(obj,material,celestial,0,Modifier.fillMaxWidth().height(224.dp))
                ReviewChoices(WidgetStudyMaterial.entries.map { it.label },material.label) { label -> materialIndex=WidgetStudyMaterial.entries.indexOfFirst { it.label==label } }
                ReviewText("Material changes rendering only; it never creates another Shape ID. The wallpaper stays outside the transparent widget bitmap.")
            }
            GoldenSet3Catalog.objects.filter { it.family==family }.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    pair.forEach { item ->
                        ReviewTile(item.name,item.id==objectId,Modifier.weight(1f),{objectId=item.id}) {
                            ObjectImage(item,material,null,false,Modifier.fillMaxWidth().height(86.dp))
                        }
                    }
                    if(pair.size==1) Spacer(Modifier.weight(1f))
                }
            }
            ReviewPanel {
                ReviewTitle("One master, two scales", "Picker miniature → canonical silhouette")
                Row(verticalAlignment=Alignment.CenterVertically) {
                    ObjectImage(obj,material,null,false,Modifier.weight(1f).height(80.dp),silhouette=true)
                    ObjectImage(obj,material,null,false,Modifier.weight(2f).height(140.dp),silhouette=true)
                }
                ReviewText("${obj.aspect}:1 intrinsic aspect · material and content use this exact path.")
            }
        }
        "Celestial" -> CelestialWardrobe(obj,material,celestial) { celestialId=it.id }
        "Orbit" -> {
            ReviewPanel {
                ReviewTitle("Quiet, familiar tools", "Actual production controls · artwork only")
                BestMatesProductionToolbarReview()
                ReviewText("Undo / Redo keep Golden Set 2 geometry. Pin keeps its production drawing; Rotate uses the approved Future canvas language master. Global Settings and local Adjust have distinct meanings.")
            }
            val icons=listOf("Undo" to OrbitIcons.Undo,"Redo" to OrbitIcons.Redo,"Settings" to OrbitIcons.Settings,"Adjust" to OrbitIcons.Adjust,"Font" to OrbitIcons.Font,"Moon" to OrbitIcons.Moon)
            icons.chunked(2).forEach { row -> Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                row.forEach { (name,icon) -> ReviewTile(name,false,Modifier.weight(1f)) { OrbitSizes(icon,name) } }
            } }
            Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                ReviewTile("Pin · retained",false,Modifier.weight(1f)) { Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) { listOf(16,20,24).forEach { ProductionPinStudy(it.dp) } } }
                ReviewTile("Rotate · approved",false,Modifier.weight(1f)) { Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) { listOf(16,20,24).forEach { ProductionRotateStudy(it.dp) } } }
            }
            ReviewText("Left to right: 16 / 20 / 24dp. Apply actions use text only; checks remain selection/success symbols.")
        }
        "Work mat" -> {
            ReviewTitle("Soft Atelier Work Mat", "Live geometry engine · disposable local scene")
            BestMatesCanvasReview()
            ReviewText("Fixed canvas above a scrolling inspector. This scene writes no project. Existing drag, resize, rotation and snapping behavior is unchanged; TASK-007 remains deferred.")
        }
        "Wallpapers" -> {
            ReviewTitle("At home on your screen", "${obj.family} / ${obj.name} · ${material.label}")
            listOf("Warm pearl","Cool linen","Evening plum","Color study").forEachIndexed { index,label ->
                ReviewPanel {
                    ReviewText(label)
                    ObjectScene(obj,material,celestial,index,Modifier.fillMaxWidth().height(236.dp))
                }
            }
        }
        else -> {
            ReviewPanel {
                ReviewTitle("Keep from the collection", "Actual bundled artwork, unchanged")
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) {
                    listOf(GoldenSet3Catalog.suns[0],GoldenSet3Catalog.suns[2],GoldenSet3Catalog.moons[0],GoldenSet3Catalog.moons[2]).forEach { CelestialImage(it,40) }
                }
                ReviewText("Classic Heart comes from BackgroundRenderer. Existing celestial masters use SolarIconRenderer and retain all persisted IDs.")
            }
            ReviewPanel {
                ReviewTitle("Reference language", "Private screenshots · visual study only")
                ReviewText("Broad crescents, open counters, soft ray ends, readable jewel companions and horizontal atmosphere. Unknown-license Kustom images are only in the private local study board; they are never bundled or traced.")
            }
            ReviewPanel {
                ReviewTitle("New Best Mates candidates", "Original code · no network or new fonts")
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) {
                    listOf(GoldenSet3Catalog.suns[5],GoldenSet3Catalog.suns[6],GoldenSet3Catalog.moons[6],GoldenSet3Catalog.moons[8]).forEach { CelestialImage(it,40) }
                }
                ReviewText("${GoldenSet3Catalog.suns.size} Suns · ${GoldenSet3Catalog.moons.size} Moons · ${GoldenSet3Catalog.objects.size} objects. ${if(failures.isEmpty()) "Closed contours and path bounds checked on this device." else "Geometry review required."}")
                ReviewText("Type Wardrobe remains device-font dependent: no new font collection is claimed. Final visual acceptance belongs to you.")
            }
        }
    }
}

@Composable private fun CelestialWardrobe(obj:AtelierObject,material:WidgetStudyMaterial,selected:AtelierCelestial,onSelect:(AtelierCelestial)->Unit) {
    var kind by rememberSaveable { mutableStateOf("Moon") }
    var group by rememberSaveable { mutableStateOf("All") }
    ReviewChoices(listOf("Sun","Moon"),kind) { next ->
        kind=next
        group="All"
        onSelect(if(next=="Sun") GoldenSet3Catalog.suns.first() else GoldenSet3Catalog.moons.first())
    }
    ReviewChoices(listOf("All","Essential","Dream","Atmosphere","Weather","Charm","Symbolic"),group) { next ->
        group=next
        val pool=if(kind=="Sun") GoldenSet3Catalog.suns else GoldenSet3Catalog.moons
        if(next!="All" && selected.group!=next) pool.firstOrNull { it.group==next }?.let(onSelect)
    }
    ReviewPanel {
        ReviewTitle(selected.name,if(selected.retained) "From the existing collection" else "New Best Mates study")
        ObjectScene(obj,material,selected,2,Modifier.fillMaxWidth().height(176.dp))
        if(selected.group=="Symbolic") ReviewText("Optional Crescent & Star: a historical symbol associated with many Islamic visual cultures, not a universal religious emblem or the default Moon.")
    }
    val assets=(if(kind=="Sun") GoldenSet3Catalog.suns else GoldenSet3Catalog.moons).filter { group=="All" || it.group==group }
    if(assets.isEmpty()) ReviewText("No candidate in this group yet.")
    assets.chunked(2).forEach { pair -> Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        pair.forEach { asset -> ReviewTile(asset.name,selected.id==asset.id,Modifier.weight(1f),{onSelect(asset)}) {
            Row(Modifier.fillMaxWidth().height(50.dp),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically) { listOf(20,28,40).forEach { CelestialImage(asset,it) } }
            ReviewText(if(asset.retained) "Collection" else "New study")
        } }
        if(pair.size==1) Spacer(Modifier.weight(1f))
    } }
    ReviewText("20 / 28 / 40dp, from left to right. Tap a candidate to judge it in the widget immediately.")
}

@Composable private fun CelestialImage(asset:AtelierCelestial,size:Int) {
    val context=LocalContext.current; val pixels=with(LocalDensity.current) { size.dp.roundToPx() }
    val color=BestMatesDesign.colors.textPrimary.toArgb()
    val bitmap=remember(asset.id,pixels,color) { GoldenSet3Renderer.celestial(context,asset,pixels,color).asImageBitmap() }
    Image(bitmap,"${asset.name}, ${size}dp",Modifier.size(size.dp))
}

@Composable private fun ObjectImage(obj:AtelierObject,material:WidgetStudyMaterial,celestial:AtelierCelestial?,content:Boolean,modifier:Modifier,silhouette:Boolean=false) {
    val context=LocalContext.current; val dark=BestMatesDesign.colors.isDark
    val bitmap=remember(obj.id,material,dark,celestial?.id,content,silhouette) { GoldenSet3Renderer.objectBitmap(context,obj,material,dark,celestial,content,silhouette).asImageBitmap() }
    Image(bitmap,"${obj.family} ${obj.name}, ${material.label}${if(content) ", Tokyo 18:40 Tue Sep 8" else " silhouette"}",modifier)
}

@Composable private fun ObjectScene(obj:AtelierObject,material:WidgetStudyMaterial,celestial:AtelierCelestial?,wallpaper:Int,modifier:Modifier) {
    val palette=when(wallpaper) {
        0 -> listOf(Color(0xFFEBDDD2),Color(0xFFF6ECDD),Color(0xFFDBD0D6))
        1 -> listOf(Color(0xFFC8D5DE),Color(0xFFE8EAF4),Color(0xFFB8CCD0))
        2 -> listOf(Color(0xFF242130),Color(0xFF544361),Color(0xFF283441))
        else -> listOf(Color(0xFFBDCCD4),Color(0xFFE7C2C5),Color(0xFFCAC4E0))
    }
    Box(modifier.clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(palette)),contentAlignment=Alignment.Center) {
        // A stationary wallpaper motif behind the transparent bitmap makes translucency inspectable.
        Box(Modifier.offset(x=70.dp,y=42.dp).size(160.dp).background(Color.White.copy(alpha=.10f),RoundedCornerShape(70.dp)))
        ObjectImage(obj,material,celestial,true,Modifier.fillMaxSize())
    }
}

@Composable private fun OrbitSizes(icon:ImageVector,name:String) {
    Row(Modifier.fillMaxWidth().height(44.dp),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically) {
        listOf(16,20,24).forEach { Icon(icon,"$name, ${it}dp",tint=BestMatesDesign.colors.textPrimary,modifier=Modifier.size(it.dp)) }
    }
}

@Composable private fun ReviewPanel(content:@Composable ColumnScope.()->Unit) {
    val colors=BestMatesDesign.colors; val dims=BestMatesDesign.dimensions
    AtelierSurface(colors,dims,color=colors.surfaceBase,radius=dims.radii.radius2xl,padding=dims.spacing.space4,crystalLevel=AtelierCrystalLevel.MilkCrystal,content=content)
}

@Composable private fun ReviewTile(label:String,selected:Boolean,modifier:Modifier=Modifier,onClick:(()->Unit)?=null,content:@Composable ColumnScope.()->Unit) {
    val colors=BestMatesDesign.colors
    val dims=BestMatesDesign.dimensions
    AtelierSurface(colors,dims,modifier=modifier.then(if(onClick!=null) Modifier.selectable(selected=selected,role=Role.RadioButton,onClick=onClick) else Modifier),
        color=if(selected) colors.selectionFill else colors.surfaceRaised,radius=20.dp,padding=10.dp,
        borderColor=if(selected) colors.selectionOutline else colors.borderSubtle,crystalLevel=AtelierCrystalLevel.MilkCrystal) {
        content()
        Row(verticalAlignment=Alignment.CenterVertically) {
            Text(label,Modifier.weight(1f),fontSize=13.sp,color=colors.textPrimary)
            if(selected) Icon(OrbitIcons.Confirm,"Selected",tint=colors.selectionContent,modifier=Modifier.size(16.dp))
        }
    }
}

@Composable private fun ReviewChoices(options:List<String>,selected:String,onSelect:(String)->Unit) {
    val colors=BestMatesDesign.colors
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
        options.forEach { label ->
            Box(Modifier.heightIn(min=48.dp).clip(RoundedCornerShape(16.dp))
                .background(if(label==selected) colors.selectionFill else colors.surfaceRaised)
                .border(1.dp,if(label==selected) colors.selectionOutline else colors.borderSubtle,RoundedCornerShape(16.dp))
                .selectable(selected==label,role=Role.Tab,onClick={onSelect(label)})
                .padding(horizontal=14.dp,vertical=12.dp),contentAlignment=Alignment.Center) {
                Text(label,fontSize=13.sp,fontWeight=if(label==selected) FontWeight.SemiBold else FontWeight.Normal,color=colors.textPrimary)
            }
        }
    }
}

@Composable private fun ReviewTitle(title:String,note:String) {
    Text(title,fontSize=22.sp,lineHeight=28.sp,fontWeight=FontWeight.SemiBold,color=BestMatesDesign.colors.textPrimary)
    ReviewText(note)
}
@Composable private fun ReviewText(text:String,error:Boolean=false) {
    Text(text,fontSize=12.sp,lineHeight=18.sp,color=if(error) BestMatesDesign.colors.destructive else BestMatesDesign.colors.textSecondary)
}
