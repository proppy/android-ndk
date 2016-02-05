package com.example.proppy.adder;

import android.app.ListActivity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proppy.adder.databinding.ActivityAdderBinding;
import java.util.LinkedList;

public class AdderActivity extends ListActivity {
    ArrayAdapter<Answer> adapter;
    final LinkedList<Answer> answers = new LinkedList<>();

    public class RandomAddition {
        public final int number1;
        public final int number2;
        public RandomAddition(int max) {
            number1 = (int)Math.floor(Math.random()*max);
            number2 = (int)Math.floor(Math.random()*max);
        }
    }

    public class Answer {
        public final RandomAddition addition;
        public final String answer;
        public final boolean ok;
        public Answer(RandomAddition addition, String answer) {
            this.addition = addition;
            this.answer = answer;
            this.ok = (addition.number1 + addition.number2) == Integer.parseInt(answer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);
        final ActivityAdderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_adder);
        binding.setAddition(new RandomAddition(10));
        adapter = new ArrayAdapter<Answer>(this, R.layout.answer_adder, answers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Answer answerItem = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.answer_adder, parent, false);
                }

                TextView numberText1 = (TextView)convertView.findViewById(R.id.numberText1);
                numberText1.setText(String.valueOf(answerItem.addition.number1));
                TextView numberText2 = (TextView)convertView.findViewById(R.id.numberText2);
                numberText2.setText(String.valueOf(answerItem.addition.number2));
                TextView answerText = (TextView)convertView.findViewById(R.id.answerText);
                answerText.setText(answerItem.answer);
                if (answerItem.ok) {
                    answerText.setTextColor(Color.GREEN);
                } else {
                    answerText.setTextColor(Color.RED);
                }

                return convertView;
            }
        };
        setListAdapter(adapter);

        final EditText answerEdit = (EditText)findViewById(R.id.answerEdit);
        answerEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                answers.addFirst(new Answer(binding.getAddition(), answerEdit.getText().toString()));
                adapter.notifyDataSetChanged();
                binding.setAddition(new RandomAddition(10));
                answerEdit.setText("");
                return true;
            }
        });
    }
}
