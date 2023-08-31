package com.mili.housead.overview


import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mili.housead.R
import com.mili.housead.utilities.CustomItemDecoration
import com.mili.housead.utilities.PaginationListener
import com.mili.housead.utilities.Utils
import com.mili.housead.databinding.FragmentOverviewBinding
import com.mili.housead.network.ConnectivityLiveData
import com.mili.housead.overview.filter.FilterBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_overview.*


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {


    private val viewModel: OverviewViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentOverviewBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        val application = requireNotNull(activity).application
        ConnectivityLiveData.init(application)

        val reverseLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        binding.filterList.layoutManager = reverseLayoutManager
        binding.filterList.adapter =
            FilterPropertyAdapter(object : FilterPropertyAdapter.OnClickListener {
                override fun onClose(filterType: FilterType) {
                    viewModel.deleteFilter(filterType)
                }
            })
        val itemDecoration = CustomItemDecoration(
            context!!,
            R.dimen.spacing_x_small
        )
        binding.filterList.addItemDecoration(itemDecoration)

        val spanCount = Utils.calculateSpanCount(
            context!!,
            R.dimen.spacing_normal,
            R.dimen.column_width,
            R.dimen.spacing_x_small
        )
        val layoutManager = GridLayoutManager(activity, spanCount)

        binding.housesList.layoutManager = layoutManager
        binding.housesList.addItemDecoration(itemDecoration)
        binding.housesList.adapter = HousePropertyAdapter(
            HousePropertyAdapter.OnClickListener { houseProperty, houseBg ->
                viewModel.displayPropertyDetails(houseProperty, houseBg)
            })

        binding.housesList.addOnScrollListener(object : PaginationListener(
            layoutManager,
            viewModel.meta.pageCount,
            viewModel.meta.perPage
        ) {
            override fun loadMoreItems() {
                viewModel.getHouseProperties()
            }

            override fun isLastPage(): Boolean {
                return (viewModel.meta.currentPage == viewModel.meta.pageCount)
            }

            override fun isLoading(): Boolean {
                return (viewModel.status.value == HouseApiStatus.LOADING)
            }

        })


        viewModel.navigateToSelectedProperty.observe(
            viewLifecycleOwner,
            Observer { pair ->
                if (null != pair) {
                    val houseProperty = pair.first
                    val houseBg = pair.second
                    val extras = FragmentNavigatorExtras(
                        houseBg to houseProperty.images[0].original
                    )
                    this.findNavController()
                        .navigate(
                            OverviewFragmentDirections.actionShowDetail(houseProperty)
                            , extras
                        )
                    viewModel.displayPropertyDetailsComplete()
                }
            })

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (binding.searchInput.isFocused) {
                    if (p0!!.length >= 3 || p0.isEmpty()) {
                        viewModel.setWordSearch(p0.toString())
                        if (viewModel.status.value != HouseApiStatus.LOADING) {
                            viewModel.refreshProperties()
                        } else {
                            viewModel.waitingSearch()
                        }
                    }
                }
            }


            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    if (p0.isNotEmpty()) {
                        binding.clearBtn.visibility = View.VISIBLE
                    } else {
                        binding.clearBtn.visibility = View.GONE
                    }
                }
            }

        })

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty() && viewModel.status.value != HouseApiStatus.ERROR){
                viewModel.setEmptyStatus()
            }
            if (viewModel.isSearch.value == true) {
                viewModel.refreshProperties()
                viewModel.searchHasCompleted()
            }
        })


        binding.clearBtn.setOnClickListener {
            binding.searchInput.text?.clear()
        }

        binding.moreMenu.setOnClickListener {
            showOverflowMenu(it)
        }

        binding.filterBtn.setOnClickListener {
            openFilterBottomSheet()
        }

        setHasOptionsMenu(true)
        return binding.root
    }


    private fun openFilterBottomSheet() {
        if (ConnectivityLiveData.isInternetOn()) {
            val fragment = FilterBottomSheetFragment.newInstance(
                object : FilterBottomSheetFragment.OnFilterListener {
                    override fun onFilter() {
                        viewModel.setFilterHouseProperties()
                        viewModel.searchHasCompleted()
                    }
                })
            fragmentManager?.let { fragment.show(it, "filter") }
        } else {
            showSnackBar()
        }
    }

    private fun showOverflowMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menuInflater.inflate(R.menu.overflow_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.aboutFragment) {
                anchor.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToAboutFragment2()
                )
            }
            true
        }
        popup.show()
    }

    private fun showSnackBar() {
        val snack =
            Snackbar.make(
                requireActivity().window.decorView,
                R.string.not_connected,
                Snackbar.LENGTH_LONG
            )
        snack.show()
    }


    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor =
                ContextCompat.getColor(
                    requireContext(), R.color.transparent
                )
        }
    }
}
