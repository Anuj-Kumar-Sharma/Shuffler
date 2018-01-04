package com.example.anujsharma.shuffler.volley;

import java.util.ArrayList;

/**
 * Created by anuj5 on 02-01-2018.
 */

public interface RequestCallback {
    void onListRequestSuccessful(ArrayList list, int check, boolean status);

    void onObjectRequestSuccessful(Object object, int check, boolean status);
}
