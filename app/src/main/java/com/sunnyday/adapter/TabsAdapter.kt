package com.sunnyday.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sunnyday.fragments.ForecastFragment
import com.sunnyday.R

class TabsAdapter(val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ForecastFragment(false)
        }
        return ForecastFragment(true)
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (position == 0) {
            return context.getString(R.string.tab_upcoming)
        }
        return context.getString(R.string.tab_hottest)
    }
}
