package com.example.capstone.ui.mock;

import java.util.List;

/** @deprecated Use {@link com.example.capstone.model.Interview} instead. */
@Deprecated
public class Interview extends com.example.capstone.model.Interview {
    public Interview(String id, String title, String difficulty, List<String> focusAreas) {
        super(id, title, difficulty, focusAreas);
    }

    public String getName() {
        return getTitle();
    }
}
