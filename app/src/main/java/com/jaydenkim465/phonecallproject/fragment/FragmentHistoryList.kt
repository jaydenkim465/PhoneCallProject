package com.jaydenkim465.phonecallproject.fragment

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaydenkim465.phonecallproject.R
import com.jaydenkim465.phonecallproject.adapter.AdapterHistoryList
import com.jaydenkim465.phonecallproject.data.HistoryData
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*
/**
 * A fragment representing a list of Items.
 */
class FragmentHistoryList() : Fragment() {
	private var columnCount = 1
	private var searchIndex = 0
	private var loadingState = false
	private var historyList = ArrayList<HistoryData>()
	private lateinit var historyListAdapter:AdapterHistoryList

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		InitHistoryList()
		val view = inflater.inflate(R.layout.fragment_history_list, container, false)

		// RecyclerView Set the adapter
		if (view is RecyclerView) {
			historyListAdapter = AdapterHistoryList(historyList)

			val swipeHelperCallBack = SwipeHelperCallBack().apply {
				//setClamp(200f)
				//Toast.makeText(context, String.format("currentDx:%f", getCurrentDx()), Toast.LENGTH_SHORT)
			}
			val itemTouchHelper = ItemTouchHelper(swipeHelperCallBack)
			itemTouchHelper.attachToRecyclerView(view)

			view.apply {
				layoutManager = LinearLayoutManager(context)
				adapter = historyListAdapter
				addItemDecoration(ItemDecoration())

				setOnTouchListener{_, _ ->
					swipeHelperCallBack.removePreviousClamp(this)
					false
				}
			}
			with(view) {
				layoutManager = when {
					columnCount <= 1 -> LinearLayoutManager(context)
					else -> GridLayoutManager(context, columnCount)
				}
				adapter = historyListAdapter
			}

			view.setOnScrollChangeListener {v, scrollX, scrollY, oldScrollX, oldScrollY ->
				var layoutManager:LinearLayoutManager = (view.layoutManager as LinearLayoutManager)
				if(!loadingState) {
					if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == historyList.size - 5) {
						loadingState = true
						loadHistory()
					}
					loadingState = false
				}
			}
		}
		return view
	}

	fun InitHistoryList() {
		searchIndex = 0;
		getHistoryList(searchIndex)
	}

	fun loadHistory() {
		getHistoryList(searchIndex)
		historyListAdapter.notifyDataSetChanged()
	}

	fun getHistoryList(index:Int) {
		if(requireActivity() == null) {
			return
		}

		// SQLite의 order by 이후에 ROWNUM 효과를 위한 구문
		val afterOrderBy = "date DESC LIMIT ${index + 20} OFFSET $index"

		/**
		SELECT	date, transcription, photo_id, subscription_component_name, call_screening_app_name, type, geocoded_location, presentation, duration, subscription_id, is_read, number,
		features, voicemail_uri, normalized_number, via_number, matched_number, last_modified, new, numberlabel, lookup_uri, data4, photo_uri, data3, data2, data1, data_usage,
		phone_account_address, formatted_number, add_for_all_users, block_reason, numbertype, call_screening_component_name, countryiso, name, post_dial_digits, transcription_state, _id
		FROM (SELECT * FROM calls WHERE logtype IN (100, 110, 150, 500, 800, 900, 950, 1000, 1050, 1100, 1150, 1350, 1400, 1450, 1500) ORDER BY date DESC ) calls
		ORDER BY date DESC LIMIT 20 OFFSET 0
		 */
		val cursor = requireActivity().contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, afterOrderBy)
		val numberIndex = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
		val typeIndex = cursor?.getColumnIndex(CallLog.Calls.TYPE)
		val dateIndex = cursor?.getColumnIndex(CallLog.Calls.DATE)
		val durationIndex = cursor?.getColumnIndex(CallLog.Calls.DURATION)

		while(cursor?.moveToNext() == true) {
			val date = Date(cursor.getString(dateIndex!!).toLong())
			val number = cursor.getString(numberIndex!!)
			val type = cursor.getString(typeIndex!!).toInt()
			val duration = cursor.getString(durationIndex!!).toLong()

			historyList.add(
				HistoryData(
					date,
					number,
					type,
					duration
				)
			)
		}
		cursor?.close()
		searchIndex += 20
	}

	class SwipeHelperCallBack : ItemTouchHelper.Callback() {
		private var currentPosition: Int? = null
		private var previousPosition: Int? = null
		private var currentDx = 0f
		private var clamp = 0f

		override fun getMovementFlags(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder
		): Int {
			return makeMovementFlags(0, LEFT or RIGHT)
		}

		override fun onMove(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			target: RecyclerView.ViewHolder
		): Boolean {
			return false
		}

		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		}

		override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
			currentDx = 0f
			previousPosition = viewHolder.adapterPosition
			getDefaultUIUtil().clearView(getView(viewHolder))
		}

		override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
			viewHolder?.let {
				currentPosition = viewHolder.adapterPosition
				getDefaultUIUtil().onSelected(getView(it))
			}
		}

		override fun onChildDraw(
			c: Canvas,
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			dX: Float,
			dY: Float,
			actionState: Int,
			isCurrentlyActive: Boolean
		) {
			if(actionState == ACTION_STATE_SWIPE) {
				val view = getView(viewHolder)
				val isClamped = getTag(viewHolder)
				val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)

				currentDx = x
				getDefaultUIUtil().onDraw(c, recyclerView, view, x, dY, actionState, isCurrentlyActive)
			}
		}

		override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
			return defaultValue * 10
		}

		override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
			val isClamped = getTag(viewHolder)
			setTag(viewHolder, !isClamped && currentDx <= -clamp)
			return 2f
		}

		private fun getView(viewHolder: RecyclerView.ViewHolder): View {
			return (viewHolder as AdapterHistoryList.ViewHolder).mainLayer
		}

		private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
			// isClamped를 view의 tag로 관리
			viewHolder.itemView.tag = isClamped
		}

		private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
			// isClamped를 view의 tag로 관리
			return viewHolder.itemView.tag as? Boolean ?: false
		}

		fun setClamp(clamp: Float) {
			this.clamp = clamp
		}

		fun getCurrentDx() : Float{
			return currentDx
		}

		// 다른 View가 swipe 되거나 터치되면 고정 해제
		fun removePreviousClamp(recyclerView: RecyclerView) {
			if (currentPosition == previousPosition)
				return
			previousPosition?.let {
				val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
				getView(viewHolder).translationX = 0f
				setTag(viewHolder, false)
				previousPosition = null
			}
		}

		private fun clampViewPositionHorizontal(
			view: View,
			dX: Float,
			isClamped: Boolean,
			isCurrentlyActive: Boolean
		) : Float {
			// View의 가로 길이의 절반까지만 swipe 되도록
			val min: Float = -view.width.toFloat()/2
			val min2 = view.width.toFloat()/2

			// RIGHT 방향으로 swipe 막기
			val max = 0f
			val max2 = view.width.toFloat()

			val x = if (isClamped) {
				// View가 고정되었을 때 swipe되는 영역 제한
				if (isCurrentlyActive) dX - clamp else -clamp
			} else {
				dX
			}

			if(x > 0) {
				return min(min2, x)
			} else {
				return max(min, x)
			}
		}
	}

	class ItemDecoration : RecyclerView.ItemDecoration() {
		override fun getItemOffsets(
			outRect: Rect,
			view: View,
			parent: RecyclerView,
			state: RecyclerView.State
		) {
			outRect.top = 15
			outRect.bottom = 15
		}
	}
}