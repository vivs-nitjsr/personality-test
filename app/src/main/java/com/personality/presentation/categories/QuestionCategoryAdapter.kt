package com.personality.presentation.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.personality.R
import kotlinx.android.extensions.LayoutContainer

internal class QuestionCategoryAdapter : RecyclerView.Adapter<QuestionCategoryViewHolder>() {

    private val categories = mutableListOf<String>()

    private var selectedIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuestionCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_category_item, parent, false)
        )

    override fun onBindViewHolder(holder: QuestionCategoryViewHolder, position: Int) {
        holder.bindView(categories[position], selectedIndex == position)
    }

    override fun getItemCount() = categories.size

    fun updateSelection(position: Int) {
        selectedIndex = position
        notifyDataSetChanged()
    }

    fun update(categories: List<String>) {
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }

}

internal class QuestionCategoryViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bindView(category: String, isSelected: Boolean) {
        val categoryTextView = containerView.findViewById<TextView>(R.id.categoryTextView)
        if (isSelected) {
            categoryTextView.setBackgroundResource(R.drawable.shape_chip_selected)
            categoryTextView.setTextColor(
                ContextCompat.getColor(containerView.context, R.color.white)
            )
        } else {
            categoryTextView.setBackgroundResource(R.drawable.shape_chip_unselected)
            categoryTextView.setTextColor(
                ContextCompat.getColor(containerView.context, R.color.black)
            )
        }
        categoryTextView.text = category
    }
}
