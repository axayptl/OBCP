package com.ceilcode.obcp.ui;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtil {

	public static final String APP_FONT_REGULAR = "font/MyriadPro-Regular.otf";
	public static final String APP_FONT_BOLD = "font/MyriadPro-BoldCond.otf";

	public static final String APP_FONT = "font/MyriadPro-Cond.otf";
	private static TypefaceUtil typefaceUtil;

	private Typeface typefaceRegular;
	private Typeface typefaceBold;
	private Typeface typeface;

	private TypefaceUtil(Context context) {
		typefaceRegular = Typeface.createFromAsset(context.getAssets(),
				APP_FONT_REGULAR);
		typefaceBold = Typeface.createFromAsset(context.getAssets(),
				APP_FONT_BOLD);
		typeface = Typeface.createFromAsset(context.getAssets(), APP_FONT);
	}

	public Typeface getTypefaceRegular() {
		return typefaceRegular;
	}

	public Typeface getTypefaceBold() {
		return typefaceBold;
	}

	public Typeface getTypeface() {
		return typeface;
	}

	public static TypefaceUtil initiate(Context context) {
		if (typefaceUtil == null) {
			typefaceUtil = new TypefaceUtil(context);
		}

		return typefaceUtil;
	}

}
