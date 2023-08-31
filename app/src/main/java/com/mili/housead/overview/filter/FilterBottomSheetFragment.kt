package com.mili.housead.overview.filter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.mili.housead.utilities.Utils
import com.mili.housead.R
import com.mili.housead.databinding.BottomSheetFragmentFilterBinding
import com.mili.housead.network.model.CityProperty
import com.mili.housead.network.model.NeighborhoodProperty
import com.mili.housead.utilities.CustomItemDecoration
import com.mili.housead.overview.filter.model.FilterItem
import com.mili.housead.overview.filter.model.OptionProperty


class FilterBottomSheetFragment: BottomSheetDialogFragment() {

    private var mListener: OnFilterListener? = null
    private lateinit var cityPropertyAdapter: CityAutoFillAdapter
    private lateinit var neighborhoodPropertyAdapter: NeighborhoodAutoFillAdapter

    companion object {
        fun newInstance(onFilterListener: OnFilterListener): FilterBottomSheetFragment =
            FilterBottomSheetFragment().apply {
                mListener = onFilterListener
            }
    }

    private lateinit var viewModel: FilterViewModel
    private lateinit var binding: BottomSheetFragmentFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetFragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FilterViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val layoutManagerCategory =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        val layoutManagerFeature =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        val layoutManagerRoom = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        val layoutManagerNeighborhood =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)

        val itemDecoration = CustomItemDecoration(
            context!!,
            R.dimen.spacing_x_small
        )

        binding.categoryList.layoutManager = layoutManagerCategory
        binding.categoryList.adapter =
            OptionPropertyAdapter(object : OptionPropertyAdapter.OnClickListener {
                override fun onClick(option: OptionProperty) {
                    if (FilterItem.categories.contains(option.id)) {
                        FilterItem.categories.remove(option.id)
                    } else {
                        FilterItem.categories[option.id] = option.title
                    }
                }
            })
        binding.categoryList.addItemDecoration(itemDecoration)

        binding.featureList.layoutManager = layoutManagerFeature
        binding.featureList.adapter =
            OptionPropertyAdapter(object : OptionPropertyAdapter.OnClickListener {
                override fun onClick(option: OptionProperty) {
                    if (FilterItem.features.contains(option.id)) {
                        FilterItem.features.remove(option.id)
                    } else {
                        FilterItem.features[option.id] = option.title
                    }
                }
            })
        binding.featureList.addItemDecoration(itemDecoration)

        binding.roomList.layoutManager = layoutManagerRoom
        binding.roomList.adapter =
            OptionPropertyAdapter(object : OptionPropertyAdapter.OnClickListener {
                override fun onClick(option: OptionProperty) {
                    if (FilterItem.rooms.contains(option.id)) {
                        FilterItem.rooms.remove(option.id)
                    } else {
                        FilterItem.rooms[option.id] = option.title
                    }
                }
            })
        binding.roomList.addItemDecoration(itemDecoration)

        binding.neighborhoodListFilter.layoutManager = layoutManagerNeighborhood
        binding.neighborhoodListFilter.adapter =
            NeighborhoodPropertyAdapter(object :
                NeighborhoodPropertyAdapter.OnClickListener {
                override fun onClose(property: NeighborhoodProperty) {
                    viewModel.deleteNeighborhoodFilter(property)
                }
            }
            )
        binding.neighborhoodListFilter.addItemDecoration(itemDecoration)

        binding.sellRange.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                viewModel.setSellFilter(leftValue.toLong(), rightValue.toLong())
                if (leftValue != 0F) {
                    binding.minSell.text = resources.getString(
                        R.string.price, Utils.persianNumber(
                            Utils.convertToNumberFormat(leftValue.toLong())
                        )
                    )
                }
                if (rightValue != 0F) {
                    binding.maxSell.text = resources.getString(
                        R.string.price, Utils.persianNumber(
                            Utils.convertToNumberFormat(rightValue.toLong())
                        )
                    )
                }
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })

        binding.prepaymentRange.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                viewModel.setPrepaymentFilter(leftValue.toLong(), rightValue.toLong())
                if (leftValue != 0F) {
                    binding.minPrepayment.text = resources.getString(
                        R.string.price, Utils.persianNumber(
                            Utils.convertToNumberFormat(leftValue.toLong())
                        )
                    )
                }

                if (rightValue != 0F) {
                    binding.maxPrepayment.text = resources.getString(
                        R.string.price, Utils.persianNumber(
                            Utils.convertToNumberFormat(rightValue.toLong())
                        )
                    )
                }
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })

        binding.rentRange.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                viewModel.setRentFilter(leftValue.toLong(), rightValue.toLong())

                if (leftValue != 0F) {
                    binding.minRent.text = resources.getString(
                        R.string.price, Utils.persianNumber(
                            Utils.convertToNumberFormat(leftValue.toLong())
                        )
                    )
                }

                if (rightValue != 0F) {
                    binding.maxRent.text = resources.getString(
                        R.string.price, Utils.persianNumber(
                            Utils.convertToNumberFormat(rightValue.toLong())
                        )
                    )
                }
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }
        })

        viewModel.navigateToApplyFilter.observe(this.viewLifecycleOwner, Observer {
            if (null != it) {
                mListener?.onFilter()
                viewModel.navigateFilterComplete()
                this.dismiss()
            }
        })

        viewModel.cities.observe(this.viewLifecycleOwner, Observer {
            cityPropertyAdapter.setCities(it)
        })

        viewModel.neighborhoods.observe(this.viewLifecycleOwner, Observer {
            neighborhoodPropertyAdapter.setNeighborhoods(it)
        })

        binding.cityList.setAdapter(cityPropertyAdapter)
        binding.cityList.threshold = 3

        binding.cityList.setOnItemClickListener { parent, _, position, _ ->
            val cityProperty = parent.adapter.getItem(position) as CityProperty
            viewModel.setNeighborhoodByCity(cityProperty.id)
            binding.neighborhoodList.setText("")
            binding.cityList.setText(cityProperty.name)
            viewModel.deleteAllNeighborhoodsFilter()
            binding.neighborhoodList.requestFocus()
        }

        binding.neighborhoodList.setAdapter(neighborhoodPropertyAdapter)
        binding.neighborhoodList.threshold = 3
        binding.neighborhoodList.setOnItemClickListener { parent, _, position, _ ->
            val neighborhoodProperty = parent.adapter.getItem(position) as NeighborhoodProperty
            viewModel.addNeighborhoodFilter(neighborhoodProperty)
            binding.neighborhoodList.setText(neighborhoodProperty.title)
            val inputManager: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                binding.neighborhoodList.windowToken,
                InputMethodManager.SHOW_FORCED
            ) // It can be done by show_forced too
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        cityPropertyAdapter = CityAutoFillAdapter(context)
        neighborhoodPropertyAdapter = NeighborhoodAutoFillAdapter(context)
//        val parent = parentFragment
//        if (parent != null) {
//            mListener = parent as OnFilterListener
//        } else {
//            mListener = context as OnFilterListener
//        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface OnFilterListener {
        fun onFilter()
    }
}
