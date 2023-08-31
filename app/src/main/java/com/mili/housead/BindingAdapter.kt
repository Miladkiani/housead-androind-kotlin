package com.mili.housead



import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.jaygoo.widget.RangeSeekBar
import com.mili.housead.utilities.Utils
import com.mili.housead.detail.FeaturePropertyAdapter
import com.mili.housead.gallery.GalleryAdapter
import com.mili.housead.network.model.*
import com.mili.housead.overview.FilterPropertyAdapter
import com.mili.housead.overview.FilterType
import com.mili.housead.overview.HouseApiStatus
import com.mili.housead.overview.HousePropertyAdapter
import com.mili.housead.overview.filter.*
import com.mili.housead.overview.filter.model.FilterItem
import com.mili.housead.overview.filter.NeighborhoodPropertyAdapter
import com.mili.housead.overview.filter.model.OptionProperty
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.util.*


@BindingAdapter("listImage")
fun bindViewPagerGallery(
    viewPager2: ViewPager2,
    data: MutableList<HouseImageProperty>?
) {
    val adapter = viewPager2.adapter as GalleryAdapter
    adapter.submitList(data)
}

@BindingAdapter("listHouse")
fun bindRecyclerViewHouse(recyclerView: RecyclerView, data: MutableList<HouseProperty>?) {
    val adapter = recyclerView.adapter as HousePropertyAdapter
    adapter.submitList(data)
}

@BindingAdapter("listFeature")
fun bindRecyclerViewFeature(
    recyclerView: RecyclerView,
    data: MutableList<FeatureProperty>?
) {
    val adapter = recyclerView.adapter as FeaturePropertyAdapter
    adapter.submitList(data)
}


@BindingAdapter("listFilterOption")
fun bindRecyclerViewOption(
    recyclerView: RecyclerView,
    data: List<OptionProperty>?
) {
    val adapter = recyclerView.adapter as OptionPropertyAdapter
    adapter.submitList(data)
}

@BindingAdapter("listNeighborhoodFilter")
fun bindRecyclerViewNeighborhoodFilter(
    recyclerView: RecyclerView,
    data: MutableList<NeighborhoodProperty>?
) {
    val adapter = recyclerView.adapter as NeighborhoodPropertyAdapter
    adapter.submitList(data)
}


@BindingAdapter("listFilterProperty")
fun bindRecyclerViewChipFilter(recyclerView: RecyclerView, data: FilterProperty?) {
    data?.let {
        val adapter = recyclerView.adapter as FilterPropertyAdapter
        val listFilter: MutableList<FilterType> = mutableListOf()
        if (data.feature.isNotEmpty()) {
            listFilter.add(FilterType.FEATURE)
        }
        if (data.category.isNotEmpty()) {
            listFilter.add(FilterType.CATEGORY)
        }
        if (data.room.isNotEmpty()) {
            listFilter.add(FilterType.ROOM)
        }
        if (data.neighborhood.isNotEmpty()) {
            listFilter.add(FilterType.NEIGHBORHOOD)
        }
        if (data.sell.min != null && data.sell.max != null) {
            listFilter.add(FilterType.SELL)
        }
        if (data.prepayment.min != null && data.prepayment.max != null) {
            listFilter.add(FilterType.PREPAYMENT)
        }
        if (data.rent.min != null && data.rent.max != null) {
            listFilter.add(FilterType.RENT)
        }
        if (listFilter.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
        }
        adapter.submitList(listFilter)
    }
}


@BindingAdapter("waitingStatus")
fun bindWaitingStatus(waitingProgress: ProgressBar, status: HouseApiStatus?) {
    when (status) {
        HouseApiStatus.LOADING -> waitingProgress.visibility = View.VISIBLE
        else -> waitingProgress.visibility = View.GONE
    }
}

@BindingAdapter("doneStatus")
fun bindDoneStatus(linearLayout: LinearLayout, status: HouseApiStatus?) {
    when (status) {
        HouseApiStatus.DONE -> linearLayout.visibility = View.VISIBLE
        else -> linearLayout.visibility = View.GONE
    }
}

@BindingAdapter("brokenStatus")
fun bindBrokenStatus(brokenLinear: LinearLayout, status: HouseApiStatus?) {
    when (status) {
        HouseApiStatus.ERROR -> brokenLinear.visibility = View.VISIBLE
        else -> brokenLinear.visibility = View.GONE
    }
}

