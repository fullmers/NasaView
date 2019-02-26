package com.amiculous.nasaview.ui.apod;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

//sources:
//https://lab.getbase.com/introduction-to-coordinator-layout-on-android/
//https://stackoverflow.com/a/32039007
//https://github.com/ianhanniballake/cheesesquare/blob/scroll_aware_fab/app/src/main/java/com/support/android/designlibdemo/FABAwareScrollingViewBehavior.java

public class FABbehavior extends AppBarLayout.ScrollingViewBehavior {

    public FABbehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) ||
                dependency instanceof FloatingActionButton;
    }

    @Override
    public boolean onStartNestedScroll(
            final CoordinatorLayout coordinatorLayout,
            final View child,
            final View directTargetChild,
            final View target,
            final int nestedScrollAxes,
            final int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedScroll(
            final CoordinatorLayout coordinatorLayout,
            final View child,
            final View target,
            final int dxConsumed,
            final int dyConsumed,
            final int dxUnconsumed,
            final int dyUnconsumed,
            final int type) {
        super.onNestedScroll(coordinatorLayout, child, target,
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        List<View> dependencies = coordinatorLayout.getDependencies(child);
        int scrollThreshold = 0;
        if ((dyUnconsumed > scrollThreshold)){
            for (View view : dependencies) {
                if (view instanceof FloatingActionButton) {
                    ((FloatingActionButton) view).hide();
                }
            }
        }
        else {
            for (View view : dependencies) {
                if (view instanceof FloatingActionButton) {
                    ((FloatingActionButton) view).show();
                }
            }
        }
    }
}
