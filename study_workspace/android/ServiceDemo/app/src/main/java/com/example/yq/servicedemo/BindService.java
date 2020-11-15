package com.example.yq.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by yq on 2017/6/30.
 */

public class BindService extends Service {

    public static final int REPORT_CODE = 0;

    private Reporter reporter;

    public final class Reporter extends Binder implements IReporter {

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case REPORT_CODE:
                    data.enforceInterface("reporter");
                    String values = data.readString();
                    Log.i("IReporter", "data is '" + values + "'");
                    int type = data.readInt();
                    int result = report(values, type);
                    reply.writeInterfaceToken("reporter");
                    reply.writeInt(result);
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public int report(String value, int type) {
            return type;
        }
    }

    public BindService() {
        reporter = new Reporter();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return reporter;
    }

    public interface IReporter {
        int report(String value, int type);
    }
}
