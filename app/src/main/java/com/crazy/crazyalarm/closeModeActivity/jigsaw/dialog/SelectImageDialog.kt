package com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog

import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.app.DialogFragment
import android.app.FragmentManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.crazy.crazyalarm.R
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.crazy.crazyalarm.closeModeActivity.jigsaw.Utils.Utils
import com.crazy.crazyalarm.closeModeActivity.jigsaw.module.ImageSoures


class SelectImageDialog : DialogFragment() {
    private lateinit  var view1: View
    private var imageList: RecyclerView? = null
    private var itemClickListener: OnItemClickListener? = null
    private lateinit var activity1: Activity
    private var selectRes = 0
    private var imageListAdapter: ImageListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        view1 = inflater.inflate(R.layout.dialog_select_image, container)
        imageList = view1.findViewById<View>(R.id.list) as RecyclerView
        return view1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity1 = getActivity()
        imageList!!.layoutManager = GridLayoutManager(getActivity().applicationContext, 2)
        imageListAdapter = ImageListAdapter()
        imageList!!.adapter = imageListAdapter
    }

    inner class ImageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ImageViewHolder(
                LayoutInflater.from(activity1!!.applicationContext)
                    .inflate(R.layout.item_list, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as ImageViewHolder?
            val bitmap = Utils.readBitmap(
                activity1!!.applicationContext, ImageSoures.imageSours[position], 3
            )
            viewHolder!!.imageView.setImageBitmap(bitmap)
        }

        override fun getItemCount(): Int {
            return ImageSoures.imageSours.size
        }


    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById<View>(R.id.itemImg) as ImageView
            imageView.setOnClickListener {
                if (adapterPosition != -1 && itemClickListener != null) {
                    itemClickListener!!.itemClick(
                        adapterPosition,
                        ImageSoures.imageSours[adapterPosition]
                    )
                    dismiss()
                }
            }
        }
    }

    fun showDialog(fragmentManager: FragmentManager?, tag: String?, res: Int) {
        show(fragmentManager, "tag")
        selectRes = res
    }

    fun addItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun itemClick(postion: Int, res: Int)
    }
}