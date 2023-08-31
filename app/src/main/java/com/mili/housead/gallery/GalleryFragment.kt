package com.mili.housead.gallery

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.mili.housead.R
import com.mili.housead.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private val args:GalleryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val gallery = args.gallery

        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN

        val binding = FragmentGalleryBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner


        val application = requireNotNull(activity).application
        val viewModelFactory = GalleryViewModelFactory(gallery.asList(), application)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(GalleryViewModel::class.java)

        binding.viewModel = viewModel
        binding.galleryList.adapter = GalleryAdapter()

        binding.galleryList.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val itemCount = binding.galleryList.adapter!!.itemCount
                    if (itemCount == 1) {
                        binding.nextImg.visibility = View.GONE
                        binding.previousImg.visibility = View.GONE
                    } else {
                        when ((position)) {
                            itemCount - 1 -> {
                                binding.nextImg.visibility = View.GONE
                                binding.previousImg.visibility = View.VISIBLE
                            }
                            0 -> {
                                binding.previousImg.visibility = View.GONE
                                binding.nextImg.visibility = View.VISIBLE
                            }
                            else -> {
                                binding.nextImg.visibility = View.VISIBLE
                                binding.previousImg.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            })

        binding.nextImg.setOnClickListener {
            binding.galleryList.currentItem++
        }
        binding.previousImg.setOnClickListener{
            binding.galleryList.currentItem--
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN
    }

}