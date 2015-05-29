package com.example.administrador.myapplication.controllers.material;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.protocol.HTTP;

import java.util.List;

public class ServiceOrderListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;
    private RecyclerView mOrders;
    private ServiceOrderListAdapter mOrdersAdapter;
    private static String filter = "active";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_list_material);

        mOrders = AppUtil.get(findViewById(R.id.recyclerViewServiceOrders));
        mOrders.setHasFixedSize(true);
        mOrders.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionButton fabAdd = AppUtil.get(findViewById(R.id.fabAdd));
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class), REQUEST_CODE_ADD);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapterItens();
    }

    private void updateAdapterItens() {
        final List<ServiceOrder> serviceOrders;
        if(filter == "active"){
            serviceOrders =  ServiceOrder.getActive();
        }else{
            serviceOrders =  ServiceOrder.getArquive();
        }

        if (mOrdersAdapter == null) {
            mOrdersAdapter = new ServiceOrderListAdapter(serviceOrders);
            mOrders.setAdapter(mOrdersAdapter);
        } else {
            mOrdersAdapter.setItens(serviceOrders);
            mOrdersAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==  RESULT_OK){
            if(requestCode == REQUEST_CODE_ADD){
                Toast.makeText(this, R.string.lbl_add_success, Toast.LENGTH_LONG).show();
            }else if(requestCode == REQUEST_CODE_EDIT){
                Toast.makeText(this, R.string.lbl_edit_success, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final ServiceOrder selectedItem = mOrdersAdapter.getSelectedItem();
        switch (item.getItemId()) {
            case R.id.actionCall:
                // Best Practices: http://stackoverflow.com/questions/4275678/how-to-make-phone-call-using-intent-in-android
                final Intent goToSOPhoneCall = new Intent(Intent.ACTION_CALL);
                goToSOPhoneCall.setData(Uri.parse("tel:" + selectedItem.getPhone()));
                startActivity(goToSOPhoneCall);
                return true;
            case R.id.actionEdit:
                final Intent goToEdit = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                final ServiceOrder serviceOrder = selectedItem;
                goToEdit.putExtra(ServiceOrderActivity.EXTRA_SERVICE_ORDER, serviceOrder);
                startActivityForResult(goToEdit, REQUEST_CODE_EDIT);
                return true;
            case R.id.actionArquive:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.lbl_confirm)
                        .setMessage(R.string.msg_delete)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem.delete();
                                Toast.makeText(ServiceOrderListActivity.this, R.string.msg_delete_success, Toast.LENGTH_LONG).show();
                                updateAdapterItens();
                            }
                        })
                        .setNeutralButton(R.string.lbl_no, null)
                        .create().show();
                return true;
            case R.id.actionActive:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.lbl_confirm)
                        .setMessage(R.string.msg_active_order)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem.active();
                                Toast.makeText(ServiceOrderListActivity.this, R.string.msg_active_success, Toast.LENGTH_LONG).show();
                                updateAdapterItens();
                            }
                        })
                        .setNeutralButton(R.string.lbl_no, null)
                        .create().show();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_service_order_list_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see <a href="http://developer.android.com/guide/components/intents-filters.html">Forcing an app chooser</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionShare:
                // Create the text message with a string
                final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ServiceOrder.getAll().toString());
                sendIntent.setType(HTTP.PLAIN_TEXT_TYPE);

                // Create intent to show the chooser dialog
                final Intent chooser = Intent.createChooser(sendIntent, "KamPow?");

                // Verify the original intent will resolve to at least one activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return true;
            case R.id.SearchActive:
                filter = "active";
                final List<ServiceOrder> serviceOrdersActive =  ServiceOrder.getActive();
                if (mOrdersAdapter == null) {
                    mOrdersAdapter = new ServiceOrderListAdapter(serviceOrdersActive);
                    mOrders.setAdapter(mOrdersAdapter);
                } else {
                    mOrdersAdapter.setItens(serviceOrdersActive);
                    mOrdersAdapter.notifyDataSetChanged();
                }
                Toast.makeText(this, R.string.msg_active, Toast.LENGTH_LONG).show();
                return true;
            case R.id.SearchNoActive:
                filter = "arquive";
                final List<ServiceOrder> serviceOrdersArquive =  ServiceOrder.getArquive();
                if (mOrdersAdapter == null) {
                    mOrdersAdapter = new ServiceOrderListAdapter(serviceOrdersArquive);
                    mOrders.setAdapter(mOrdersAdapter);
                } else {
                    mOrdersAdapter.setItens(serviceOrdersArquive);
                    mOrdersAdapter.notifyDataSetChanged();
                }
                Toast.makeText(this, R.string.msg_arquive, Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
