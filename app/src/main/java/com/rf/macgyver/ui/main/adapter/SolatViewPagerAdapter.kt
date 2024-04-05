import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class SolatViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle?) :
    FragmentStateAdapter(fragmentManager, lifecycle!!) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    @NonNull
    override fun createFragment(position: Int): Fragment { lateinit var solatViewPagerAdapter: SolatViewPagerAdapter
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun removeSwipeFunctionality(viewPager: ViewPager2) {
        viewPager.isUserInputEnabled = false
        viewPager.setOnTouchListener { _, _ -> true }
    }
}