@BindingAdapter("emptyStatus")
fun bindEmptyStatus(emptyTextView: TextView, status: HouseApiStatus?) {
    when (status) {
        HouseApiStatus.EMPTY -> emptyTextView.visibility = View.VISIBLE
        else -> emptyTextView.visibility = View.GONE
    }
}

@BindingAdapter("galleryUrl")
fun bindGallery(imgView: ImageView, galleryUrl: String?) {
    galleryUrl?.let {
        Utils.loadImage(
            galleryUrl, imgView, R.drawable.house_anim,
            transition = false, centerCrop = false
        )
    }
}

@BindingAdapter("detailImageUrl")
fun bindDetailImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Utils.loadImage(
            imgUrl, imgView, null,
            transition = true, centerCrop = false
        )
    }
}

@BindingAdapter("overviewImageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Utils.loadImage(
            imgUrl, imgView, R.drawable.house_anim,
            transition = true, centerCrop = true
        )
    }
}

@BindingAdapter("houseRent")
fun TextView.setHouseRent(rent: Long?) {
    rent?.let {
        if (it != 0L) {
            text = resources.getString(
                R.string.house_rent,
                Utils.persianNumber(
                    Utils.convertToNumberFormat(it)
                )
            )
        } else {
            visibility = View.GONE
        }
    }
}

@BindingAdapter(
    "houseSell", "housePrepayment",
    "houseLeaseType", requireAll = false
)
fun TextView.setHouseSellPrepayment(
    sell: Long?, prepayment: Long?, lease_type: Int
) {
    when (lease_type) {
        22 -> sell?.let {
            text = resources.getString(
                R.string.house_sell,
                Utils.persianNumber(
                    Utils.convertToNumberFormat(sell)
                )
            )
        }
        33 -> prepayment?.let {
            text = resources.getString(
                R.string.house_prepayment,
                Utils.persianNumber(
                    Utils.convertToNumberFormat(prepayment)
                )
            )
        }
    }
}


@BindingAdapter("houseLastUpdate")
fun TextView.setHouseLastUpdate(timeInMillis: Long) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis * 1000 + 5000
    val persianDate = PersianDate(calendar.timeInMillis)
    val pDateFormatter = PersianDateFormat("l j F Y")
    text = Utils.persianNumber(pDateFormatter.format(persianDate))
}


@BindingAdapter("optionChip")
fun Chip.setOptionChip(option: OptionProperty?) {
    option?.let {
        when (option) {
            is FeatureProperty -> {
                isChecked = FilterItem.features.contains(option.id)
                text = resources.getString(R.string.house_option, it.title)
            }
            is CategoryProperty -> {
                isChecked = FilterItem.categories.contains(option.id)
                text = resources.getString(R.string.house_option, it.title)
            }
            is RoomProperty -> {
                isChecked = FilterItem.rooms.contains(option.id)
                text =
                    Utils.persianNumber(
                        resources.getString(R.string.house_room_option, it.title)
                    )
            }
        }
    }
}


@BindingAdapter("filterChip")
fun Chip.setNeighborhoodChip(filter: FilterType?) {
    filter?.let {
        text = when (it) {
            FilterType.FEATURE -> resources.getString(R.string.feature_title)
            FilterType.CATEGORY -> resources.getString(R.string.category_title)
            FilterType.ROOM -> resources.getString(R.string.room_title)
            FilterType.NEIGHBORHOOD -> resources.getString(R.string.neighborhood)
            FilterType.SELL -> resources.getString(R.string.sell_price)
            FilterType.PREPAYMENT -> resources.getString(R.string.prepayment_price)
            FilterType.RENT -> resources.getString(R.string.rent_price)
        }
    }
}


@BindingAdapter("minRange", "maxRange", "minProgress", "maxProgress")
fun RangeSeekBar.setPrice(
    minRange: Long, maxRange: Long,
    minProgress: Long, maxProgress: Long
) {
    val minRangeF = minRange.toFloat()
    var maxRangeF = maxRange.toFloat()

    if (maxRangeF <= minRangeF) {
        maxRangeF += 100000.toFloat()
    }
    if (maxRangeF > minRangeF) {
        setRange(minRangeF, maxRangeF)
    }

    if (minProgress != 0L && maxProgress != 0L) {
        val minProgressF = minProgress.toFloat()
        val maxProgressF = maxProgress.toFloat()
        if (minProgressF >= minRangeF && maxProgressF <= maxRangeF) {
            setProgress(minProgressF, maxProgressF)
        }
    }
}

