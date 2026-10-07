package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.adapter.InterviewAdapter;
import com.example.capstone.model.InterviewTopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectInterviewFragment extends Fragment {
    private InterviewAdapter adapter;
    private List<InterviewTopic> fullTopicsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_select_interview, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.card_interview);
        EditText etSearch = view.findViewById(R.id.et_search);
        adapter = new InterviewAdapter(topic -> {
            // Handle topic click here
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));;
        recyclerView.setAdapter(adapter);

        loadDummyData();
        adapter.setTopics(fullTopicsList);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterTopics(s.toString());
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
        });

        }
        public void filterTopics(String query) {
        List<InterviewTopic> filteredList = new ArrayList<>();
        for (InterviewTopic topic : fullTopicsList) {
            if (topic.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(topic);
            }
        }
        adapter.setTopics(filteredList);
        }
        public void loadDummyData() {
        fullTopicsList = new ArrayList<>();
        List<String> defaultTags = Arrays.asList("SQL", "Problem solving", "Communication", "System design");

        fullTopicsList.add(new InterviewTopic("Java", "Hard", defaultTags, "1"));
        fullTopicsList.add(new InterviewTopic("C#", "Hard", defaultTags, "2"));
        fullTopicsList.add(new InterviewTopic("Python", "Hard", defaultTags, "3"));
    }

}
