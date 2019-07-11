package com.aihtd.translatelanguage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aihtd.translatelanguage.R;
import com.aihtd.translatelanguage.bean.MessageBean;
import com.aihtd.translatelanguage.constant.ConstantInt;

import java.util.List;

/**
 * 所在包名：com.dabin.huanxinone.adapter
 * 描述：聊天界面
 * 作者：Dabin
 * 创建时间：2019-03-05
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MessageBean> messageList;

    public ChatAdapter(Context context, List<MessageBean> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, viewGroup, false);
        return new VoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        MessageBean MessageBean = messageList.get(i);
        VoiceHolder voiceHolder = (VoiceHolder) viewHolder;
        setUpBaseView(MessageBean, voiceHolder, i);
        onSetUpView(MessageBean, voiceHolder, i);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
        }

    }

    private void onSetUpView(final MessageBean messageBean, final VoiceHolder voiceHolder, final int position) {
        if (messageBean.getType() == ConstantInt.Companion.getLEFT()) {
            voiceHolder.rvLeft.setVisibility(View.VISIBLE);
            voiceHolder.rvRight.setVisibility(View.GONE);
            voiceHolder.tvSendChContent.setText(messageBean.getChContent());
            voiceHolder.tvSendWeiContent.setText(messageBean.getWeiContent());
        } else {
            voiceHolder.rvLeft.setVisibility(View.GONE);
            voiceHolder.rvRight.setVisibility(View.VISIBLE);
            voiceHolder.tvtoChContent.setText(messageBean.getChContent());
            voiceHolder.tvtoWeiContent.setText(messageBean.getWeiContent());
        }
        voiceHolder.rvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position,voiceHolder.ivVoiceRight,messageBean);
                }
            }
        });

        voiceHolder.rvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position,voiceHolder.ivVoiceRight,messageBean);
                }
            }
        });

    }

    /**
     * 公共view
     */
    private void setUpBaseView(MessageBean MessageBean, VoiceHolder voiceHolder, int position) {
//        if (voiceHolder.timestamp != null) {
//            if (position == 0) {
//                voiceHolder.timestamp.setText(DateUtils.getTimestampString(new Date(MessageBean.getMsgTime())));
//                voiceHolder.timestamp.setVisibility(View.VISIBLE);
//            } else {
//                //TODO 如果最后一条消息的间隔为> 30秒，则显示时间戳
//                // show time stamp if interval with last message is > 30 seconds
//                MessageBean prevMessage = (MessageBean) messageList.get(position - 1);
//                if (prevMessage != null && DateUtils.isCloseEnough(MessageBean.getMsgTime(), prevMessage.getMsgTime())) {
//                    voiceHolder.timestamp.setVisibility(View.GONE);
//                } else {
//                    voiceHolder.timestamp.setText(DateUtils.getTimestampString(new Date(MessageBean.getMsgTime())));
//                    voiceHolder.timestamp.setVisibility(View.VISIBLE);
//                }
//            }
//        }

        /**
         * 设置头像和昵称
         */
        if (voiceHolder.ivUserheadLeft != null && voiceHolder.ivUserheadRight != null) {


        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public class VoiceHolder extends RecyclerView.ViewHolder {
        TextView timestamp;

        RelativeLayout rvLeft;
        ImageView ivUserheadLeft;
        ImageView ivVoiceLeft;
        LinearLayout bubbleLeft;
        TextView tvSendChContent;
        TextView tvSendWeiContent;


        RelativeLayout rvRight;
        ImageView ivUserheadRight;
        ImageView ivVoiceRight;
        LinearLayout bubbleRight;
        TextView tvtoWeiContent;
        TextView tvtoChContent;


        public VoiceHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.timestamp);

            rvLeft = itemView.findViewById(R.id.left_rlv);
            ivUserheadLeft = itemView.findViewById(R.id.iv_userhead_left);
            ivVoiceLeft = itemView.findViewById(R.id.iv_voice_left);
            bubbleLeft = itemView.findViewById(R.id.bubble_left);
            tvSendChContent = itemView.findViewById(R.id.tv_send_ch_content);
            tvSendWeiContent = itemView.findViewById(R.id.tv_send_wei_content);

            rvRight = itemView.findViewById(R.id.right_rlv);
            ivUserheadRight = itemView.findViewById(R.id.iv_userhead_right);
            ivVoiceRight = itemView.findViewById(R.id.iv_voice_right);
            bubbleRight = itemView.findViewById(R.id.bubble_right);
            tvtoWeiContent = itemView.findViewById(R.id.tv_to_wei_content);
            tvtoChContent = itemView.findViewById(R.id.tv_to_ch_content);

        }
    }


    protected OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, ImageView imageView, MessageBean messageBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
