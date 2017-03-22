package android.ctm.com.ctm.data;

import android.content.Context;
import android.ctm.com.ctm.CTMApp;
import android.ctm.com.ctm.R;
import android.ctm.com.ctm.adapters.SearchAdapter.ROW_TYPE;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Paul on 3/3/17.
 */

public class SearchObj extends RowObj{

    public String code;

    public String name;

    public String description;

    public SearchObj(String code, String name, String description) {
        super(ROW_TYPE.SEARCH_RESULT.ordinal());
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        SearchViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            LayoutInflater inflater = (LayoutInflater) CTMApp.getInstance().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.row_search, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new SearchViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.title);
            holder.description = (TextView) vi.findViewById(R.id.description);

            vi.setTag(holder);
        } else {
            holder = (SearchViewHolder) vi.getTag();
        }

        String displayTitle = code + " " + name;
        holder.title.setText(displayTitle);
        holder.description.setText(this.description);

        return vi;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class SearchViewHolder {
        public TextView title;
        public TextView description;
    }
}
