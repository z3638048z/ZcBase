package base.zc.com.project.fragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.zc.com.project.R;
import base.zc.com.project.adapter.BaseListAdapter;
import base.zc.com.project.util.DisplayUtil;

public class TestFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv_main;
    private List<JSONObject> dataList = new ArrayList<>();
    private BaseListAdapter mAdapter;


    //    private ImageView iv_main;
//    private Button btn_click;
    public TestFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        rv_main = (RecyclerView) view.findViewById(R.id.rv_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0){
                    return 3;
                }else if(position == 21){
                    return 2;
                }
                return 1;
            }

        });
        rv_main.setLayoutManager(gridLayoutManager);
        mAdapter = new BaseListAdapter(getActivity());
        for (int i = 0; i < 100; i++) {
            JSONObject json = new JSONObject();
            try {
                json.put("name", "数据" + (i + 1));
                dataList.add(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter.updateData(dataList);
        rv_main.setAdapter(mAdapter);



        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, 0) {
            /**
             * @param recyclerView
             * @param viewHolder 拖动的ViewHolder
             * @param target 目标位置的ViewHolder
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.e("TestFragment", "TestFragment.onMove*****qqqqqqqqqq**********" + viewHolder.itemView.getLeft());
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    //分别把中间所有的item的位置重新交换
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(dataList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(dataList, i, i - 1);
                    }
                }
//                viewHolder.itemView.setScaleX(1f);
//                viewHolder.itemView.setScaleY(1f);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }


//            @Override
//            public int getBoundingBoxMargin() {
//                return 500;
//            }

//            @Override
//            public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
//                return 0;
//            }


            @Override
            public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
                Log.e("TestFragment", "TestFragment.chooseDropTarget*****curX***curY******" + curX + "," + curY);
                int right = curX + selected.itemView.getWidth();
                int bottom = curY + selected.itemView.getHeight();
                RecyclerView.ViewHolder winner = null;
                int winnerScore = -1;
                final int dx = curX - selected.itemView.getLeft();
                final int dy = curY - selected.itemView.getTop();
                final int targetsSize = dropTargets.size();
                for (int i = 0; i < targetsSize; i++) {
                    final RecyclerView.ViewHolder target = dropTargets.get(i);
                    if (dx > 0) {
                        int diff = target.itemView.getRight() - target.itemView.getWidth() / 2 - right;
                        if (diff < 0 && target.itemView.getRight() > selected.itemView.getRight()) {
                            final int score = Math.abs(diff);
                            if (score > winnerScore) {
                                winnerScore = score;
                                winner = target;
                            }
                        }
                    }
                    if (dx < 0) {
                        int diff = target.itemView.getLeft() + target.itemView.getWidth() / 2 - curX;
                        if (diff > 0 && target.itemView.getLeft() < selected.itemView.getLeft()) {
                            final int score = Math.abs(diff);
                            if (score > winnerScore) {
                                winnerScore = score;
                                winner = target;
                            }
                        }
                    }
                    if (dy < 0) {
                        int diff = target.itemView.getTop() + target.itemView.getHeight() / 2 - curY;
                        if (diff > 0 && target.itemView.getTop() < selected.itemView.getTop()) {
                            final int score = Math.abs(diff);
                            if (score > winnerScore) {
                                winnerScore = score;
                                winner = target;
                            }
                        }
                    }

                    if (dy > 0) {
                        int diff = target.itemView.getBottom() - target.itemView.getHeight() / 2 - bottom;
                        if (diff < 0 && target.itemView.getBottom() > selected.itemView.getBottom()) {
                            final int score = Math.abs(diff);
                            if (score > winnerScore) {
                                winnerScore = score;
                                winner = target;
                            }
                        }
                    }
                }
                return winner;
            }

            Paint paint;
            int strokeWidth = 4;


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if(paint == null){
                    paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(strokeWidth);
                    DashPathEffect dashPathEffect = new DashPathEffect(new float[]{15, 5}, 0);
                    paint.setPathEffect(dashPathEffect);
                }
                int strokeWidthInPix = DisplayUtil.dip2px(getActivity(), strokeWidth);
                int offset = strokeWidthInPix / 2;
                int left = viewHolder.itemView.getLeft() + offset;
                int top = viewHolder.itemView.getTop() + offset;
                int right = viewHolder.itemView.getRight() - offset;
                int bottom = viewHolder.itemView.getBottom() - offset;
                c.drawRect(new Rect(left, top, right, bottom), paint);

            }

            @Override
            public void onSelectedChanged(final RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                switch (actionState){
                    case ItemTouchHelper.ACTION_STATE_DRAG:
                        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f).setDuration(400);
                        animator.setInterpolator(new OvershootInterpolator(3));
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float percent = (float) animation.getAnimatedValue();
                                viewHolder.itemView.setScaleX(1f + 0.1f * percent);
                                viewHolder.itemView.setScaleY(1f + 0.1f * percent);
                            }
                        });
                        animator.start();
                        break;
                    case ItemTouchHelper.ACTION_STATE_SWIPE:
                        break;
                    case ItemTouchHelper.ACTION_STATE_IDLE:
                        break;
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.CYAN);
                viewHolder.itemView.setScaleX(1f);
                viewHolder.itemView.setScaleY(1f);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                dataList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

        };
        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(rv_main);
    }

    ItemTouchHelper itemTouchHelper;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_click:
                break;
        }
    }
}
