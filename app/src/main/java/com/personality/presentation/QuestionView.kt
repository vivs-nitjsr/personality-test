package com.personality.presentation

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.get
import androidx.core.view.isVisible
import com.personality.R
import com.personality.domain.model.Question
import com.personality.domain.model.Range

@RequiresApi(Build.VERSION_CODES.O)
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
    private val conditionGroup by lazy { findViewById<Group>(R.id.conditionGroup) }
    private val radioGroupCondition by lazy { findViewById<LinearLayout>(R.id.radioGroupCondition) }
    private val conditionTitle by lazy { findViewById<TextView>(R.id.conditionTitle) }

    fun getAnswer(): Int? {
        val radioGroup = answerGroup.findViewById<RadioGroup>(R.id.radioGroup)
        if (radioGroup != null) {
            if (radioGroup.isVisible && radioGroup.checkedRadioButtonId >= 0) {
                return radioGroup.checkedRadioButtonId
            }
        }

        return null
    }

    fun updateQuestion(question: Question) {
        questionTitle.text = question.question
        answerGroup.removeAllViews()
        radioGroupCondition.removeAllViews()
        conditionGroup.isVisible = false

        loadSingleChoiceQuestion(question)
    }

    private fun loadSingleChoiceQuestion(question: Question) {
        if (question.questionType.type == "number_range") {
            loadSeekBar(answerGroup, question.questionType.range)
        } else {
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
    }

    private fun inflateConditions(question: Question) {
        conditionGroup.isVisible = true

        conditionTitle.text = question.question
        if (question.questionType.type == "number_range") {
            loadSeekBar(radioGroupCondition, question.questionType.range)
        } else {
            loadRadioGroup(radioGroupCondition, question.questionType.options) {
                // TODO: Save condition as answer
            }
        }
    }

    private fun removeConditions() {
        conditionGroup.isVisible = false
    }

    private fun loadSeekBar(parent: ViewGroup, range: Range?) {
        if (range == null) return

        val view = LayoutInflater.from(context).inflate(R.layout.view_question_range, null)
        val seekBarProgress = view.findViewById<TextView>(R.id.seekBarProgress)

        view.findViewById<TextView>(R.id.minTextView).text = range.from
        view.findViewById<TextView>(R.id.maxTextView).text = range.to
        seekBarProgress.text = range.from

        view.findViewById<SeekBar>(R.id.seekBar).apply {
            min = range.from.toInt()
            max = range.to.toInt()
            progress = range.from.toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    seekBarProgress.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
        }

        parent.addView(view)
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
}
