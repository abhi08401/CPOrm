package za.co.cporm.model;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

import za.co.cporm.model.generate.TableDetails;

/**
 * Created by hennie.brink on 2016/06/12.
 */
public class CPOrmTransaction {

    private List<CPDefaultRecord> records = new ArrayList<>();

    public <T extends CPDefaultRecord> void addRecord(T record){

        records.add(record);
    }

    public void commit() throws RemoteException, OperationApplicationException {

        commit(CPOrm.getApplicationContext());
    }

    public void commit(Context context) throws RemoteException, OperationApplicationException {

        if(records.isEmpty())
            return;

        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = TransactionHelper.prepareTransaction(context, records);

        TableDetails tableDetails = CPOrm.findTableDetails(context, records.get(0).getClass());

        ContentProviderResult[] contentProviderResults = contentResolver.applyBatch(tableDetails.getAuthority(), operations);

        for (int i = 0; i < contentProviderResults.length; i++) {

            ContentProviderResult contentProviderResult = contentProviderResults[i];
            if(contentProviderResult.uri == null)
                continue;

            CPDefaultRecord cpDefaultRecord = records.get(i);
            if(cpDefaultRecord.getId() == null){

                long id = ContentUris.parseId(contentProviderResult.uri);

                if(id != -1) {
                    cpDefaultRecord.setId(id);
                }
            }
        }
    }
}
