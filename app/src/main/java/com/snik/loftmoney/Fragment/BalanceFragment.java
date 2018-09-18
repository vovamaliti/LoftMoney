package com.snik.loftmoney.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snik.loftmoney.Api.Api;
import com.snik.loftmoney.App.App;
import com.snik.loftmoney.Response.BalanceResult;
import com.snik.loftmoney.VIew.DiagramView;
import com.snik.loftmoney.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {

    private Api api;
    private DiagramView diagramView;
    private TextView expenseSum, incomeSum, total;

    public BalanceFragment() {
    }

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = ((App) getActivity().getApplication()).getApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBalance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diagramView = view.findViewById(R.id.diagram_view);
        incomeSum = view.findViewById(R.id.incomeSum);
        expenseSum = view.findViewById(R.id.expenseSum);
        total = view.findViewById(R.id.total);
    }

    private void loadBalance() {
        Call<BalanceResult> call = api.balance();

        call.enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
                BalanceResult result = response.body();
                assert result != null;
                if (result.getStatus().equals("success")) {
                    diagramView.updateBalance(result.getTotalIncomes(), result.getTotalExpenses());
                    incomeSum.setText(String.valueOf(result.getTotalIncomes()) + "\u20BD");
                    expenseSum.setText(String.valueOf(result.getTotalExpenses()) + "\u20BD");
                    total.setText(String.valueOf(result.getTotalIncomes() - result.getTotalExpenses()) + "\u20BD");
                }

            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {

            }
        });
    }


}
