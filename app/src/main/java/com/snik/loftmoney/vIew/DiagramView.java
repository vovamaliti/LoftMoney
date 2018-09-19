package com.snik.loftmoney.vIew;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.snik.loftmoney.R;

public class DiagramView extends View {
    private int income;
    private int expense;

    private Paint incomePaint = new Paint();
    private Paint expensePaint = new Paint();

    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        incomePaint.setColor(getResources().getColor(R.color.apple_green));
        expensePaint.setColor(getResources().getColor(R.color.lightish_blue));
    }

    public void updateBalance(int income, int expense) {
        this.income = income;
        this.expense = expense;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPieDiagram(canvas);
    }

    private void drawPieDiagram(Canvas canvas) {
        if (expense + income == 0)
            return;

        float expenseAngle = 360.f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);

        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        final int xMargin = (getWidth() - size) / 2, yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space, getHeight() - yMargin, 180 - expenseAngle / 2, expenseAngle, true, expensePaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space, getHeight() - yMargin, 360 - incomeAngle / 2, incomeAngle, true, incomePaint);
    }
}
