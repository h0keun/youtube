package com.youtube.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youtube.R
import com.youtube.model.VideoModel

// 원래라면 메인엑티비티쪽을 다루는 어댑터 하나와
// 프래그먼트에쪽을 다루는 어댑터 하나를 두는게 적절함
// 이 프로젝트에서는 하나의 UI 를 활용하는 방식으로 하나의 어댑터만 사용
// 실제 유투브 앱을 보더라도 리사이클러뷰가 각각 다른 어댑터와 아이템들을 보여줌!

class VideoAdapter(val callback: (String, String) -> Unit): ListAdapter<VideoModel, VideoAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: VideoModel) {
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val subTitleTextView = view.findViewById<TextView>(R.id.subTitleTextView)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)

            titleTextView.text = item.title
            subTitleTextView.text = item.subtitle

            Glide.with(thumbnailImageView.context)
                .load(item.thumb)
                .into(thumbnailImageView)

            view.setOnClickListener {
                callback(item.sources, item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<VideoModel>(){
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem // 이부분은 사실 모델에서 고유한 아이디 값을 통해 중복체크하는편이 더 좋음..여기선 그냥 old 와 new 로 구분
            }

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}