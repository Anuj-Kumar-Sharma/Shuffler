package com.example.anujsharma.shuffler.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 30-12-2017.
 */

public class TypefaceTextView extends android.support.v7.widget.AppCompatTextView {

    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<>(12);

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Typeface, 0, 0);

        try {
            String typefaceName = a.getString(
                    R.styleable.Typeface_typeface);

            if (!isInEditMode() && !TextUtils.isEmpty(typefaceName)) {
                Typeface typeface = sTypefaceCache.get(typefaceName);

                if (typeface == null) {
                    if (typefaceName.startsWith("Mon")) {
                        typeface = Typeface.createFromAsset(context.getAssets(),
                                String.format("fonts/%s.otf", typefaceName));
                    } else {
                        typeface = Typeface.createFromAsset(context.getAssets(),
                                String.format("fonts/%s.ttf", typefaceName));
                    }


                    // Cache the Typeface object
                    sTypefaceCache.put(typefaceName, typeface);
                }
                setTypeface(typeface);

                // Note: This flag is required for proper typeface rendering
                setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        } finally {
            a.recycle();
        }
    }
}
