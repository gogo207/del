package com.delex.interfaceMgr;

import android.view.View;

/**
 * <h1>OnItemViewClickNotifier</h1>
 * <p>
 *interface to notify that recycler view's item has clicked and
 * give provides view clicked vied id, its position and the clicked type
 * </P>
 * @author 3Embed.
 * @since 6/01/17..
 */
public interface OnItemViewClickNotifier
{
    void OnItemViewClicked(View view, final int position, final int listType);
}
