package com.personality.presentation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import com.personality.R
import com.personality.domain.model.Question

internal class QuestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleId: Int = 0
) : ConstraintLayout(context, attrs, defStyleId) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_item, this, true)
    }

    private val answerGroup by lazy { findViewById<LinearLayout>(R.id.answerGroup) }
    private val questionTitle by lazy { findViewById<TextView>(R.id.questionTitle) }
    private val submitButton by lazy { findViewById<Button>(R.id.submitButton) }
    private val conditionGroup by lazy { findViewById<Group>(R.id.conditionGroup) }
    private val radioGroupCondition by lazy { findViewById<LinearLayout>(R.id.radioGroupCondition) }
    private val conditionTitle by lazy { findViewById<TextView>(R.id.conditionTitle) }

    fun setOnSubmitListener(submitClicked: (Int) -> Unit, onError: () -> Unit) {
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val radioGroup = answerGroup.findViewById<RadioGroup>(R.id.radioGroup)
            if (radioGroup != null) {
                if (radioGroup.isVisible && radioGroup.checkedRadioButtonId >= 0) {
                    submitClicked(radioGroup.checkedRadioButtonId)
                } else {
                    onError()
                }
            }
        }
    }

    fun updateQuestion(question: Question) {
        questionTitle.text = question.question
        answerGroup.removeAllViews()
        radioGroupCondition.removeAllViews()
        conditionGroup.isVisible = false

        loadSingleChoiceQuestion(question)
    }

    private fun loadSingleChoiceQuestion(question: Question) {
        loadRadioGroup(answerGroup, question.questionType.options) {
            if (question.questionType.type == "single_choice_conditional") {
                if (question.questionType.condition?.predicate?.exactEquals?.contains(it) == true) {
                    inflateConditions(question.questionType.condition.ifPositive)
                } else {
                    removeConditions()
                }
            }
        }
    }

    private fun inflateConditions(question: Question) {
        conditionGroup.isVisible = true

        conditionTitle.text = question.question
        loadRadioGroup(radioGroupCondition, question.questionType.options) {
            // TODO: Save condition as answer
        }
    }

    private fun removeConditions() {
        conditionGroup.isVisible = false
    }

    private fun loadRadioGroup(
        parent: ViewGroup, choices: List<String>?, choiceClicked: (String) -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_question_radiogroup, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        parent.addView(view)

        if (choices.isNullOrEmpty()) {
            radioGroup.isVisible = false

            return
        }

        choices.forEachIndexed { index, choice ->
            radioGroup.addView(
                RadioButton(context).apply {
                    id = index
                    text = choice
                }
            )
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selected = (group[checkedId] as RadioButton).text
            Log.d("Vivek", selected.toString())
            choiceClicked(selected.toString())
        }
    }

    fun shouldShowSubmitButton(submit: Boolean) {
        submitButton.text =
            if (submit) {
                context.getString(R.string.submit)
            } else {
                context.getString(R.string.next)
            }
    }
}