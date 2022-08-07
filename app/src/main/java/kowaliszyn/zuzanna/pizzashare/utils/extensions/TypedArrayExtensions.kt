package kowaliszyn.zuzanna.pizzashare.utils.extensions

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.StyleableRes

fun TypedArray?.getStringOrDef(@StyleableRes resId: Int, def: String) =
    try {
        this?.getString(resId) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getStringArrayOrDef(@StyleableRes resId: Int, def: Int) =
    try {
        this?.getTextArray(resId) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getIntOrDef(@StyleableRes resId: Int, def: Int) =
    try {
        this?.getInt(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getFloatOrDef(@StyleableRes resId: Int, def: Float) =
    try {
        this?.getFloat(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getDimensionOrDef(@StyleableRes resId: Int, def: Float) =
    try {
        this?.getDimension(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getDimensionPixelOffsetOrDef(@StyleableRes resId: Int, def: Int) =
    try {
        this?.getDimensionPixelOffset(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getDimensionPixelSizeOrDef(@StyleableRes resId: Int, def: Int) =
    try {
        this?.getDimensionPixelSize(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getBooleanOrDef(@StyleableRes resId: Int, def: Boolean) =
    try {
        this?.getBoolean(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getColorOrDef(@StyleableRes resId: Int, def: Int) =
    try {
        this?.getColor(resId, def) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getColorStateListOrDef(@StyleableRes resId: Int, def: ColorStateList) =
    try {
        this?.getColorStateList(resId) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getDrawableOrDef(@StyleableRes resId: Int, def: Drawable?) =
    try {
        this?.getDrawable(resId) ?: def
    } catch (e: Exception) {
        def
    }

fun TypedArray?.getResourceIdOrDef(@StyleableRes resId: Int, def: Int) =
    try {
        this?.getResourceId(resId, def) ?: def
    } catch (e: Exception) {
        def
    }
