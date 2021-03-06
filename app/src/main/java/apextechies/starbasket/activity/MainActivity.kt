package apextechies.starbasket.activity

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.EXTRA_DATA
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import apextechies.starbasket.R
import apextechies.starbasket.adapter.CategoryAdapter
import apextechies.starbasket.adapter.SubCategoryAdapter
import apextechies.starbasket.adapter.ViewPagerAdapter
import apextechies.starbasket.fragment.ImageFragment
import apextechies.starbasket.model.CategoryDataModel
import apextechies.starbasket.model.CategoryModel
import apextechies.starbasket.model.CategorysSubcatModel
import apextechies.starbasket.model.HomeBannerModel
import apextechies.starbasket.retrofit.DownlodableCallback
import apextechies.starbasket.retrofit.RetrofitDataProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.navigation_drawer.*

class MainActivity : AppCompatActivity(), Runnable, CategoryAdapter.OnItemClickListener,  SubCategoryAdapter.OnItemClickListener, ViewPager.OnPageChangeListener{
    private var isAscending = true
    private var userScrollChange = false
    private var isBackPressed:Boolean = false
    private var prevPos = 0
    private var previousState = ViewPager.SCROLL_STATE_IDLE
    private var mAdapter: ViewPagerAdapter?= null
    private var retrofitDataProvider: RetrofitDataProvider?= null
    private var mBannerHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        retrofitDataProvider = RetrofitDataProvider(this)
        for (i in 0 until ll_navigation.getChildCount()) {
//            ll_navigation.getChildAt(i).setOnClickListener(this)
        }

        mAdapter = ViewPagerAdapter(getSupportFragmentManager());
        vp_banner.setScrollDurationFactor(3.0)
        vp_banner.setAdapter(mAdapter)
        vp_banner.addOnPageChangeListener(this)
        mBannerHandler = Handler()

        rv_category.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_subcategory.isNestedScrollingEnabled = false
        tab_layout.setupWithViewPager(vp_banner)

        getBanner()
        gethomeCategory()
    }

    private fun getBanner() {
        retrofitDataProvider!!.homeBanner(object : DownlodableCallback<HomeBannerModel> {
            override fun onSuccess(result: HomeBannerModel?) {

                for (i in 0 until result!!.data!!.size) {
                    mAdapter!!.addItem(ImageFragment.newInstance(result.data!![i].banner!!))
                }
            }

            override fun onFailure(error: String?) {
            }

            override fun onUnauthorized(errorNumber: Int) {
            }
        })
    }

    private fun gethomeCategory() {
        retrofitDataProvider!!.category(object : DownlodableCallback<CategoryModel> {
            override fun onSuccess(result: CategoryModel?) {
                rv_category.adapter = CategoryAdapter(this@MainActivity, result!!.data!!)
                rv_subcategory.adapter = SubCategoryAdapter(this@MainActivity, result!!.data!!)
            }

            override fun onFailure(error: String?) {
            }

            override fun onUnauthorized(errorNumber: Int) {
            }
        })
    }

    override fun onStop() {
        super.onStop()
        mBannerHandler!!.removeCallbacks(this)
    }

    override fun run() {

        try {
            val nextPos: Int

            if (isAscending) {
                nextPos = (vp_banner.getCurrentItem() + 1) % mAdapter!!.getCount()
                isAscending = nextPos < mAdapter!!.getCount() - 1
            } else {
                nextPos = (vp_banner.getCurrentItem() - 1) % mAdapter!!.getCount()
                isAscending = nextPos < 1
            }

            vp_banner.setCurrentItem(nextPos, true)
            mBannerHandler!!.postDelayed(this, 3000)
        } catch (e: ArithmeticException) {
        }

    }

    override fun onPageScrollStateChanged(state: Int) {
        if (previousState == ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_SETTLING) {
            userScrollChange = true
        } else if (previousState == ViewPager.SCROLL_STATE_SETTLING && state == ViewPager.SCROLL_STATE_IDLE) {
            userScrollChange = false
        }

        previousState = state
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (userScrollChange) {
            isAscending = prevPos < position && position < mAdapter!!.getCount() - 1 && position > 0
        }

        prevPos = position
    }

    override fun onItemClick(item: CategorysSubcatModel) {
        val intent = Intent(this, ProductListActivity::class.java)
        intent.putExtra(NfcAdapter.EXTRA_DATA, item)
        startActivity(intent)

    }

    override fun onViewAll(item: CategoryDataModel) {

        val intent = Intent(this, CategoryActivity::class.java)
         intent.putExtra(EXTRA_DATA, item)
         startActivity(intent)
    }

    override fun onItemClick(item: CategoryDataModel) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(NfcAdapter.EXTRA_DATA, item)
        startActivity(intent)

    }
}
