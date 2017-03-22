package android.ctm.com.ctm.adapters;

import android.ctm.com.ctm.data.RowObj;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Paul on 3/16/17.
 */

public class SearchAdapter extends BaseAdapter {

    private static final String LOG_TAG = SearchAdapter.class.getName();

    private ArrayList<RowObj> data;

    public enum ROW_TYPE {
        SEARCH_RESULT,

        //This must be the last item.
        COUNT
    }

    public SearchAdapter() {
        super();
        data = new ArrayList<RowObj>();
    }

    public void addRow(RowObj row) {
        data.add(row);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        RowObj rowObj = data.get(position);
        return rowObj.type;
    }

    @Override
    public int getViewTypeCount() {
        return ROW_TYPE.COUNT.ordinal();
    }

    public int getCount() {
        return data.size();
    }

    public RowObj getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        data.clear();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {
        RowObj obj = data.get(position);
        View v = obj.getView(position, convertView, parent);
        return v;
    }

}
