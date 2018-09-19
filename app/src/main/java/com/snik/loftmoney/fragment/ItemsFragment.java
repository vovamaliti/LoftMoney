package com.snik.loftmoney.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.snik.loftmoney.AddActivity;
import com.snik.loftmoney.R;
import com.snik.loftmoney.adapter.ItemsAdapter;
import com.snik.loftmoney.adapter.ItemsAdapterListener;
import com.snik.loftmoney.api.Api;
import com.snik.loftmoney.app.App;
import com.snik.loftmoney.model.Item;
import com.snik.loftmoney.response.AddItemResult;
import com.snik.loftmoney.response.RemoveItemResult;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {
    public static final int REQUEST_CODE = 100;
    public static final String KEY_TYPE = "type";

    public static ItemsFragment newInstance(String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ItemsFragment() {
    }

    private ItemsAdapter adapter;
    private String type;
    private Api api;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode mode;
    private int position;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        type = args.getString(KEY_TYPE);

        api = ((App) getActivity().getApplication()).getApi();
        adapter = new ItemsAdapter();
        adapter.setItemsAdapterListener(new AdapterListener());
        loadItems();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.apple_green),
                ContextCompat.getColor(requireContext(), R.color.colorAccent),
                ContextCompat.getColor(requireContext(), R.color.dark_sky_blue)
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

    }


    public void loadItems() {
        Call<List<Item>> call = api.getItems(type);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Item>> call, Response<List<Item>> response) {
                swipeRefreshLayout.setRefreshing(false);
                List<Item> items = response.body();
                adapter.setItems(items);
            }

            @Override
            public void onFailure(retrofit2.Call<List<Item>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void addItem(final Item item) {
        Call<AddItemResult> call = api.addItem(item.getPrice(), item.getName(), item.getType());
        call.enqueue(new Callback<AddItemResult>() {
            @Override
            public void onResponse(Call<AddItemResult> call, Response<AddItemResult> response) {
                AddItemResult result = response.body();
                assert result != null;
                if (result.status.equals("success")) {
                    adapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<AddItemResult> call, Throwable t) {

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            Item item = data.getParcelableExtra(AddActivity.KEY_ITEM);
            if (item.getType().equals(type)) {
                addItem(item);
            }
        }
    }

    class AdapterListener implements ItemsAdapterListener {

        @Override
        public void onItemClick(Item item, int position) {
            if (mode == null) {
                return;
            }
            toggleItem(position);
        }

        @Override
        public void onItemLongClick(Item item, int position) {
            if (mode != null) {
                return;
            }
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).startSupportActionMode(new ActionModeCallback());
            toggleItem(position);


        }

        private void toggleItem(int position) {
            adapter.toogleItem(position);
        }
    }


    private void removeSelectedItem() {
        final List<Integer> selected = adapter.getSelectedItems();
        for (int i = selected.size() - 1; i >= 0; i--) {
            position = i;
            Call<RemoveItemResult> call = api.removeItem(selected.get(i));
            call.enqueue(new Callback<RemoveItemResult>() {
                @Override
                public void onResponse(Call<RemoveItemResult> call, Response<RemoveItemResult> response) {
                    RemoveItemResult result = response.body();
                    if (result.status.equals("success")) {
                        adapter.remove(position);
                        Toast.makeText(getContext(), "Removed Successfully!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RemoveItemResult> call, Throwable t) {

                }
            });

        }
        mode.finish();
    }

    class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mode = actionMode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = new MenuInflater(requireContext());
            inflater.inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.menu_item_delete) {
                showConfirmationDialog();
                return true;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapter.clearSelections();
            mode = null;
        }

        private void showConfirmationDialog() {
            ConfirmDeleteFragment dialog = new ConfirmDeleteFragment();
            assert getFragmentManager() != null;
            dialog.show(getFragmentManager(), null);
            dialog.setListener(new ConfirmDeleteFragment.Listener() {
                @Override
                public void onDeleteConfirmed() {
                    removeSelectedItem();
                }

                @Override
                public void onCancelConfirmed() {
                    mode.finish();
                }
            });
        }
    }


}
