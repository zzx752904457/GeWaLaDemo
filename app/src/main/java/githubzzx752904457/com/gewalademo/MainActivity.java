package githubzzx752904457.com.gewalademo;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;


import butterknife.Bind;
import butterknife.ButterKnife;
import githubzzx752904457.com.gewalademo.adapter.RecyclerViewAdapter;


public class MainActivity extends AppCompatActivity {
    private RecyclerViewAdapter mAdapter;

    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new RecyclerViewAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(ImageView imageView, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, "iv");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

}
