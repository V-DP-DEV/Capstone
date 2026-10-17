package com.example.capstone.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.ColorRes;
import java.util.Objects;

public class QuickAction {
    private final String title;
    private final int iconResId;
    private final int iconTintResId;
    private final boolean enabled;

    public QuickAction(String title, @DrawableRes int iconResId, @ColorRes int iconTintResId, boolean enabled) {
        this.title = title;
        this.iconResId = iconResId;
        this.iconTintResId = iconTintResId;
        this.enabled = enabled;
    }

    public QuickAction(String title, @DrawableRes int iconResId, @ColorRes int iconTintResId) {
        this(title, iconResId, iconTintResId, true);
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getIconTintResId() {
        return iconTintResId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuickAction that = (QuickAction) o;
        return iconResId == that.iconResId &&
                iconTintResId == that.iconTintResId &&
                enabled == that.enabled &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, iconResId, iconTintResId, enabled);
    }
}
