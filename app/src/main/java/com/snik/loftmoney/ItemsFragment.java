package com.snik.loftmoney;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {
    public static final int REQUEST_CODE = 100;
    private static final String TAG = "ItemsFragment";
    public static final String KEY_TYPE = "type";

    public static ItemsFragment newInstance(String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ItemsFragment() {
        // Required empty public constructor
    }

    private ItemsAdapter adapter;
    private String type;
    private Api api;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private Toolbar toolbar;
    private ActionMode mode;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        toolbar = ((MainActivity) getActivity()).getToolbar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        Bundle args = getArguments();
        type = args.getString(KEY_TYPE);

        api = ((App) getActivity().getApplication()).getApi();
        adapter = new ItemsAdapter();
        adapter.setItemsAdapterListener(new AdapterListener());
        loadItems();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = data.getParcelableExtra(AddActivity.KEY_ITEM);
            if (item.getType().equals(type)) {
                adapter.addItem(item);
            }
        }
    }

    class AdapterListener implements ItemsAdapterListener {

        @Override
        public void onItemClick(Item item, int position) {
            Log.i(TAG, "onItemClick: name" + item.getName() + "position" + position);
            if (mode == null) {
                return;
            }
            toggleItem(position);
        }

        @Override
        public void onItemLongClick(Item item, int position) {
            Log.i(TAG, "onItemLongClick: name" + item.getName() + "position" + position);

            if (mode != null) {
                return;
            }
            ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionModeCallback());
            toggleItem(position);


        }

        private void toggleItem(int position) {
            adapter.toogleItem(position);
        }
    }


    private void removeSelectedItem() {
        for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i--) {
            adapter.removeItem(adapter.getSelectedItems().get(i));
        }
        mode.finish();
    }

    class ActionModeCallback implements ActionMode.Callback {

        @SuppressLint("ResourceAsColor")
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Log.i(TAG, "onCreateActionMode: ");
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
            dialog.show(getFragmentManager(), null);
            dialog.setListener(new ConfirmDeleteFragment.Listener() {
                @Override
                public void onDeleteConfirmed() {
                    removeSelectedItem();
                    Log.i(TAG, "onDeleteConfirmed: ");
                }

                @Override
                public void onCancelConfirmed() {
                    Log.i(TAG, "onCancelConfirmed: ");
                    mode.finish();
                }
            });
        }
    }


}
