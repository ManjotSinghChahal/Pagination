package app.com.pagination.recyclerviewpagination;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.com.pagination.DetailActivity;
import app.com.pagination.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<ItemModel> mItemModels;
    LoadMoreCallback callback;

    public PostRecyclerAdapter(List<ItemModel> itemModels, LoadMoreCallback callback) {
        this.mItemModels = itemModels;
        this.callback=callback;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mItemModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mItemModels == null ? 0 : mItemModels.size();
    }

    public void add(ItemModel response) {
        mItemModels.add(response);
        notifyItemInserted(mItemModels.size() - 1);
    }

    public void addAll(List<ItemModel> itemModels) {
        for (ItemModel response : itemModels) {
            add(response);
        }
    }


    private void remove(ItemModel itemsModel) {
        int position = mItemModels.indexOf(itemsModel);
        if (position > -1) {
            mItemModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new ItemModel());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mItemModels.size() - 1;
        ItemModel item = getItem(position);
        if (item != null) {
            mItemModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    ItemModel getItem(int position) {
        return mItemModels.get(position);
    }


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_list)
        ImageView imgView;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @OnClick(R.id.img_list)
        public void onImageCliek(){
            Bundle bundle =new Bundle();
            bundle.putParcelable("data",mItemModels.get(getAdapterPosition()));
            itemView.getContext().startActivity(new Intent(itemView.getContext(), DetailActivity.class).putExtras(bundle));
        }

        public void onBind(int position) {
            super.onBind(position);
            ItemModel item = mItemModels.get(position);

            Glide.with(itemView.getContext()).load(item.getImageUrl()).into(imgView);
            ;
//            imgView.setText(item.getTitle());
        }
    }

    public class FooterHolder extends BaseViewHolder {

      /*  @BindView(R.id.progressBar)
        ProgressBar mProgressBar;*/

        @BindView(R.id.btn_load_more)
        Button btnLoadMore;


        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        @OnClick(R.id.btn_load_more)
        public void onLoadMore(){
            callback.onLoadMore();
        }

    }

    public interface LoadMoreCallback{
        void onLoadMore();
    }

}
