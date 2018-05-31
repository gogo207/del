package com.delex.utility;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <h>ItemDecorationGrideview</h>
 * <p>
 *     class use to decorate the recyclerView cell
 * </p>
 * @since 22/08/17.
 */
public class ItemDecorationGrideview extends RecyclerView.ItemDecoration
{
    private int mItemOffset;

    public ItemDecorationGrideview(int item, int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemDecorationGrideview(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(0,context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, 0, mItemOffset, 0);
    }
}