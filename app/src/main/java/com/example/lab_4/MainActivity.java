package com.example.lab_4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_4.databinding.ActivityMainBinding;
import com.example.lab_4.service.CommonService;
import com.example.lab_4.service.Service;
import com.example.lab_4.service.models.SongModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongAsyncTask.ResultListener {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ActivityMainBinding binding;
    private SongsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<SongModel> songs = Service.getInstance().multimediaRepository.getSongs();
        mAdapter = new SongsAdapter(this, songs);
        binding.rvSongs.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSongs.setAdapter(mAdapter);

        binding.toolbar.setTitle("Журнал радио");
        if (CommonService.checkInternet(this)) {
            Service.getInstance().startPolling(this);
        } else {
            binding.toolbar.setSubtitle("Автономный режим");
            binding.idLoading.setVisibility(View.GONE);
            CommonService.getInstance().showToast("Автономный режим. Только просмотр.");
        }
    }

    @Override
    public void result(String author, String song) {
        /** Пересоздавать адаптер каждый, потому что при обновлении существующей записи ничего не поменяется на форме*/
        Service.getInstance().multimediaRepository.updateOrInsertSong(author, song);
        List<SongModel> songs = Service.getInstance().multimediaRepository.getSongs();
        mAdapter = new SongsAdapter(this, songs);
        binding.rvSongs.setAdapter(mAdapter);
    }

    class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {
        private LayoutInflater mInflater;
        @Nullable
        private List<SongModel> mData;

        public SongsAdapter(Context context, @Nullable List<SongModel> data) {
            mInflater = LayoutInflater.from(context);
            mData = data;
        }

        @Override
        public int getItemCount() {
            if (mData == null) return 0;
            return mData.size();
        }

        public void setData(@Nullable List<SongModel> data) {
            mData = data;
            if (mData != null) notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.SongsAdapter.SongsViewHolder holder, int position) {
            if (mData == null) {
                return;
            }
            holder.setData(mData.get(position));
        }

        @NonNull
        @Override
        public MainActivity.SongsAdapter.SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_song, parent, false);
            return new SongsViewHolder(view);
        }


        class SongsViewHolder extends RecyclerView.ViewHolder {
            private View mItemView;

            public SongsViewHolder(View itemView) {
                super(itemView);
                mItemView = itemView;
            }

            public void setData(SongModel songModel) {
                TextView tvFio = (TextView) mItemView.findViewById(R.id.tv_song);
                TextView tvTimeInsert = (TextView) mItemView.findViewById(R.id.tv_timeInsert);
                String fioString = String.format("%s - %s", songModel.musician, songModel.name);
                tvFio.setText(fioString);
                Date date = new Date(songModel.timeInsert);
                String timeInsert = "Дата добавления: " + dateFormat.format(date);
                tvTimeInsert.setText(timeInsert);
            }
        }
    }
}