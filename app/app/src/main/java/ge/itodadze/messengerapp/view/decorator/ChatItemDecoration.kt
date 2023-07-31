package ge.itodadze.messengerapp.view.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ChatItemDecoration(private val spacingProvider: (Int) -> Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val topSpacing = spacingProvider(position)
        outRect.top = topSpacing
    }

}