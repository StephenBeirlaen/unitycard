package be.nmct.unitycard.adapters;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.nmct.unitycard.R;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.models.Retailer;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 6/11/2016.
 */

public class AddRetailerRecyclerViewAdapter
        extends RecyclerView.Adapter<AddRetailerRecyclerViewAdapter.AddRetailerViewHolder> {

    //private Cursor cursorRetailers;
    private ObservableList<Retailer> mRetailers;
    private Context mContext;
    //private TracksFragment.TracksFragmentListener mListener;

    public AddRetailerRecyclerViewAdapter(Context context, ObservableList<Retailer> retailers/*Cursor cursorRetailers*//*, TracksFragment.TracksFragmentListener listener*/) {
        this.mContext = context;
        //this.cursorRetailers = cursorRetailers;
        this.mRetailers = retailers;
        //this.mListener = listener;
    }

    class AddRetailerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewAddRetailerName) TextView txvRetailerName;
        @Bind(R.id.textViewAddRetailerType) TextView txvRetailerType;
        @Bind(R.id.textViewAddRetailerLocatie) TextView txvRetailerLocation;
        @Bind(R.id.imageViewLogoRetailer) ImageView imgLogoRetailer;

        public AddRetailerViewHolder(View row) {
            super(row);
            ButterKnife.bind(this, row);

            /*row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    cursorRetailers.moveToPosition(position);

                    String url = cursorRetailers.getString(cursorRetailers.getColumnIndex(Contract.TrackColumns.COLUMN_URL));

                    //mListener.onTrackSelected(url);
                }
            });*/
        }
    }

    @Override
    public AddRetailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_retailer, parent, false);
        AddRetailerViewHolder addRetailerViewHolder = new AddRetailerViewHolder(viewRow);
        return addRetailerViewHolder;
    }

    @Override
    public void onBindViewHolder(AddRetailerViewHolder holder, int position) {
        /*cursorRetailers.moveToPosition(position);

        holder.txvRetailerName.setText(cursorRetailers.getString(cursorRetailers.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME)));
        holder.txvRetailerType.setText(cursorRetailers.getString(cursorRetailers.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID)));
        holder.txvRetailerLocation.setText(cursorRetailers.getString(cursorRetailers.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_ID)));
        String logoUrl = cursorRetailers.getString(cursorRetailers.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOGOURL));
        if (logoUrl != null && !logoUrl.equals("")) {
            Picasso.with(mContext)
                    .load(logoUrl)
                    .into(holder.imgLogoRetailer);
        }*/

        Retailer retailer = mRetailers.get(position);
        /*holder.getBinding().setSeries(series);
        holder.getBinding().executePendingBindings();*/ // todo: ook rows binden

        holder.txvRetailerName.setText(retailer.getName());
        holder.txvRetailerType.setText(retailer.getRetailerCategoryId());
        holder.txvRetailerLocation.setText(retailer.getId());
        String logoUrl = retailer.getLogoUrl();
        if (logoUrl != null && !logoUrl.equals("")) {
            Picasso.with(mContext)
                    .load(logoUrl)
                    .into(holder.imgLogoRetailer);
        }
    }

    @Override
    public int getItemCount() {
        return /*cursorRetailers.getCount()*/mRetailers.size();
    }

    /*public Cursor swapCursor(Cursor cursor) {
        if (cursorRetailers == cursor) {
            return null;
        }
        Cursor oldCursor = cursorRetailers;
        this.cursorRetailers = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }*/
}
