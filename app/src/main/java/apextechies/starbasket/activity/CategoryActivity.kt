package apextechies.starbasket.activity

import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import apextechies.starbasket.R
import apextechies.starbasket.adapter.AllCategoryAdapter
import apextechies.starbasket.model.CategoryDataModel
import apextechies.starbasket.model.CategoryModel
import apextechies.starbasket.model.CategorysSubcatModel
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity: AppCompatActivity(), AllCategoryAdapter.OnItemClickListener {


    private var mAdapter: AllCategoryAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

      var  mCategory = intent.getParcelableExtra<CategoryDataModel>(EXTRA_DATA) as CategoryModel

        mAdapter = AllCategoryAdapter(this)
        rv_category.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_category.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        rv_category.adapter = mAdapter
    }

    override fun onItemClick(item: CategorysSubcatModel) {

    }
}