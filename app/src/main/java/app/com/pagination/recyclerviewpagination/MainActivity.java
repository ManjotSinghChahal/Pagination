package app.com.pagination.recyclerviewpagination;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import app.com.pagination.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "DetailActivity";
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private PostRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);


        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PostRecyclerAdapter(new ArrayList<ItemModel>(), new PostRecyclerAdapter.LoadMoreCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                currentPage++;
                preparedListItem();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        preparedListItem();
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void preparedListItem() {
        final ArrayList<ItemModel> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    itemCount++;
                    Log.d(TAG, "run: " + itemCount);
                    ItemModel itemModel = new ItemModel();
                    itemModel.setSetImageUrl("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");
                    items.add(itemModel);

                }
                if (currentPage != PAGE_START) mAdapter.removeLoading();
                mAdapter.addAll(items);

                if (currentPage < totalPage) mAdapter.addLoading();
                else isLastPage = true;
                isLoading = false;

            }
        }, 2000);
    }

}
