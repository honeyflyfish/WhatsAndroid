package com.haiyunshan.demo.recyclerview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.TextView;
import club.andnext.recyclerview.bridge.BridgeAdapter;
import club.andnext.recyclerview.bridge.BridgeAdapterProvider;
import club.andnext.recyclerview.bridge.BridgeBuilder;
import club.andnext.recyclerview.swipe.SwipeActionHelper;
import club.andnext.recyclerview.swipe.SwipeHolder;
import club.andnext.recyclerview.swipe.SwipeViewHolder;
import club.andnext.recyclerview.swipe.runner.DeleteRunner;
import com.haiyunshan.dataset.PiliDataset;
import com.haiyunshan.whatsandroid.R;

public class SwipeDeleteDemoFragment extends BaseSwipeRVDemoFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected BridgeAdapter createAdapter() {
        this.adapter = new BridgeAdapter(getActivity(), new BridgeAdapterProvider<PiliDataset.PiliEntity>() {

            @Override
            public PiliDataset.PiliEntity get(int position) {
                return dataset.get(position);
            }

            @Override
            public int size() {
                return dataset.size();
            }
        });

        adapter.bind(PiliDataset.PiliEntity.class,
                new BridgeBuilder(DemoViewHolder.class, DemoViewHolder.LAYOUT_RES_ID, this));

        return adapter;
    }

    /**
     *
     */
    private static class DemoViewHolder extends SwipeViewHolder<PiliDataset.PiliEntity> implements View.OnClickListener {

        static final int LAYOUT_RES_ID = R.layout.layout_pili_swipe_delete_list_item;

        SwipeDeleteDemoFragment parent;

        View contentView;

        View deleteBtn;
        TextView nameView;
        TextView poemView;

        public DemoViewHolder(SwipeDeleteDemoFragment parent, View itemView) {
            super(itemView);

            this.parent = parent;
        }

        @Override
        public int getLayoutResourceId() {
            return LAYOUT_RES_ID;
        }

        @Override
        public void onViewCreated(@NonNull View view) {
            {
                this.contentView = view.findViewById(R.id.content_layout);

                this.deleteBtn = view.findViewById(R.id.action_delete);
                this.nameView = view.findViewById(R.id.tv_name);
                this.poemView = view.findViewById(R.id.tv_poem);

                contentView.setOnClickListener(this);

                deleteBtn.setOnClickListener(this);
            }

            {
                SwipeHolder swipe = new SwipeHolder(parent.swipeActionhelper, view, contentView);

                DeleteRunner r = new DeleteRunner();
                r.add(deleteBtn);

                swipe.add(r);

                this.setSwipeHolder(swipe);
            }

        }

        @Override
        public void onBind(PiliDataset.PiliEntity item, int position) {
            super.onBind(item, position);

            nameView.setText(item.getName());
            poemView.setText(item.getPoem());
        }

        @Override
        public void onActionBegin(SwipeActionHelper helper, int action) {
            if (action == SwipeActionHelper.ACTION_RIGHT) {
                this.delete();
            }
        }

        @Override
        public void onActionEnd(SwipeActionHelper helper, int action) {

        }

        @Override
        public void onClick(View v) {
            if (v == contentView) {
                this.click();
            } else if (v == deleteBtn) {
                this.delete();
            }
        }

        void click() {
            int position = this.getAdapterPosition();
            PiliDataset.PiliEntity entity = parent.dataset.get(position);

            Snackbar.make(itemView, entity.getPoem(), Snackbar.LENGTH_LONG).show();

        }

        void delete() {
            int position = this.getAdapterPosition();
            parent.dataset.remove(position);
            parent.adapter.notifyItemRemoved(position);
        }
    }
}
