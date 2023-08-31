package com.mili.housead.detail


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.mili.housead.R
import com.mili.housead.databinding.FragmentDetailBinding
import com.mili.housead.utilities.CustomItemDecoration

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {


    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition =
                TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val houseProperty = args.selectedProperty


        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner


        binding.motionLayout.setTransitionListener(
            object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {

                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                }

                override fun onTransitionChange(
                    p0: MotionLayout?, p1: Int, p2: Int, progress: Float
                ) {
                    if (progress >= 0.6) {
                        requireActivity().window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_FULLSCREEN
                        binding.houseBg.isEnabled = false
                    } else {
                        requireActivity().window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        binding.houseBg.isEnabled = true
                    }
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                }
            })

        val application = requireNotNull(activity).application
        val viewModelFactory = DetailViewModelFactory(houseProperty, application)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(DetailViewModel::class.java)

        binding.viewModel = viewModel

        val itemDecoration = CustomItemDecoration(
            context!!,
            R.dimen.spacing_normal
        )

        val layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, true
        )
        layoutManager.reverseLayout = true
        binding.featureList.layoutManager = layoutManager
        binding.featureList.adapter = FeaturePropertyAdapter()
        binding.featureList.addItemDecoration(itemDecoration)


        viewModel.navigateToSelectedProperty
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    this.findNavController().navigate(
                        DetailFragmentDirections.actionShowGallery(it.toTypedArray())
                    )
                    viewModel.displayGalleryComplete()
                }
            })

        viewModel.navigateToDial.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$it")
                }
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
                viewModel.navigateToDialComplete()
            }
        })

        binding.homeBtn.setOnClickListener {
            this.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            )
            window.statusBarColor =
                ContextCompat.getColor(
                    requireContext(), R.color.transparent
                )
        }
    }

}


