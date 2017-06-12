package kr.spyec.spyecvote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 최예찬 on 2016-08-22.
 */
public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {

    private Context context;

    public VoteAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(DataManager.getInstance().getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return DataManager.getInstance().getItems().size();
    }

    public void addItem(VoteItem item) {
        DataManager.getInstance().addItem(item);
        notifyItemInserted(DataManager.getInstance().getItems().size() - 1);
    }

    public void removeItem(int position) {
        DataManager.getInstance().removeItem(position);
        notifyItemInserted(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mainNameText, etcText, countText;

        public ViewHolder(View itemView) {
            super(itemView);
            mainNameText = (TextView) itemView.findViewById(R.id.text_mainName);
            etcText = (TextView) itemView.findViewById(R.id.text_etc);
            countText = (TextView) itemView.findViewById(R.id.text_count);
        }

        public void bind(VoteItem item) {
            mainNameText.setText(item.getMainName());

            StringBuilder etcStrBuilder = new StringBuilder();
            String comma = "";
            for (String str : item.getEtc()) {
                etcStrBuilder.append(comma);
                comma = ", ";
                etcStrBuilder.append(str);
            }

            etcText.setText(etcStrBuilder.toString());countText.setText(DataManager.getInstance().getVoteCount(item.getMainName()) + "표");

        }
    }

}