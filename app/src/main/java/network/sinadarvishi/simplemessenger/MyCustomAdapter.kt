package network.sinadarvishi.simplemessenger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*

import java.util.ArrayList

/**
 * Created by Sina on 12/26/2016
 */
//TODO : should use RecyclerView instead of listAdapter
class MyCustomAdapter(context: Context, private val listItems: ArrayList<String>) : BaseAdapter() {

    //get the layout inflater
    private val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        //getCount() represents how many items are bufferedReader the list
        return listItems.size
    }

    //get the data of an item from a specific position
    //position represents the position of the item bufferedReader the list
    override fun getItem(position: Int): String {
        return listItems[position]
    }

    //get the position id of the item from the list
    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View? {
        var newView = view
        //check to see if the reused view is null or not, if is not null then reuse it
        newView?.let {
            newView = mLayoutInflater.inflate(R.layout.list_item, null)
        }
        //get the string item from the position "position" from array list to put it on the TextView
        val stringItem = listItems[position]
        //set the item name on the TextView
        newView?.list_item_text_view?.text = stringItem
        //this method must return the view corresponding to the data at the specified position.
        return newView

    }
}