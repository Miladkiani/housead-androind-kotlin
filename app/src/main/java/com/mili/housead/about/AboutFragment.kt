package com.mili.housead.about


import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.mili.housead.R
import com.mili.housead.databinding.FragmentAboutBinding
import com.mili.housead.databinding.FragmentGalleryBinding

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAboutBinding.inflate(inflater)

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
