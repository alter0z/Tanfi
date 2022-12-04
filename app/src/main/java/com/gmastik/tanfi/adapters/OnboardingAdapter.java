package com.gmastik.tanfi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gmastik.tanfi.R;

import org.jetbrains.annotations.NotNull;

public class OnboardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    int[] images = {R.drawable.ic_task_onboarding,R.drawable.ic_finance_onboarding};
    int[] grammars = {R.string.task_grammar,R.string.finance_grammar};

    @Override
    public int getCount() {
        return grammars.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider_layout,container,false);

        // here is hooks
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView grammar = view.findViewById(R.id.slider_grammar);

        imageView.setImageResource(images[position]);
        grammar.setText(grammars[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
